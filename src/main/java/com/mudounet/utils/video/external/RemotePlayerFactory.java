package com.mudounet.utils.video.external;

import com.mudounet.utils.video.classic.VideoPlayerException;
import com.sun.jna.Native;
import java.awt.Canvas;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Factory class responsible for creating the out of process video players and
 * providing remote objects to control them.
 * @author gmanciet
 */
public class RemotePlayerFactory {
    protected static Logger logger = LoggerFactory.getLogger(RemotePlayerFactory.class.getName());
    private static int portCounter = 5555;

    /**
     * Get a remote embedded player, pointing to an embedded player in another
     * VM.
     * @param canvas the canvas ID (got using JNA) that the other player should
     * use.
     * @return the remote embedded player.
     */
    public static RemotePlayer getEmbeddedRemotePlayer(Canvas canvas) {
        try {
            long drawable = Native.getComponentID(canvas);
            StreamWrapper wrapper = startSecondPlayerJVM(OutOfProcessEmbeddedPlayer.class, Long.toString(drawable));
            final RemotePlayer player = new RemotePlayer(wrapper);
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        player.close();
                    } catch (VideoPlayerException ex) {
                        logger.error("Remote player error : " , ex);
                    }
                }
            });
            return player;
        }
        catch (IOException ex) {
            throw new RuntimeException("Couldn't create embedded remote player", ex);
        }
    }
    
    /**
     * Get the next available port to use for the next headless player.
     * @return the next available port number.
     */
    public static int getNextPort() {
        return portCounter;
    }
    
    /**
     * Get a headless remote player interface to a player in a separate VM.
     * @return a headless remote player interface to a player in a separate VM.
     */
    public static RemotePlayer getHeadlessRemotePlayer() {
  logger.debug("Creating process in external JVM");
        try {
            
            StreamWrapper wrapper = startSecondPlayerJVM(OutOfProcessHeadlessPlayer.class, Long.toString(0L));
            final RemotePlayer player = new RemotePlayer(wrapper);
            Runtime.getRuntime().addShutdownHook(new Thread() {

                @Override
                public void run() {
                    try {
                        player.close();
                    } catch (VideoPlayerException ex) {
                        logger.error("Remote player error : ", ex);
                    }
                }
            });
            return player;
        }
        catch (IOException ex) {
            logger.error("Couldn't create headless remote player : "+ex);
                    
            throw new RuntimeException("Couldn't create headless remote player", ex);
        }
    }

    /**
     * Start a second JVM to control an out of process player.
     * @param clazz the type of out of process player we're launching (must be
     * a class with a main method.)
     * @param option any options to pass to VLCJ.
     * @return a stream wrapper object for controlling the second VM.
     * @throws IOException if something goes wrong.
     */
    private static StreamWrapper startSecondPlayerJVM(Class<? extends OutOfProcessPlayer> clazz, String option) throws IOException {
        String separator = System.getProperty("file.separator");
        String classpath = System.getProperty("java.class.path");
        String path = System.getProperty("java.home")
                + separator + "bin" + separator + "java";
        ProcessBuilder processBuilder = new ProcessBuilder(path, "-cp", classpath, "-Djna.library.path=" + System.getProperty("jna.library.path"), clazz.getName(), option);
        logger.debug("Starting process in external JVM");
        Process process = processBuilder.start();
        logger.debug("Linking processes");
        StreamWrapper wrapper = new StreamWrapper(process.getInputStream(), process.getOutputStream());
        logger.debug("Streamwrapper created");
        return wrapper;
    }
}

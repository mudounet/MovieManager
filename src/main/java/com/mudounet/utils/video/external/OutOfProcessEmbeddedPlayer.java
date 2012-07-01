package com.mudounet.utils.video.external;

import com.sun.jna.NativeLibrary;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.videosurface.ComponentIdVideoSurface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An embedded player that sits out of process in a separate VM. In process
 * players are unstable using VLC due to concurrency bugs in libvlc and its
 * dependencies, so we need to fall back on a mechanism that's a little more
 * complicated than the norm!
 * @author Michael
 */
public class OutOfProcessEmbeddedPlayer extends OutOfProcessPlayer {
    protected static Logger logger = LoggerFactory.getLogger(OutOfProcessEmbeddedPlayer.class.getName());
    public OutOfProcessEmbeddedPlayer(final long canvasId) throws IOException {

        MediaPlayerFactory factory = new MediaPlayerFactory(getPrepareOptions());
        mediaPlayer = factory.newEmbeddedMediaPlayer();

        ComponentIdVideoSurface videoSurfaceById = factory.newVideoSurface(canvasId);
        videoSurfaceById.attach(LibVlc.INSTANCE, mediaPlayer);


    }

    /**
     * No special options needed for this player.
     * @return an empty string array.
     */
    @Override
    public final String[] getPrepareOptions() {
        return new String[0];
    }

    /**
     * Testing stuff.
     * @param args 
     */
    public static void main(String[] args) {
        File nativeDir = new File("lib/native");
        NativeLibrary.addSearchPath("libvlc", nativeDir.getAbsolutePath());
        NativeLibrary.addSearchPath("vlc", nativeDir.getAbsolutePath());
        PrintStream stream = null;

        try {
            //stream = new PrintStream(new File("logfile.txt"));
            System.setErr(null); //Important, MUST redirect err stream
            OutOfProcessEmbeddedPlayer player = new OutOfProcessEmbeddedPlayer(Integer.parseInt(args[0]));
            
           logger.info("Begin of process");
            player.read();
            logger.info("End of process");
        } catch (Exception ex) {
            logger.error("Exception occured : "+ex);

        } finally {
            if (stream != null) {
                stream.close();
            }
     
        }
    }
}

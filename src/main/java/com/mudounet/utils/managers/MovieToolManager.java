/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.managers;

import com.mudounet.hibernate.movies.TechData;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import java.io.File;
import org.apache.log4j.Logger;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.TrackInfo;
import uk.co.caprica.vlcj.player.VideoTrackInfo;
import uk.co.caprica.vlcj.player.events.VideoOutputEventListener;

/**
 *
 * @author isabelle
 */
public class MovieToolManager {

    protected static Logger logger = Logger.getLogger(SimpleTagManager.class.getName());
    private static final String OS_NAME = System.getProperty("os.name").toLowerCase();

    /**
     * Test whether the runtime operating system is "unix-like".
     * 
     * @return true if the runtime OS is unix-like, Linux, Unix, FreeBSD etc
     */
    public static boolean isNix() {
        return OS_NAME.indexOf("nux") != -1 || OS_NAME.indexOf("nix") != -1 || OS_NAME.indexOf("freebsd") != -1;
    }

    /**
     * Test whether the runtime operating system is a Windows variant. 
     * 
     * @return true if the runtime OS is Windows
     */
    public static boolean isWindows() {
        return OS_NAME.indexOf("win") != -1;
    }

    /**
     * Test whether the runtime operating system is a Mac variant.
     * 
     * @return true if the runtime OS is Mac
     */
    public static boolean isMac() {
        return OS_NAME.indexOf("mac") != -1;
    }

    public static TechData getMovieInformations(File file) {
        TechData techData = new TechData();

        techData.setSize(file.length());

        if(isWindows())
            NativeLibrary.addSearchPath("libvlc", "C:\\Program Files\\VideoLAN\\VLC"); // or whatever
        
        MediaPlayerFactory factory = new MediaPlayerFactory();
        MediaPlayer mediaPlayer = factory.newHeadlessMediaPlayer();

        mediaPlayer.prepareMedia(file.getPath());

        mediaPlayer.parseMedia();

        // logger.debug("Track Information after parse(): " + mediaPlayer.getTrackInfo());
        // logger.debug("Track Information after parse(): " + mediaPlayer.getLength());



        for (TrackInfo t : mediaPlayer.getTrackInfo()) {
            if (t.getClass().getName().equals("uk.co.caprica.vlcj.player.VideoTrackInfo")) {
                VideoTrackInfo v = (VideoTrackInfo) t;
                techData.setCodecName(v.codecName());
                techData.setHeight(v.height());
                techData.setWidth(v.width());
            }
        }

        logger.info(techData);

//        mediaPlayer.start();
//
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//        }
//
//        mediaPlayer.stop();

        mediaPlayer.release();
        factory.release();
        return null;
    }
}

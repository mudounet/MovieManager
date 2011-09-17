/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.managers;

import com.mudounet.hibernate.movies.TechData;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import org.apache.log4j.Logger;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.TrackInfo;
import uk.co.caprica.vlcj.player.VideoTrackInfo;
import uk.co.caprica.vlcj.player.events.VideoOutputEventListener;

/**
 *
 * @author isabelle
 */
public class MovieToolManager {

    private static final String[] VLC_ARGS = {
        "--intf", "dummy", /* no interface */
        "--vout", "dummy", /* we don't want video (output) */
        "--no-audio", /* we don't want audio (decoding) */
        "--no-video-title-show", /* nor the filename displayed */
        "--no-stats", /* no stats */
        "--no-sub-autodetect-file", /* we don't want subtitles */
        //"--no-inhibit", /* we don't want interfaces */
        "--no-disable-screensaver", /* we don't want interfaces */
        "--no-snapshot-preview", /* no blending in dummy vout */};
    private static final float VLC_THUMBNAIL_POSITION = 30.0f / 100.0f;
    protected static Logger logger = Logger.getLogger(SimpleTagManager.class.getName());
    private static final String OS_NAME = System.getProperty("os.name").toLowerCase();
    private File file;
    private MediaPlayer mediaPlayer;

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
    
    public void initializeMedia(File file) {
        
    }

    public static TechData getMovieInformations(File file) throws InterruptedException, IOException {
        TechData techData = new TechData();

        techData.setSize(file.length());

        if (isWindows()) {
            NativeLibrary.addSearchPath("libvlc", "C:\\Program Files\\VideoLAN\\VLC"); // or whatever
        }
        
        MediaPlayerFactory factory = new MediaPlayerFactory(VLC_ARGS);
        MediaPlayer mediaPlayer = factory.newHeadlessMediaPlayer();

        final CountDownLatch inPositionLatch = new CountDownLatch(1);
        final CountDownLatch snapshotTakenLatch = new CountDownLatch(1);

        mediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {

            @Override
            public void positionChanged(MediaPlayer mediaPlayer, float newPosition) {
                if (newPosition >= VLC_THUMBNAIL_POSITION * 0.9f) { /* 90% margin */
                    inPositionLatch.countDown();
                }
            }

            @Override
            public void snapshotTaken(MediaPlayer mediaPlayer, String filename) {
                logger.debug("snapshotTaken(filename=" + filename + ")");
                snapshotTakenLatch.countDown();
            }
        });

        mediaPlayer.prepareMedia(file.getPath());

        mediaPlayer.parseMedia();

        if (mediaPlayer.startMedia(file.getPath())) {



            mediaPlayer.setPosition(0.2f);
            inPositionLatch.await(); // Might wait forever if error

            //mediaPlayer.saveSnapshot("test.jpg", 300, 0);
            File testFile = new File("./test.jpg");
            mediaPlayer.saveSnapshot(testFile);
            snapshotTakenLatch.await(); // Might wait forever if error
            techData.setPlayTime(mediaPlayer.getLength());


            for (TrackInfo t : mediaPlayer.getTrackInfo()) {
                if (t.getClass().getName().equals("uk.co.caprica.vlcj.player.VideoTrackInfo")) {
                    VideoTrackInfo v = (VideoTrackInfo) t;
                    techData.setCodecName(v.codecName());
                    techData.setHeight(v.height());
                    techData.setWidth(v.width());
                }
            }

            mediaPlayer.stop();
        }

        mediaPlayer.release();
        factory.release();

        logger.info(techData);

        return null;
    }

    public static void test(String mrl, int imageWidth, File snapshotFile) throws InterruptedException {
   
        MediaPlayerFactory factory = new MediaPlayerFactory(VLC_ARGS);
        MediaPlayer mediaPlayer = factory.newHeadlessMediaPlayer();

        final CountDownLatch inPositionLatch = new CountDownLatch(1);
        final CountDownLatch snapshotTakenLatch = new CountDownLatch(1);

        mediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {

            @Override
            public void positionChanged(MediaPlayer mediaPlayer, float newPosition) {
                if (newPosition >= VLC_THUMBNAIL_POSITION * 0.9f) { /* 90% margin */
                    inPositionLatch.countDown();
                }
            }

            @Override
            public void snapshotTaken(MediaPlayer mediaPlayer, String filename) {
                System.out.println("snapshotTaken(filename=" + filename + ")");
                snapshotTakenLatch.countDown();
            }
        });

        if (mediaPlayer.startMedia(mrl)) {
            mediaPlayer.setPosition(VLC_THUMBNAIL_POSITION);
            inPositionLatch.await(); // Might wait forever if error

            mediaPlayer.saveSnapshot(snapshotFile);
            snapshotTakenLatch.await(); // Might wait forever if error

            mediaPlayer.stop();
        }

        mediaPlayer.release();
        factory.release();

    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.video;

import com.sun.jna.NativeLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.runtime.x.LibXUtil;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
 

/**
 *
 * @author isabelle
 */
public class VlcPlayerFactory {
    protected static Logger logger = LoggerFactory.getLogger(VlcPlayerFactory.class.getName());
    private static final String[] VLC_ARGS_HEADLESS = {
        "--intf", "dummy", /*
         * no interface
         */
        "--vout", "dummy", /*
         * we don't want video (output)
         */
        "--no-audio", /*
         * we don't want audio (decoding)
         */
        "--no-video-title-show", /*
         * nor the filename displayed
         */
        "--no-stats", /*
         * no stats
         */
        "--no-sub-autodetect-file", /*
         * we don't want subtitles
         */
        "--no-disable-screensaver", /*
         * we don't want interfaces
         */
        "--no-snapshot-preview", /*
     * no blending in dummy vout
     */};
    
       /**
     * Log level, used only if the -Dvlcj.log= system property has not already been set.
     */
    private static final String VLCJ_LOG_LEVEL = "INFO";
 
    /**
     * Change this to point to your own vlc installation, or comment out the code if you want to use
     * your system default installation.
     * <p>
     * This is a bit more explicit than using the -Djna.library.path= system property.
     */
    private static final String NATIVE_LIBRARY_SEARCH_PATH = "/Applications/VLC.app/Contents/MacOS/lib";
 
    /**
     * Set to true to dump out native JNA memory structures.
     */
    private static final String DUMP_NATIVE_MEMORY = "false";
 
    /**
     * Static initialisation.
     */
    static {
        if(null == System.getProperty("vlcj.log")) {
            System.setProperty("vlcj.log", VLCJ_LOG_LEVEL);
        }
 
        // Safely try to initialise LibX11 to reduce the opportunity for native
        // crashes - this will silently throw an Error on Windows (and maybe MacOS)
        // that can safely be ignored
        LibXUtil.initialise();
 
        if(null != NATIVE_LIBRARY_SEARCH_PATH) {
            logger.info("Explicitly adding JNA native library search path: '{}'", NATIVE_LIBRARY_SEARCH_PATH);
            NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), NATIVE_LIBRARY_SEARCH_PATH);
        }
 
        System.setProperty("jna.dump_memory", DUMP_NATIVE_MEMORY);
    }
 
    /**
     * Set the standard look and feel.
     */
    protected static final void setLookAndFeel() {
        String lookAndFeelClassName = null;
        LookAndFeelInfo[] lookAndFeelInfos = UIManager.getInstalledLookAndFeels();
        for(LookAndFeelInfo lookAndFeel : lookAndFeelInfos) {
            if("Nimbus".equals(lookAndFeel.getName())) {
                lookAndFeelClassName = lookAndFeel.getClassName();
            }
        }
        if(lookAndFeelClassName == null) {
            lookAndFeelClassName = UIManager.getSystemLookAndFeelClassName();
        }
        try {
            UIManager.setLookAndFeel(lookAndFeelClassName);
        }
        catch(Exception e) {
            // Silently fail, it doesn't matter
        }
    }

    public static VlcPlayer getHeadlessPlayer() {
        MediaPlayerFactory factory = new MediaPlayerFactory(VLC_ARGS_HEADLESS);
        MediaPlayer mediaPlayer = factory.newHeadlessMediaPlayer();
        return new VlcPlayer(mediaPlayer) {

            @Override
            public String[] getPrepareOptions() {
                return VLC_ARGS_HEADLESS;
            }
        };
    }
}

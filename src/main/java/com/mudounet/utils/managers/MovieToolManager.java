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
    
    public static TechData getMovieInformations(File file) {
        TechData techData = new TechData();
        
        techData.setSize(file.length());
         
        
        NativeLibrary.addSearchPath("libvlc", "C:\\Program Files\\VideoLAN\\VLC"); // or whatever
        MediaPlayerFactory factory = new MediaPlayerFactory();
        MediaPlayer mediaPlayer = factory.newHeadlessMediaPlayer();

        mediaPlayer.prepareMedia(file.getPath());
        
        mediaPlayer.parseMedia();

       // logger.debug("Track Information after parse(): " + mediaPlayer.getTrackInfo());
       // logger.debug("Track Information after parse(): " + mediaPlayer.getLength());

        
        
        for(TrackInfo t : mediaPlayer.getTrackInfo()) {
            if(t.getClass().getName().equals("uk.co.caprica.vlcj.player.VideoTrackInfo")) {
                VideoTrackInfo v = (VideoTrackInfo) t;
                logger.debug("Codec name: "+v.codecName());
                logger.debug("height : "+v.height());
                logger.debug("width : "+v.width());
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

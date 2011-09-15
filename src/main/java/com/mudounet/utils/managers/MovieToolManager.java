/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.managers;

import com.mudounet.hibernate.movies.TechData;
import org.apache.log4j.Logger;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.events.VideoOutputEventListener;

/**
 *
 * @author isabelle
 */
public class MovieToolManager {

    protected static Logger logger = Logger.getLogger(SimpleTagManager.class.getName());
    
    public static TechData getMovieInformations(String moviePath) {
        MediaPlayerFactory factory = new MediaPlayerFactory();
        MediaPlayer mediaPlayer = factory.newHeadlessMediaPlayer();

//        mediaPlayer.addVideoOutputEventListener(new VideoOutputEventListener() {
//
//            @Override
//            public void videoOutputAvailable(MediaPlayer mediaPlayer, boolean videoOutput) {
//                System.out.println("     Track Information: " + mediaPlayer.getTrackInfo());
//                System.out.println("    Title Descriptions: " + mediaPlayer.getTitleDescriptions());
//                System.out.println("    Video Descriptions: " + mediaPlayer.getVideoDescriptions());
//                System.out.println("    Audio Descriptions: " + mediaPlayer.getAudioDescriptions());
//                for (int i = 0; i < mediaPlayer.getTitleDescriptions().size(); i++) {
//                    System.out.println("Chapter Descriptions " + i + ": " + mediaPlayer.getChapterDescriptions(i));
//                }
//            }
//        });

        mediaPlayer.prepareMedia(moviePath);

        mediaPlayer.parseMedia();

        logger.debug("Track Information after parse(): " + mediaPlayer.getTrackInfo());

        mediaPlayer.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        mediaPlayer.stop();

        mediaPlayer.release();
        factory.release();
        return null;
    }
}

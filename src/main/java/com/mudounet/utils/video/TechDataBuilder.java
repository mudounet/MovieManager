/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.video;

import com.mudounet.hibernate.movies.others.TechData;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.caprica.vlcj.player.AudioTrackInfo;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.TrackInfo;
import uk.co.caprica.vlcj.player.VideoTrackInfo;

/**
 *
 * @author gmanciet
 */
public class TechDataBuilder {

    protected static Logger logger = LoggerFactory.getLogger(TechDataBuilder.class.getName());
    
    public static TechData getTechData(MediaPlayer mediaPlayer) {
        List<TrackInfo> trackInfo = mediaPlayer.getTrackInfo();
        TechData techData = new TechData();
        techData.setPlayTime(mediaPlayer.getLength());
        
        for (int i = 0; i < trackInfo.size(); i++) {
            Object track = trackInfo.get(i);

            if (track.getClass() == VideoTrackInfo.class) {
                VideoTrackInfo t = (VideoTrackInfo) track;
                techData.setVideoCodec(t.codec());
                techData.setVideoHeight(t.height());
                techData.setVideoWidth(t.width());
            }
            else if (track.getClass() == AudioTrackInfo.class) {
                 AudioTrackInfo t = (AudioTrackInfo) track;
                 techData.setAudioCodec(t.codec());
                 techData.setAudioSamplingRate(t.rate());
                 techData.setAudioChannels(t.channels());
            }
        }
        logger.debug("track properties : " + techData);
        return techData;
    }
}

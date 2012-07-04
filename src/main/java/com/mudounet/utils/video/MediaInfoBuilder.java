/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.video;

import com.mudounet.hibernate.movies.others.MediaInfo;
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
public class MediaInfoBuilder {

    protected static Logger logger = LoggerFactory.getLogger(MediaInfoBuilder.class.getName());
    
    public static MediaInfo getMediaInfo(MediaPlayer mediaPlayer) {
        List<TrackInfo> trackInfo = mediaPlayer.getTrackInfo();
        MediaInfo mediaInfo = new MediaInfo();
        mediaInfo.setPlayTime(mediaPlayer.getLength());
        
        for (int i = 0; i < trackInfo.size(); i++) {
            Object track = trackInfo.get(i);

            if (track.getClass() == VideoTrackInfo.class) {
                VideoTrackInfo t = (VideoTrackInfo) track;
                mediaInfo.setVideoCodec(t.codec());
                mediaInfo.setVideoHeight(t.height());
                mediaInfo.setVideoWidth(t.width());
            }
            else if (track.getClass() == AudioTrackInfo.class) {
                 AudioTrackInfo t = (AudioTrackInfo) track;
                 mediaInfo.setAudioCodec(t.codec());
                 mediaInfo.setAudioSamplingRate(t.rate());
                 mediaInfo.setAudioChannels(t.channels());
            }
        }
        logger.debug("track properties : " + mediaInfo);
        return mediaInfo;
    }
}

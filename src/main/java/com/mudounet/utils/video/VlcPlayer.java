/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.video;

import com.mudounet.hibernate.movies.others.TechData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author isabelle
 */
public class VlcPlayer {
    protected static Logger logger = LoggerFactory.getLogger(VlcPlayer.class.getName());

    public VlcPlayer() {
    }

    /**
     * Terminate the OutOfProcessPlayer. MUST be called before closing, otherwise
     * the player won't quit!
     * @throws VideoPlayerException
     */
    public void close() throws VideoPlayerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Get the length of the currently loaded video.
     * @return the length of the currently loaded video.
     * @throws VideoPlayerException
     */
    public long getLength() throws VideoPlayerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Determine if this video is muted.
     * @return true if it's muted, false if not.
     * @throws VideoPlayerException
     */
    public boolean getMute() throws VideoPlayerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Get the time in milliseconds of the current position in the video.
     * @return the time in milliseconds of the current position in the video.
     * @throws VideoPlayerException
     */
    public long getTime() throws VideoPlayerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Determine whether the remote player is paused.
     * @return true if its paused, false otherwise.
     */
    public boolean isPaused() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Determine if the current video is playable, i.e. one is loaded and
     * ready to start playing when play() is called.
     * @return true if the video is playable, false otherwise.
     * @throws VideoPlayerException
     */
    public boolean isPlayable() throws VideoPlayerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Determine whether the remote player is playing.
     * @return true if its playing, false otherwise.
     * @throws VideoPlayerException
     */
    public boolean isPlaying() throws VideoPlayerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Load the given path into the remote player.
     * @param path the path to load.
     * @throws VideoPlayerException
     */
    public void load(String path) throws VideoPlayerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Pause the video.
     * @throws VideoPlayerException
     */
    public void pause() throws VideoPlayerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Play the loaded video.
     * @throws VideoPlayerException
     */
    public void play() throws VideoPlayerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Retrieve technical data.
     * @return technical Data
     * @throws VideoPlayerException
     */
    public TechData retrieveTechData() throws VideoPlayerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Set whether this video is muted.
     * @param mute true to mute, false to unmute.
     * @throws VideoPlayerException
     */
    public void setMute(boolean mute) throws VideoPlayerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Set the time in milliseconds of the current position in the video.
     * @param time the time in milliseconds of the current position in the
     * video.
     * @throws VideoPlayerException
     */
    public void setTime(long time) throws VideoPlayerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Stop the video.
     * @throws VideoPlayerException
     */
    public void stop() throws VideoPlayerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Take a snapshot.
     * @param time
     * @param path
     * @return Snapshot is taken
     * @throws VideoPlayerException
     */
    public boolean takeSnapshot(long time, String path) throws VideoPlayerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
  
  
}

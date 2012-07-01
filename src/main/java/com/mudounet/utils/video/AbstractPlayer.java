/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.video;

import com.mudounet.hibernate.movies.others.TechData;
import com.mudounet.utils.video.classic.VideoPlayerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author isabelle
 */
public abstract class AbstractPlayer {
    protected static Logger logger = LoggerFactory.getLogger(AbstractPlayer.class.getName());

    public AbstractPlayer() {
    }

    /**
     * Terminate the OutOfProcessPlayer. MUST be called before closing, otherwise
     * the player won't quit!
     * @throws VideoPlayerException
     */
    public abstract void close() throws VideoPlayerException;

    /**
     * Get the length of the currently loaded video.
     * @return the length of the currently loaded video.
     * @throws VideoPlayerException
     */
    public abstract long getLength() throws VideoPlayerException;

    /**
     * Determine if this video is muted.
     * @return true if it's muted, false if not.
     * @throws VideoPlayerException
     */
    public abstract boolean getMute() throws VideoPlayerException;

    /**
     * Get the time in milliseconds of the current position in the video.
     * @return the time in milliseconds of the current position in the video.
     * @throws VideoPlayerException
     */
    public abstract long getTime() throws VideoPlayerException;

    /**
     * Determine whether the remote player is paused.
     * @return true if its paused, false otherwise.
     */
    public abstract boolean isPaused();

    /**
     * Determine if the current video is playable, i.e. one is loaded and
     * ready to start playing when play() is called.
     * @return true if the video is playable, false otherwise.
     * @throws VideoPlayerException
     */
    public abstract boolean isPlayable() throws VideoPlayerException;

    /**
     * Determine whether the remote player is playing.
     * @return true if its playing, false otherwise.
     * @throws VideoPlayerException
     */
    public abstract boolean isPlaying() throws VideoPlayerException;

    /**
     * Load the given path into the remote player.
     * @param path the path to load.
     * @throws VideoPlayerException
     */
    public abstract void load(String path) throws VideoPlayerException;

    /**
     * Pause the video.
     * @throws VideoPlayerException
     */
    public abstract void pause() throws VideoPlayerException;

    /**
     * Play the loaded video.
     * @throws VideoPlayerException
     */
    public abstract void play() throws VideoPlayerException;

    /**
     * Retrieve technical data.
     * @return technical Data
     * @throws VideoPlayerException
     */
    public abstract TechData retrieveTechData() throws VideoPlayerException;

    /**
     * Set whether this video is muted.
     * @param mute true to mute, false to unmute.
     * @throws VideoPlayerException
     */
    public abstract void setMute(boolean mute) throws VideoPlayerException;

    /**
     * Set the time in milliseconds of the current position in the video.
     * @param time the time in milliseconds of the current position in the
     * video.
     * @throws VideoPlayerException
     */
    public abstract void setTime(long time) throws VideoPlayerException;

    /**
     * Stop the video.
     * @throws VideoPlayerException
     */
    public abstract void stop() throws VideoPlayerException;

    /**
     * Take a snapshot.
     * @param time
     * @param path
     * @return Snapshot is taken
     * @throws VideoPlayerException
     */
    public abstract boolean takeSnapshot(long time, String path) throws VideoPlayerException;
    
}

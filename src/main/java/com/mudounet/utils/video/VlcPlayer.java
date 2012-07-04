/* 
 * This file is part of Quelea, free projection software for churches.
 * Copyright (C) 2011 Michael Berry
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mudounet.utils.video;

import com.mudounet.hibernate.movies.others.TechData;
import com.mudounet.utils.video.remotecommands.*;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.TrackInfo;
import uk.co.caprica.vlcj.player.VideoTrackInfo;

/**
 * Sits out of process so as not to crash the primary VM.
 *
 * @author Michael
 */
public abstract class VlcPlayer {

    protected static Logger logger = LoggerFactory.getLogger(VlcPlayer.class.getName());
    protected ObjectOutputStream oos;
    protected ObjectInputStream ois;
    private long length;
    private CountDownLatch inTimePositionLatch  = new CountDownLatch(1);
    private CountDownLatch lengthUpdatedLatch = new CountDownLatch(1);
    private CountDownLatch snapshotTakenLatch  = new CountDownLatch(1);
    private long snapshotTimePosition;
    protected MediaPlayer mediaPlayer;
    private File fileRead;
    private TechData techData;

    public VlcPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        this.addSnapshotFunction();
        this.addTechDataFunction();
        this.length = -1;
    }

    private void addSnapshotFunction() {
        mediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {

            @Override
            public void timeChanged(MediaPlayer mediaPlayer, long newTime) {

                if (inTimePositionLatch.getCount() > 0 && newTime >= snapshotTimePosition) {
                    logger.debug("Position reached (time in ms) : " + newTime + ")");
                    inTimePositionLatch.countDown();
                }
            }

            @Override
            public void error(MediaPlayer mediaPlayer) {
                logger.error("Unknown error occured");
            }

            @Override
            public void lengthChanged(MediaPlayer mediaPlayer, long newLength) {
                if (lengthUpdatedLatch != null) {
                    lengthUpdatedLatch.countDown();
                }

                if (newLength > 0) {
                    logger.debug("Length updated : from " + length + " to " + newLength);
                    length = newLength;
                    techData.setPlayTime(newLength);
                }
            }

            @Override
            public void snapshotTaken(MediaPlayer mediaPlayer, String filename) {
                logger.debug("snapshotTaken(filename=" + filename + ")");
                snapshotTakenLatch.countDown();
            }
        });
    }

    private void addTechDataFunction() {
        mediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {

            @Override
            public void videoOutput(MediaPlayer mediaPlayer, int newCount) {
                techData = TechDataBuilder.getTechData(mediaPlayer);
            }
        });

    }

    /**
     * This method should return an array of any options that need to be passed
     * onto VLCJ and in turn libvlc. If no options are required, an empty array
     * should be returned rather than null.
     *
     * @return the options required by libvlc.
     */
    public abstract String[] getPrepareOptions();

    /**
     * Set whether this video is muted.
     *
     * @param mute true to mute, false to unmute.
     * @throws VideoPlayerException
     */
    public void setMute(boolean mute) throws VideoPlayerException {
        logger.debug("Request to set mute to " + mute);
        mediaPlayer.mute(mute);
    }

    /**
     * Terminate the VlcPlayer. MUST be called before closing,
     * otherwise the player won't quit!
     *
     * @throws VideoPlayerException
     */
    public void close() throws VideoPlayerException {
            logger.debug("Close command received");
            mediaPlayer.stop();
            mediaPlayer.release();
    }

    /**
     * Get the length of the currently loaded video.
     *
     * @return the length of the currently loaded video.
     * @throws VideoPlayerException
     */
    public long getLength() throws VideoPlayerException {
        if (this.length <= 0) {
            this.lengthUpdatedLatch = new CountDownLatch(1);
            try {
                if (lengthUpdatedLatch.await(10L, TimeUnit.SECONDS)) {
                    return this.length;
                } else {
                    return -1;
                }
            } catch (InterruptedException ex) {
                return -1;
            }
        } else {
            return this.length;
        }
    }

    /**
     * Determine if this video is muted.
     *
     * @return true if it's muted, false if not.
     * @throws VideoPlayerException
     */
    public boolean getMute() throws VideoPlayerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Get the time in milliseconds of the current position in the video.
     *
     * @return the time in milliseconds of the current position in the video.
     * @throws VideoPlayerException
     */
    public long getTime() throws VideoPlayerException {
        return mediaPlayer.getTime();
    }

    /**
     * Determine whether the remote player is paused.
     *
     * @return true if its paused, false otherwise.
     */
    public boolean isPaused() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Determine if the current video is playable, i.e. one is loaded and ready
     * to start playing when play() is called.
     *
     * @return true if the video is playable, false otherwise.
     * @throws VideoPlayerException
     */
    public boolean isPlayable() throws VideoPlayerException {
        return mediaPlayer.isPlayable();
    }

    /**
     * Determine whether the remote player is playing.
     *
     * @return true if its playing, false otherwise.
     * @throws VideoPlayerException
     */
    public boolean isPlaying() throws VideoPlayerException {
        return mediaPlayer.isPlaying();
    }

    /**
     * Load the given path into the remote player.
     *
     * @param path the path to load.
     * @throws VideoPlayerException
     */
    public void load(String path) throws VideoPlayerException {
        logger.debug("Load command received : " + path);
        fileRead = new File(path);
        techData = new TechData();

        this.length = -1;
        mediaPlayer.prepareMedia(fileRead.getAbsolutePath(), getPrepareOptions());
        mediaPlayer.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        mediaPlayer.stop();
    }

    /**
     * Pause the video.
     *
     * @throws VideoPlayerException
     */
    public void pause() throws VideoPlayerException {
        logger.debug("Pause command received");
        mediaPlayer.pause();
    }

    /**
     * Play the loaded video.
     *
     * @throws VideoPlayerException
     */
    public void play() throws VideoPlayerException {
        mediaPlayer.play();
    }

    /**
     * Retrieve technical data.
     *
     * @return technical Data
     * @throws VideoPlayerException
     */
    public TechData retrieveTechData() throws VideoPlayerException {
        return techData;
    }

    /**
     * Set the time in milliseconds of the current position in the video.
     *
     * @param time the time in milliseconds of the current position in the
     * video.
     * @throws VideoPlayerException
     */
    public void setTime(long newPosition) throws VideoPlayerException, InterruptedException {
        logger.debug("Request to set time to " + newPosition);
              this.snapshotTimePosition = newPosition;
            inTimePositionLatch = new CountDownLatch(1);
            mediaPlayer.setTime(newPosition);
            logger.debug("Going to specified time (ms) : " + newPosition);
            inTimePositionLatch.await(10L, TimeUnit.SECONDS); // Might wait forever if error
            logger.debug("Latch reached.");
    }

    /**
     * Stop the video.
     *
     * @throws VideoPlayerException
     */
    public void stopVideo() throws VideoPlayerException {
        logger.debug("Stop command received");
        mediaPlayer.stop();
    }

    /**
     * Take a snapshot.
     *
     * @param time
     * @param path
     * @return Snapshot is taken
     * @throws VideoPlayerException
     */
    public boolean takeSnapshot(long time, String path) throws VideoPlayerException {

        logger.debug("Snapshot command received : " + time + "@path : " + path);

        boolean result2 = false;

        try {
            this.setTime(time);
            return this.takeSnapshot(path);
        } catch (InterruptedException ex) {
        }
        return false;
    }

    private boolean takeSnapshot(File file) {
        logger.debug("Snapshot @ " + mediaPlayer.getTime());

        snapshotTakenLatch = new CountDownLatch(1);
        mediaPlayer.saveSnapshot(new File(file.getAbsolutePath()));
        logger.debug("Waiting latch.");
        try {
            snapshotTakenLatch.await(10L, TimeUnit.SECONDS); // Might wait forever if error
        } catch (InterruptedException ex) {
            logger.error(ex.getMessage());
            return false;
        }

        if (file.exists()) {
            logger.debug("File found : " + file.getAbsolutePath());
            return true;
        } else {
            logger.debug("File is not found : " + file.getAbsolutePath());
            return false;
        }



    }

    private boolean takeSnapshot(String path) throws InterruptedException {
        return this.takeSnapshot(new File(path));
    }
}

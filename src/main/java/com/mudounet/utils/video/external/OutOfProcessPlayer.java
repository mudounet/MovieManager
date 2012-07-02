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
package com.mudounet.utils.video.external;

import com.mudounet.utils.video.VideoPlayerException;
import com.mudounet.hibernate.movies.others.TechData;
import com.mudounet.utils.video.remotecommands.*;
import java.io.*;
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
public abstract class OutOfProcessPlayer {

    protected static Logger logger = LoggerFactory.getLogger(OutOfProcessPlayer.class.getName());
    protected ObjectOutputStream oos;
    protected ObjectInputStream ois;
    private long length = 0;
    protected CountDownLatch operationFinished;
    protected MediaPlayer mediaPlayer;
    private File fileRead;
    private TechData techData;

    /**
     * Start the main loop reading from the standard input stream and writing to
     * sout.
     */
    public void read() {
        try {
            logger.debug("Création des flux");
            oos = new ObjectOutputStream(System.out);
            ois = new ObjectInputStream(System.in);
            // Création de l'output stream

            Object receivedObject;

            ThreadedAction t = new ThreadedAction();
            t.start();

            while ((receivedObject = ois.readObject()) != null) {
                operationFinished = t.requestNewOperation(receivedObject);
                try {
                    if (operationFinished.await(10L, TimeUnit.SECONDS)) {
                        execReturnObject(t.getResult());
                    } else {
                        logger.error("TimeOut Exception");
                        execReturnObject(new VideoPlayerException("Operation has timed-out."));
                    }
                } catch (InterruptedException ex) {
                    execReturnObject(new VideoPlayerException(ex));
                }
            }

            t.requestStop();
            logger.debug("Flux crées");
        } catch (ClassNotFoundException ex) {
            logger.error("Class not found Exception" + ex);
        } catch (EOFException ex) {
            logger.error("Remote player has been closed" + ex);
        } catch (IOException ex) {
            logger.error("IO Exception" + ex);
        }
        System.exit(-1);
    }

    private void execReturnObject(Object o) throws IOException {
        logger.debug("Answer : " + o);
        oos.writeObject(o);
        oos.flush();
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
     * Terminate the OutOfProcessPlayer. MUST be called before closing,
     * otherwise the player won't quit!
     *
     * @throws VideoPlayerException
     */
    public void close() throws VideoPlayerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Get the length of the currently loaded video.
     *
     * @return the length of the currently loaded video.
     * @throws VideoPlayerException
     */
    public long getLength() throws VideoPlayerException {
        throw new UnsupportedOperationException("Not supported yet.");
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
        throw new UnsupportedOperationException("Not supported yet.");
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Determine whether the remote player is playing.
     *
     * @return true if its playing, false otherwise.
     * @throws VideoPlayerException
     */
    public boolean isPlaying() throws VideoPlayerException {
        throw new UnsupportedOperationException("Not supported yet.");
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Set the time in milliseconds of the current position in the video.
     *
     * @param time the time in milliseconds of the current position in the
     * video.
     * @throws VideoPlayerException
     */
    public void setTime(long time) throws VideoPlayerException {
        throw new UnsupportedOperationException("Not supported yet.");
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private class ThreadedAction extends Thread {

        private volatile boolean stop = false;
        private Object command;
        private Object result;
        private long length = -1;
        private CountDownLatch inTimePositionLatch;
        private CountDownLatch lengthUpdatedLatch = new CountDownLatch(1);
        private CountDownLatch snapshotTakenLatch;
        private CountDownLatch newOperationLatch;
        private long snapshotTimePosition;

        private ThreadedAction() {

            this.addSnapshotFunction();
            this.addTechDataFunction();
            this.length = -1;

        }

        @Override
        public void run() {
            while (!stop) {
                if (newOperationLatch != null && newOperationLatch.getCount() > 0) {
                    try {
                        try {
                            result = execReqstdAction(command);
                        } catch (VideoPlayerException ex) {
                            java.util.logging.Logger.getLogger(OutOfProcessPlayer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (InterruptedException ex) {
                        ex.printStackTrace(System.err);
                    }
                    newOperationLatch.countDown();
                }
            }
            close();
        }

        public CountDownLatch requestNewOperation(Object receivedObject) {
            command = receivedObject;
            newOperationLatch = new CountDownLatch(1);
            result = null;
            return newOperationLatch;
        }

        public boolean operationInProgress() {
            return newOperationLatch.getCount() > 0;
        }

        public Object getResult() {
            return result;
        }

        private Object execReqstdAction(Object receivedObject) throws InterruptedException, VideoPlayerException {
            Object returnObject = new BooleanCommand();

            if (receivedObject.getClass() == LoadFile.class) {
                load(((LoadFile) receivedObject).getFilePath());
            } else if (receivedObject.getClass() == CloseCommand.class) {

                close();
            } else if (receivedObject.getClass() == PlayCommand.class) {
                play();
            } else if (receivedObject.getClass() == PauseCommand.class) {
                pause();
            } else if (receivedObject.getClass() == StopCommand.class) {
                stopVideo();
            } else if (receivedObject.getClass() == TechDataCommand.class) {
                logger.debug("Tech Data command received");
                TechDataCommand t = (TechDataCommand) receivedObject;

                if (techData.getPlayTime() == 0 || techData.getVideoHeight() == 0) {
                } else {
                    returnObject = techData;
                }

            } else if (receivedObject.getClass() == SnapshotCommand.class) {
                SnapshotCommand t = (SnapshotCommand) receivedObject;
                logger.debug("Snapshot command received : " + t.getTime() + "@path : " + t.getPath());

                boolean result2 = false;

                try {
                    this.moveToTime(t.getTime());
                    result2 = this.takeSnapshot(t.getPath());
                } catch (InterruptedException ex) {
                }

                returnObject = new BooleanCommand(result2);

            } else if (receivedObject.getClass() == TimeCommand.class) {
                TimeCommand t = (TimeCommand) receivedObject;
                if (t.getValue() < 0) {
                    logger.debug("request to get time.");
                    t.setValue(mediaPlayer.getTime());
                    returnObject = t;
                } else {
                    logger.debug("Request to set time to " + t.getValue());
                    mediaPlayer.setTime(t.getValue());
                }
            } else if (receivedObject.getClass() == LengthCommand.class) {
                LengthCommand t = (LengthCommand) receivedObject;
                logger.debug("Request to get length.");

                t.setValue(getLength());

                returnObject = t;
            } else if (receivedObject.getClass() == MuteCommand.class) {
                MuteCommand t = (MuteCommand) receivedObject;
                if (!t.isSet()) {
                    logger.debug("request to get mute state.");
                    t.setValue(mediaPlayer.isMute());
                    returnObject = t;
                } else {
                    setMute(t.getValue());
                }
            } else if (receivedObject.getClass() == StateCommand.class) {
                StateCommand t = (StateCommand) receivedObject;
                if (t.getValue() == StateCommand.PLAYABLE) {

                    t.setValue(0);
                    if (mediaPlayer.isPlayable()) {
                        t.setValue(StateCommand.PLAYABLE);
                    }
                } else if (t.getValue() == StateCommand.PLAYED) {

                    t.setValue(0);
                    if (mediaPlayer.isPlaying()) {
                        t.setValue(StateCommand.PLAYED);
                    }

                } else {
                    logger.error("State not currently managed : " + t.getValue());
                }
                returnObject = t;
            } else {
                logger.error("Unknown object : " + receivedObject);
            }

            return returnObject;
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
                    List<TrackInfo> trackInfo = mediaPlayer.getTrackInfo();

                    for (int i = 0; i < trackInfo.size(); i++) {
                        Object track = trackInfo.get(i);

                        if (track.getClass() == VideoTrackInfo.class) {
                            VideoTrackInfo t = (VideoTrackInfo) track;
                            techData.setVideoCodec(t.codecName());
                            techData.setVideoHeight(t.height());
                            techData.setVideoWidth(t.width());
                            break;
                        }
                    }
                    logger.debug("track properties : " + techData);
                }
            });

        }

        private long getLength() throws InterruptedException {
            if (this.length <= 0) {
                this.lengthUpdatedLatch = new CountDownLatch(1);
                if (lengthUpdatedLatch.await(10L, TimeUnit.SECONDS)) {
                    return this.length;
                } else {
                    return -1;
                }
            } else {
                return this.length;
            }
        }

        private void moveToTime(long newPosition) throws InterruptedException {
            this.snapshotTimePosition = newPosition;
            inTimePositionLatch = new CountDownLatch(1);
            mediaPlayer.setTime(newPosition);
            logger.debug("Going to specified time (ms) : " + newPosition);
            inTimePositionLatch.await(10L, TimeUnit.SECONDS); // Might wait forever if error
            logger.debug("Latch reached.");
        }

        private boolean takeSnapshot(File file) throws InterruptedException {
            logger.debug("Snapshot @ " + mediaPlayer.getTime());

            snapshotTakenLatch = new CountDownLatch(1);
            mediaPlayer.saveSnapshot(new File(file.getAbsolutePath()));
            logger.debug("Waiting latch.");
            snapshotTakenLatch.await(10L, TimeUnit.SECONDS); // Might wait forever if error

            if (file.exists()) {
                logger.debug("File found : " + file.getAbsolutePath());
                return true;
            } else {
                logger.debug("File is not found : " + file.getAbsolutePath());
                Thread.sleep(500);
                return false;
            }



        }

        private boolean takeSnapshot(String path) throws InterruptedException {
            return this.takeSnapshot(new File(path));
        }

        private void close() {
            logger.debug("Close command received");
            mediaPlayer.stop();
            mediaPlayer.release();
            System.exit(0);
        }

        public void requestStop() {
            stop = true;
        }
    }
}

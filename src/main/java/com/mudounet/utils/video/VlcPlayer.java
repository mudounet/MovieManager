/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.video;

import com.mudounet.hibernate.movies.others.TechData;
import com.mudounet.utils.video.remotecommands.*;
import java.io.File;
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
 *
 * @author isabelle
 */
public class VlcPlayer extends Thread {

    protected static Logger logger = LoggerFactory.getLogger(VlcPlayer.class.getName());
    protected MediaPlayer mediaPlayer;
    private volatile boolean stop = false;
    private Object command;
    private Object result;
    private long length = -1;
    private File fileRead;
    private TechData techData;
    private CountDownLatch inTimePositionLatch;
    private CountDownLatch lengthUpdatedLatch = new CountDownLatch(1);
    private CountDownLatch snapshotTakenLatch;
    private CountDownLatch newOperationLatch;
    private long snapshotTimePosition;
    
    public VlcPlayer() {
        this.addSnapshotFunction();
        this.addTechDataFunction();
        this.length = -1;
    }
    
    public String[] getPrepareOptions() {
        return null;
    }



    @Override
    public void run() {
        while (!stop) {
            if (newOperationLatch != null && newOperationLatch.getCount() > 0) {
                try {
                    try {
                        result = execReqstdAction(command);
                    } catch (VideoPlayerException ex) {
                        ex.printStackTrace(System.err);
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace(System.err);
                }
                newOperationLatch.countDown();
            }
        }
        try {
            close();
        } catch (VideoPlayerException ex) {
           ex.printStackTrace(System.err);
        }
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
            logger.debug("Load command received : " + receivedObject);
            fileRead = new File(((LoadFile) receivedObject).getFilePath());
            techData = new TechData();

            this.length = -1;
            mediaPlayer.prepareMedia(fileRead.getAbsolutePath(), getPrepareOptions());
            mediaPlayer.start();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }

            mediaPlayer.stop();

        } else if (receivedObject.getClass() == CloseCommand.class) {
            logger.debug("Close command received");
            close();
        } else if (receivedObject.getClass() == PlayCommand.class) {
            logger.debug("Play command received");
            mediaPlayer.play();
        } else if (receivedObject.getClass() == PauseCommand.class) {
            logger.debug("Pause command received");
            mediaPlayer.pause();
        } else if (receivedObject.getClass() == StopCommand.class) {
            logger.debug("Stop command received");
            mediaPlayer.stop();
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

            t.setValue(_getLength());

            returnObject = t;
        } else if (receivedObject.getClass() == MuteCommand.class) {
            MuteCommand t = (MuteCommand) receivedObject;
            if (!t.isSet()) {
                logger.debug("request to get mute state.");
                t.setValue(mediaPlayer.isMute());
                returnObject = t;
            } else {
                logger.debug("Request to set mute to " + t.getValue());
                mediaPlayer.mute(t.getValue());
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

    private long _getLength() throws InterruptedException {
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

        System.exit(0);
    }

    public void requestStop() {
        stop = true;
    }
}

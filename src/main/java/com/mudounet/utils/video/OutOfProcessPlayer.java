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

import com.mudounet.utils.video.remotecommands.*;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;

/**
 * Sits out of process so as not to crash the primary VM.
 * @author Michael
 */
public abstract class OutOfProcessPlayer {

    protected MediaPlayer mediaPlayer;
    private float snapshotPercPosition;
    private long snapshotTimePosition;
    private long length = 0;
    final CountDownLatch inTimePositionLatch = new CountDownLatch(1);
    final CountDownLatch inPercPositionLatch = new CountDownLatch(1);
    final CountDownLatch snapshotTakenLatch = new CountDownLatch(1);
    final CountDownLatch operationFinished = new CountDownLatch(1);

    /**
     * Start the main loop reading from the standard input stream and writing
     * to sout.
     * @throws IOException if something goes wrong.
     * @throws ClassNotFoundException  
     */
    public void read() throws IOException, ClassNotFoundException, InterruptedException {

        //BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        System.err.println("Création des flux");
        ObjectOutputStream oos = new ObjectOutputStream(System.out);
        ObjectInputStream ois = new ObjectInputStream(System.in);
// Création de l'output stream

        Object receivedObject;
        this.addSnapshotFunction();

        while ((receivedObject = ois.readObject()) != null) {

            if (receivedObject.getClass() == LoadFile.class) {
                System.err.println("Load command received : " + receivedObject);
                mediaPlayer.prepareMedia(((LoadFile) receivedObject).getFilePath(), getPrepareOptions());
                length = 0;
            } else if (receivedObject.getClass() == CloseCommand.class) {
                System.err.println("Close command received");
                close();

            } else if (receivedObject.getClass() == PlayCommand.class) {
                System.err.println("Play command received");
                mediaPlayer.play();
            } else if (receivedObject.getClass() == PauseCommand.class) {
                System.err.println("Pause command received");
                mediaPlayer.pause();
            } else if (receivedObject.getClass() == StopCommand.class) {
                System.err.println("Stop command received");
                mediaPlayer.stop();
            } else if (receivedObject.getClass() == SnapshotCommand.class) {
                System.err.println("Snapshot command received");
                SnapshotCommand t = (SnapshotCommand) receivedObject;

                try {
                    System.err.println("Snapshot command received : " + t.getTime() + "@path : " + t.getPath());
                    this.moveToTime(t.getTime());
            
                    this.takeSnapshot(t.getPath());
                    oos.writeObject(new BooleanCommand(true));
                    
                } catch (InterruptedException ex) {
                    ex.printStackTrace(System.err);
                }
                
                oos.writeObject(new BooleanCommand(false));

            } else if (receivedObject.getClass() == TimeCommand.class) {
                TimeCommand t = (TimeCommand) receivedObject;
                if (t.getValue() < 0) {
                    System.err.println("request to get time.");
                    t.setValue(mediaPlayer.getTime());
                    oos.writeObject(t);
                } else {
                    System.err.println("Request to set time to " + t.getValue());
                    mediaPlayer.setTime(t.getValue());
                }
            } else if (receivedObject.getClass() == LengthCommand.class) {
                LengthCommand t = (LengthCommand) receivedObject;
                System.err.println("Request to get length.");
                while((length = mediaPlayer.getLength()) == 0) {
                    
                }
                t.setValue(length);
                oos.writeObject(t);
            } else if (receivedObject.getClass() == MuteCommand.class) {
                MuteCommand t = (MuteCommand) receivedObject;
                if (!t.isSet()) {
                    System.err.println("request to get mute state.");
                    t.setValue(mediaPlayer.isMute());
                    oos.writeObject(t);
                } else {
                    System.err.println("Request to set mute to " + t.getValue());
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
                    System.err.println("State not currently managed : " + t.getValue());
                }
                oos.writeObject(t);
            } else {
                System.err.println("Unknown object : " + receivedObject);
            }
        }

        System.err.println("Flux crées");
        close();
    }

    private void close() {
        mediaPlayer.stop();
        mediaPlayer.release();
        System.exit(0);
    }

    private void addSnapshotFunction() {
        mediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {

            @Override
            public void timeChanged(MediaPlayer mediaPlayer, long newTime) {

                if (newTime >= snapshotTimePosition) {
                    System.err.println("Position reached (time in ms) : " + newTime + ")");
                    inTimePositionLatch.countDown();
                }
            }

            @Override
            public void positionChanged(MediaPlayer mediaPlayer, float newPosition) {

                if (newPosition >= snapshotPercPosition * 0.9f) { /* 90% margin */
                    System.err.println("Position reached (%) : " + newPosition + ")");
                    inPercPositionLatch.countDown();
                }
            }

            @Override
            public void lengthChanged(MediaPlayer mediaPlayer, long newLength) {
                operationFinished.countDown();
            }

            @Override
            public void snapshotTaken(MediaPlayer mediaPlayer, String filename) {
                System.err.println("snapshotTaken(filename=" + filename + ")");
                snapshotTakenLatch.countDown();
            }
        });
    }

    private void moveToPerc(float newPosition) throws InterruptedException {
        this.snapshotPercPosition = newPosition;
        mediaPlayer.setPosition(newPosition);
        inPercPositionLatch.await(); // Might wait forever if error
    }

    private void moveToTime(long newPosition) throws InterruptedException {
        this.snapshotTimePosition = newPosition;
        mediaPlayer.setTime(newPosition);
        inTimePositionLatch.await(); // Might wait forever if error
    }

    private void takeSnapshot(File file) throws InterruptedException {
        mediaPlayer.saveSnapshot(new File(file.getAbsolutePath()));
        snapshotTakenLatch.await(); // Might wait forever if error
    }

    /**
     * This method should return an array of any options that need to be passed 
     * onto VLCJ and in turn libvlc. If no options are required, an empty array
     * should be returned rather than null.
     * @return the options required by libvlc.
     */
    public abstract String[] getPrepareOptions();

    private void takeSnapshot(String path) throws InterruptedException {
        this.takeSnapshot(new File(path));
    }
}

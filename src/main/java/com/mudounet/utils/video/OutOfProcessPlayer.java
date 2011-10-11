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
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import uk.co.caprica.vlcj.player.MediaPlayer;

/**
 * Sits out of process so as not to crash the primary VM.
 * @author Michael
 */
public abstract class OutOfProcessPlayer {

    protected MediaPlayer mediaPlayer;

    /**
     * Start the main loop reading from the standard input stream and writing
     * to sout.
     * @param mediaPlayer the media player to control via the commands 
     * received.
     * @throws IOException if something goes wrong.
     */
    public void read() throws IOException, ClassNotFoundException {

        //BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        System.err.println("Création des flux");
        ObjectOutputStream oos = new ObjectOutputStream(System.out);
        ObjectInputStream ois = new ObjectInputStream(System.in);
// Création de l'output stream

        Object receivedObject;

        while ((receivedObject = ois.readObject()) != null) {

            if (receivedObject.getClass() == LoadFile.class) {
                System.err.println("Load command received : " + receivedObject);
                mediaPlayer.prepareMedia(((LoadFile) receivedObject).getFilePath(), getPrepareOptions());
            } else if (receivedObject.getClass() == CloseCommand.class) {
                System.err.println("Close command received");
                System.exit(0);
            } else if (receivedObject.getClass() == PlayCommand.class) {
                System.err.println("Play command received");
                mediaPlayer.play();
            } else if (receivedObject.getClass() == PauseCommand.class) {
                System.err.println("Pause command received");
                mediaPlayer.pause();
            } else if (receivedObject.getClass() == StopCommand.class) {
                System.err.println("Stop command received");
                mediaPlayer.stop();
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
                t.setValue(mediaPlayer.getLength());
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

        System.exit(0);
        System.err.println("Flux crées");

    }

    /**
     * This method should return an array of any options that need to be passed 
     * onto VLCJ and in turn libvlc. If no options are required, an empty array
     * should be returned rather than null.
     * @return the options required by libvlc.
     */
    public abstract String[] getPrepareOptions();
}

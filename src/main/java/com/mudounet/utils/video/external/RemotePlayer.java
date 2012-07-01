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

import com.mudounet.utils.video.AbstractPlayer;
import com.mudounet.hibernate.movies.others.TechData;
import com.mudounet.utils.video.classic.VideoPlayerException;
import com.mudounet.utils.video.remotecommands.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Controls an OutOfProcessPlayer via input / output process streams.
 * @author Michael
 */
public class RemotePlayer extends AbstractPlayer {

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean open;
    private boolean playing;
    private boolean paused;

    /**
     * Internal use only.
     */
    RemotePlayer(StreamWrapper wrapper) throws IOException {
        logger.debug("Begin to associate input stream");
        in = new ObjectInputStream(wrapper.getInputStream());
        logger.debug("Begin to associate output stream");
        out = new ObjectOutputStream(wrapper.getOutputStream());
        logger.debug("End of streams association");
        playing = false;
        open = true;
    }

    /**
     * Write a given command out to the remote VM player.
     * @param command the command to send.
     */
    private Object writeOut(Object command, boolean requireAnswer) throws VideoPlayerException {
        if (!open) {
            logger.error("This remote player has been closed!");
            throw new IllegalArgumentException("This remote player has been closed!");
        }
        try {
            logger.debug("Sending command " + command);
            out.writeObject(command);
            out.flush();
            if (!requireAnswer) {
                return null;
            }
            return getInput();
        } catch (IOException ex) {

            throw new RuntimeException("Couldn't perform operation", ex);
        }
    }

    private Object writeOut(Object command) throws VideoPlayerException {
        return writeOut(command, true);
    }

    /**
     * Block until receiving input from the remote VM player.
     * @return the input string received.
     */
    private Object getInput() throws VideoPlayerException {
        try {
            logger.debug("Awaiting command.");
            Object returnedInfo = in.readObject();
            if (returnedInfo.getClass() == VideoPlayerException.class) {
                throw new VideoPlayerException((VideoPlayerException) returnedInfo);
            } else {
                logger.debug("received command : " + returnedInfo);
                return returnedInfo;
            }
        } catch (IOException ex) {
            logger.error("IO error", ex);
        } catch (ClassNotFoundException ex) {
            logger.error("Class not found", ex);
        }
        return null;
    }

    /**
     * Load the given path into the remote player.
     * @param path the path to load.
     * @throws VideoPlayerException  
     */
    public void load(String path) throws VideoPlayerException {
        writeOut(new LoadFile(path));
    }

    /**
     * Play the loaded video.
     * @throws VideoPlayerException 
     */
    public void play() throws VideoPlayerException {
        writeOut(new PlayCommand());
        playing = true;
        paused = false;
    }

    /**
     * Pause the video.
     * @throws VideoPlayerException 
     */
    public void pause() throws VideoPlayerException {
        if (!paused) {
            writeOut(new PauseCommand());
            playing = false;
            paused = true;
        }
    }

    /**
     * Stop the video.
     * @throws VideoPlayerException 
     */
    public void stop() throws VideoPlayerException {
        writeOut(new StopCommand());
        playing = false;
        paused = false;
    }

    /**
     * Take a snapshot.
     * @param time
     * @param path
     * @return Snapshot is taken
     * @throws VideoPlayerException  
     */
    public boolean takeSnapshot(long time, String path) throws VideoPlayerException {

        SnapshotCommand c = new SnapshotCommand();
        c.setTime(time);
        c.setPath(path);
        logger.debug("Request snapshot :" + c);
        return ((BooleanCommand) writeOut(c)).getValue();
    }
    
        /**
     * Retrieve technical data.
     * @return technical Data
     * @throws VideoPlayerException  
     */
    public TechData retrieveTechData() throws VideoPlayerException {

        TechDataCommand c = new TechDataCommand();

        logger.debug("Request tech data :" + c);
        return ((TechData) writeOut(c));
    }

    /**
     * Determine if the current video is playable, i.e. one is loaded and 
     * ready to start playing when play() is called.
     * @return true if the video is playable, false otherwise.
     * @throws VideoPlayerException  
     */
    public boolean isPlayable() throws VideoPlayerException {
        return ((StateCommand) writeOut(new StateCommand(StateCommand.PLAYABLE))).getValue() == StateCommand.PLAYABLE;
    }

    /**
     * Get the length of the currently loaded video.
     * @return the length of the currently loaded video.
     * @throws VideoPlayerException  
     */
    public long getLength() throws VideoPlayerException {
        return ((LengthCommand) writeOut(new LengthCommand())).getValue();
    }

    /**
     * Get the time in milliseconds of the current position in the video.
     * @return the time in milliseconds of the current position in the video.
     * @throws VideoPlayerException  
     */
    public long getTime() throws VideoPlayerException {
        return ((TimeCommand) writeOut(new TimeCommand())).getValue();
    }

    /**
     * Set the time in milliseconds of the current position in the video.
     * @param time the time in milliseconds of the current position in the
     * video.
     * @throws VideoPlayerException  
     */
    public void setTime(long time) throws VideoPlayerException {
        writeOut(new TimeCommand(time));
    }

    /**
     * Determine if this video is muted.
     * @return true if it's muted, false if not.
     * @throws VideoPlayerException  
     */
    public boolean getMute() throws VideoPlayerException {
        return ((MuteCommand) writeOut(new MuteCommand())).getValue();
    }

    /**
     * Set whether this video is muted.
     * @param mute true to mute, false to unmute.
     * @throws VideoPlayerException  
     */
    public void setMute(boolean mute) throws VideoPlayerException {
        writeOut(new MuteCommand(mute));
    }

    /**
     * Terminate the OutOfProcessPlayer. MUST be called before closing, otherwise
     * the player won't quit!
     * @throws VideoPlayerException 
     */
    public void close() throws VideoPlayerException {
        if (open) {
            writeOut(new CloseCommand(), false);
            playing = false;
            open = false;
        }
    }

    /**
     * Determine whether the remote player is playing.
     * @return true if its playing, false otherwise.
     * @throws VideoPlayerException  
     */
    public boolean isPlaying() throws VideoPlayerException {
        return ((StateCommand) writeOut(new StateCommand(StateCommand.PLAYED))).getValue() == StateCommand.PLAYED;
    }

    /**
     * Determine whether the remote player is paused.
     * @return true if its paused, false otherwise.
     */
    public boolean isPaused() {
        return paused;
    }
}

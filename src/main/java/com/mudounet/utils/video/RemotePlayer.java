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
import org.apache.log4j.Logger;

/**
 * Controls an OutOfProcessPlayer via input / output process streams.
 * @author Michael
 */
public class RemotePlayer {

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean open;
    private boolean playing;
    protected static Logger logger = Logger.getLogger(RemotePlayer.class.getName());
    private boolean paused;

    /**
     * Internal use only.
     */
    RemotePlayer(StreamWrapper wrapper) throws IOException {
        out = new ObjectOutputStream(wrapper.getOutputStream());
        in = new ObjectInputStream(wrapper.getInputStream());
        playing = false;
        open = true;
    }

    /**
     * Write a given command out to the remote VM player.
     * @param command the command to send.
     */
    private void writeOut(Object command) {
        if (!open) {
            logger.error("This remote player has been closed!");
            throw new IllegalArgumentException("This remote player has been closed!");
        }
        try {
            logger.debug("Sending command "+command);
            out.writeObject(command);
            out.flush();
        }
        catch (IOException ex) {
            
            throw new RuntimeException("Couldn't perform operation", ex);
        }
    }

    /**
     * Block until receiving input from the remote VM player.
     * @return the input string received.
     */
    private Object getInput() {
        try {
            Object returnedInfo = in.readObject();
            logger.debug("received command : "+returnedInfo);
            return returnedInfo;
        }
        catch (Exception ex) {
            throw new RuntimeException("Couldn't perform operation", ex);
        }
    }

    /**
     * Load the given path into the remote player.
     * @param path the path to load.
     */
    public void load(String path) {
        writeOut(new LoadFile(path));
        logger.error(getInput());
    }

    /**
     * Play the loaded video.
     */
    public void play() {
        writeOut(new PlayCommand());
        playing = true;
        paused = false;
    }

    /**
     * Pause the video.
     */
    public void pause() {
        if(!paused) {
            writeOut("pause");
            playing = false;
            paused = true;
        }
    }

    /**
     * Stop the video.
     */
    public void stop() {
        writeOut("stop");
        playing = false;
        paused = false;
    }

    /**
     * Determine if the current video is playable, i.e. one is loaded and 
     * ready to start playing when play() is called.
     * @return true if the video is playable, false otherwise.
     */
    public boolean isPlayable() {
        writeOut("playable?");
        return Boolean.parseBoolean(getInput().toString());
    }

    /**
     * Get the length of the currently loaded video.
     * @return the length of the currently loaded video.
     */
    public long getLength() {
        writeOut("length?");
        return Long.parseLong(getInput().toString());
    }

    /**
     * Get the time in milliseconds of the current position in the video.
     * @return the time in milliseconds of the current position in the video.
     */
    public long getTime() {
        writeOut("time?");
        return Long.parseLong(getInput().toString());
    }

    /**
     * Set the time in milliseconds of the current position in the video.
     * @param time the time in milliseconds of the current position in the
     * video.
     */
    public void setTime(long time) {
        writeOut("setTime " + time);
    }

    /**
     * Determine if this video is muted.
     * @return true if it's muted, false if not.
     */
    public boolean getMute() {
        writeOut("mute?");
        return Boolean.parseBoolean(getInput().toString());
    }

    /**
     * Set whether this video is muted.
     * @param mute true to mute, false to unmute.
     */
    public void setMute(boolean mute) {
        writeOut("setMute " + mute);
    }

    /**
     * Terminate the OutOfProcessPlayer. MUST be called before closing, otherwise
     * the player won't quit!
     */
    public void close() {
        if (open) {
            writeOut("close");
            playing = false;
            open = false;
        }
    }

    /**
     * Determine whether the remote player is playing.
     * @return true if its playing, false otherwise.
     */
    public boolean isPlaying() {
        return playing;
    }
    
    /**
     * Determine whether the remote player is paused.
     * @return true if its paused, false otherwise.
     */
    public boolean isPaused() {
        return paused;
    }
    
}

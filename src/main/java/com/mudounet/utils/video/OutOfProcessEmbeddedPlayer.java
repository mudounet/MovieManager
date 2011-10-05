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

import com.sun.jna.NativeLibrary;
import java.awt.Canvas;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import org.apache.log4j.Logger;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;

/**
 * An embedded player that sits out of process in a separate VM. In process
 * players are unstable using VLC due to concurrency bugs in libvlc and its
 * dependencies, so we need to fall back on a mechanism that's a little more
 * complicated than the norm!
 * @author Michael
 */
public class OutOfProcessEmbeddedPlayer extends OutOfProcessPlayer {

    private EmbeddedMediaPlayer mediaPlayer;
    private CanvasVideoSurface videoSurface;
    protected static Logger logger = Logger.getLogger(OutOfProcessEmbeddedPlayer.class.getName());
    private Canvas canvas;

    public OutOfProcessEmbeddedPlayer(final long canvasId) throws IOException {

        canvas = new Canvas();

        MediaPlayerFactory factory = new MediaPlayerFactory("--no-video-title");
        mediaPlayer = factory.newEmbeddedMediaPlayer();

        videoSurface = factory.newVideoSurface(canvas);

        mediaPlayer.setVideoSurface(videoSurface); //Required with a dummy canvas to active the above nativeSetVideoSurface method

    }

    /**
     * No special options needed for this player.
     * @return an empty string array.
     */
    @Override
    public String[] getPrepareOptions() {
        return new String[0];
    }
    /**
     * Set this to true if we want to test a file straight off.
     */
    private static final boolean TEST_MODE = false;

    /**
     * Testing stuff.
     * @param args 
     */
    public static void main(String[] args) {
        if (TEST_MODE) {
            args = new String[]{"0"};
        }
        File nativeDir = new File("lib/native");
        NativeLibrary.addSearchPath("libvlc", nativeDir.getAbsolutePath());
        NativeLibrary.addSearchPath("vlc", nativeDir.getAbsolutePath());
        PrintStream stream = null;
        try {
            stream = new PrintStream(new File("ooplog.txt"));
            System.setErr(stream); //Important, MUST redirect err stream
            OutOfProcessEmbeddedPlayer player = new OutOfProcessEmbeddedPlayer(Integer.parseInt(args[0]));
            if (TEST_MODE) {
                player.mediaPlayer.prepareMedia("dvdsimple://E:");
                player.mediaPlayer.play();
            } else {
                player.read(player.mediaPlayer);
            }
        } catch (Exception ex) {
            logger.warn(ex);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }
}

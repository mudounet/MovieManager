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

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * An internal test class to play around with the video stuff without launching
 * a full Quelea instance.
 * @author Michael
 */
public class Test {

    /**
     * Start the test.
     * @param args command line arguments.
     * @throws Exception if something goes wrong.
     */
    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame();
        frame.setLayout(new GridLayout(1, 2));
        Canvas panel = new Canvas();
        panel.setPreferredSize(new Dimension(300,300));
        Canvas panel2 = new Canvas();
        panel2.setPreferredSize(new Dimension(300,300));
        frame.add(panel);
        frame.add(panel2);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        RemotePlayer player = RemotePlayerFactory.getEmbeddedRemotePlayer(panel);
        player.load("sample_video.flv");
        player.play();
        RemotePlayer player2 = RemotePlayerFactory.getEmbeddedRemotePlayer(panel2);
        player2.load("sample_video.flv");
        player2.play();
        //go();
    }

    /**
     * Fire off the test.
     */
    public static void go() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                JFrame frame = new JFrame();
                frame.setLayout(new GridLayout(1, 2));
                VideoControlPanel panel1 = new VideoControlPanel();
                panel1.loadVideo("http://www.youtube.com/watch?v=W0WD4nduCsg");
                panel1.playVideo();
                frame.add(panel1);
                frame.pack();
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
    }
}

package com.mudounet.ui;

/**
 * @(#)DialogMovieManager.java
 *
 * Copyright (2003) Bro3
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Boston, MA 02111.
 *
 * Contact: bro3@users.sourceforge.net
 *
 */
import com.mudounet.MovieManagerConfig;
import com.mudounet.ui.swing.ext.FileDrop;
import com.mudounet.utils.Utils;
import com.mudounet.utils.managers.MovieManager;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DialogMovieManager extends JFrame implements ComponentListener {

    protected static final Logger logger = LoggerFactory.getLogger(Utils.class.getName());
    private static final long serialVersionUID = 1L;
    JPanel myPanel = new JPanel();
    public static MovieManagerConfig config = MovieManager.getConfig();

    public DialogMovieManager() {
        FileDrop fileDrop = new FileDrop(myPanel, new FileDrop.Listener() {

            public void filesDropped(java.io.File[] files) {
                // handle file drop
            }   // end filesDropped
        });
    }

    public void setup() {

        logger.debug("Start setting up the MovieManager."); //$NON-NLS-1$
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Point location = config.getScreenLocation();

        if (location != null && location.getX() < screenSize.getWidth() && location.getY() < screenSize.getHeight()) {
            setLocation(location);
        } else {
            setLocation((int) (screenSize.getWidth() - getSize().getWidth()) / 2,
                    (int) (screenSize.getHeight() - getSize().getHeight()) / 2 - 12);
        }

        
        logger.debug("MovieManager SetUp done!"); //$NON-NLS-1$
    }

    public void componentResized(ComponentEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void componentMoved(ComponentEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void componentShown(ComponentEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void componentHidden(ComponentEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
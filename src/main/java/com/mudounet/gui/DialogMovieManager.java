/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.gui;

import com.mudounet.MovieManager;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JFrame;
import javax.swing.JTree;

/**
 *
 * @author isabelle
 */
public class DialogMovieManager extends JFrame implements ComponentListener {

    private static final long serialVersionUID = 1L;
    private int movieListWidth = 0;
    public int fontSize = 12;
    public static MovieManagerConfig config = MovieManager.getConfig();
    private JTree moviesList;

    public void componentResized(ComponentEvent ce) {
        movieListWidth = (int) getMoviesList().getSize().getWidth();

        /*
         * Maximized
         */
        if (getExtendedState() == JFrame.MAXIMIZED_BOTH) {
            config.setMainMaximized(true);
        } else {
            config.setMainSize(getMainSize());
            config.setMainMaximized(false);
        }
    }

    public void componentMoved(ComponentEvent ce) {
        if (isShowing()) {
            config.setScreenLocation(getLocationOnScreen());
        }
    }

    public Dimension getMainSize() {
        return this.getSize();
    }

    public void componentShown(ComponentEvent ce) {
    }

    public void componentHidden(ComponentEvent ce) {
    }

    /**
     * Gets the Movie List.
     *
     * @return JList that displays the MovieList.
     *
     */
    public JTree getMoviesList() {
        return moviesList;
    }

    /**
     * 
     * @throws Throwable
     */
    @Override
    public void finalize() throws Throwable {
        super.finalize();
        dispose();
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.gui;

import com.mudounet.MovieManager;
import com.mudounet.models.ModelMovie;
import com.mudounet.utils.FileUtil;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import javax.swing.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author isabelle
 */
public class DialogMovieManager extends JFrame implements ComponentListener {

    private static final long serialVersionUID = 1L;
    private static Logger log = LoggerFactory.getLogger(DialogMovieManager.class.getName());
    private int movieListWidth = 0;
    public int fontSize = 12;
    public static MovieManagerConfig config = MovieManager.getConfig();
    MovieManagerMenuBar menuBar = null;
    private JTree moviesList;
    ArrayList<ModelMovie> currentMovieList;

    public static JApplet getApplet() {
        log.warn("JApplet is not defined");
        return null;
    }
    private JSplitPane mainWindowSplitPane;
    private JSplitPane movieInfoSplitPane;
    private JSplitPane additionalInfoNotesSplitPane;

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

    public void setCurrentMovieList(ArrayList<ModelMovie> currentMovieList) {
        this.currentMovieList = currentMovieList;
    }

    public ArrayList<ModelMovie> getCurrentMoviesList() {
        return currentMovieList;
    }

    @Override
    public void finalize() {
        dispose();
    }

    public void setUp() {
        /*
         * Starts other inits.
         */
        log.debug("Start setting up the MovieManager."); //$NON-NLS-1$

        Toolkit.getDefaultToolkit().setDynamicLayout(true);
        // Tooltip delay
        ToolTipManager.sharedInstance().setDismissDelay(100000);

        if (!MovieManager.isApplet()) {
            System.setProperty("sun.awt.noerasebackground", "true"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        setTitle(config.sysSettings.getAppTitle()); //$NON-NLS-1$
        setIconImage(FileUtil.getImage("/images/film.png").getScaledInstance(16, 16, Image.SCALE_SMOOTH)); //$NON-NLS-1$

        setJMenuBar(createMenuBar());

        getContentPane().add(createWorkingArea(), BorderLayout.CENTER);

        setResizable(true);

        setHotkeyModifiers();

        /*
         * Hides database related components.
         */
        menuBar.setDatabaseComponentsEnable(false);

        updateJTreeIcons();

        addComponentListener(this);

        /*
         * All done, pack.
         */
        pack();

        setSize(config.mainSize);
        if (config.getMainMaximized()) {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        }


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Point location = config.getScreenLocation();

        if (location != null && location.getX() < screenSize.getWidth() && location.getY() < screenSize.getHeight()) {
            setLocation(location);
        } else {
            setLocation((int) (screenSize.getWidth() - getSize().getWidth()) / 2,
                    (int) (screenSize.getHeight() - getSize().getHeight()) / 2 - 12);
        }


        /*
         * Setting Main Window slider position
         */
        if (config.mainWindowSliderPosition == -1) {
            getMainWindowSplitPane().setDividerLocation(0.537);
            getMainWindowSplitPane().setLastDividerLocation(getMainWindowSplitPane().getDividerLocation());
        } else {
            getMainWindowSplitPane().setDividerLocation(config.mainWindowSliderPosition);
            if (config.mainWindowLastSliderPosition != -1) {
                getMainWindowSplitPane().setLastDividerLocation(config.mainWindowLastSliderPosition);
            }
        }

        /*
         * Setting Movie Info slider position
         */
        if (config.movieInfoSliderPosition == -1) {
            getMovieInfoSplitPane().setDividerLocation(0.5);
            getMovieInfoSplitPane().setLastDividerLocation(getMovieInfoSplitPane().getDividerLocation());
        } else if (getMovieInfoSplitPane() != null) {
            getMovieInfoSplitPane().setDividerLocation(config.movieInfoSliderPosition);

            if (config.movieInfoLastSliderPosition != -1) {
                getMovieInfoSplitPane().setLastDividerLocation(config.movieInfoLastSliderPosition);
            }
        }


        if (getAdditionalInfoNotesSplitPane() != null) {

            /*
             * Setting Additional Info / Notes slider position
             */
            if (config.additionalInfoNotesSliderPosition == -1) {
                getAdditionalInfoNotesSplitPane().setDividerLocation(0.5);
                getAdditionalInfoNotesSplitPane().setLastDividerLocation(getAdditionalInfoNotesSplitPane().getDividerLocation());
            } else {
                getAdditionalInfoNotesSplitPane().setDividerLocation(config.additionalInfoNotesSliderPosition);

                if (config.additionalInfoNotesLastSliderPosition != -1) {
                    getAdditionalInfoNotesSplitPane().setLastDividerLocation(config.additionalInfoNotesLastSliderPosition);
                }
            }
        }

        resetInfoFieldsDisplay();


        log.debug("MovieManager SetUp done!"); //$NON-NLS-1$
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public JSplitPane getMainWindowSplitPane() {
        return mainWindowSplitPane;
    }

    public JSplitPane getMovieInfoSplitPane() {
        return movieInfoSplitPane;
    }

    public JSplitPane getAdditionalInfoNotesSplitPane() {
        return additionalInfoNotesSplitPane;
    }

    public void showDialog() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void setHotkeyModifiers() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void resetInfoFieldsDisplay() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private JMenuBar createMenuBar() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private Component createWorkingArea() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void updateJTreeIcons() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

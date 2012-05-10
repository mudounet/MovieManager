/**
 * @(#)MovieManager.java
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
package net.sf.xmm.moviemanager;

import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import net.sf.xmm.moviemanager.commands.MovieManagerCommandExit;
import net.sf.xmm.moviemanager.database.Database;
import net.sf.xmm.moviemanager.gui.DialogMovieManager;
import net.sf.xmm.moviemanager.imdblib.IMDbLib;
import net.sf.xmm.moviemanager.util.SysUtil;
import net.sf.xmm.moviemanager.util.plugins.MovieManagerLoginHandler;
import net.sf.xmm.moviemanager.util.plugins.MovieManagerStartupHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MovieManager {

    protected static final Logger log = LoggerFactory.getLogger(MovieManager.class.getName());
    static DatabaseHandler databaseHandler = new DatabaseHandler();

    /**
     * Reference to the only instance of MovieManagerConfig.
     *
     */
    public static MovieManagerConfig config = new MovieManagerConfig();
    /**
     * Reference to the only instance of MovieManager.
     *
     */
    static MovieManager movieManager;
    /**
     * Reference to the only instance of DialogMovieManager.
     *
     */
    static DialogMovieManager dialogMovieManager;
    /*
     * While multi-deleting, this is set to true
     */
    private boolean deleting = false;
    private boolean sandbox = false;

    public boolean isSandbox() {
        return sandbox;
    }

    /**
     * Creates the main movie manager dialog
     */
    void createDialog() {

        dialogMovieManager = new DialogMovieManager();

        dialogMovieManager.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                MovieManagerCommandExit.execute();
            }
        });
    }

    /**
     * Constructor.
     *
     */
    private MovieManager() {
    }

    /*
     * Applet feature is not finished
     */
    MovieManager(Object applet) {


        SysUtil.getAppMode();

        movieManager = this;
        dialogMovieManager = new DialogMovieManager(applet);

        EventQueue.invokeLater(new Runnable() {

            public final void run() {


                /*
                 * Writes the date.
                 */
                log.debug("Log Start: " + new Date(System.currentTimeMillis())); //$NON-NLS-1$
                log.debug("Starting applet"); //$NON-NLS-1$

                /*
                 * Loads the config
                 */
                config.loadConfig();

                /*
                 * Starts the MovieManager.
                 */
                MovieManager.getDialog().setUp();

                /*
                 * Loads the database.
                 */
                databaseHandler.loadDatabase();
            }
        });
    }

    /**
     * Returns a reference to the only instance of MovieManager.
     *
     * @return Reference to the only instance of MovieManager.
     *
     */
    public static MovieManager getIt() {
        return movieManager;
    }

    /**
     * Returns a reference to the only instance of MovieManager.
     *
     * @return Reference to the only instance of the DialogMovieManager.
     *
     */
    public static DialogMovieManager getDialog() {
        return dialogMovieManager;
    }

    /**
     * Returns a reference to the only instance of MovieManager.
     *
     * @return Reference to the only instance of MovieManager.
     *
     */
    public static MovieManagerConfig getConfig() {
        return config;
    }

    public static DatabaseHandler getDatabaseHandler() {
        return databaseHandler;
    }



    /**
     * Returns the current database.
     *
     * @return The current database.
     *
     */
    synchronized public Database getDatabase() {
        return databaseHandler.getDatabase();
    }

    public int getHeight() {
        return dialogMovieManager.getHeight();
    }

    public int getWidth() {
        return dialogMovieManager.getWidth();
    }

    public Point getLocation() {
        return dialogMovieManager.getLocation();
    }

    public int getFontSize() {
        return dialogMovieManager.getFontSize();
    }

    public static boolean isApplet() {
        return DialogMovieManager.isApplet();
    }

    public void addDatabaseList(String listName) {

        log.info("Ceating list " + listName);

        getDatabase().addListsColumn(listName);
        MovieManager.getConfig().addToCurrentLists(listName);

        MovieManager.getDialog().loadMenuLists();
    }

    public void setDeleting(boolean deleting) {
        this.deleting = deleting;
    }

    public boolean isDeleting() {
        return deleting;
    }

    public static void exit() {

        if (isApplet()) {
            DialogMovieManager.destroy();
        } else {
            System.exit(0);
        }
    }

    public static void main(String args[]) {

        boolean sandbox = SysUtil.isRestrictedSandbox();

        // Uses this to check if the app is running in a sandbox with limited privileges
        try {

        } catch (java.security.AccessControlException s) {
            s.printStackTrace();
            sandbox = true;
        }


        // Places the Log file in the user directory (the program location)

        /*
         * Writes the date.
         */
        log.debug("================================================================================"); //$NON-NLS-1$
        log.debug("Log Start: " + new Date(System.currentTimeMillis())); //$NON-NLS-1$
        log.debug("MeD's Movie Manager v" + config.sysSettings.getVersion()); //$NON-NLS-1$
        log.debug("MovieManager release:" + MovieManager.getConfig().sysSettings.getRelease() + " - "
                + "IMDb Lib release:" + IMDbLib.getRelease() + " (" + IMDbLib.getVersion() + ")");
        log.debug(SysUtil.getSystemInfo(SysUtil.getLineSeparator())); //$NON-NLS-1$

        /*
         * Loads the config
         */
        if (!sandbox) {
            config.loadConfig();
        }

        // Calls the plugin startup method 
        MovieManagerStartupHandler startupHandler = MovieManager.getConfig().getStartupHandler();

        if (startupHandler != null) {
            startupHandler.startUp();
        }

        if (!sandbox) {



            SysUtil.includeJarFilesInClasspath("lib/drivers");

        }

        movieManager = new MovieManager();
        movieManager.sandbox = sandbox;

        EventQueue.invokeLater(new Runnable() {

            public final void run() {

                try {

                    log.debug("Creating MovieManager Dialog");
                    movieManager.createDialog();

                    /*
                     * Starts the MovieManager.
                     */
                    MovieManager.getDialog().setUp();
                    log.debug("MovieManager Dialog - setup.");

                    MovieManager.getDialog().showDialog();


                    // Calls the plugin startup method 
                    MovieManagerLoginHandler loginHandler = MovieManager.getConfig().getLoginHandler();

                    if (loginHandler != null) {
                        loginHandler.loginStartUp();
                    }

                    log.debug("Loading Database....");

                    /*
                     * Loads the database.
                     */
                    databaseHandler.loadDatabase(true);

                    log.debug("Database loaded.");

                } catch (Exception e) {
                    log.error("Exception occured while intializing MeD's Movie Manager", e);
                }
            }
        });
    }
}
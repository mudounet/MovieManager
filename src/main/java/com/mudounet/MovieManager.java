/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet;

import com.mudounet.commands.MovieManagerCommandExit;
import com.mudounet.database.Database;
import com.mudounet.gui.DialogMovieManager;
import com.mudounet.gui.MovieManagerConfig;
import com.mudounet.hibernate.Movie;
import com.mudounet.hibernate.MovieProxy;
import com.mudounet.ui.swing.ext.MovieTable;
import com.mudounet.utils.MovieFileFilter;
import com.mudounet.utils.SysUtil;
import com.mudounet.utils.hibernate.DataAccessLayerException;
import com.mudounet.utils.hibernate.HibernateUtils;
import com.mudounet.utils.managers.MovieListManager;
import com.mudounet.utils.managers.SimpleTagManager;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.swing.JFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author isabelle
 */
public class MovieManager {
    
    static DatabaseHandler databaseHandler = new DatabaseHandler();

    private static MovieManagerConfig config = new MovieManagerConfig();
    /**
     * Reference to the only instance of DialogMovieManager.
     *
     */
    private static DialogMovieManager dialogMovieManager;
    /**
     * Reference to the only instance of MovieManager.
     *
     */
    static MovieManager movieManager;

    public static boolean isApplet() {
        return false;
    }

    /**
     * Returns a reference to the only instance of MovieManager.
     *
     * @return Reference to the only instance of MovieManager.
     **/
    public static MovieManager getIt() {
        return movieManager;
    }

    public static DatabaseHandler getDatabaseHandler() {
        return databaseHandler;
    }
    
    private boolean sandbox = false;

    public static void exit() {
        throw new UnsupportedOperationException("Not yet implemented");
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
     * Creates the main movie manager dialog
     */
    void createDialog() {

        dialogMovieManager = new DialogMovieManager();

        dialogMovieManager.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                MovieManagerCommandExit.execute();
            }
        });
    }

    public static MovieManagerConfig getConfig() {
        return config;
    }
    protected static Logger logger = LoggerFactory.getLogger(MovieManager.class.getName());

    public static void main(String[] args) throws DataAccessLayerException, IOException, InterruptedException, Exception {

        boolean sandbox = SysUtil.isRestrictedSandbox();
        
        movieManager = new MovieManager();
        movieManager.sandbox = sandbox;


        EventQueue.invokeLater(new Runnable() {

            public final void run() {

                try {


                    logger.debug("Creating MovieManager Dialog");
                    movieManager.createDialog();

                    /*
                     * Starts the MovieManager.
                     */
                    MovieManager.getDialog().setUp();
                    logger.debug("MovieManager Dialog - setup.");

                    MovieManager.getDialog().showDialog();



                    logger.debug("Loading Database....");

                    /*
                     * Loads the database.
                     */
                    databaseHandler.loadDatabase(true);

                    logger.error("Database method not loaded.");


                } catch (Exception e) {
                    logger.error("Exception occured while intializing MeD's Movie Manager", e);
                }
            }
        });


        System.out.println("Building Movie list...");
        List<File> movieList = readDirWithMovies(GlobalProperties.getMoviesDirectory());

        if (movieList.isEmpty()) {
            logger.error("No movies found in path " + GlobalProperties.getMoviesDirectory().getAbsolutePath());
            System.exit(0);
        }

        SimpleTagManager manager = new SimpleTagManager();

        List<Movie> listOfMovies = manager.getMovies();
        GlobalVariables.setListOfMovies(listOfMovies);

        if (listOfMovies.size() != movieList.size()) {
            for (File file : movieList) {
                checkOrUpdateMovie(listOfMovies, file);
            }
        }

        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                createAndShowGUI();
            }
        });

        HibernateUtils.destroySession();
        HibernateUtils.closeAll();
        Thread.sleep(5000);
    }

    public static List<File> readDirWithMovies(File directory) {
        File[] listFiles = directory.listFiles(new MovieFileFilter());
        List<File> listOfMovies = Arrays.asList(listFiles);
        return listOfMovies;
    }

    public static void checkOrUpdateMovie(List<Movie> movies, File file) throws Exception {
        MovieProxy movieProxy = new MovieProxy(file);
        movieProxy.getFastMd5();
        Movie movie = movieProxy.getMovie();

        int index = movies.indexOf(movie);

        if (index >= 0) {
            logger.debug("EXISTS: " + file.getName());
            movie = movies.get(index);
        } else {
            logger.info("NEW: " + file.getName());
            movieProxy.getMd5();
            movie = MovieListManager.addMovie(movieProxy.getMovie());
        }

        if (movie.getMediaInfo() == null) {
            MovieListManager.addBasicInfosToMovie(movieProxy);
        }

        if (movie.getSnapshots().isEmpty()) {
            MovieListManager.genSnapshotsToMovie(movieProxy);
        }

        if (GlobalVariables.getTemplate().isSessionOpened()) {
            GlobalVariables.getTemplate().closeSession();
        }
    }

    /**
     * Create the GUI and show it. For thread safety, this method should be
     * invoked from the event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("TableRenderDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        MovieTable newContentPane = new MovieTable();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

/**
     * Returns the current database.
     *
     * @return The current database.
     **/
    synchronized public Database getDatabase() {
        return databaseHandler.getDatabase();
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet;

import com.mudounet.gui.MovieManagerConfig;
import com.mudounet.hibernate.Movie;
import com.mudounet.hibernate.MovieProxy;
import com.mudounet.ui.swing.ext.MovieTable;
import com.mudounet.utils.MovieFileFilter;
import com.mudounet.utils.hibernate.DataAccessLayerException;
import com.mudounet.utils.hibernate.HibernateUtils;
import com.mudounet.utils.managers.MovieListManager;
import com.mudounet.utils.managers.SimpleTagManager;
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
    private static MovieManagerConfig movieManagerConfig;

    public static MovieManagerConfig getConfig() {
        return movieManagerConfig;
    }
    protected static Logger logger = LoggerFactory.getLogger(MovieManager.class.getName());

    public static void main(String[] args) throws DataAccessLayerException, IOException, InterruptedException, Exception {

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
}

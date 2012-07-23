/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet;

import com.mudounet.hibernate.Movie;
import com.mudounet.ui.swing.ext.MovieTable;
import com.mudounet.utils.MovieFileFilter;
import com.mudounet.utils.hibernate.DataAccessLayerException;
import com.mudounet.utils.hibernate.HibernateThreadSession;
import com.mudounet.utils.hibernate.HibernateUtils;
import com.mudounet.utils.managers.MovieListManager;
import com.mudounet.utils.managers.SimpleTagManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import javax.swing.JFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author isabelle
 */
public class App {

    protected static Logger logger = LoggerFactory.getLogger(App.class.getName());
    public static HibernateThreadSession template = HibernateUtils.currentSession();
    public static Properties properties = new Properties();
    public static File initDirectory;
    public static List<Movie> listOfMovies;

    public static void main(String[] args) throws DataAccessLayerException, IOException, InterruptedException, Exception {

        System.out.println("Loading properties...");
        App.properties.load(new FileInputStream("app.properties"));

        initDirectory = new File(App.properties.getProperty("init.directory"));

        if (initDirectory.isDirectory()) {
            logger.info("Base directory : " + initDirectory.getAbsolutePath());
        } else {
            logger.error("Init directory is not defined correctly : " + initDirectory.getAbsolutePath());
            System.exit(0);
        }

        System.out.println("Building Movie list...");
        List<File> movieList = readDirWithMovies(initDirectory);

        if (movieList.isEmpty()) {
            logger.error("No movies found in path " + initDirectory.getAbsolutePath());
            System.exit(0);
        }

        SimpleTagManager manager = new SimpleTagManager();

        listOfMovies = manager.getMovies();

        for (File file : movieList) {
            checkOrUpdateMovie(listOfMovies, file);
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
        Movie movie = new Movie();
        movie.setRealFilename(file.getAbsolutePath());
        movie.setSize(file.length());
        movie.getFastMd5();

        int index = movies.indexOf(movie);
        if (index >= 0) {
            logger.debug("EXISTS: " + file.getName());
            movie = movies.get(index);
            movie.setRealFilename(initDirectory.getAbsolutePath() + "/" + movie.getFilename());
        } else {
            logger.info("NEW: " + file.getName());
            movie = MovieListManager.addMovie(file.getAbsolutePath(), file.getName());
        }

        if (movie.getMediaInfo() == null) {
            MovieListManager.addBasicInfosToMovie(movie);
        }
        
        if(movie.getSnapshots().isEmpty()) {
            MovieListManager.genSnapshotsToMovie(movie);
        }

        if (template.isSessionOpened()) {
            template.closeSession();
        }
    }
    
        /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("TableRenderDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Create and set up the content pane.
        MovieTable newContentPane = new MovieTable(listOfMovies);
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    

}

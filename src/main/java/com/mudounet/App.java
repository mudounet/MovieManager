/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet;

import com.mudounet.hibernate.Movie;
import com.mudounet.utils.MovieFileFilter;
import com.mudounet.utils.hibernate.AbstractDao;
import com.mudounet.utils.hibernate.DataAccessLayerException;
import com.mudounet.utils.hibernate.HibernateFactory;
import com.mudounet.utils.managers.MovieListManager;
import com.mudounet.utils.managers.SimpleTagManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author isabelle
 */
public class App {

    protected static Logger logger = LoggerFactory.getLogger(App.class.getName());
    public static AbstractDao template;
    public static Properties properties = new Properties();

    public static void main(String[] args) throws DataAccessLayerException, IOException {

        System.out.println("Loading properties...");
        App.properties.load(new FileInputStream("app.properties"));
        
        File initDirectory = new File( App.properties.getProperty("init.directory"));
        
        if(initDirectory.isDirectory()) {
            logger.info("Base directory : "+initDirectory.getAbsolutePath());
        }
        else {
            logger.error("Init directory is not defined correctly : "+initDirectory.getAbsolutePath());
            System.exit(0);
        }
        
        System.out.println("Building Hibernate...");
        HibernateFactory.buildSessionFactory();
        template = new AbstractDao();

        System.out.println("Building Movie list...");

        List<File> listOfMovies = readDirWithMovies(initDirectory);
        
        if(listOfMovies.isEmpty()) {
            logger.error("No movies found in path "+initDirectory.getAbsolutePath());
            System.exit(0);
        }
        
        SimpleTagManager manager = new SimpleTagManager(template);
        
        List<Movie> movies = manager.getMovies();
        
        for (File e : listOfMovies) {
            if(movies.contains(e)) {
                 logger.debug("EXISTS: "+e.getName());
            } 
            else {
                logger.info("NEW: "+e.getName());
                MovieListManager.addMovie(e.getAbsolutePath(), e.getName());
            }
        }
        template.closeConnection();
    }

    public static List<File> readDirWithMovies(File directory) {
        File[] listFiles = directory.listFiles(new MovieFileFilter());
        List<File> listOfMovies = Arrays.asList(listFiles);
        return listOfMovies;
    }
}

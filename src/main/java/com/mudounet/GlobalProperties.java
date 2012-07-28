/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet;

import com.mudounet.hibernate.Movie;
import com.mudounet.utils.hibernate.HibernateThreadSession;
import com.mudounet.utils.hibernate.HibernateUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author gmanciet
 */
public class GlobalProperties {

    private static File moviesDirectory;
    private static File snapshotDirectory;
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(GlobalProperties.class.getName());
    private static HibernateThreadSession template = HibernateUtils.currentSession();
    private static Properties properties = new Properties();

    static {
        try {
            logger.debug("Initializing static class "+GlobalProperties.class.getName());
            GlobalProperties.properties.load(new FileInputStream("app.properties"));
            moviesDirectory = loadDirectory("movies.dir");
            snapshotDirectory = loadDirectory("snapshots.dir");
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }
    
    private static File loadDirectory(String dirProperty) {
        logger.debug("Loading directory "+dirProperty);
        String result = GlobalProperties.getProperties().getProperty(dirProperty);
        if(result == null) {
            logger.error("Property not found : "+dirProperty);
            return null;
        }
        File directory = new File(result);
        
        if (directory.isDirectory()) {
            logger.info("Base directory : " + directory.getAbsolutePath());
            return directory;
        } else {
            logger.error("\""+dirProperty+"\" directory is not defined correctly : " + directory.getAbsolutePath());
        }
        return null;
    }

    public static File getMoviesDirectory() {
        return moviesDirectory;
    }

    public static void setMoviesDirectory(File moviesDirectory) {
        GlobalProperties.moviesDirectory = moviesDirectory;
    }

    public static Properties getProperties() {
        return properties;
    }

    public static void setProperties(Properties properties) {
        GlobalProperties.properties = properties;
    }

    public static File getSnapshotDirectory() {
        return snapshotDirectory;
    }

    public static void setSnapshotDirectory(File snapshotDirectory) {
        GlobalProperties.snapshotDirectory = snapshotDirectory;
    }

    public static HibernateThreadSession getTemplate() {
        return template;
    }
}

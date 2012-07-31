/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.gui;

import java.awt.Dimension;
import java.awt.Point;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author isabelle
 */
public class MovieManagerConfig {

    private static Properties properties = new Properties();
    private static Logger logger = LoggerFactory.getLogger(MovieManagerConfig.class.getName());
    private static final String PROPERTY_FILE = "app.properties";
    public final SystemSettings sysSettings = new SystemSettings();
    private Point screenLocation;

    static {
        try {
            logger.debug("Initializing static class " + MovieManagerConfig.class.getName());
            properties.load(new FileInputStream(PROPERTY_FILE));
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }
    Dimension mainSize;
    int mainWindowSliderPosition;
    int mainWindowLastSliderPosition;
    int movieInfoSliderPosition;
    int movieInfoLastSliderPosition;
    int additionalInfoNotesSliderPosition;
    int additionalInfoNotesLastSliderPosition;

    private void writeProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    public void saveConfig() throws IOException {
        properties.store(new FileOutputStream(PROPERTY_FILE), "");
    }

    public Point getScreenLocation() {
        return screenLocation;
    }

    boolean getMainMaximized() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public boolean getUseJTreeCovers() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public boolean getUseJTreeIcons() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public int getMovieListRowHeight() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public String getCoversPath() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public boolean getStoreCoversLocally() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public class SystemSettings {

        /**
         * The current version of the program.
         *
         */
        private static final String _version = "0.1"; //$NON-NLS-1$
        String appTitle = " Mudounet's Movie Manager v" + getVersion().trim();

        /**
         * Returns the version.
         *
         * @return Program Version.
         *
         */
        public String getVersion() {
            return _version;
        }

        public String getAppTitle() {
            return appTitle;
        }

        public void setAppTitle(String t) {
            appTitle = t;
        }
    }

    public void setMainMaximized(boolean b) {
        writeProperty("Interface.maximized", Boolean.toString(b));
    }

    public void setScreenLocation(Point locationOnScreen) {
        writeProperty("Interface.locationOnScreen", locationOnScreen.toString());
    }

    void setMainSize(Dimension mainSize) {
        writeProperty("Interface.mainSize", mainSize.toString());
    }
}

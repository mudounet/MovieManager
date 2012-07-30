/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.gui;

import com.mudounet.GlobalProperties;
import java.awt.Dimension;
import java.awt.Point;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author isabelle
 */
public class MovieManagerConfig {

    private static Properties properties = new Properties();
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(MovieManagerConfig.class.getName());
    private static final String PROPERTY_FILE = "app.properties";

    static {
        try {
            logger.debug("Initializing static class " + MovieManagerConfig.class.getName());
            MovieManagerConfig.properties.load(new FileInputStream(PROPERTY_FILE));
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }

    private void writeProperty(String key, String value) {
        properties.setProperty("setMainMaximized", value);
        try {
            properties.store(new FileOutputStream(PROPERTY_FILE), "");
        } catch (IOException ex) {
            logger.error("Property write failed : "+ex.getMessage());
        }
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

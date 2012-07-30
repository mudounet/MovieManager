/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.commands;

import com.mudounet.MovieManager;
import com.mudounet.gui.DialogAlert;
import com.mudounet.swing.util.GUIUtil;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author isabelle
 */
public class MovieManagerCommandExit implements ActionListener {

    private static Logger logger = LoggerFactory.getLogger(MovieManagerCommandExit.class.getName());

    public static void execute() {
        try {
            shutDown();
        } catch (Exception e) {
            logger.error("Exception:" + e.getMessage(), e);


        } finally {
            MovieManager.exit();
        }
    }

    private static void shutDown() {
        try {
            // Saving config file
            MovieManager.getConfig().saveConfig();
        } catch (IOException io) {
            DialogAlert alert = new DialogAlert(MovieManager.getDialog(), "Config error", "<html>Error occured when saving config file:<br>" + io.getMessage() + " </html>", true);
            GUIUtil.showAndWait(alert, true);
        } catch (Exception e) {
            logger.error("Exception:" + e.getMessage(), e);
        }

        /*
         * Finalizes the main frame...
         */
        try {
                MovieManager.getDialog().finalize();
        } catch (Exception e) {
            logger.debug("MovieManager.getDialog().finalize() produced errors.");
        }
    }

    public void actionPerformed(ActionEvent ae) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

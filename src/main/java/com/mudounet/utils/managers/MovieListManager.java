/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.managers;

import com.mudounet.hibernate.Movie;
import com.mudounet.hibernate.tags.GenericTag;
import com.mudounet.utils.hibernate.AbstractDao;
import com.mudounet.utils.hibernate.DataAccessLayerException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author isabelle
 */
public class MovieListManager {

    private static AbstractDao template;

    public MovieListManager(AbstractDao template) {
        this.template = template;
    }

    public static boolean addMovie(String path, String title) {
        try {

            Movie m = MovieToolManager.buildMovie(path, title);
            template.saveOrUpdate(m);

        } catch (DataAccessLayerException ex) {
            Logger.getLogger(MovieListManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(MovieListManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MovieListManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    public static boolean addTagToMovie(Movie movie, GenericTag tag) {
        return false;
    }

    public static boolean addBasicInfosToMovie(Movie movie, Object mediaInfo) {
        return false;
    }
}

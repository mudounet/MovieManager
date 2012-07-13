/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.managers;

import com.mudounet.App;
import com.mudounet.hibernate.Movie;
import com.mudounet.hibernate.movies.others.MediaInfo;
import com.mudounet.hibernate.tags.GenericTag;
import com.mudounet.utils.hibernate.HibernateThreadSession;
import com.mudounet.utils.hibernate.DataAccessLayerException;
import com.mudounet.utils.hibernate.HibernateUtils;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author isabelle
 */
public class MovieListManager {

    private static HibernateThreadSession template = HibernateUtils.currentSession();

    public static Movie addMovie(String path, String title) {
        Movie m = null;
        try {

            m = MovieToolManager.buildMovie(path, title);
            template.beginTransaction();
            template.saveOrUpdate(m);
            template.endTransaction();
            return m;
        } catch (DataAccessLayerException ex) {
            Logger.getLogger(MovieListManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(MovieListManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MovieListManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return m;
    }

    public static boolean addTagToMovie(Movie movie, GenericTag tag) {
        return false;
    }
    
    public static MediaInfo addBasicInfosToMovie(Movie movie) throws Exception {
        try {
            MediaInfo mediaInfo = MovieToolManager.getMovieInformations(movie);
            return addBasicInfosToMovie(movie, mediaInfo);
        } catch (DataAccessLayerException ex) {
            Logger.getLogger(MovieListManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(MovieListManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MovieListManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static MediaInfo addBasicInfosToMovie(Movie movie, MediaInfo mediaInfo) throws DataAccessLayerException, Exception {
        template.beginTransaction();
        template.saveOrUpdate(mediaInfo);
        movie.setMediaInfo(mediaInfo);
        template.saveOrUpdate(movie);
        template.endTransaction();
        return mediaInfo;
    }
}

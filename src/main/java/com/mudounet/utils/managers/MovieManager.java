/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.managers;

import com.mudounet.hibernate.movies.GenericMovie;
import com.mudounet.hibernate.tags.GenericTag;

/**
 *
 * @author isabelle
 */
public class MovieManager {

    public static boolean addMovie(String path, String title) {
        return false;
    }

    /**
     * 
     * @param movie
     * @param tag
     * @return Success of operation
     */
    public static boolean addTagToMovie(GenericMovie movie, GenericTag tag) {
        return false;
    }

    public static boolean addBasicInfosToMovie(GenericMovie movie, Object techData) {
        return false;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet;

import com.mudounet.hibernate.Movie;
import com.mudounet.utils.hibernate.HibernateThreadSession;
import com.mudounet.utils.hibernate.HibernateUtils;
import java.util.List;

/**
 *
 * @author isabelle
 */
public class GlobalVariables {

    private static HibernateThreadSession template = HibernateUtils.currentSession();
    private static List<Movie> listOfMovies;

    public static List<Movie> getListOfMovies() {
        return listOfMovies;
    }

    public static void setListOfMovies(List<Movie> listOfMovies) {
        GlobalVariables.listOfMovies = listOfMovies;
    }

    public static HibernateThreadSession getTemplate() {
        return template;
    }

}

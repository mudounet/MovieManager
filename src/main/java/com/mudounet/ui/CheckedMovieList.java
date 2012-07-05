/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.ui;

import com.mudounet.hibernate.Movie;
import java.util.ArrayList;

/**
 *
 * @author gmanciet
 */
public class CheckedMovieList {
    private ArrayList<CheckedMovie> listOfCheckedMovies = new ArrayList<CheckedMovie>();

    public ArrayList<CheckedMovie> getListOfMovies() {
        return listOfCheckedMovies;
    }

    public void setListOfMovies(ArrayList<Movie> listOfMovies) {
        
        for (Movie movie : listOfMovies) {
            listOfCheckedMovies
            System.out.println();
        }
        
    }
    
    
}

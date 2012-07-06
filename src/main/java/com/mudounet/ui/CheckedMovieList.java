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
    private ArrayList<Movie> listOfMovies;
    private ArrayList<Movie> storedMovies;
    
    private ArrayList<CheckedMovie> listOfCheckedMovies = new ArrayList<CheckedMovie>();

    public ArrayList<CheckedMovie> getListOfCheckedMovies() {
        return listOfCheckedMovies;
    }


    /**
     * @return the storedMovies
     */
    public ArrayList<Movie> getStoredMovies() {
        return storedMovies;
    }

    /**
     * @param storedMovies the storedMovies to set
     */
    public void setStoredMovies(ArrayList<Movie> storedMovies) {
        this.storedMovies = storedMovies;
    }

    /**
     * @param listOfMovies the listOfMovies to set
     */
    public void setListOfMovies(ArrayList<Movie> listOfMovies) {
        this.listOfMovies = listOfMovies;
    }
    
    
    
    
    
}

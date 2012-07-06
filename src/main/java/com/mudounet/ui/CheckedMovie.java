/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.ui;

import com.mudounet.hibernate.Movie;

/**
 *
 * @author gmanciet
 */
public class CheckedMovie extends Movie {

    private MovieState state = MovieState.UNDEFINED;

    public MovieState getState() {
        return state;
    }

    public void setState(MovieState state) {
        this.state = state;
    }
}

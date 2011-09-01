/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.managers;

import com.mudounet.hibernate.movie.GenericMovie;
import com.mudounet.hibernate.tags.GenericTag;
import java.util.ArrayList;

/**
 *
 * @author gmanciet
 */
public class TagResult {
    private GenericTag _tag;
    private ArrayList<GenericMovie> _movies;

    public TagResult(GenericTag _tag, ArrayList<GenericMovie> _movies) {
        this._tag = _tag;
        this._movies = _movies;
    }

    public ArrayList<GenericMovie> getMovies() {
        return _movies;
    }

    public GenericTag getTag() {
        return _tag;
    }
}
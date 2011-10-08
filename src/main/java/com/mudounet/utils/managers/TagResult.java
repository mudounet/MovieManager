/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.managers;

import com.mudounet.hibernate.tags.GenericTag;

/**
 *
 * @author gmanciet
 */
public class TagResult {

    private GenericTag _tag;
    private long _moviesCount;

    /*public TagResult(SimpleTag _tag,  long moviesCount) {
    //this._tag = _tag;
    this._moviesCount = moviesCount;
    }*/
    public TagResult(GenericTag _tag, long moviesCount) {
        this._tag = _tag;
        this._moviesCount = moviesCount;
    }

    public long getMoviesCount() {
        return this._moviesCount;
    }

    public GenericTag getTag() {
        return _tag;
    }
}
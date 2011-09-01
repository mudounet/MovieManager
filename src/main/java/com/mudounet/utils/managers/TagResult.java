/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.managers;

import com.mudounet.hibernate.tags.SimpleTag;

/**
 *
 * @author gmanciet
 */
public class TagResult {
    private SimpleTag _tag;
    private int _moviesCount;

    public TagResult(SimpleTag _tag,  int moviesCount) {
        this._tag = _tag;
        this._moviesCount = moviesCount;
    }

    public int getMoviesCount() {
        return this._moviesCount;
    }

    public SimpleTag getTag() {
        return _tag;
    }
}
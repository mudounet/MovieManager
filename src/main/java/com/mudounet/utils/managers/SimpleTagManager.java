package com.mudounet.utils.managers;

import com.mudounet.hibernate.tags.GenericTag;
import java.util.ArrayList;

/**
 * @author isabelle
 */
public class SimpleTagManager {

    private ArrayList<GenericTag> _tagList;
    private ArrayList<TagResult> _resultList;

    public SimpleTagManager() {
        this._resultList = new ArrayList<TagResult>();
        this._tagList = new ArrayList<GenericTag>();
    }

    public void addFilterTag(GenericTag tag) {
        this._tagList.add(tag);
    }

    public ArrayList<TagResult> getTagLists() {
        return _resultList;
    }
}
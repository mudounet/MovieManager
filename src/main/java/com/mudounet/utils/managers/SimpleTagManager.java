package com.mudounet.utils.managers;

import com.mudounet.hibernate.movie.GenericMovie;
import com.mudounet.hibernate.tags.SimpleTag;
import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
 * @author isabelle
 */
public class SimpleTagManager {

    protected static Logger logger = Logger.getLogger(SimpleTagManager.class.getName());
    private ArrayList<SimpleTag> _tagList;
    private ArrayList<TagResult> _resultList;

    public SimpleTagManager() {
        this._resultList = new ArrayList<TagResult>();
        this._tagList = new ArrayList<SimpleTag>();
    }

    public void addFilterTag(SimpleTag tag) {
        this._tagList.add(tag);
    }
    
    public ArrayList<SimpleTag> getFilterTagsList() {
        return this._tagList;
    }

    public ArrayList<TagResult> getTagLists() {
        return _resultList;
    }
    
    public static ArrayList<GenericMovie> getMovies(SimpleTagManager st) {
        return null;
    }
}
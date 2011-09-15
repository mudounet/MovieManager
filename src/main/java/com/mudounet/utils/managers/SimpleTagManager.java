package com.mudounet.utils.managers;

import com.mudounet.hibernate.movies.GenericMovie;
import com.mudounet.hibernate.tags.SimpleTag;
import com.mudounet.utils.hibernate.AbstractDao;
import com.mudounet.utils.hibernate.DataAccessLayerException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Query;

/**
 * @author isabelle
 */
public class SimpleTagManager {

    protected static Logger logger = Logger.getLogger(SimpleTagManager.class.getName());
    private ArrayList<SimpleTag> _tagList;
    protected AbstractDao template;

    public SimpleTagManager() {
        this._tagList = new ArrayList<SimpleTag>();
        this.template = new AbstractDao();
    }
    
    public static boolean addSimpleTag(String key) {
        try {
            AbstractDao lTemplate = new AbstractDao();
            lTemplate.keepConnectionOpened();
            
            lTemplate.find(SimpleTag.class, "key", key);
            
            lTemplate.saveOrUpdate(new SimpleTag(key));
            lTemplate.closeConnection();
        
            return false;
        } catch (DataAccessLayerException e) {
            return false;
        }
    }
    
    public static boolean deleteSimpleTag(String key) {
        return false;
    }

    public boolean addFilterTag(SimpleTag tag) {
        if (tag != null) {
            return this._tagList.add(tag);
        } else {
            return false;
        }

    }

    public boolean addFilterTag(String tag) throws Exception {
        SimpleTag t = (SimpleTag) template.find(SimpleTag.class, "key", tag);
        return this._tagList.add(t);
    }

    public ArrayList<SimpleTag> getFilterTagsList() {
        return this._tagList;
    }

    public List<TagResult> getTagLists() throws DataAccessLayerException {


        // select tags.key, count(*) from GenericMovie as movie join movie.tags tags where movie in (select m from GenericMovie as m join m.tags t where t.key in ('Oscar') group by m having count(t)=1) and tags.class = SimpleTag and tags.key not in ('Oscar') group by tags



        String hql = "select new com.mudounet.utils.managers.TagResult(tags , count(*)) from GenericMovie as movie join movie.tags tags "
                + "where movie in (select m from GenericMovie as m join m.tags t ";

        if (_tagList.size() > 0) {
            hql += "where t in (:tags) group by m having count(t)=:tag_count ";
        }

        hql = hql + ") and tags.class = SimpleTag ";

        if (_tagList.size() > 0) {
            hql += " and tags not in (:tags) ";
        }

        hql = hql + "group by tags ";


        Query query = template.createQuery(hql);
        
        if (_tagList.size() > 0) {
            query.setParameterList("tags", _tagList);
            query.setInteger("tag_count", _tagList.size());
        }

        List<TagResult> results = template.getQueryResults();

        return results;
    }

    public List<GenericMovie> getMovies() throws DataAccessLayerException {

        String hql = "select m from Movie m "
                + "join m.tags t ";
        
        if (_tagList.size() > 0) {
            hql += "where t in (:tags) group by m having count(t)=:tag_count ";
        }

        Query query = template.createQuery(hql);
        
        if (_tagList.size() > 0) {
            query.setParameterList("tags", _tagList);
            query.setInteger("tag_count", _tagList.size());
        }

        List<GenericMovie> results = template.getQueryResults();

        return results;
    }
}
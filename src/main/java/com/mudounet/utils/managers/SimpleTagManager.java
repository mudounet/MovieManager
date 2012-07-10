package com.mudounet.utils.managers;

import com.mudounet.hibernate.Movie;
import com.mudounet.hibernate.tags.Tag;
import com.mudounet.hibernate.tags.TagResult;
import com.mudounet.utils.hibernate.AbstractDao;
import com.mudounet.utils.hibernate.DataAccessLayerException;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author isabelle
 */
public class SimpleTagManager {

    private static Logger logger = LoggerFactory.getLogger(SimpleTagManager.class.getName());
    private ArrayList<Tag> _tagList = new ArrayList<Tag>();
    private AbstractDao template;

    public SimpleTagManager() {
        this.template = new AbstractDao();
    }

    public SimpleTagManager(AbstractDao template) {
        this.template = template;
    }

    
    public static boolean addSimpleTag(String key) {
        try {
            AbstractDao lTemplate = new AbstractDao();
            lTemplate.keepConnectionOpened();
            
            lTemplate.find(Tag.class, "key", key);
            
            lTemplate.saveOrUpdate(new Tag(key));
            lTemplate.closeConnection();
        
            return false;
        } catch (DataAccessLayerException e) {
            return false;
        }
    }
    
    public static boolean deleteSimpleTag(String key) {
        return false;
    }

    public boolean addFilterTag(Tag tag) {
        if (tag != null) {
            return this._tagList.add(tag);
        } else {
            return false;
        }

    }

    public boolean addFilterTag(String tag) throws Exception {
        Tag t = (Tag) template.find(Tag.class, "key", tag);
        return this._tagList.add(t);
    }

    public ArrayList<Tag> getFilterTagsList() {
        return this._tagList;
    }

    public List<TagResult> getTagLists() throws DataAccessLayerException {


        String hql = "select new com.mudounet.hibernate.tags.TagResult(tags , count(*)) from Movie as m1 join m1.tags tags "
                + "where m1 in (select m2 from Movie as m2 join m2.tags t ";

        if (_tagList.size() > 0) {
            hql += "where t in (:tags) group by m2 having count(t)=:tag_count ";
        }

        hql += ") and tags.class = Tag ";

        if (_tagList.size() > 0) {
            hql += " and tags not in (:tags) ";
        }

        hql += "group by tags ";


        Query query = template.createQuery(hql);
        
        if (_tagList.size() > 0) {
            query.setParameterList("tags", _tagList);
            query.setInteger("tag_count", _tagList.size());
        }

        List<TagResult> results = template.getQueryResults();

        return results;
    }

    public List<Movie> getMovies() throws DataAccessLayerException {

        
        
        String hql = "select m from Movie m ";
        
        if (_tagList.size() > 0) {
            hql += "join m.tags t ";
            hql += "where t in (:tags) group by m having count(t)=:tag_count ";
        }

        Query query = template.createQuery(hql);
        
        if (_tagList.size() > 0) {
            query.setParameterList("tags", _tagList);
            query.setInteger("tag_count", _tagList.size());
        }

        return template.getQueryResults();
    }
}

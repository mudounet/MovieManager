package com.mudounet.utils.managers;

import com.mudounet.hibernate.Movie;
import com.mudounet.hibernate.tags.SimpleTag;
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

    protected static Logger logger = LoggerFactory.getLogger(SimpleTagManager.class.getName());
    private ArrayList<SimpleTag> _tagList;
    protected AbstractDao template;

    public SimpleTagManager() {
        this._tagList = new ArrayList<SimpleTag>();
        this.template = new AbstractDao();
    }

    public SimpleTagManager(AbstractDao template) {
        this.template = template;
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


        String hql = "select new com.mudounet.utils.managers.TagResult(tags , count(*)) from Movie as m1 join m1.tags tags "
                + "where m1 in (select m2 from Movie as m2 join m2.tags t ";

        if (_tagList.size() > 0) {
            hql += "where t in (:tags) group by m2 having count(t)=:tag_count ";
        }

        hql += ") and tags.class = SimpleTag ";

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

    @SuppressWarnings("unchecked")
    public List<Movie> getMovies() throws DataAccessLayerException {

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

        return template.getQueryResults();
    }
}

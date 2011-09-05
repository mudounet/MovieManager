package com.mudounet.utils.managers;

import com.mudounet.hibernate.movie.GenericMovie;
import com.mudounet.hibernate.tags.SimpleTag;
import com.mudounet.utils.hibernate.AbstractDao;
import com.mudounet.utils.hibernate.HibernateFactory;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * @author isabelle
 */
public class SimpleTagManager {

    //protected static Logger logger = Logger.getLogger(SimpleTagManager.class.getName());
    private ArrayList<SimpleTag> _tagList;
    private ArrayList<TagResult> _resultList;
    private Session session;
    protected AbstractDao template;

    public SimpleTagManager() {
        this._resultList = new ArrayList<TagResult>();
        this._tagList = new ArrayList<SimpleTag>();
        this.template = new AbstractDao();
    }

    public boolean addFilterTag(SimpleTag tag) {
        if(tag != null) return this._tagList.add(tag);
        else return false;
        
    }
    
    public boolean addFilterTag(String tag) throws Exception {
        SimpleTag t = (SimpleTag) template.find(SimpleTag.class, "key", tag);
        return this._tagList.add(t);
    }

    public ArrayList<SimpleTag> getFilterTagsList() {
        return this._tagList;
    }

    public ArrayList<TagResult> getTagLists() {
        
        String hql = "from SimpleTag t "
                + "left join fetch t.movies m "
                + "left join fetch m.tags r";
               // + "join m.tags t "
               // + "where t.key in (:tags) "
               // + "group by m "
               // + "having count(t)=:tag_count";

        
        session = HibernateFactory.openSession();
        Query query = session.createQuery(hql);
        //query.setParameterList("tags", _tagList);
        //query.setInteger("tag_count", _tagList.size());
        List results = query.list();
        session.close();      
        
        return _resultList;
    }

    public List<GenericMovie> getMovies() {
        
        /*ArrayList<String> tagList = new ArrayList<String>();
        for(SimpleTag t : _tagList) {
            tagList.add(t.getKey());
        }
        
        String[] tags = (String[]) tagList.toArray(new String[0]);*/
        
        String hql = "select m from Movie m "
                + "join m.tags t "
                + "where t in (:tags) "
                + "group by m "
                + "having count(t)=:tag_count";

        session = HibernateFactory.openSession();
        Query query = session.createQuery(hql);
        query.setParameterList("tags", _tagList);
        query.setInteger("tag_count", _tagList.size());
        List<GenericMovie> results = query.list();
        session.close();
        return results;
    }
}
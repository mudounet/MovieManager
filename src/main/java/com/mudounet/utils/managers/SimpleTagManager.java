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
    private Session session;
    protected AbstractDao template;

    public SimpleTagManager() {
        this._tagList = new ArrayList<SimpleTag>();
        this.template = new AbstractDao();
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

    public List<TagResult> getTagLists() {


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


        session = HibernateFactory.openSession();
        Query query = session.createQuery(hql);
        
        if (_tagList.size() > 0) {
            query.setParameterList("tags", _tagList);
            query.setInteger("tag_count", _tagList.size());
        }

        List<TagResult> results = query.list();
        session.close();

        return results;
    }

    public List<GenericMovie> getMovies() {

        String hql = "select m from Movie m "
                + "join m.tags t ";
        
        if (_tagList.size() > 0) {
            hql += "where t in (:tags) group by m having count(t)=:tag_count ";
        }

        session = HibernateFactory.openSession();
        Query query = session.createQuery(hql);
        if (_tagList.size() > 0) {
            query.setParameterList("tags", _tagList);
            query.setInteger("tag_count", _tagList.size());
        }

        List<GenericMovie> results = query.list();
        session.close();
        return results;
    }
}
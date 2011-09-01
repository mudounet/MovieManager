/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.managers;

import com.mudounet.utils.managers.SimpleTagManager;
import com.mudounet.hibernate.movie.Movie;
import com.mudounet.test.ResultSetReporter;
import org.dbunit.dataset.ITable;
import org.hibernate.Transaction;
import org.hibernate.Session;
import com.mudounet.utils.dbunit.ProjectDatabaseTestCase;
import com.mudounet.hibernate.tags.GenericTag;
import com.mudounet.hibernate.tags.SimpleTag;
import com.mudounet.utils.hibernate.HibernateFactory;
import java.util.ArrayList;
import java.util.List;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.log4j.Logger;
import org.hibernate.Query;

/**
 *
 * @author gmanciet
 */
public class SimpleTagManagerTest extends ProjectDatabaseTestCase {

    protected static Logger logger = Logger.getLogger(SimpleTagManagerTest.class.getName());
    private Session session;
    private Transaction tx;

    public SimpleTagManagerTest(String name) {
        super(name);
    }

    @Override
    protected DatabaseOperation getTearDownOperation() {
        return DatabaseOperation.NONE;
    }

    /**
     * Test of addFilterTag method, of class TagManager.
     */
    @Test
    public void testAddFilterTag() {
        logger.info("addFilterTag");
        SimpleTag tag = new SimpleTag();
        tag.setKey("test");
        SimpleTagManager instance = new SimpleTagManager();
        instance.addFilterTag(tag);
        
        assertEquals(1, instance.getFilterTagsList().size());
    }

    /**
     * Test of getTagLists method, of class TagManager.
     */
    @Test
    public void testGetTagLists() throws Exception {
        logger.info("getTagLists");
        SimpleTagManager instance = new SimpleTagManager();
        ArrayList expResult = null;
        ArrayList result = instance.getTagLists();

        ITable resultSet //= this.getResults("select * from GENERICMOVIE AS G,GENERICTAG AS T");
                = this.getResults("select TITLE from GENERICMOVIE AS M, GENERICTAG as T, MOVIES_TAGS as MT where M.ID = MT.FK_MOVIE and T.ID = MT.FK_TAG AND (T.KEY='Comedie')");
        logger.debug("Number of films: " + resultSet.getRowCount());

        session = HibernateFactory.openSession();

        String[] tags = {"Oscar", "Documentaire", "Comedie"};

        String hql = "select m from Movie m "
                + "join m.tags t "
                + "where t.key in (:tags) "
                + "group by m "
                + "having count(t)=:tag_count";

        Query query = session.createQuery(hql);
        query.setParameterList("tags", tags);
        query.setInteger("tag_count", tags.length);
        List<Movie> results = query.list();

        for (int i = 0; i < results.size(); i++) {
            logger.debug(results.get(i));
        }

        session.close();

        logger.debug("Results : " + results.size());

        //ResultSetReporter.dump(resultSet);     

        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    protected String getDataSetFilename() {
        return "TestSimpleTagManager.xml";
    }

    /**
     * Test of getFilterTagsList method, of class SimpleTagManager.
     */
    @Test
    public void testGetFilterTagsList() {
        logger.info("getFilterTagsList");
        SimpleTagManager instance = new SimpleTagManager();
        ArrayList expResult = null;
        ArrayList result = instance.getFilterTagsList();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMovies method, of class SimpleTagManager.
     */
    @Test
    public void testGetMovies() {
        logger.info("getMovies");
        SimpleTagManager st = null;
        ArrayList expResult = null;
        ArrayList result = SimpleTagManager.getMovies(st);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}

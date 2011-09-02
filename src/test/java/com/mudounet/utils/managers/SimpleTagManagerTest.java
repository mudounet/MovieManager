/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.managers;

import com.mudounet.hibernate.movie.GenericMovie;
import java.util.List;
import org.dbunit.dataset.ITable;
import org.hibernate.Transaction;
import org.hibernate.Session;
import com.mudounet.utils.dbunit.ProjectDatabaseTestCase;
import com.mudounet.hibernate.tags.SimpleTag;
import com.mudounet.utils.hibernate.HibernateFactory;
import java.lang.String;
import java.util.ArrayList;
import java.util.Collections;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.log4j.Logger;

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
    public void testGetMovies() throws Exception {
        logger.info("getMovies");


        ITable resultSet = this.getResults("select KEY FROM GENERICTAG  WHERE TYPE='S'");

        ArrayList<String> completeTagList = new ArrayList<String>();
        for (int i = 0; i < resultSet.getRowCount(); i++) {
            completeTagList.add((String) resultSet.getValue(i, "key"));
        }
        Collections.shuffle(completeTagList);

        
        ArrayList<String> testedTagList = new ArrayList<String>();
        String keyList = "";
        for (String key : completeTagList) {
            testedTagList.add(key);
            
            if(keyList.equals("")) keyList = "'" + key+ "'";
            else keyList = keyList + ", '" + key+ "'";

            logger.debug("Testing keys : " + keyList);
            
            SimpleTagManager st = new SimpleTagManager();
            for (String testedKey : testedTagList) {
                st.addFilterTag(testedKey);
            }
            List<GenericMovie> result = st.getMovies();
            
            String query = "SELECT fk_movie " +
 "FROM Movies_Tags INNER JOIN GenericMovie m ON m.id = fk_movie INNER JOIN Generictag t ON t.id = fk_tag " +
" WHERE t.key IN (" + keyList + ") "+
"GROUP BY fk_movie " +
"HAVING Count(fk_tag) = " + testedTagList.size();
            
            resultSet = this.getResults(query);
            
            logger.debug("Number of films found : " + resultSet.getRowCount());
            
            assertEquals("Number of films for key(s) " + keyList + " : ", resultSet.getRowCount(), result.size());
        }
    }
}

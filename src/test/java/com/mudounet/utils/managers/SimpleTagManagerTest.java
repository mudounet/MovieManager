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
     * Test of getFilterTagsList method, of class SimpleTagManager.
     */
    @Test
    public void testGetFilterTagsList() throws Exception {
        logger.info("getFilterTagsList");
        SimpleTagManager instance = new SimpleTagManager();

        ITable resultSet = this.getResults("select KEY FROM GENERICTAG INNER JOIN SIMPLETAG ON ID = FK_TAG");

        for (int i = 0; i < resultSet.getRowCount(); i++) {
            instance.addFilterTag((String) resultSet.getValue(i, "key"));

            assertEquals(i + 1, instance.getFilterTagsList().size());
        }
    }

    /**
     * Test of getTagLists method, of class TagManager.
     */
    @Test
    public void testGetTagLists() throws Exception {
        logger.info("getTagLists");

        // Building list of tags to test
        ITable resultSet = this.getResults("select KEY FROM GENERICTAG INNER JOIN SIMPLETAG ON ID = FK_TAG");
        ArrayList<String> completeTagList = new ArrayList<String>();
        for (int i = 0; i < resultSet.getRowCount(); i++) {
            completeTagList.add((String) resultSet.getValue(i, "key"));
        }
        Collections.shuffle(completeTagList);

        ArrayList<String> testedTagList = new ArrayList<String>();
        String keyList = "";
        for (String key : completeTagList) {
            // Testing progressively tags
            testedTagList.add(key);

            // Building list of tags used by SQL query
            if (keyList.equals("")) {
                keyList = "'" + key + "'";
            } else {
                keyList = keyList + ", '" + key + "'";
            }
            logger.debug("Testing keys : " + keyList);

            // Building list of tags used by Hibernate 
            SimpleTagManager st = new SimpleTagManager();
            for (String testedKey : testedTagList) {
                st.addFilterTag(testedKey);
            }

            String query = "SELECT KEY, ID, COUNT(*) AS FILMS_COUNT FROM GENERICTAG as T, MOVIES_TAGS as MT INNER JOIN SIMPLETAG AS S ON T.ID = S.FK_TAG WHERE T.ID = MT.FK_TAG AND MT.FK_MOVIE IN ( "
                    + "select M.ID from GENERICMOVIE AS M, GENERICTAG as T, MOVIES_TAGS as MT where M.ID = MT.FK_MOVIE and T.ID = MT.FK_TAG AND (T.KEY = 'Animation') AND FK_MOVIE IN (SELECT Movies_Tags.fk_movie "
                    + "FROM Movies_Tags "
                    + "INNER JOIN GenericMovie a "
                    + "ON a.id = Movies_Tags.fk_movie "
                    + "INNER JOIN Generictag t "
                    + "ON t.id = Movies_Tags.fk_tag "
                    + "WHERE t.key IN ("+keyList+") "
                    + "GROUP BY Movies_Tags.fk_movie "
                    + "HAVING Count(Movies_Tags.fk_tag) = "+testedTagList.size()+")) AND KEY NOT IN ("+keyList+") "
                    + "GROUP BY KEY, ID";

            logger.debug(query);
            resultSet = this.getResults(query);
            List<TagResult> tagList = st.getTagLists();
            
            int refArrayDim = resultSet.getRowCount();

            assertEquals("Number of items returned did not match : ", refArrayDim, tagList.size());

            for (TagResult tag : tagList) {
                String foundItemName = "";
                
                for(int i = 0; i < refArrayDim; i++) {
                    String itemName = (String)resultSet.getValue(i, "KEY");
                    long itemID = Long.parseLong(resultSet.getValue(i, "ID").toString());
                    long itemValue = Long.parseLong(resultSet.getValue(i, "FILMS_COUNT").toString());
                    
                    if(itemName.equals(tag.getTag().getKey()) && itemID == tag.getTag().getId()) {
                        foundItemName = itemName;
                        assertEquals("Value of item is invalid : ", itemValue , tag.getMoviesCount());
                        break;
                    }
                }
                
                assertEquals("Item has not been found into referential : ", tag.getTag().getKey(), foundItemName);
            }
        }
    }

    protected String getDataSetFilename() {
        return "TestSimpleTagManager.xml";
    }

    /**
     * Test of getMovies method, of class SimpleTagManager.
     */
    @Test
    public void testGetMovies() throws Exception {
        logger.info("getMovies");


        ITable resultSet = this.getResults("select KEY FROM GENERICTAG INNER JOIN SIMPLETAG ON ID = FK_TAG");

        ArrayList<String> completeTagList = new ArrayList<String>();
        for (int i = 0; i < resultSet.getRowCount(); i++) {
            completeTagList.add((String) resultSet.getValue(i, "key"));
        }
        Collections.shuffle(completeTagList);


        ArrayList<String> testedTagList = new ArrayList<String>();
        String keyList = "";
        for (String key : completeTagList) {
            testedTagList.add(key);

            if (keyList.equals("")) {
                keyList = "'" + key + "'";
            } else {
                keyList = keyList + ", '" + key + "'";
            }

            logger.debug("Testing keys : " + keyList);

            SimpleTagManager st = new SimpleTagManager();
            for (String testedKey : testedTagList) {
                st.addFilterTag(testedKey);
            }
            List<GenericMovie> result = st.getMovies();

            String query = "SELECT fk_movie "
                    + "FROM Movies_Tags INNER JOIN GenericMovie m ON m.id = fk_movie INNER JOIN Generictag t ON t.id = fk_tag "
                    + " WHERE t.key IN (" + keyList + ") "
                    + "GROUP BY fk_movie "
                    + "HAVING Count(fk_tag) = " + testedTagList.size();

            resultSet = this.getResults(query);

            logger.debug("Number of films found : " + resultSet.getRowCount());

            assertEquals("Number of films for key(s) " + keyList + " : ", resultSet.getRowCount(), result.size());
        }
    }
}

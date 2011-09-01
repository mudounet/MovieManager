/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.util.managers;

import com.mudounet.test.ResultSetReporter;
import org.dbunit.dataset.ITable;
import org.hibernate.Transaction;
import org.hibernate.Session;
import com.mudounet.utils.dbunit.ProjectDatabaseTestCase;
import com.mudounet.hibernate.tags.GenericTag;
import java.util.ArrayList;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author gmanciet
 */
public class SimpleTagManagerTest extends ProjectDatabaseTestCase {
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
        GenericTag tag = null;
        SimpleTagManager instance = new SimpleTagManager();
        instance.addFilterTag(tag);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
        logger.debug("Number of films: "+resultSet.getRowCount());
       
        ResultSetReporter.dump(resultSet);     
        
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
    protected String getDataSetFilename() {
        return "TestSimpleTagManager.xml";
    }
}

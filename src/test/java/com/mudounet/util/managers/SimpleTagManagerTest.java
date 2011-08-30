/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.util.managers;

import java.sql.ResultSet;
import org.dbunit.dataset.Column;
import org.dbunit.database.ResultSetTableMetaData;
import org.dbunit.dataset.LowerCaseTableMetaData;
import org.dbunit.dataset.DefaultTableMetaData;
import org.dbunit.dataset.ITable;
import org.hibernate.Transaction;
import org.hibernate.Session;
import helper.ProjectDatabaseTestCase;
import com.mudounet.hibernate.tags.GenericTag;
import com.mudounet.test.ResultSetReporter;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
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

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of addFilterTag method, of class TagManager.
     */
    @Test
    public void testAddFilterTag() {
        System.out.println("addFilterTag");
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
        System.out.println("getTagLists");
        SimpleTagManager instance = new SimpleTagManager();
        ArrayList expResult = null;
        ArrayList result = instance.getTagLists();
        
        ITable resultSet = this.getResults("select * from GenericTag where type = 'S'");
       
       //ResultSetReporter.dump(resultSet);     
   
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
    protected String getDataSetFilename() {
        return "TestSimpleTagManager.xml";
    }
}

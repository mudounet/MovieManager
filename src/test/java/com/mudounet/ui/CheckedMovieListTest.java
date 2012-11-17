/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.ui;

import com.mudounet.hibernate.Movie;
import com.mudounet.utils.dbunit.ProjectDatabaseTestCase;
import java.util.ArrayList;
import org.dbunit.dataset.ITable;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author gmanciet
 */
@Ignore public class CheckedMovieListTest extends ProjectDatabaseTestCase {
    
    public CheckedMovieListTest(String name) {
        super(name);
    }
    
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    /**
     * Test of getStoredMovies method, of class CheckedMovieList.
     */
    @Test
    public void testGetStoredMovies() {
        System.out.println("getStoredMovies");
        CheckedMovieList instance = new CheckedMovieList();
        ArrayList<Movie> expResult = new ArrayList<Movie>();
        Movie m = new Movie();
        m.setTitle("Mon test");
        expResult.add(m);
        instance.setStoredMovies(expResult);
        ArrayList<Movie> result = instance.getStoredMovies();
        assertEquals(expResult, result);
    }
    
     /**
     * Test of getListOfCheckedMovies method, of class CheckedMovieList.
     * @throws Exception 
     */
    @Test
    public void testGetListOfCheckedMovies() throws Exception {
        System.out.println("getListOfMovies");
        
        ITable resultSet = this.getResults("select * FROM MOVIE");
        assertEquals(5, resultSet.getRowCount());

        CheckedMovieList instance = new CheckedMovieList();
        ArrayList<CheckedMovie> expResult = null;
        ArrayList<CheckedMovie> results = instance.getListOfCheckedMovies();
        for (CheckedMovie result : results) {
           assertEquals(expResult, result);
        }
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Override
    protected String getDataSetFilename() {
        return "TestMovieManager.xml";
    }
}

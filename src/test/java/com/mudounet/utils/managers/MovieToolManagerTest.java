/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.managers;

import java.io.File;
import com.mudounet.utils.dbunit.TestTools;
import com.mudounet.hibernate.movies.TechData;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author isabelle
 */
public class MovieToolManagerTest {
    
    public MovieToolManagerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of getMovieInformations method, of class MovieToolManager.
     */
    @Test
    public void testGetMovieInformations() throws Exception {
        System.out.println("getMovieInformations");
        String moviePath = "";
        File movieFile = TestTools.getFileFromClasspath("sample_video.flv");

        TechData expResult = null;
        TechData result = MovieToolManager.getMovieInformations(movieFile);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}

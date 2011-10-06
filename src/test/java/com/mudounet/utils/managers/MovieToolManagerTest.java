/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.managers;

import org.apache.log4j.Logger;
import java.io.File;
import com.mudounet.utils.dbunit.TestTools;
import com.mudounet.hibernate.movies.others.TechData;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author isabelle
 */
public class MovieToolManagerTest {
    protected static Logger logger = Logger.getLogger(SimpleTagManager.class.getName());
   
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
        logger.info("getMovieInformations");
        File movieFile = TestTools.getFileFromClasspath("sample_video.flv");

        //TechData result = MovieToolManager.getMovieInformations(movieFile);
        
        //movieFile = TestTools.getFileFromClasspath("sample_video.mp4");

        //result = MovieToolManager.getMovieInformations(movieFile);

        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}

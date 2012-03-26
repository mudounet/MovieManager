/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.managers;

import com.mudounet.hibernate.movies.others.Snapshot;
import com.mudounet.utils.dbunit.TestTools;
import com.mudounet.hibernate.movies.Movie;
import com.mudounet.hibernate.movies.GenericMovie;
import com.mudounet.utils.Utils;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author isabelle
 */
public class MovieToolManagerTest {

    protected static Logger logger = LoggerFactory.getLogger(SimpleTagManager.class.getName());

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
        File movieFile = Utils.getFileFromClasspath("sample_video.flv");

        TechData result = MovieToolManager.getMovieInformations(movieFile);

        //movieFile = TestTools.getFileFromClasspath("sample_video.mp4");

        //result = MovieToolManager.getMovieInformations(movieFile);

        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of genSnapshots method, of class MovieToolManager.
     */
    @Test
    public void testGenSnapshots() throws Exception {
        logger.info("genSnapshots");
        GenericMovie movie = new Movie();

        File directory = TestTools.createTempDirectory();
        logger.info("Temporary directory is : "+directory);

        movie.setPath(Utils.getFileFromClasspath("sample_video.flv").getAbsolutePath());

        int nbOfSnapshots = 9;

        Set<Snapshot> results = MovieToolManager.genSnapshots(movie, directory, nbOfSnapshots);

        assertEquals("Snapshot quantity is not correct : ", nbOfSnapshots, results.size());

        for (Snapshot s : results) {
            assertEquals("Snapshot doesn't exists : ", true, s.getFile().exists());
            assertEquals("Snapshot is not a file : ", true, s.getFile().isFile());
            assertEquals("Snapshot is not readable : ", true, s.getFile().canRead());
        }
    }
}

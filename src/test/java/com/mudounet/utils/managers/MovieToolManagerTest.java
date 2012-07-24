/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.managers;

import com.mudounet.hibernate.Movie;
import com.mudounet.hibernate.MovieProxy;
import com.mudounet.hibernate.movies.others.MediaInfo;
import com.mudounet.hibernate.movies.others.Snapshot;
import com.mudounet.utils.Utils;
import com.mudounet.utils.dbunit.TestTools;
import java.io.File;
import java.util.Set;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author isabelle
 */
public class MovieToolManagerTest {

    private Logger logger = LoggerFactory.getLogger(getClass().getName());

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
     * @throws Exception 
     */
    @Test
    public void testGetMovieInformations() throws Exception {
        logger.info("getMovieInformations");
        File movieFile = Utils.getFileFromClasspath("sample_video.flv");

        MovieProxy movieProxy = new MovieProxy(movieFile, "My movie");
        Movie m = movieProxy.getMovie();
        
        
        assertEquals("sample_video.flv", m.getFilename());
        assertEquals("b20d0bdbd19487bbfe28e4b92a0b0873", m.getMd5());
        assertEquals(true, m.getSize() > 0);
        assertEquals("My movie", m.getTitle());
        assertEquals(m.getModificationDate().getTime() > 0, true);
        
        MediaInfo result = MovieToolManager.getMovieInformations(movieProxy);

        assertEquals(result.getPlayTime() > 0, true);
        assertEquals(result.getVideoHeight() > 0, true);
        assertEquals(result.getVideoWidth() > 0, true);
        assertNotNull(result.getVideoCodec());
        assertEquals(result.getVideoCodec().length() > 0, true);
        assertNotNull(result.getAudioCodec());
        assertEquals(result.getAudioCodec().length() > 0, true);
        assertEquals(result.getAudioSamplingRate() > 0, true);
        assertEquals(result.getAudioChannels() > 0, true);
        
    }

    /**
     * Test of genSnapshots method, of class MovieToolManager.
     * @throws Exception 
     */
    @Test
    public void testGenSnapshots() throws Exception {
        logger.info("genSnapshots");
        File movieFile = Utils.getFileFromClasspath("sample_video.flv");
        MovieProxy movieProxy = new MovieProxy(movieFile, "My movie");
        Movie m = movieProxy.getMovie();

        File directory = TestTools.createTempDirectory();
        logger.info("Temporary directory is : " + directory);

        int nbOfSnapshots = 9;

        Set<Snapshot> results = MovieToolManager.genSnapshots(movieProxy, directory, nbOfSnapshots);

        assertEquals("Snapshot quantity is not correct : ", nbOfSnapshots, results.size());

        for (Snapshot s : results) {
            assertEquals("Snapshot doesn't exists : ", true, s.getFile().exists());
            assertEquals("Snapshot is not a file : ", true, s.getFile().isFile());
            assertEquals("Snapshot is not readable : ", true, s.getFile().canRead());
        }
    }
}
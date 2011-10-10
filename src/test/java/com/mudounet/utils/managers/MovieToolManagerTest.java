/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.managers;

import com.mudounet.utils.Utils;
import org.apache.log4j.Logger;
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
        File movieFile = Utils.getFileFromClasspath("sample_video.flv");

        //TechData result = MovieToolManager.getMovieInformations(movieFile);
        
        //movieFile = TestTools.getFileFromClasspath("sample_video.mp4");

        //result = MovieToolManager.getMovieInformations(movieFile);

        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testOnVideoPicture() {
        System.out.println("onVideoPicture");
//        try {
//            String filename = "testdata/RRTT - Christmas Delivery trailer.mp4";
//            String alternateName = "RRTT";
//            ImageBuilder b = new ImageBuilder(filename);
//            int numberOfFrames = 10;
//            ArrayList<BufferedImage> list = b.extractFrames(numberOfFrames);
//            assertEquals(numberOfFrames, list.size());
//
//            for(int i = 0;i<list.size();i++) {
//                String outImageName = "test_img_"+alternateName+"_"+i+".jpg";
//                File file = new File(outImageName);
//                System.out.println("Writing image "+outImageName);
//                ImageIO.write(((BufferedImage)list.get(i)), "jpg", file);
//            }
//
//            //File dir = new File(".");
//            //File file = File.createTempFile("frame", ".png", dir);
//        } catch (Exception ex) {
//            Logger.getLogger(ImageBuilderTestOld.class.getName()).log(Level.SEVERE, null, ex);
//        }

        fail("The test case is a prototype.");
    }
}

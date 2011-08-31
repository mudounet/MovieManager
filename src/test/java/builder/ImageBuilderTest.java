/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package builder;

import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class ImageBuilderTest {

    public ImageBuilderTest() {
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
     * Test of onVideoPicture method, of class ImageBuilder.
     */
    @Test
    public void testOnVideoPicture() {
        System.out.println("onVideoPicture");
        try {
            String filename = "testdata/RRTT - Christmas Delivery trailer.mp4";
            String alternateName = "RRTT";
            ImageBuilder b = new ImageBuilder(filename);
            int numberOfFrames = 10;
            ArrayList<BufferedImage> list = b.extractFrames(numberOfFrames);
            assertEquals(numberOfFrames, list.size());

            for(int i = 0;i<list.size();i++) {
                String outImageName = "test_img_"+alternateName+"_"+i+".jpg";
                File file = new File(outImageName);
                System.out.println("Writing image "+outImageName);
                ImageIO.write(((BufferedImage)list.get(i)), "jpg", file);
            }

            //File dir = new File(".");
            //File file = File.createTempFile("frame", ".png", dir);
        } catch (Exception ex) {
            Logger.getLogger(ImageBuilderTest.class.getName()).log(Level.SEVERE, null, ex);
        }

       // write out PNG

        //ImageIO.write(event.getImage(), "png", file);
    }
}

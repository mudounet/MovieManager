/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.hibernate.movies.others;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author gmanciet
 */
@Ignore public class MediaInfoTest {
    
    public MediaInfoTest() {
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
     * Test of setVideoCodec method, of class MediaInfo.
     */
    @Test
    public void testSetVideoCodec_String() {
        System.out.println("setVideoCodec");
        String codecName = "YUV2";
        MediaInfo instance = new MediaInfo();
        instance.setVideoCodec(codecName);
        assertEquals(instance.getVideoCodec(), codecName);
    }

    /**
     * Test of setVideoCodec method, of class MediaInfo.
     */
    @Test
    public void testSetVideoCodec_int() {
        System.out.println("setVideoCodec");
        int FourCcCodecId = 1177964630; // Code for VP6F codec
        MediaInfo instance = new MediaInfo();
        instance.setVideoCodec(FourCcCodecId);
        assertEquals("VP6F",instance.getVideoCodec());
    }
}

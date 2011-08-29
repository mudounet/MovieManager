/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package builder;

import hibernate.TechData;
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
public class TechDataBuilderTest {

    public TechDataBuilderTest() {
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
     * Test of build method, of class TechDataBuilder.
     */
    @Test
    public void testBuild() throws Exception {
        String filename = "testdata/RRTT - Christmas Delivery trailer.mp4";
        System.out.println("Test file: "+filename);
        
        TechData expResult = null;
        TechData result = TechDataBuilder.build(filename);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
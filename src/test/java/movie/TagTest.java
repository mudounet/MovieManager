/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package movie;

import movie.tagvalue.TagString;
import movie.tagvalue.TagValue;
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
public class TagTest {

    public TagTest() {
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
     * Test of isDefined method, of class Tag.
     */
    @Test
    public void testIsDefined() {
        try {
            System.out.println("isDefined");
            Tag successTag = new Tag("myKey", "");
            Tag instance = new Tag("myKey", "");
            boolean result = instance.isDefined(successTag);
            assertEquals(true, result);
            Tag failedTag = new Tag("failedKey", "");
            assertEquals(false, instance.isDefined(failedTag));

        } catch (Exception ex) {
            fail(ex.fillInStackTrace().toString());
        }
    }

    /**
     * Test of getKey method, of class Tag.
     */
    @Test
    public void testGetKey() {
        System.out.println("getKey");
        Tag instance;
        try {
            instance = new Tag("myKey", "");
            String result = instance.getKey();
            assertEquals("myKey", result);
        } catch (Exception ex) {
            fail(ex.fillInStackTrace().toString());
        }
    }

    /**
     * Test of getValue method, of class Tag.
     */
    @Test
    public void testGetValue() {
        try {
            System.out.println("getValue");
            Tag instance = new Tag("myKey", "");
            String value = "StringTest";
            TagString expResult = new TagString(value);
            instance.setValue(expResult);
            TagValue result = instance.getValue();
            assertEquals(expResult, result);
            assertEquals(value, result.toString());
        } catch (Exception ex) {
            fail(ex.fillInStackTrace().toString());
        }
    }

    /**
     * Test of setValue method, of class Tag.
     */
    @Test
    public void testSetValue_TagValue() {
        try {
            String value = "StringTest";
            TagString _value = new TagString(value);
            Tag instance = new Tag("myKey", "");
            instance.setValue(_value);
            assertEquals(value, instance.getValue().toString());
            value = "StringTest2";
            _value.setValue(value);
            instance.setValue(_value);
            assertEquals(value, instance.getValue().toString());
        } catch (Exception ex) {
            fail(ex.fillInStackTrace().toString());
        }
    }

    /**
     * Test of setValue method, of class Tag.
     */
    @Test
    public void testSetValue_boolean() {
        try {
            System.out.println("setValue");
            boolean value = false;
            Tag instance = new Tag("myKey", "");
            instance.setValue(value);
            assertEquals(value, instance.getValue().toString());
            value = true;
            instance.setValue(value);
            assertEquals(value, instance.getValue().toString());
        } catch (Exception ex) {
            fail(ex.fillInStackTrace().toString());
        }
     }

    /**
     * Test of setValue method, of class Tag.
     */
    @Test
    public void testSetValue_String() {
        try {
            System.out.println("setValue");
            String value = "";
            Tag instance = new Tag("myKey", "");
            instance.setValue(value);
            assertEquals(value, instance.getValue().toString());
            value = "StringTest";
            instance.setValue(value);
            assertEquals(value, instance.getValue().toString());
        } catch (Exception ex) {
            fail(ex.fillInStackTrace().toString());
        }
    }

}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package movie;

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
public class TagListTestOld {

    public TagListTestOld() {
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
     * Test of getList method, of class TagList.
     */
    @Test
    public void testGetList() {
        try {
            System.out.println("getList");
            TagList instance = new TagList();
            ArrayList result = instance.getList();
            assertEquals(0, result.size());
            this.addElements(instance);
            result = instance.getList();
            assertEquals(3, result.size());
        } catch (Exception ex) {
            fail(ex.fillInStackTrace().toString());
        }
    }

    /**
     * Test of addTag method, of class TagList.
     */
    @Test
    public void testAddTag() {
        try {
            System.out.println("addTag");
            Tag t = null;
            TagList instance = new TagList();
            boolean result = instance.addTag(t);
            assertEquals(false, result);
            t = new Tag("myKey", "");
            result = instance.addTag(t);
            assertEquals(true, result);
            result = instance.addTag(t);
            assertEquals(false, result);
        } catch (Exception ex) {
            fail(ex.fillInStackTrace().toString());
        }
    }

    /**
     * Test of getSize method, of class TagList.
     */
    @Test
    public void testGetSize() {
        System.out.println("getSize");
        TagList instance = new TagList();
        int sizeArray = instance.getList().size();
        assertEquals(0, sizeArray);
        this.addElements(instance);
        sizeArray = instance.getList().size();
        assertEquals(3, sizeArray);
    }

    /**
     * Test of deleteTag method, of class TagList.
     */
    @Test
    public void testDeleteTag_String() {
        try {
            System.out.println("deleteTag");
            TagList instance = new TagList();
            this.addElements(instance);
            Tag t = new Tag("successFullTag", "");
            instance.addTag(t);
            assertEquals(true, instance.exists("successFullTag"));
            instance.deleteTag("successFullTag");
            assertEquals(false, instance.exists("successFullTag"));

        } catch (Exception ex) {
            fail(ex.getLocalizedMessage());
        }
    }

    /**
     * Test of exists method, of class TagList.
     */
    @Test
    public void testExists_String() {
        try {
            System.out.println("exists");
            TagList instance = new TagList();
            this.addElements(instance);
            Tag t = new Tag("successFullTag", "");
            instance.addTag(t);
            assertEquals(true, instance.exists("successFullTag"));
            assertEquals(false, instance.exists("incorrectTag"));
        } catch (Exception ex) {
            fail(ex.getLocalizedMessage());
        }
    }

    /**
     * Test of deleteTag method, of class TagList.
     */
    @Test
    public void testDeleteTag_Tag() {
        try {
            System.out.println("deleteTag");
            TagList instance = new TagList();
            this.addElements(instance);
            Tag t = new Tag("successFullTag", "");
            instance.addTag(t);
            assertEquals(true, instance.exists(t));
            instance.deleteTag(t);
            assertEquals(false, instance.exists(t));

        } catch (Exception ex) {
            fail(ex.getLocalizedMessage());
        }
    }

    /**
     * Test of exists method, of class TagList.
     */
    @Test
    public void testExists_Tag() {
        try {
            System.out.println("exists");

            TagList instance = new TagList();
            this.addElements(instance);
            Tag t = new Tag("successFullTag", "");
            instance.addTag(t);
            assertEquals(true, instance.exists(t));
            assertEquals(false, instance.exists(new Tag("incorrectTag", "")));
        } catch (Exception ex) {
            fail(ex.getLocalizedMessage());
        }
    }

    public void addElements(TagList instance) {
        try {
            instance.addTag(new Tag("myKey1", ""));
            instance.addTag(new Tag("myKey2", ""));
            instance.addTag(new Tag("myKey3", ""));
            instance.addTag(new Tag("myKey3", ""));
        } catch (Exception ex) {
            fail(ex.getLocalizedMessage());
        }
    }
}
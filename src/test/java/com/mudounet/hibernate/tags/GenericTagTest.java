/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.hibernate.tags;

import com.mudounet.hibernate.Movie;
import com.mudounet.utils.dbunit.ProjectDatabaseTestCase;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.dbunit.dataset.ITable;
import org.hibernate.Hibernate;
import org.hibernate.Transaction;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author gmanciet
 */
public class GenericTagTest extends ProjectDatabaseTestCase {

    protected static Logger logger = LoggerFactory.getLogger(GenericTagTest.class.getName());
    private Transaction tx;

    public GenericTagTest(String name) {
        super(name);
    }

    /**
     * Test of getId method, of class GenericTag.
     */
    @Test
    public void testPersists() throws Exception {

        logger.info("Test persistance");

        assertEquals("Specified event not found.", 0, this.getNbResults("select * from GENERICTAG where KEY='TestKey'"));

        GenericTag t = new GenericTag();
        t.setKey("TestKey");

        assertEquals(t.getId(), 0);

        template.saveOrUpdate(t);

        assertEquals(true, t.getId() != 0);
        logger.info("Id is " + t.getId());

        assertEquals("Specified event not found.", 1, this.getNbResults("select * from GENERICTAG where KEY='TestKey'"));


    }

    @Test
    public void testList() throws Exception {
        logger.info("Test retrieval");

        assertEquals(template.findList(GenericTag.class).size(), this.getNbResults("select * from GENERICTAG"));

        assertEquals(template.findList(SimpleTag.class).size(), this.getNbResults("select * from GENERICTAG inner join SIMPLETAG ON id = fk_tag"));

        assertEquals(template.findList(TagValue.class).size(), this.getNbResults("select * from GENERICTAG  inner join TAGVALUE ON id = fk_tag"));

    }

    @Test
    public void testMovies() throws Exception {

        template.keepConnectionOpened();
        List list = template.findList(Movie.class);
        ITable refList = this.getResults("select * from MOVIE");
        assertEquals(list.size(), refList.getRowCount());

        Iterator i = list.iterator();
        while (i.hasNext()) {
            Movie t = (Movie) i.next();
            logger.debug(t.toString());

            Movie m = (Movie) t;
            Set taglist = m.getTags();

            refList = this.getResults("select * from MOVIES_TAGS WHERE fk_movie=" + t.getId());
            assertEquals(taglist.size(), refList.getRowCount());

            for (Object obj : taglist) {
                GenericTag tag = (GenericTag) obj;

                String classType = "";

                if (this.getResults("select FK_TAG from TAGVALUE WHERE FK_TAG=" + tag.getId()).getRowCount() == 1) {
                    classType = com.mudounet.hibernate.tags.TagValue.class.getCanonicalName();
                }
                if (this.getResults("select FK_TAG from SIMPLETAG WHERE FK_TAG=" + tag.getId()).getRowCount() == 1) {
                    classType = com.mudounet.hibernate.tags.SimpleTag.class.getCanonicalName();
                }

                assertEquals(classType, Hibernate.getClass(tag).getCanonicalName());
            }
        }
        template.closeConnection();

        assertEquals(list.size(), this.getNbResults("select * from MOVIE"));

    }

    @Test
    public void testTags() throws Exception {
        template.keepConnectionOpened();
        List list = template.findList(GenericTag.class);
        ITable refList = this.getResults("select ID, KEY, S.FK_TAG AS S_TAG, T.FK_TAG AS T_TAG  from GENERICTAG LEFT OUTER JOIN SIMPLETAG AS S ON ID=S.FK_TAG LEFT OUTER JOIN TAGVALUE AS T ON ID=T.FK_TAG");
        assertEquals(list.size(), refList.getRowCount());

        Iterator i = list.iterator();
        while (i.hasNext()) {
            GenericTag t = (GenericTag) i.next();

            Set movielist = t.getMovies();

            refList = this.getResults("select * from MOVIES_TAGS WHERE fk_tag=" + t.getId());
            assertEquals(movielist.size(), refList.getRowCount());

            for (Object obj : movielist) {
                Movie movie = (Movie) obj;
                refList = this.getResults("select * from MOVIE WHERE id=" + movie.getId());
                assertEquals(1, refList.getRowCount());

                assertEquals(movie.getTitle(), refList.getValue(0, "TITLE"));
            }
        }

        template.closeConnection();
    }

    @Test
    public void testAddTag() throws Exception {
        String newKeyDescription = "myNewTag";
        SimpleTag newTag = new SimpleTag(newKeyDescription);

        template.keepConnectionOpened();
        template.saveOrUpdate(newTag);
        long newTagId = newTag.getId();
        logger.debug("ID of new tag is" + newTagId);
        template.closeConnection();
        assertEquals(1, this.getResults("select * from GenericTag inner join SIMPLETAG ON id = fk_tag where KEY='" + newKeyDescription + "'").getRowCount());
        template.keepConnectionOpened();
        newTag = (SimpleTag) template.find(SimpleTag.class, newTagId);
        template.closeConnection();
        template.keepConnectionOpened();
        template.delete(newTag);
        template.closeConnection();

    }

    @Test
    public void testDeleteTag() throws Exception {
        template.keepConnectionOpened();
        long idToDelete = 10;
        SimpleTag foundItem = (SimpleTag) template.find(SimpleTag.class, idToDelete);
        assertEquals("simpleKey3", foundItem.getKey());
        assertEquals(1, this.getResults("select * from GenericTag inner join SIMPLETAG ON id = fk_tag where ID=" + idToDelete + "").getRowCount());

        template.closeConnection();
        logger.debug("Tring to delete " + foundItem);
        template.delete(foundItem);
        assertEquals(0, this.getResults("select * from GenericTag inner join SIMPLETAG ON id = fk_tag where ID=" + idToDelete + "").getRowCount());
    }

    @Override
    protected String getDataSetFilename() {
        return "TestGenericTag.xml";
    }
}

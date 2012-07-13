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

    private static Logger logger = LoggerFactory.getLogger(GenericTagTest.class.getName());

    public GenericTagTest(String name) {
        super(name);
    }

    /**
     * Test of getId method, of class GenericTag.
     * @throws Exception 
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
        template.closeSession();

        assertEquals("Specified event not found.", 1, this.getNbResults("select * from GENERICTAG where KEY='TestKey'"));


    }

    @Test
    public void testList() throws Exception {
        logger.info("Test retrieval");

        assertEquals(template.findList(GenericTag.class).size(), this.getNbResults("select * from GENERICTAG"));

        assertEquals(template.findList(Tag.class).size(), this.getNbResults("select * from GENERICTAG inner join TAG ON id = fk_tag"));

        assertEquals(template.findList(Actor.class).size(), this.getNbResults("select * from GENERICTAG  inner join ACTOR ON id = fk_tag"));

    }

    @Test
    public void testMovies() throws Exception {

        template.beginTransaction();
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

                if (this.getResults("select FK_TAG from ACTOR WHERE FK_TAG=" + tag.getId()).getRowCount() == 1) {
                    classType = com.mudounet.hibernate.tags.Actor.class.getCanonicalName();
                }
                if (this.getResults("select FK_TAG from TAG WHERE FK_TAG=" + tag.getId()).getRowCount() == 1) {
                    classType = com.mudounet.hibernate.tags.Tag.class.getCanonicalName();
                }

                assertEquals(classType, Hibernate.getClass(tag).getCanonicalName());
            }
        }
        template.closeSession();

        assertEquals(list.size(), this.getNbResults("select * from MOVIE"));

    }

    @Test
    public void testTags() throws Exception {
        template.beginTransaction();
        List<GenericTag> list = template.findList(GenericTag.class);
        ITable refList = this.getResults("select ID, KEY, S.FK_TAG AS S_TAG, T.FK_TAG AS T_TAG  from GENERICTAG LEFT OUTER JOIN TAG AS S ON ID=S.FK_TAG LEFT OUTER JOIN ACTOR AS T ON ID=T.FK_TAG");
        assertEquals(list.size(), refList.getRowCount());

        Iterator i = list.iterator();
        while (i.hasNext()) {
            GenericTag t = (GenericTag) i.next();

            Set<Movie> movielist = t.getMovies();

            refList = this.getResults("select * from MOVIES_TAGS WHERE fk_tag=" + t.getId());
            assertEquals(movielist.size(), refList.getRowCount());

            for (Object obj : movielist) {
                Movie movie = (Movie) obj;
                refList = this.getResults("select * from MOVIE WHERE id=" + movie.getId());
                assertEquals(1, refList.getRowCount());

                assertEquals(movie.getTitle(), refList.getValue(0, "TITLE"));
            }
        }

        template.closeSession();
    }

    @Test
    public void testAddTag() throws Exception {
        String newKeyDescription = "myNewTag";
        Tag newTag = new Tag(newKeyDescription);

        template.beginTransaction();
        template.saveOrUpdate(newTag);
        long newTagId = newTag.getId();
        logger.debug("ID of new tag is" + newTagId);
        template.closeSession();
        assertEquals(1, this.getResults("select * from GenericTag inner join TAG ON id = fk_tag where KEY='" + newKeyDescription + "'").getRowCount());
        template.beginTransaction();
        newTag = (Tag) template.find(Tag.class, newTagId);
        template.closeSession();
        template.beginTransaction();
        template.delete(newTag);
        template.closeSession();

    }

    @Test
    public void testDeleteTag() throws Exception {
        template.beginTransaction();
        long idToDelete = 10;
        Tag foundItem = (Tag) template.find(Tag.class, idToDelete);
        assertEquals("simpleKey3", foundItem.getKey());
        assertEquals(1, this.getResults("select * from GenericTag inner join TAG ON id = fk_tag where ID=" + idToDelete + "").getRowCount());

        template.closeSession();
        logger.debug("Tring to delete " + foundItem);
        template.delete(foundItem);
        template.closeSession();
        assertEquals(0, this.getResults("select * from GenericTag inner join TAG ON id = fk_tag where ID=" + idToDelete + "").getRowCount());
    }

    @Override
    protected String getDataSetFilename() {
        return "TestGenericTag.xml";
    }
}

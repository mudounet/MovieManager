/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.hibernate.tags;

import com.mudounet.hibernate.movie.GenericMovie;
import com.mudounet.hibernate.movie.ProcessedMovie;
import com.mudounet.hibernate.movie.QueuedMovie;
import com.mudounet.utils.hibernate.HibernateFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;
import com.mudounet.utils.dbunit.ProjectDatabaseTestCase;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.dbunit.dataset.ITable;
import org.hibernate.Hibernate;

/**
 *
 * @author gmanciet
 */
public class GenericTagTest extends ProjectDatabaseTestCase {
    protected static Logger logger = Logger.getLogger(GenericTagTest.class.getName());
	private Session session;
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
        List list = template.findList(GenericMovie.class);
        ITable refList = this.getResults("select * from GENERICMOVIE");
        assertEquals(list.size(), refList.getRowCount());

        Iterator i = list.iterator();
        while (i.hasNext()) {
            GenericMovie t = (GenericMovie) i.next();
            logger.debug(t.toString());

            Set taglist = t.getTags();

            refList = this.getResults("select * from MOVIES_TAGS WHERE fk_movie=" + t.getId());
            assertEquals(taglist.size(), refList.getRowCount());

            for (Object obj : taglist) {
                GenericTag tag = (GenericTag) obj;
                refList = this.getResults("select * from GENERICTAG WHERE id=" + tag.getId());
                assertEquals(1, refList.getRowCount());

                assertEquals(tag.getKey(), refList.getValue(0, "KEY"));

                char type = refList.getValue(0, "TYPE").toString().charAt(0);
                String classType = "";

                switch (type) {
                    case 'G':
                        classType = com.mudounet.hibernate.tags.GenericTag.class.getCanonicalName();
                        break;
                    case 'T':
                        classType = com.mudounet.hibernate.tags.TagValue.class.getCanonicalName();
                        break;
                    case 'S':
                        classType = com.mudounet.hibernate.tags.SimpleTag.class.getCanonicalName();
                        break;
                }

                assertEquals(classType, Hibernate.getClass(tag).getCanonicalName());
            }
        }
        template.closeConnection();

        assertEquals(list.size(), this.getNbResults("select * from GENERICMOVIE"));

    }

    @Test
    public void testTags() throws Exception {
        template.keepConnectionOpened();
        List list = template.findList(GenericTag.class);
        ITable refList = this.getResults("select * from GENERICTAG");
        assertEquals(list.size(), refList.getRowCount());

        Iterator i = list.iterator();
        while (i.hasNext()) {
            GenericTag t = (GenericTag) i.next();

            Set movielist = t.getMovies();

            refList = this.getResults("select * from MOVIES_TAGS WHERE fk_tag=" + t.getId());
            assertEquals(movielist.size(), refList.getRowCount());

            for (Object obj : movielist) {
                GenericMovie movie = (GenericMovie) obj;
                refList = this.getResults("select * from GENERICMOVIE WHERE id=" + movie.getId());
                assertEquals(1, refList.getRowCount());

                assertEquals(movie.getTitle(), refList.getValue(0, "TITLE"));

                char type = refList.getValue(0, "TYPE").toString().charAt(0);
                String classType = "";

                switch (type) {
                    case 'G':
                        classType = com.mudounet.hibernate.movie.GenericMovie.class.getCanonicalName();
                        break;
                    case 'M':
                        classType = com.mudounet.hibernate.movie.Movie.class.getCanonicalName();
                        break;
                    case 'P':
                        classType = com.mudounet.hibernate.movie.ProcessedMovie.class.getCanonicalName();
                        break;
                    case 'Q':
                        classType = com.mudounet.hibernate.movie.QueuedMovie.class.getCanonicalName();
                        break;
                }

                assertEquals(classType, Hibernate.getClass(movie).getCanonicalName());
            }
        }

        template.closeConnection();
    }

    @Test
    public void testQueue() throws Exception {

        List list = null;
        ITable refList;
        list = template.findList(QueuedMovie.class);
        refList = this.getResults("select * from GENERICMOVIE where type='Q'");
        assertEquals(list.size(), refList.getRowCount());

        Iterator i = list.iterator();
        while (i.hasNext()) {
            QueuedMovie q = (QueuedMovie) i.next();
            refList = this.getResults("select * from GENERICMOVIE where id="+q.getId());

            session = HibernateFactory.openSession();
            tx = session.beginTransaction();


            ProcessedMovie p = new ProcessedMovie();
            p.setTitle(q.getTitle());
            p.setTags(q.getTags());

            session.saveOrUpdate(p);
            session.delete(q);
            tx.commit();
            session.close();

            // Checking that object has been deleted
            assertEquals(0,this.getResults("select * from GENERICMOVIE where id="+q.getId()).getRowCount());

            // And also its associated tag
            assertEquals(0,this.getResults("select * from MOVIES_TAGS where fk_movie="+q.getId()).getRowCount());


            ITable refList2 = this.getResults("select * from GENERICMOVIE where id="+p.getId());
            // Checking that new object exist
            assertEquals(1,refList2.getRowCount());

            // And also its associated tag
            assertEquals(q.getTags().size(),this.getResults("select * from MOVIES_TAGS where fk_movie="+p.getId()).getRowCount());

            

            


        }
    }

    @Test
    public void testAddTag() throws Exception {
        SimpleTag newTag = new SimpleTag();
        String newKeyDescription = "myNewTag";
        newTag.setKey(newKeyDescription);
        template.keepConnectionOpened();
        template.saveOrUpdate(newTag);
        long newTagId = newTag.getId();
        logger.debug("ID of new tag is"+newTagId);
        template.closeConnection();
        assertEquals(1,this.getResults("select * from GenericTag inner join SIMPLETAG ON id = fk_tag where KEY='"+newKeyDescription+"'").getRowCount());
        template.keepConnectionOpened();
        newTag = (SimpleTag)template.find(SimpleTag.class, newTagId);
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
        assertEquals(1,this.getResults("select * from GenericTag inner join SIMPLETAG ON id = fk_tag where ID="+idToDelete+"").getRowCount());

        template.closeConnection();
        logger.debug("Tring to delete "+foundItem);
        template.delete(foundItem);
        assertEquals(0,this.getResults("select * from GenericTag inner join SIMPLETAG ON id = fk_tag where ID="+idToDelete+"").getRowCount());
    }

    @Override
    protected String getDataSetFilename() {
        return "TestGenericTag.xml";
    }
}

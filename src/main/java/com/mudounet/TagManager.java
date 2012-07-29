package com.mudounet;

import com.mudounet.hibernate.tags.Tag;
import com.mudounet.utils.hibernate.DataAccessLayerException;
import com.mudounet.utils.hibernate.HibernateThreadSession;
import javax.swing.JOptionPane;
import org.slf4j.LoggerFactory;

/**
 *
 * @author isabelle
 */
public class TagManager {

    protected static org.slf4j.Logger logger = LoggerFactory.getLogger(TagManager.class.getName());

    public static void main(String[] args) {
        String tagRef;
        do {
            Object[] possibilities = null;
            tagRef = (String) JOptionPane.showInputDialog(
                    null,
                    "Enter tag to add :\n",
                    "Customized Dialog",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    possibilities,
                    "");

            if ((tagRef != null) && (tagRef.length() > 0)) {
                addTag(tagRef);
            }

        } while (((tagRef != null) && (tagRef.length() > 0)));
    }

    public static void addTag(String tagToAdd) {
        try {
            HibernateThreadSession template = GlobalProperties.getTemplate();

            Tag t = new Tag();
            t.setKey(tagToAdd);
            if(template.find(Tag.class, "key", tagToAdd) != null) {
                logger.error("Tag "+tagToAdd+" is already defined");
                return;
            }
            template.beginTransaction();
            template.saveOrUpdate(t);
            template.endTransaction();
        } catch (DataAccessLayerException ex) {
            logger.error(ex.getMessage());
        }
    }
}
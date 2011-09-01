/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.hibernate;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;

public class HibernateFactory {
    protected static Logger logger = Logger.getLogger(HibernateFactory.class.getName());
    private static SessionFactory sessionFactory;
    private static Configuration configuration;
/*    private static Log log =
            LogFactory.getLog(HibernateFactory.class);*/

    public static Configuration getConfiguration() {
        return configuration;
    }

    public static SessionFactory buildSessionFactory()
            throws HibernateException {
        if (sessionFactory != null) {
            closeFactory();
        }
        configuration = new Configuration();
        configuration.configure();
        sessionFactory = configuration.buildSessionFactory();
        return sessionFactory;
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static Session openSession() throws HibernateException {
        return sessionFactory.openSession();
    }

    public static void closeFactory() {
        if (sessionFactory != null) {
            try {
                sessionFactory.close();
            } catch (HibernateException ignored) {
                logger.error("Couldn't close SessionFactory",
                        ignored);
            }
        }
    }

    public static void close(Session session) {
        if (session != null) {
            try {
                session.close();
            } catch (HibernateException ignored) {
                logger.error("Couldn't close Session", ignored);
            }
        }
    }

    public static void rollback(Transaction tx) {
        try {
            if (tx != null) {
                tx.rollback();
            }
        } catch (HibernateException ignored) {
            logger.error("Couldn't rollback Transaction", ignored);
        }
    }

    static void buildIfNeeded() {
        if (sessionFactory == null) {
            logger.debug("Opening session");
            buildSessionFactory();
        }
    }



}

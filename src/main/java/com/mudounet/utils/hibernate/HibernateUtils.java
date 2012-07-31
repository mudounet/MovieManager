/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author gmanciet
 */
public class HibernateUtils {

    private static final Logger logger = LoggerFactory.getLogger(HibernateUtils.class.getName());
    private static final SessionFactory sessionFactory;
    private static final Configuration configuration;
    private static final ThreadLocal<HibernateThreadSession> threadedSession = new ThreadLocal<HibernateThreadSession>();

    static {
        try {
            logger.debug("Building Hibernate Factory");
            configuration = new Configuration();
            configuration.configure();
            sessionFactory = configuration.buildSessionFactory();
            logger.debug("Hibernate Factory built");
        } catch (HibernateException ex) {
            throw new RuntimeException("Exception building SessionFactory: " + ex.getMessage(), ex);
        }
    }
    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static HibernateThreadSession currentSession() throws HibernateException {
        HibernateThreadSession s = threadedSession.get();
        if (s == null) {
            logger.debug("Creating threaded session...");
            s = new HibernateThreadSession();
            threadedSession.set(s);
        }

        return s;
    }
    
    public static void destroySession() throws Exception {
        HibernateThreadSession s = threadedSession.get();
        if(s != null) {
            s.closeSession();
            s = null;
        }
        threadedSession.set(s);
    }
    
    public static void closeAll() {
        HibernateUtils.getSessionFactory().close();
    }
    
}

package com.mudounet.utils.hibernate;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A layer supertype that handles the common operations for all Data Access
 * Objects.
 */
public class AbstractDao {

    private static final Logger logger = LoggerFactory.getLogger(AbstractDao.class.getName());
    private static final SessionFactory sessionFactory;
    private static final Configuration configuration;
    private static final ThreadLocal<Session> session = new ThreadLocal<Session>();
    private Query query;
    private Transaction tx;
    private boolean transactionInitiated = false;

    public static Configuration getConfiguration() {
        return configuration;
    }

    static {
        try {
            logger.debug("Building Hibernate Factory");
            configuration = new Configuration();
            configuration.configure();
            sessionFactory = configuration.buildSessionFactory();

        } catch (HibernateException ex) {
            throw new RuntimeException("Exception building SessionFactory: " + ex.getMessage(), ex);
        }
    }

    public static Session currentSession() throws HibernateException {
        Session s = session.get();
        if (!isSessionOpened()) {
            logger.debug("Opening session...");
            s = sessionFactory.openSession();
            session.set(s);
            if (isSessionOpened()) {
                logger.debug("Session opened successfully.");
            } else {
                logger.error("Error while opening connection");
            }
        }

        return s;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (tx != null) {
            throw new Exception("Commit should be properly cancelled!");
        }
        rollback();
    }

    public static void closeSession() throws HibernateException {

        Session s = session.get();
        if (s != null) {
            logger.debug("Closing connection...");
            s.flush();
            s.close();
        }
        session.set(null);
    }

    public void rollback() {
        try {
            if (tx != null) {
                tx.rollback();
            }
        } catch (HibernateException ignored) {
            logger.error("Couldn't rollback Transaction", ignored);
        }
    }

    public static boolean isSessionOpened() {
        Session s = session.get();
        return (s != null && (s.isConnected() && s.isOpen()));
    }

    private boolean _startOperation(boolean needTransaction) throws HibernateException {
        currentSession();

        if (needTransaction && !transactionInitiated) {
            beginTransaction();
            return true;
        }
        return false;
    }

    private void _endOperation() {
    }

    public Query createQuery(String hql) throws DataAccessLayerException {
        try {
            _startOperation(false);
            this.query = currentSession().createQuery(hql);
            _endOperation();
            return query;
        } catch (HibernateException e) {
            handleException(e);
            return null;
        }
    }

    public List getQueryResults() throws DataAccessLayerException {
        try {
            _startOperation(true);
            List results = query.list();
            _endOperation();
            return results;
        } catch (HibernateException e) {
            handleException(e);
            return null;
        }
    }

    public void saveOrUpdate(Object obj) throws DataAccessLayerException {
        try {
            _startOperation(true);
            currentSession().saveOrUpdate(obj);
            logger.debug("Object \"" + obj + "\" saved");
            _endOperation();
        } catch (HibernateException e) {
            handleException(e);
        }
    }

    public void beginTransaction() {


        if (tx == null || !tx.isActive()) {
            tx = currentSession().beginTransaction();
            if (tx.isActive()) {
                logger.debug("Transaction initiated");
                transactionInitiated = true;
            } else {
                logger.error("Transaction not initiated");
                transactionInitiated = false;
            }
        }
    }

    public void cancelTransaction() {
        if (tx != null && tx.isActive()) {
            tx.rollback();
            logger.debug("Transaction commit cancelled.");
        } else {
            logger.error("Transaction was not initiated");
        }
        this.transactionInitiated = false;
    }

    public void endTransaction() {
        if (tx != null && tx.isActive()) {
            tx.commit();
            logger.debug("Transaction commit performed.");
        } else {
            logger.error("Transaction was not initiated");
        }
        this.transactionInitiated = false;
    }

    public void delete(Object obj) throws DataAccessLayerException {
        try {
            _startOperation(true);
            currentSession().delete(obj);
            _endOperation();
        } catch (HibernateException e) {
            handleException(e);
        }
    }

    public Object find(Class clazz, Long id) throws DataAccessLayerException {
        Object obj = null;
        try {
            _startOperation(false);
            obj = currentSession().load(clazz, id);
            _endOperation();
        } catch (HibernateException e) {
            handleException(e);
        }
        return obj;
    }

    public Object find(Class clazz, String Column, String Value) throws DataAccessLayerException {
        List result = this.findList(clazz, Restrictions.like(Column, Value, MatchMode.EXACT), 1);
        if (result.size() == 1) {
            return result.get(0);
        } else {
            return null;
        }
    }

    public List findList(Class clazz) throws DataAccessLayerException {
        return findList(clazz, null, 0);
    }

    public List findList(Class clazz, Criterion crit, int resultLimit) throws DataAccessLayerException {
        List objects = null;
        try {
            _startOperation(false);

            Criteria criteria = currentSession().createCriteria(clazz.getName());
            if (crit != null) {
                criteria.add(crit);
            }
            if (resultLimit > 0) {
                criteria.setMaxResults(resultLimit);
            }
            objects = criteria.list();
            _endOperation();
        } catch (HibernateException e) {
            handleException(e);
        }
        return objects;
    }

    protected void handleException(HibernateException e) throws DataAccessLayerException {
        rollback();

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        //
        // Instead of writting the stack trace in the console we write it
        // to the PrintWriter, to get the stack trace message we then call
        // the toString() method of StringWriter.
        //
        e.printStackTrace(pw);
        logger.debug("Unexpected exception : " + sw.toString());
        throw e;
    }
}

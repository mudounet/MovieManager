package com.mudounet.utils.hibernate;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
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
public class HibernateThreadSession {

    private static final Logger logger = LoggerFactory.getLogger(HibernateThreadSession.class.getName());
    private Query query;
    private Session session;
    private Transaction tx;
    private boolean transactionInitiated = false;

    public HibernateThreadSession() {
        tx = null;
        query = null;
        transactionInitiated = false;
    }

    public Session openSession() throws HibernateException {

        if(!isSessionOpened()) {
            session = HibernateUtils.getSessionFactory().openSession();
            if (isSessionOpened()) {
                logger.debug("Created threaded session successfully...");
            } else {
                logger.error("Error while creating threaded session");
                session = null;
            }
        }
        return session;
    }

    @Override
    protected void finalize() throws Throwable {

        super.finalize();
        if (isSessionOpened()) {
            logger.error("Session should be closed by yourself.");
            closeSession();
        }
    }

    public void closeSession() throws Exception {
        if (tx != null) {
            cancelTransaction();
            throw new Exception("Commit should be properly cancelled using cancelTransaction() or endTransaction()!");
        }

        if (session != null) {
            logger.debug("Closing session...");
            session.flush();
            session.close();
            HibernateUtils.getSessionFactory().close();
            session = null;
            logger.debug("Session cleanup finished.");
        }
    }

    public boolean isSessionOpened() {
        return (session != null && (session.isConnected() && session.isOpen()));
    }

    private boolean _startOperation(boolean needTransaction) throws HibernateException {
        openSession();

        if (needTransaction && !transactionInitiated) {
            throw new HibernateException("Needs to create Transaction for this kind of operation.");
        }
        return false;
    }

    private void _endOperation() {
    }

    public Query createQuery(String hql) throws DataAccessLayerException {
        try {
            _startOperation(false);
            this.query = session.createQuery(hql);
            _endOperation();
            return query;
        } catch (HibernateException e) {
            handleException(e);
            return null;
        }
    }

    public List getQueryResults() throws DataAccessLayerException {
        try {
            _startOperation(false);
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
            session.saveOrUpdate(obj);
            logger.debug("Object \"" + obj + "\" saved");
            _endOperation();
        } catch (HibernateException e) {
            handleException(e);
        }
    }

    public void beginTransaction() {
        openSession();
        
        if (tx == null || !tx.isActive()) {
            tx = session.beginTransaction();
            if (tx.isActive()) {
                logger.debug("Transaction initiated");
                transactionInitiated = true;
            } else {
                logger.error("Transaction not initiated");
                transactionInitiated = false;
            }
        }
    }

    public void cancelTransaction() throws HibernateException {
        if (tx != null && tx.isActive()) {
            tx.rollback();
            this.transactionInitiated = false;
            logger.debug("Transaction cancelled.");
        } else {
            throw new HibernateException("Transaction was not initiated");
        }
        tx = null;
    }

    public void endTransaction() {
        if (tx != null && tx.isActive()) {
            tx.commit();
            tx = null;
            this.transactionInitiated = false;
            logger.debug("Transaction commit done.");

        } else {
            logger.error("Transaction was not initiated");
        }
        tx = null;
    }

    public void delete(Object obj) throws DataAccessLayerException {
        try {
            _startOperation(true);
            session.delete(obj);
            _endOperation();
        } catch (HibernateException e) {
            handleException(e);
        }
    }

    public Object find(Class clazz, Long id) throws DataAccessLayerException {
        Object obj = null;
        try {
            _startOperation(false);
            obj = session.load(clazz, id);
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

            Criteria criteria = session.createCriteria(clazz.getName());
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

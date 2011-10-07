package com.mudounet.utils.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import org.apache.log4j.Logger;
import java.io.StringWriter;
import java.io.PrintWriter;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 * A layer supertype that handles the common operations for all Data Access Objects.
 */
public class AbstractDao {

    protected static Logger logger = Logger.getLogger(AbstractDao.class.getName());
    private Session session;
    private boolean keepConnectionOpened = false;
    private boolean oldKeepConnectionOpened = false;
    protected Query query;
    private Transaction tx;

    public AbstractDao() {
        HibernateFactory.buildIfNeeded();
    }

    private void _startOperation() throws HibernateException {
        logger.debug("Initiating action");
        if (session != null && session.isOpen() && session.isConnected()) {
            logger.debug("Session already running");
        } else {
            session = HibernateFactory.openSession();
            if (session.isOpen() && session.isConnected()) {
                logger.debug("Session opened");
            } else {
                logger.error("Error while opening connection");
            }
        }

        tx = session.beginTransaction();
        if (tx.isActive()) {
            logger.debug("Transaction initiated");
        } else {
            logger.error("Transaction not initiated");
        }
    }

    private void _endOperation() {
        if (tx != null && tx.isActive()) {
            tx.commit();
        } else {
            logger.error("Transaction was not initiated");
        }
    }

    public Query createQuery(String hql) throws DataAccessLayerException {
        try {
            _startOperation();
            this.oldKeepConnectionOpened = this.keepConnectionOpened; // backup of current value, since session must be kept opened between two queries.
            this.keepConnectionOpened = true;
            this.query = session.createQuery(hql);
            _endOperation();
            return query;
        } catch (HibernateException e) {
            handleException(e);
            return null;
        } finally {
            _closeConnectionIfRequested();
        }
    }

    public List<Object> getQueryResults() throws DataAccessLayerException {
        try {
            _startOperation();
            List<Object> results = query.list();
            this.keepConnectionOpened = this.oldKeepConnectionOpened;
            _endOperation();
            return results;
        } catch (HibernateException e) {
            handleException(e);
            return null;
        } finally {
            _closeConnectionIfRequested();
        }
    }

    public void saveOrUpdate(Object obj) throws DataAccessLayerException {
        try {
            _startOperation();
            session.saveOrUpdate(obj);
            logger.debug("Object \"" + obj + "\" saved");
            _endOperation();
        } catch (HibernateException e) {
            handleException(e);
        } finally {
            _closeConnectionIfRequested();
        }
    }

    public void keepConnectionOpened() {
        this.keepConnectionOpened = true;
    }

    public void closeConnection() {
        this.keepConnectionOpened = false;
        this._closeConnectionIfRequested();
    }

    public void delete(Object obj) throws DataAccessLayerException {
        try {
            _startOperation();
            session.delete(obj);
            _endOperation();
        } catch (HibernateException e) {
            handleException(e);
        } finally {
            _closeConnectionIfRequested();
        }
    }

    public Object find(Class<Object> clazz, Long id) throws DataAccessLayerException {
        Object obj = null;
        try {
            _startOperation();
            obj = session.load(clazz, id);
            _endOperation();
        } catch (HibernateException e) {
            handleException(e);
        } finally {
            _closeConnectionIfRequested();
        }
        return obj;
    }

    public Object find(Class<Object> clazz, String Column, String Value) throws DataAccessLayerException {
        List<Object> result = this.findList(clazz, Restrictions.like(Column, Value, MatchMode.EXACT), 1);

        if (result.size() == 1) {
            return result.get(0);
        } else {
            return null;
        }
    }

    public List<Object> findList(Class<Object> clazz) throws DataAccessLayerException {
        return findList(clazz, null, 0);
    }

    @SuppressWarnings("unchecked")
    public List<Object> findList(Class<Object> clazz, Criterion crit, int resultLimit) throws DataAccessLayerException {
        List<Object> objects = null;
        try {
            _startOperation();
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
        } finally {
            _closeConnectionIfRequested();
        }
        return objects;
    }

    protected void handleException(HibernateException e) throws DataAccessLayerException {
        HibernateFactory.rollback(tx);

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

    private void _closeConnectionIfRequested() {
        if (this.keepConnectionOpened) {
            logger.debug("Connection is kept opened");
            return;
        }

        HibernateFactory.close(session);
        if (session.isConnected() || session.isOpen()) {
            logger.error("Connection is not closed successfully.");
        } else {
            logger.debug("Connection closed");
        }
    }
}

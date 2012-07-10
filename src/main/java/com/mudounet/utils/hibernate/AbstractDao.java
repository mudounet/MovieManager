package com.mudounet.utils.hibernate;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import org.hibernate.*;
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

    protected static Logger logger = LoggerFactory.getLogger(AbstractDao.class.getName());
    private Session session;
    private boolean keepConnectionOpened = false;
    private boolean oldKeepConnectionOpened = false;
    private Query query;
    private Transaction tx;
    private boolean transactionInitiated = false;

    public AbstractDao() {
        HibernateFactory.buildIfNeeded();
    }

    private void _startOperation() throws HibernateException {
        if (!transactionInitiated) {
            beginTransaction();
        }
    }
    
    private void _openSession() {
        if (session == null || !session.isOpen() || !session.isConnected()) {
            session = HibernateFactory.openSession();
            if (session != null && session.isOpen() && session.isConnected()) {
                logger.debug("Session opened");
            } else {
                logger.error("Error while opening connection");
            }
        }
    }

    private void _endOperation() {
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
        }
    }

    public List getQueryResults() throws DataAccessLayerException {
        try {
            _startOperation();
            List results = query.list();
            this.keepConnectionOpened = this.oldKeepConnectionOpened;
            _endOperation();
            return results;
        } catch (HibernateException e) {
            handleException(e);
            return null;
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
        } 
    }

    
    
    public void beginTransaction() {
        _openSession();
        
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
    
    public void closeConnection() {

        this._closeConnectionIfRequested();
    }

    public void delete(Object obj) throws DataAccessLayerException {
        try {
            _startOperation();
            session.delete(obj);
            _endOperation();
        } catch (HibernateException e) {
            handleException(e);
        } 
    }

    public Object find(Class clazz, Long id) throws DataAccessLayerException {
        Object obj = null;
        try {
            _startOperation();
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

    public boolean isSessionOpened() {
        return (session != null && (session.isConnected() || session.isOpen()));
    }
    
    private void _closeConnectionIfRequested() {
        if(this.transactionInitiated) {
            this.endTransaction();
        }
        session.flush();
        session.close();
        if (session.isConnected() || session.isOpen()) {
            logger.error("Connection is not closed successfully.");
        } else {
            logger.debug("Connection closed");
        }
    }
}

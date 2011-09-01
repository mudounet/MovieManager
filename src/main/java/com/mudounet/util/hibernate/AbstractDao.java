package com.mudounet.util.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import org.apache.log4j.Logger;
import java.io.StringWriter;
import java.io.PrintWriter;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;

/**
 * A layer supertype that handles the common operations for all Data Access Objects.
 */
public class AbstractDao {

    private static class DataAccessLayerException extends Exception {

        public DataAccessLayerException() {
        }

        private DataAccessLayerException(HibernateException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);

            //
            // Instead of writting the stack trace in the console we write it
            // to the PrintWriter, to get the stack trace message we then call
            // the toString() method of StringWriter.
            //
            e.printStackTrace(pw);
            logger.debug("Unexpected exception : " + sw.toString());
            throw new UnsupportedOperationException("Error = " + sw.toString());
        }
    }
    protected static Logger logger = Logger.getLogger(AbstractDao.class.getName());
    private Session session;
    private boolean keepConnectionOpened = false;
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

    public void saveOrUpdate(Object obj) throws DataAccessLayerException {
        try {
            _startOperation();
            session.saveOrUpdate(obj);
            logger.debug("Object " + obj + " saved");
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

    public Object find(Class clazz, Long id) throws DataAccessLayerException {
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

    public List findList(Class clazz) throws DataAccessLayerException {
        return findList(clazz, null);
    }
    
    public List findList(Class clazz, Criterion crit) throws DataAccessLayerException {
        List objects = null;
        try {
            _startOperation();
            Criteria criteria = session.createCriteria(clazz.getName());
            if(crit != null) criteria.add(crit);        
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

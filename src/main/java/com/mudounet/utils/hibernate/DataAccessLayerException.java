/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.hibernate;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.HibernateException;

/**
 *
 * @author gmanciet
 */
public class DataAccessLayerException extends Exception {

    protected static Logger logger = LoggerFactory.getLogger(DataAccessLayerException.class.getName());
    private static final long serialVersionUID = 1L;

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

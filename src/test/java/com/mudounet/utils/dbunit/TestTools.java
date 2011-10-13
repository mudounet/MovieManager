/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.dbunit;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.log4j.Logger;

/**
 *
 * @author isabelle
 */
public class TestTools {

protected static Logger logger = Logger.getLogger(TestTools.class.getName());
    
    public static InputStream loadFromClasspath(String s) throws Exception {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        return cl.getResourceAsStream(s);
    }

    public static File createTempDirectory() throws IOException {
        final File temp;

        temp = File.createTempFile("temp", Long.toString(System.nanoTime()));

        if (!temp.delete()) {
            throw new IOException("Could not delete temp file: " + temp.getAbsolutePath());
        }

        if (!temp.mkdir()) {
            throw new IOException("Could not create temp directory: " + temp.getAbsolutePath());
        }

        logger.debug("Created temporary directory : "+temp.getAbsolutePath());
        return temp;
    }
    
    public static File createTempFile() throws IOException {
        final File temp;

        temp = File.createTempFile("temporaryComponent", ".xml");

        return temp;
    }

}

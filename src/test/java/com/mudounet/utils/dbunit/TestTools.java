/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.dbunit;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author isabelle
 */
public class TestTools {

    public static InputStream loadFromClasspath(String s) throws Exception {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        return cl.getResourceAsStream(s);
    }

    public static File getFileFromClasspath(String s) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL url = cl.getResource(s);
        File f;
        try {
            f = new File(url.toURI());
        } catch (URISyntaxException ex) {
            f = new File(url.getPath());
        }
        
        return f;
    }
}

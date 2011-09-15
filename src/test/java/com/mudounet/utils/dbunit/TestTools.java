/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.dbunit;

import java.io.File;
import java.io.InputStream;

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
        return new File(cl.getResource(s).getFile());
    }
}

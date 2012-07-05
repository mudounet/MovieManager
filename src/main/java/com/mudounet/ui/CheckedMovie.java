/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.ui;

import com.mudounet.hibernate.Movie;

/**
 *
 * @author gmanciet
 */
public class CheckedMovie extends Movie {
    private byte state = 0;
    
    public enum state {
    EQUALS, DELETED, RENAMED, MODIFIED
    }
    
    public void checkMovie() {
        
    }
}

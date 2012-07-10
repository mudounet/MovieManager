/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.hibernate;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author gmanciet
 */
public class MovieTest {
    
    public MovieTest() {
    }

    /**
     * Test of equals method, of class Movie.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        
        Movie refMovie = new Movie();
        refMovie.setMd5("aaaabbbbccccdddd");
        refMovie.setFastMd5("bbbbccccddddeeee");
        refMovie.setSize(54666789);
        refMovie.setModificationDate(45678L);
        refMovie.setTitle("Reference title");
        Movie instance = new Movie();
        instance.setMd5("aaaabbbbccccdddd");
        instance.setFastMd5("bbbbccccddddeeee");
        instance.setSize(54666789);
        instance.setModificationDate(45678L);
        instance.setTitle("Reference title");

        assertEquals(true, instance.equals(refMovie));
        instance.setTitle("Reference title old");
        assertEquals(true, instance.equals(refMovie));
        instance.setModificationDate(12L);
        assertEquals(true, instance.equals(refMovie));
        instance.setMd5("bbbbccccddddaaaa");
        assertEquals(true, instance.equals(refMovie));
        instance.setSize(10000);
        assertEquals(false, instance.equals(refMovie));
        instance.setSize(54666789);
        assertEquals(true, instance.equals(refMovie));
        instance.setFastMd5("eeeebbbbddddeeee");
        assertEquals(false, instance.equals(refMovie));
        instance.setFastMd5("bbbbccccddddeeee");
        assertEquals(true, instance.equals(refMovie));
    }

    /**
     * Test of hashCode method, of class Movie.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        Movie instance = new Movie();

        assertEquals(4805, instance.hashCode());
        instance.setFastMd5("aaaabbbbccccdddd");
        assertEquals(10741061, instance.hashCode());
        instance.setSize(54666789);
        assertEquals(1705411520, instance.hashCode());
        instance.setFastMd5("ddddbbbbccccdddd");
        assertEquals(-1995387776, instance.hashCode());
        instance.setFastMd5("aaaabbbbccccdddd");
        assertEquals(1705411520, instance.hashCode());
        instance.setModificationDate(12L);
        assertEquals(1705411520, instance.hashCode());
    }
}

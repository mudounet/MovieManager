/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author isabelle
 */
public class MovieFileFilter implements FileFilter {

    public boolean accept(File file) {
        
        String[] list = {"avi", "mp4", "mpg", "wmv", "mkv", "flv", "vob", "mov"};
        List<String> listOfMovies = Arrays.asList(list);

        for (String e : listOfMovies) {
            if(file.getName().toLowerCase().endsWith(e)) {
                return true;
            }
        }

        return false;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.hibernate;

import com.mudounet.utils.Md5Generator;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author gmanciet
 */
public class MovieProxy {

    private static final Logger logger = LoggerFactory.getLogger(MovieProxy.class.getName());
    private String realPath;
    private File fileOfMovie = null;
    private Movie movie = new Movie();
    
    public MovieProxy(File fileOfMovie) {
        this(fileOfMovie, fileOfMovie.getName());
    }
    
    public MovieProxy(File fileOfMovie, String title) {
        this.fileOfMovie = fileOfMovie;
        this.realPath = fileOfMovie.getPath();
        movie.setFilename(fileOfMovie.getName());
        movie.setTitle(fileOfMovie.getName());
        movie.setSize(fileOfMovie.length());
        movie.setModificationDate(fileOfMovie.lastModified());
    }

    public MovieProxy(String realPath, Movie movie) {
        this.movie = movie;
        this.realPath = realPath;
        this.fileOfMovie = new File(this.realPath + "/" + this.movie.getFilename());
    }
    
    public File getFile() {
        return this.fileOfMovie;
    }
    
    public String getFilename() {
        return fileOfMovie.getAbsolutePath();
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }

    public String getMd5() {
        if (movie.getMd5() == null) {
            try {
                movie.setMd5(Md5Generator.computeMD5(this.fileOfMovie));
            } catch (Exception ex) {
                movie.setMd5(null);
                logger.error("Exception found with file \"" + this.fileOfMovie.getAbsolutePath() + "\" : ", ex);
            }
        }

        return movie.getMd5();
    }

    public String getFastMd5() {
        if (movie.getFastMd5() == null) {
            try {
                movie.setFastMd5(Md5Generator.computeFastMD5(this.fileOfMovie));
            } catch (Exception ex) {
                movie.setFastMd5(null);
                logger.error("Exception found with file \"" + this.fileOfMovie.getAbsolutePath() + "\" : ", ex);
            }
        }

        return movie.getFastMd5();
    }

    public Movie getMovie() {
        return movie;
    }

}

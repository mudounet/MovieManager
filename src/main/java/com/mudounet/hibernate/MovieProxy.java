/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.hibernate;

import com.mudounet.utils.Md5Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author gmanciet
 */
public class MovieProxy {

    private static final Logger logger = LoggerFactory.getLogger(MovieProxy.class.getName());
    private String realFilename;
    private Movie movie;

    public MovieProxy(Movie movie) {
        this.movie = movie;
    }
    
    public String getRealFilename() {
        return realFilename;
    }

    public void setRealFilename(String realFilename) {
        this.realFilename = realFilename;
    }

    public String getMd5() {
        if (movie.getMd5() == null) {
            try {
                movie.setMd5(Md5Generator.computeMD5(realFilename));
            } catch (Exception ex) {
                movie.setMd5(null);
                logger.error("Exception found with file \"" + realFilename + "\" : ", ex);
            }
        }

        return movie.getMd5();
    }

    public String getFastMd5() {
        if (movie.getFastMd5() == null) {
            try {
                movie.setFastMd5(Md5Generator.computeFastMD5(realFilename));
            } catch (Exception ex) {
                movie.setFastMd5(null);
                logger.error("Exception found with file \"" + realFilename + "\" : ", ex);
            }
        }

        return movie.getFastMd5();
    }

    public Movie getMovie() {
        return movie;
    }
    
}

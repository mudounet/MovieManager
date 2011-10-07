/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.hibernate.movies.others;

import com.mudounet.hibernate.Image;

/**
 * @hibernate.subclass
 * discriminator-value="M"
 **/
public class Snapshot extends Image {

    private long time;
    private static final long serialVersionUID = 1L;
    

     /**
     * @hibernate.property
     */
    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}

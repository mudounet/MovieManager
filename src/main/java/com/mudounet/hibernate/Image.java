/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.hibernate;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import javax.imageio.ImageIO;

/**
 * @hibernate.class
 * discriminator-value="I"
 * @hibernate.discriminator
 * column="TYPE"
 * type="char"
 **/
public class Image implements Serializable {

    private static final long serialVersionUID = 1L;
    private String path;
    private String md5sum;
    private long id;

    public Image() {
    }

    /**
     * @hibernate.id
     * generator-class="native"
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @hibernate.property
     */
    public String getMd5sum() {
        return md5sum;
    }

    public void setMd5sum(String md5sum) {
        this.md5sum = md5sum;
    }

    /**
     * @hibernate.property
     */
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public BufferedImage load() {
        BufferedImage img = null;

        try {
            img = ImageIO.read(new File("strawberry.jpg"));
        } catch (IOException e) {
        }
        return img;
    }

    @Override
    public String toString() {
        return "Image{" + "path=" + path + '}';
    }
}

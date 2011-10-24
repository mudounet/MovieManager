/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.hibernate;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * @hibernate.class
 * discriminator-value="I"
 * @hibernate.discriminator
 * column="TYPE"
 * type="char"
 **/
public class Image implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    protected String path;
    private String md5sum;
    private long id;

    public Image() {
    }

    /**
     * @hibernate.id
     * generator-class="native"
     * @return Identifier
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @hibernate.property
     * @return MD5 string representation of image.
     */
    public String getMd5sum() {
        return md5sum;
    }

    public void setMd5sum(String md5sum) {
        this.md5sum = md5sum;
    }

    /**
     * @hibernate.property
     * @return Path of image
     */
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return File represented by this image
     */
    public File getFile() {
        File f = new File(this.getPath());
        return f;
    }
    
    /**
     * 
     * @return image loaded, ready to use.
     */
    public BufferedImage load() {
        BufferedImage img = null;

        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
        }
        return img;
    }

    @Override
    public String toString() {
        return "Image{" + "path=" + path + '}';
    }
}

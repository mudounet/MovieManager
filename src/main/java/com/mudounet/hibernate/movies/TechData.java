/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.hibernate.movies;

/**
 * @hibernate.class
 **/
public class TechData {

    private Long id;
    private long playTime;
    private int width;
    private int height;
    private String codecName;
    private long size;

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
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @hibernate.property
     */
    public long getPlayTime() {
        return playTime;
    }

    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }

    /**
     * @hibernate.property
     */
    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    /**
     * @hibernate.property
     */
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @hibernate.property
     */
    public String getCodecName() {
        return codecName;
    }

    public void setCodecName(String codecName) {
        this.codecName = codecName;
    }

    @Override
    public String toString() {
        return "TechData{" + "id=" + id + ", playTime=" + playTime + ", width=" + width + ", height=" + height + ", codecName=" + codecName + ", size=" + size + '}';
    }
}

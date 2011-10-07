/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.hibernate.movies.others;

/**
 * @hibernate.class
 **/
public class TechData {

    private Long id;
    private long playTime = -1;
    private int width = -1;
    private int height = -1;
    private String codecName = "";
    private long size = -1;

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
     * @return Height of movie
     */
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @hibernate.property
     * @return PlayTime
     */
    public long getPlayTime() {
        return playTime;
    }

    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }

    /**
     * @hibernate.property
     * @return Size of movie in bytes
     */
    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    /**
     * @hibernate.property
     * @return Width of movie
     */
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @hibernate.property
     * @return Codec name
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

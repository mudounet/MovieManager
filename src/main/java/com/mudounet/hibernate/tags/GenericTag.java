/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.hibernate.tags;

import java.util.HashSet;
import java.util.Set;

/**
 * @hibernate.class
 * */
public class GenericTag implements java.io.Serializable {

    private long id;
    private String key;
    private Set movies = new HashSet(0);

    public GenericTag(String key) {
        this.key = key;
    }

    public GenericTag() {
    }

    /**
     * @hibernate.id
     * generator-class="native"
     */
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * Key of this tag. Used to reference data
     * @hibernate.property
     * not-null="true"
     */
    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

     /**
     * @hibernate.set
     * table="movies_tags"
     * inverse="true"
     * lazy="false"
     * @hibernate.many-to-many
     * column="fk_movie"
     * class="com.mudounet.hibernate.movies.GenericMovie"
     * @hibernate.key
     * column="fk_tag"
     */
    public Set getMovies() {
        return this.movies;
    }

    public void setMovies(Set movies) {
        this.movies = movies;
    }

    /**
     * toString
     * @return String
     */
    public String toString() {
        StringBuilder buffer = new StringBuilder();

        buffer.append(getClass().getName()).append("@").append(Integer.toHexString(hashCode())).append(" [");
        buffer.append("key").append("='").append(getKey()).append("' ");
        buffer.append("]");

        return buffer.toString();
    }
}

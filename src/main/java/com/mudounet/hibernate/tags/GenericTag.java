/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.hibernate.tags;

import com.mudounet.hibernate.Movie;
import java.util.HashSet;
import java.util.Set;

/**
 * @hibernate.class
 * */
public class GenericTag implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private String key;
    private Set<Movie> movies = new HashSet<Movie>(0);

    public GenericTag(String key) {
        this.key = key;
    }

    public GenericTag() {
    }

    /**
     * @hibernate.id
     * generator-class="native"
     * @return Identifier of tag
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
     * @return key of tag
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
     * class="com.mudounet.hibernate.Movie"
     * @hibernate.key
     * column="fk_tag"
     * @return List of movies having this tag
     */
    public Set<Movie> getMovies() {
        return this.movies;
    }

    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }

    /**
     * toString
     * @return Representation of Tag
     */
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();

        buffer.append(getClass().getName()).append("@").append(Integer.toHexString(hashCode())).append(" [");
        buffer.append("key").append("='").append(getKey()).append("' ");
        buffer.append("]");

        return buffer.toString();
    }
}

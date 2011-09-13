/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mudounet.hibernate.movies;

import java.util.HashSet;
import java.util.Set;

/**
 * @hibernate.class
  * discriminator-value="G"
 * @hibernate.discriminator
 * column="TYPE"
 * type="char"
 **/
public abstract class GenericMovie {
     private Long id;
     private String title;


     /**
      * Tags describing this movie
     */
     private Set Tags = new HashSet(0);

    public GenericMovie() {
    }


    public GenericMovie(String title) {
        this.title = title;
    }

    /**
     * @hibernate.id
     * generator-class="native"
     */
    public Long getId() {
        return this.id;
    }

    protected void setId(Long id) {
        this.id = id;
    }

    /**
     * @hibernate.property
     */
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @hibernate.set
     * table="movies_tags"
     * cascade="save-update"
     * lazy="false"
     * @hibernate.many-to-many
     * column="fk_tag"
     * class="com.mudounet.hibernate.tags.GenericTag"
     * @hibernate.key
     * column="fk_movie"
     */
    public Set getTags() {
        return this.Tags;
    }

    public void setTags(Set Tags) {
        this.Tags = Tags;
    }

    /**
     * toString
     * @return String
     */
     public String toString() {
	  StringBuilder buffer = new StringBuilder();

      buffer.append(getClass().getName()).append("@").append(Integer.toHexString(hashCode())).append(" [");
      buffer.append("title").append("='").append(getTitle()).append("' ");
      buffer.append("Tags").append("='").append(getTags()).append("' ");
      buffer.append("]");

      return buffer.toString();
     }
}

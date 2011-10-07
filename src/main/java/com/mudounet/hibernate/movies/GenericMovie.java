/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.hibernate.movies;

import com.mudounet.hibernate.movies.others.TechData;
import com.mudounet.hibernate.tags.GenericTag;
import java.util.Collections;
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
    private String path;
    private Set<GenericTag> tags = new HashSet<GenericTag>(0);
    private TechData techData;

    /**
     * Tags describing this movie
     */
    public GenericMovie() {
    }

    public GenericMovie(String title) {
        this.title = title;
    }

    /**
     * @hibernate.id
     * generator-class="native"
     * @return Identifier of movie
     */
    public Long getId() {
        return this.id;
    }

    protected void setId(Long id) {
        this.id = id;
    }

    /**
     * @hibernate.property
     * @return path of movie
     */
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @hibernate.property
     * @return movie title
     */
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @hibernate.many-to-one
     * class="com.mudounet.hibernate.movies.others.TechData"
     * @return technical data
     */
    public TechData getTechData() {
        return techData;
    }

    public void setTechData(TechData techData) {
        this.techData = techData;
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
     * @return list of tags applied to movie
     */
    public Set<GenericTag> getTags() {
        return Collections.unmodifiableSet(this.tags);
    }

    public void setTags(Set<GenericTag> Tags) {
        this.tags = Tags;
    }

    /**
     * toString
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();

        buffer.append(getClass().getName()).append("@").append(Integer.toHexString(hashCode())).append(" [");
        buffer.append("title").append("='").append(getTitle()).append("' ");
        buffer.append("Tags").append("='").append(getTags()).append("' ");
        buffer.append("]");

        return buffer.toString();
    }
}

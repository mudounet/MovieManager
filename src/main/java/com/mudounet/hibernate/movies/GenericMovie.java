/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.hibernate.movies;

import com.mudounet.hibernate.movies.others.TechData;
import com.mudounet.hibernate.tags.GenericTag;
import com.mudounet.utils.Md5Generator;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @hibernate.class
 * discriminator-value="G"
 * @hibernate.discriminator
 * column="TYPE"
 * type="char"
 **/
public abstract class GenericMovie {

         protected static final Logger logger = LoggerFactory.getLogger(GenericMovie.class.getName());
    protected Long id;
    protected String title;
    protected String path;
    protected String md5;
    protected Set<GenericTag> tags = new HashSet<GenericTag>(0);
    protected TechData techData;

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
     * Get the value of md5
     * @hibernate.property
     * @return the value of md5
     */
    public String getMd5() {
        if(md5 == null) {
            try {
                md5 = Md5Generator.computeMD5(path);
            } catch (Exception ex) {
                md5 = "";
                logger.error("Exception found with file \""+path+"\" : ", ex);
            }
        }
        
        return md5;
    }

    /**
     * Set the value of md5
     *
     * @param md5 new value of md5
     */
    public void setMd5(String md5) {
        this.md5 = md5;
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

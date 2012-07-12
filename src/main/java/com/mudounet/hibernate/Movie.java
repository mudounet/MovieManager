/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.hibernate;

import com.mudounet.hibernate.movies.others.MediaInfo;
import com.mudounet.hibernate.movies.others.Snapshot;
import com.mudounet.hibernate.tags.GenericTag;
import com.mudounet.utils.Md5Generator;
import java.io.File;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @hibernate.class
 *
 */
public class Movie {

    protected static final Logger logger = LoggerFactory.getLogger(Movie.class.getName());
    private Long id;
    protected String title;
    protected String filename;
    protected String realFilename;
    private Date modificationDate;
    protected String md5;
    private Set<GenericTag> tags = new HashSet<GenericTag>(0);
    private List<Snapshot> snapshots;
    private MediaInfo mediaInfo;
    protected long size = -1;
    private String fastMd5;

    /**
     * Tags describing this movie
     */
    public Movie() {
    }

    public Movie(String title) {
        this.title = title;
    }

    /**
     * @hibernate.id generator-class="native"
     *
     * @return Identifier of movie
     */
    public Long getId() {
        return this.id;
    }

    protected void setId(Long id) {
        this.id = id;
    }

    /**
     * @hibernate.property length=32 unique=true
     *
     * @return Get the value of md5
     */
    public String getMd5() {
        if (md5 == null) {
            try {
                md5 = Md5Generator.computeMD5(realFilename);
            } catch (Exception ex) {
                md5 = "";
                logger.error("Exception found with file \"" + realFilename + "\" : ", ex);
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
     * @hibernate.property length=32 unique=true
     *
     * @return Get the value of md5
     */
    public String getFastMd5() {
        if (fastMd5 == null) {
            try {
                fastMd5 = Md5Generator.computeFastMD5(realFilename);
            } catch (Exception ex) {
                fastMd5 = "";
                logger.error("Exception found with file \"" + realFilename + "\" : ", ex);
            }
        }

        return fastMd5;
    }

    /**
     * Set the value of md5
     *
     * @param md5 new value of md5
     */
    public void setFastMd5(String md5) {
        this.fastMd5 = md5;
    }

    
    /**
     * @hibernate.property
     *
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
     *
     * @return path of movie
     */
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * @hibernate.property
     *
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
     * class="com.mudounet.hibernate.movies.others.MediaInfo"
     *
     * @return Media informations
     */
    public MediaInfo getMediaInfo() {
        return mediaInfo;
    }

    public void setMediaInfo(MediaInfo mediaInfo) {
        this.mediaInfo = mediaInfo;
    }

    /**
     * @hibernate.set table="movies_tags" cascade="save-update" lazy="false"
     * @hibernate.many-to-many column="fk_tag"
     * class="com.mudounet.hibernate.tags.GenericTag" 
     * @hibernate.key column="fk_movie"
     *
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
     *
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

    public String getRealFilename() {
        return realFilename;
    }

    public void setRealFilename(String realFilename) {
        this.realFilename = realFilename;
    }

    /**
     * @hibernate.property

     *
     * @return modification date of file
     */
    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public void setModificationDate(long lastModified) {
        Date t = new Date(lastModified);
        setModificationDate(t);
    }

    /**
     * @hibernate.set cascade="save-update" lazy="false"
     * @hibernate.one-to-many  class="com.mudounet.hibernate.movies.others.Snapshot" 
     * @hibernate.key column="fk_movie"
     * @hibernate.collection-index time
     *
     * @return list of snapshots applied to movie
     */
    public List<Snapshot> getSnapshots() {
        return snapshots;
    }

    public void setSnapshots(List<Snapshot> snapshots) {
        this.snapshots = snapshots;
    }

    
    
    /**
     * 
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if(o.getClass() == Movie.class) {
            Movie movie = (Movie)o;
            return (movie.getSize() == this.getSize() && movie.getFastMd5().equals(this.getFastMd5()) );
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + (int) (this.size ^ (this.size >>> 32));
        hash = 31 * hash + (this.fastMd5 != null ? this.fastMd5.hashCode() : 0);
        return hash;
    }
   
    
}

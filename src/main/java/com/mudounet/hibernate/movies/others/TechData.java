/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.hibernate.movies.others;

/**
 * @hibernate.class
 *
 */
public class TechData implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private long playTime = 0; //
    private String videoCodec = ""; //
    private String videoFormat = ""; //
    private float videoBitrate = 0; //
    private float videoFramerate = 0; //
    private int videoWidth = 0; //
    private int videoHeight = 0; //
    private String audioCodec = ""; //
    private float audioBitrate = 0; //
    private float audioSamplingRate = 0;

    /**
     * @hibernate.id generator-class="native"
     *
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
     * Get the value of videoFramerate
     *
     * @return the value of videoFramerate
     */
    public float getVideoFramerate() {
        return videoFramerate;
    }

    /**
     * Set the value of videoFramerate
     *
     * @param videoFramerate new value of videoFramerate
     */
    public void setVideoFramerate(float videoFramerate) {
        this.videoFramerate = videoFramerate;
    }

    /**
     * @hibernate.property
     * Get the value of videoFormat
     *
     * @return the value of videoFormat
     */
    public String getVideoFormat() {
        return videoFormat;
    }

    /**
     * Set the value of videoFormat
     *
     * @param videoFormat new value of videoFormat
     */
    public void setVideoFormat(String videoFormat) {
        this.videoFormat = videoFormat;
    }

    /**
     * @hibernate.property
     * Get the value of audioSamplingRate
     *
     * @return the value of audioSamplingRate
     */
    public float getAudioSamplingRate() {
        return audioSamplingRate;
    }

    /**
     * Set the value of audioSamplingRate
     *
     * @param audioSamplingRate new value of audioSamplingRate
     */
    public void setAudioSamplingRate(float audioSamplingRate) {
        this.audioSamplingRate = audioSamplingRate;
    }

    /**
     * @hibernate.property
     * Get the value of audioBitrate
     *
     * @return the value of audioBitrate
     */
    public float getAudioBitrate() {
        return audioBitrate;
    }

    /**
     * Set the value of audioBitrate
     *
     * @param audioBitrate new value of audioBitrate
     */
    public void setAudioBitrate(float audioBitrate) {
        this.audioBitrate = audioBitrate;
    }

    /**
     * @hibernate.property
     * Get the value of audioCodec
     *
     * @return the value of audioCodec
     */
    public String getAudioCodec() {
        return audioCodec;
    }

    /**
     * Set the value of audioCodec
     *
     * @param audioCodec new value of audioCodec
     */
    public void setAudioCodec(String audioCodec) {
        this.audioCodec = audioCodec;
    }

    /**
     * @hibernate.property
     * Get the value of videoBitrate
     *
     * @return the value of videoBitrate
     */
    public float getVideoBitrate() {
        return videoBitrate;
    }


    /**
     * @hibernate.property
     *
     * @return Height of movie
     */
    public int getVideoHeight() {
        return videoHeight;
    }

    public void setVideoHeight(int videoHeight) {
        this.videoHeight = videoHeight;
    }

    /**
     * @hibernate.property
     *
     * @return Playing time in seconds
     */
    public long getPlayTime() {
        return playTime;
    }

    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }

    /**
     * @hibernate.property
     *
     * @return Width of movie
     */
    public int getVideoWidth() {
        return videoWidth;
    }

    public void setVideoWidth(int videoWidth) {
        this.videoWidth = videoWidth;
    }

    /**
     * @hibernate.property
     *
     * @return Codec name
     */
    public String getVideoCodec() {
        return videoCodec;
    }

    public void setVideoCodec(String codecName) {
        this.videoCodec = codecName;
    }


}

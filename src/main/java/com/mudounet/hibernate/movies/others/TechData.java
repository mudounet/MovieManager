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
    private float videoBitrate = 0; //
    private float videoFramerate = 0; //
    private int videoWidth = 0; //
    private int videoHeight = 0; //
    private String audioCodec = ""; //
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
    
    public void setAudioCodec(int fourCcCodecId) {
        
        char[] codecName = {32, 32, 32, 32};
        codecName[0] = (char)(fourCcCodecId & 0xFF);
        codecName[1] = (char)((fourCcCodecId >> 8) & 0xFF);
        codecName[2] = (char)((fourCcCodecId >> 16) & 0xFF);
        codecName[3] = (char)((fourCcCodecId >> 24) & 0xFF);   
        
        this.audioCodec = String.valueOf(codecName);
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

    public void setVideoCodec(int fourCcCodecId) {
        
        char[] codecName = {32, 32, 32, 32};
        codecName[0] = (char)(fourCcCodecId & 0xFF);
        codecName[1] = (char)((fourCcCodecId >> 8) & 0xFF);
        codecName[2] = (char)((fourCcCodecId >> 16) & 0xFF);
        codecName[3] = (char)((fourCcCodecId >> 24) & 0xFF);   
        
        this.videoCodec = String.valueOf(codecName);
    }
    
    

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.video;

/**
 *
 * @author gmanciet
 */
public class VideoPlayerException extends Exception {
    private static final long serialVersionUID = 1L;

    public VideoPlayerException(String string) {
        super(string);
    }
    
    public VideoPlayerException(Exception exception) {
        super(exception);
    }
    
}
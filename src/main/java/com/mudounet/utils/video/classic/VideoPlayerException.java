/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.video;

/**
 *
 * @author gmanciet
 */
public class RemotePlayerException extends Exception {
    private static final long serialVersionUID = 1L;

    RemotePlayerException(String string) {
        super(string);
    }
    
    RemotePlayerException(Exception exception) {
        super(exception);
    }
    
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.video.remotecommands;

/**
 *
 * @author gmanciet
 */
public class CloseCommand extends BooleanCommand {
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "CloseCommand{" + this.getValueStr() +'}';
    }
    
}

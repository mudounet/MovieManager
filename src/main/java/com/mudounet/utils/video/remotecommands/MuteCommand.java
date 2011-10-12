/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.video.remotecommands;

/**
 *
 * @author gmanciet
 */
public class MuteCommand extends BooleanCommand {
    private static final long serialVersionUID = 1L;

    public MuteCommand(boolean mute) {
        super(mute);
    }

    public MuteCommand() {
        super();
    }

    @Override
    public String toString() {
        return "MuteCommand{" + this.getValueStr() + '}';
    }
    
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.video.remotecommands;

/**
 *
 * @author gmanciet
 */
public class TimeCommand extends LongCommand {
    private static final long serialVersionUID = 1L;

    public TimeCommand(long value) {
        super(value);
    }

    public TimeCommand() {
    }

    @Override
    public String toString() {
        return "TimeCommand{value=" + value + '}';
    }
    
}

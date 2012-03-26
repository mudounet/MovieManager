/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.video.remotecommands;

/**
 *
 * @author gmanciet
 */
public class LongCommand extends Command {
    private static final long serialVersionUID = 1L;
 
    protected long value = -1;

    public LongCommand(long value) {
        this.value = value;
    }

    public LongCommand() {
    }
    
    
    /**
     * Get the value of value
     *
     * @return the value of value
     */
    public long getValue() {
        return value;
    }

    /**
     * Set the value of value
     *
     * @param value new value of value
     */
    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "LongCommand{" + "value=" + value + '}';
    }

}
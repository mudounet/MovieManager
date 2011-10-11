/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.video.remotecommands;

/**
 *
 * @author gmanciet
 */
public abstract class BooleanCommand extends Command {

    private boolean result;

    /**
     * Get the value of result
     *
     * @return the value of result
     */
    public boolean isResult() {
        return result;
    }

    /**
     * Set the value of result
     *
     * @param result new value of result
     */
    public void setResult(boolean result) {
        this.result = result;
    }

}

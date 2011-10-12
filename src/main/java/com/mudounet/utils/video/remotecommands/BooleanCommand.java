/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.video.remotecommands;

/**
 *
 * @author gmanciet
 */
public class BooleanCommand extends Command {
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "BooleanCommand{" + "result=" + this.getValueStr() + '}';
    }

    private byte result = 127;

    public BooleanCommand() {
    }

    public BooleanCommand(boolean result) {
        setValue(result);
    }
   
     /**
     * Indicate if value has been set previously
     *
     * @return the value of result
     */
    public boolean isSet() {
        if(this.result == 127) {
            return false;
        }
        else {
            return true;
        }
    }
  
    
    
    /**
     * Get the value of result
     *
     * @return the value of result
     */
    public boolean getValue() {
        if(this.result == 1) {
            return true;
        }
        else {
            return false;
        }
    }

    protected String getValueStr() {
        if(result == 127) {
            return "unset";
        }
        return ""+this.getValue();
    }
    
    /**
     * Set the value of result
     *
     * @param result new value of result
     */
    public final void setValue(boolean result) {
        if(result == true) {
            this.result = 1;
        } 
        else {
            this.result = 0;
        }
        
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.video.remotecommands;

/**
 *
 * @author gmanciet
 */
public class StateCommand extends LongCommand {
    private static final long serialVersionUID = 1L;
    
    public static final long PLAYABLE = 1;
    public static final long PLAYED = 2;
    public static final long PAUSED = 4;
    public static final long STOPPED = 8;
    public static final long SEEKABLE = 16;

    public StateCommand(long requestedInformation) {
        super(requestedInformation);
    }

    @Override
    public String toString() {
        return "StateCommand{state=" + stateStr() +'}';
    }

    public String stateStr() {
        String str = "";
        if((value & PLAYABLE) != 0) {
            str += "PLAYABLE,";
        }
        if((value & PLAYED) != 0) {
            str += "PLAYED,";
        }
        if((value & PAUSED) != 0) {
            str += "PAUSED,";
        }
        if((value & STOPPED) != 0) {
            str += "STOPPED,";
        }
        if((value & SEEKABLE) != 0) {
            str += "SEEKABLE,";
        }
        if(value == 0) {
            str = "UNDEF";
        }
        return str;
    }

}

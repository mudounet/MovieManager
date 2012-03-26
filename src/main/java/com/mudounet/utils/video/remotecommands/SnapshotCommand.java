/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.video.remotecommands;

public class SnapshotCommand extends com.mudounet.hibernate.movies.others.Snapshot {
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "SnapshotCommand{" + "time=" + time +",path=" + path + '}';
    }
}

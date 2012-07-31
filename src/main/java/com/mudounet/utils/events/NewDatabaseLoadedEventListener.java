/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.events;

import java.util.EventListener;

/**
 *
 * @author gmanciet
 */
public interface NewDatabaseLoadedEventListener extends EventListener {
    public void newDatabaseLoaded(NewDatabaseLoadedEvent evt);
}

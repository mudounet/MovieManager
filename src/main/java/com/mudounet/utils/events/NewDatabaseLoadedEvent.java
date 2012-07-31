/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.events;

import java.util.EventObject;

/**
 *
 * @author gmanciet
 */
public class NewDatabaseLoadedEvent extends EventObject {
    private static final long serialVersionUID = 1L;
	public NewDatabaseLoadedEvent(Object source) {
		super(source);
	}
} 

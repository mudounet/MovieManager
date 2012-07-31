/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.commands;

/**
 * @(#)CommandDialogDispose.java 1.0 10.04.05 (dd.mm.yy)
 *
 * Copyright (2003) Mediterranean
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2, or any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Boston, MA 02111.
 * 
 * Contact: mediterranean@users.sourceforge.net
 **/

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class CommandDialogDispose implements ActionListener {

	Logger log = LoggerFactory.getLogger(getClass());

	private JDialog _dialog;

	/**
	 * Constructor. Initializes the _dialog var.
         *
         * @param dialog 
         */
	public CommandDialogDispose(JDialog dialog) {
		_dialog = dialog;
	}

	/**
	 * Invoked when an action occurs.
         *
         * @param event 
         */
	public void actionPerformed(ActionEvent event) {
		log.debug("ActionPerformed: "+ event.getActionCommand());
		_dialog.dispose();
	}
}
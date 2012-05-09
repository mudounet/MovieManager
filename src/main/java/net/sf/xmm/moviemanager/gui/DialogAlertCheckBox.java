/**
 * @(#)DialogAlert.java
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

package net.sf.xmm.moviemanager.gui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;

import javax.swing.JCheckBox;

import org.slf4j.LoggerFactory;

public class DialogAlertCheckBox extends DialogAlert {

	protected org.slf4j.Logger log = LoggerFactory.getLogger(getClass());
	
	public DialogAlertCheckBox(Dialog parent, String title, String alertMsg, String checkBoxText) {
		super(parent, title, alertMsg, true);
		addButton(checkBoxText);
	}

	public DialogAlertCheckBox(Frame parent, String title, String alertMsg, String checkBoxText) {
		super(parent, title, alertMsg, true);
		addButton(checkBoxText);
	}

	JCheckBox checkBox;
	
	void addButton(String checkBoxText) {
		checkBox = new JCheckBox(checkBoxText);
		panelButtons.add(checkBox, BorderLayout.WEST);
	}
	
	public boolean isButtonChecked() {
		return checkBox.isSelected();
	}
}

    

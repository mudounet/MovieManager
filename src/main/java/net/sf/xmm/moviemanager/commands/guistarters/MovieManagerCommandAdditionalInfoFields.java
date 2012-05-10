/**
 * @(#)MovieManagerCommandAdditionalInfoFields.java 1.0 26.09.06 (dd.mm.yy)
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

package net.sf.xmm.moviemanager.commands.guistarters;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.sf.xmm.moviemanager.MovieManager;
import net.sf.xmm.moviemanager.gui.DialogAdditionalInfoFields;
import net.sf.xmm.moviemanager.util.GUIUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MovieManagerCommandAdditionalInfoFields implements ActionListener {

	static Logger log =  LoggerFactory.getLogger(MovieManager.class);

	/**
	 * Executes the command.
	 **/
	protected static void execute() {
		DialogAdditionalInfoFields dialogAdditionalInfoFields = new DialogAdditionalInfoFields();
		GUIUtil.show(dialogAdditionalInfoFields, true);
	}

	/**
	 * Invoked when an action occurs.
	 **/
	public void actionPerformed(ActionEvent event) {
		log.debug("ActionPerformed: "+ event.getActionCommand());
		execute();
	}
}

/**
 * @(#)AppletStarter.java 1.0 21.04.06 (dd.mm.yy)
 *
 * Copyright (2008) Bro	
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
 * Contact: bro3@users.sourceforge.net
 **/

package com.mudounet;

import javax.swing.JApplet;
import javax.swing.JFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppletStarter extends JApplet {
    
    JApplet applet;
    public JFrame moviemanager;
    
    protected static final Logger logger = LoggerFactory.getLogger(AppletStarter.class.getName());
    
    /** Initializes Applet by adding MovieManager's content pane. */
    public void init() {
        setSize(400,300);
        
  
        new MovieManager(this);
    }
}

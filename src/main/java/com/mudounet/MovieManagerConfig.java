/**
 * @(#)MovieManagerConfig.java
 *
 * Copyright (2003) Bro3
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

import com.mudounet.utils.NewDatabaseLoadedEventListener;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author gmanciet
 */
public class MovieManagerConfig implements NewDatabaseLoadedEventListener {

	protected static final org.slf4j.Logger log = LoggerFactory.getLogger(MovieManagerConfig.class.getName());

    void loadConfig() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public class sysSettings {

        static String getVersion() {
            throw new UnsupportedOperationException("Not yet implemented");
        }

        public sysSettings() {
        }
    }

}





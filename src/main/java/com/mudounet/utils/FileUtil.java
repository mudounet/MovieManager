/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils;

import com.mudounet.gui.DialogMovieManager;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.net.URL;
import javax.swing.JApplet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author isabelle
 */
public class FileUtil {
    protected static Logger log = LoggerFactory.getLogger(FileUtil.class.getName());

    public static Image getImage(String imageName) { 
    	return getImage(imageName, DialogMovieManager.getApplet());
    }
    
    public static Image getImage(String imageName, JApplet applet) {
    	Image image = null;
    	
    	try {

    		try {
    			URL url = FileUtil.class.getResource(imageName);
    			
    			if (url != null)
    				image = Toolkit.getDefaultToolkit().getImage(url);
    		}
    		catch (Exception e) {
    			log.error("Exception:" + e.getMessage()); //$NON-NLS-1$
    		}
    		
    		if (image == null) {

    			if (applet != null) {
    				URL url = getImageURL(imageName);
    				image = applet.getImage(url);
    			}
    			else {
    				String path = "";

    				if (!new File(imageName).exists()){
    					path = System.getProperty("user.dir");
    				}

    				if (new File(path + imageName).exists()) {
    					image = Toolkit.getDefaultToolkit().getImage(path + imageName);
    				}
    			}
    		}
    	} catch (Exception e) {
    		log.error("Exception:" + e.getMessage(), e); //$NON-NLS-1$
    	}

    	return image;
    }

   public static URL getImageURL(String imageName) {
	   return FileUtil.class.getResource(imageName);
   }
    
}

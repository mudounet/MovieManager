/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author isabelle
 */
public class SysUtil {
    private static Logger log = LoggerFactory.getLogger(SysUtil.class.getName());

    public static boolean isMac() {
        String os = System.getProperty("os.name"); //$NON-NLS-1$
        return os != null && os.toLowerCase().startsWith("mac"); //$NON-NLS-1$
    }

    public static boolean isOSX() {
        String os = System.getProperty("os.name"); //$NON-NLS-1$
        return os != null && os.toLowerCase().startsWith("Mac OS X"); //$NON-NLS-1$
    }

    public static boolean isLinux() {
        String os = System.getProperty("os.name"); //$NON-NLS-1$
        return os != null && os.toLowerCase().startsWith("linux"); //$NON-NLS-1$
    }

    public static boolean isSolaris() {
        String os = System.getProperty("os.name"); //$NON-NLS-1$
        return os != null && (os.toLowerCase().startsWith("sunos") || os.toLowerCase().startsWith("solaris")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public static boolean isWindows() {
        String os = System.getProperty("os.name"); //$NON-NLS-1$
        return os != null && os.toLowerCase().startsWith("windows"); //$NON-NLS-1$
    }

    public static boolean isWindows98() {
        String os = System.getProperty("os.name"); //$NON-NLS-1$
        return os != null && os.toLowerCase().startsWith("Windows 98"); //$NON-NLS-1$
    }

    public static boolean isWindowsXP() {
        String os = System.getProperty("os.name"); //$NON-NLS-1$
        String osVersion = System.getProperty("os.version"); //$NON-NLS-1$

        return os != null && osVersion != null
                && os.toLowerCase().indexOf("windows") != -1
                && osVersion.equals("5.1"); //$NON-NLS-1$
    }

    /**
     * Bug in Java (not yet fixed in 1.6.0_13) causing
     * System.getProperty("os.name") to return "Windows XP" on Windows Vista.
     * System.getProperty("os.version") returns "6.0" on Windows Vista and "5.1"
     * on XP
     *
     * @return
     */
    public static boolean isWindowsVista() {
        String os = System.getProperty("os.name"); //$NON-NLS-1$
        String osVersion = System.getProperty("os.version"); //$NON-NLS-1$

        return os != null && osVersion != null
                && os.toLowerCase().indexOf("windows") != -1
                && osVersion.equals("6.0"); //$NON-NLS-1$
    }

    /**
     * Bug in Java (not yet fixed in 1.6.0_13) causing
     * System.getProperty("os.name") to return "Windows Vista" on Windows 7.
     * System.getProperty("os.version") returns "6.1" on Windows 7 and "6.0" on
     * Vista
     *
     * @return
     */
    public static boolean isWindows7() {
        String os = System.getProperty("os.name"); //$NON-NLS-1$
        String osVersion = System.getProperty("os.version"); //$NON-NLS-1$

        return os != null && osVersion != null
                && os.toLowerCase().indexOf("windows") != -1
                && osVersion.equals("6.1"); //$NON-NLS-1$
    }

    public static boolean isAMD64() {
        String arch = System.getProperty("os.arch"); //$NON-NLS-1$
        return arch != null && arch.equals("amd64");
    }

    public static boolean isRestrictedSandbox() {
    	
    	SecurityManager securityManager = System.getSecurityManager();
    	
    	if (securityManager == null) {
    		return false;
    	}
    	
    	try {
    		securityManager.checkPropertiesAccess();
    	} catch (Exception e) {
    		log.debug("Exception:" + e.getMessage());
    		return true;
    	} 
    	
    	return false;
    }
}

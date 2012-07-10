package com.mudounet.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

public class Md5Generator {

    /**
     * 
     * @param filename 
     * @return Array of byte computed
     * @throws Exception
     */
    public static byte[] createChecksum(String filename) throws Exception {
        
        return createGenericChecksum(filename, 0);
    }
    
    private static byte[] createGenericChecksum(String filename, int numberOfpackets) throws Exception {
        InputStream fis = new FileInputStream(filename);

        if(numberOfpackets <= 0) {
            numberOfpackets = -1;
        }
        
        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("MD5");
        int numRead;
        do {
            numRead = fis.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
            
            if(numberOfpackets > 0) {
                numberOfpackets--;
            }
        } while (numRead != -1 && numberOfpackets != 0);
        fis.close();
        return complete.digest();
    }

    // see this How-to for a faster way to convert
    // a byte array to a HEX string
    public static String computeMD5(String filename) throws Exception {
        byte[] b = createGenericChecksum(filename, 0);
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }
    
    // see this How-to for a faster way to convert
    // a byte array to a HEX string
    public static String computeFastMD5(String filename) throws Exception {
        byte[] b = createGenericChecksum(filename, 100 * 1024); // 100 Mo
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }


}
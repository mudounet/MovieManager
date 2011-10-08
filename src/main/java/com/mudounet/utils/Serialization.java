/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.apache.log4j.Logger;

/**
 *
 * @author gmanciet
 */
public class Serialization {

    protected static Logger logger = Logger.getLogger(Serialization.class.getName());

    static public boolean writeObject(Object obj, String filename) {
        try {
            // création d'une personne


            // ouverture d'un flux de sortie vers le fichier "personne.serial"
            FileOutputStream fos = new FileOutputStream(filename);

            // création d'un "flux objet" avec le flux fichier
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            try {
                // sérialisation : écriture de l'objet dans le flux de sortie
                oos.writeObject(obj);
                // on vide le tampon
                oos.flush();
                logger.debug(obj + " has been serialized");
                return true;

            } finally {
                //fermeture des flux
                try {
                    oos.close();
                } finally {
                    fos.close();
                }
            }
        } catch (IOException ioe) {
            logger.error(obj + " has not been serialized : " + ioe);
        }

        return false;
    }

    static public Object readObject(String filename) {
        Object obj = null;
        try {
            // ouverture d'un flux d'entrée depuis le fichier "personne.serial"
            FileInputStream fis = new FileInputStream(filename);
            // création d'un "flux objet" avec le flux fichier
            ObjectInputStream ois = new ObjectInputStream(fis);
            try {
                // désérialisation : lecture de l'objet depuis le flux d'entrée
                obj = ois.readObject();
            } finally {
                // on ferme les flux
                try {
                    ois.close();
                } finally {
                    fis.close();
                }
            }
        } catch (IOException ioe) {
            logger.error(obj + " has not been serialized : " + ioe);
        } catch (ClassNotFoundException cnfe) {
            logger.error(obj + " has not been serialized : " + cnfe);
        }
        if (obj != null) {
            logger.debug(obj + " has been unserialized");
        }

        return obj;
    }
}

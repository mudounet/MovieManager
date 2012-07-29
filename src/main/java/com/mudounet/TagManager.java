package com.mudounet;

import java.io.Console;
import javax.swing.JOptionPane;

/**
 *
 * @author isabelle
 */
public class TagManager {

    public static void main(String[] args) {
        String tagRef;
        do {
            Object[] possibilities = null;
            tagRef = (String) JOptionPane.showInputDialog(
                    null,
                    "Enter tag to add :\n",
                    "Customized Dialog",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    possibilities,
                    "");

        } while (((tagRef != null) && (tagRef.length() > 0)));
    }
}
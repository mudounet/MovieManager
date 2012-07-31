/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.ui.swing.ext;

import java.awt.Insets;
import java.awt.Point;
import java.awt.dnd.Autoscroll;
import javax.swing.JTree;

/**
 *
 * @author gmanciet
 */
public class ExtendedJTree extends JTree implements Autoscroll {

    JTree getTree() {
        return this;
    }

    public Insets getAutoscrollInsets() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void autoscroll(Point cursorLocn) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

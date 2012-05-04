/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.ui;

/**
 *
 * @author gmanciet
 */
public class DialogMovieManagerTest {
    private static final long serialVersionUID = 1L;
    
   
    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new  DialogMovieManager().setVisible(true);
            }
        });
    }
}

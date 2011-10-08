/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.ui.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.TransferHandler;
import org.apache.log4j.Logger;

/**
 *
 * @author gmanciet
 */
class FileDropHandler extends TransferHandler {

    protected static Logger logger = Logger.getLogger(FileDropHandler.class.getName());
    private static final long serialVersionUID = 1L;

    @Override
    public boolean canImport(TransferSupport support) {
        /* for the demo, we'll only support drops (not clipboard paste) */
        if (!support.isDrop()) {
            return false;
        }

        /* return false if the drop doesn't contain a list of files */
        if (!support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            return false;
        }

        /* check to see if the source actions (a bitwise-OR of supported
         * actions) contains the COPY action
         */
        boolean copySupported = (COPY & support.getSourceDropActions()) == COPY;

        /* if COPY is supported, choose COPY and accept the transfer */
        if (copySupported) {
            support.setDropAction(COPY);
            return true;
        }

        /* COPY isn't supported, so reject the transfer.
         *
         * Note: If you want to accept the transfer with the default
         *       action anyway, you could instead return true.
         */
        return false;
    }

    @Override
    public boolean importData(TransferSupport support) {
        if (!canImport(support)) {
            return false;
        }

        /* fetch the Transferable */
        Transferable t = support.getTransferable();

        try {
            /* fetch the data from the Transferable */
            Object data = t.getTransferData(DataFlavor.javaFileListFlavor);

            /* data of type javaFileListFlavor is a list of files */
            List<File> fileList = (java.util.List) data;

            /* loop through the files in the file list */
            for (File file : fileList) {
                logger.debug(file.toString());
                logger.warn("Ceci est un test");
                /* This is where you place your code for opening the
                 * document represented by the "file" variable.
                 * For example:
                 * - create a new internal frame with a text area to
                 *   represent the document
                 * - use a BufferedReader to read lines of the document
                 *   and append to the text area
                 * - add the internal frame to the desktop pane,
                 *   set its bounds and make it visible
                 */
            }
        } catch (UnsupportedFlavorException e) {
            return false;
        } catch (IOException e) {
            return false;
        }

        return true;
    }
}
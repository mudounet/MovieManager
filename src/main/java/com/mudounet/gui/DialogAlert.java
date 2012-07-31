/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.gui;

import com.mudounet.commands.CommandDialogDispose;
import com.mudounet.utils.FileUtil;
import com.mudounet.swing.util.GUIUtil;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author isabelle
 */
public class DialogAlert extends JDialog {

    private static final long serialVersionUID = 1L;
    protected static Logger log = LoggerFactory.getLogger(DialogAlert.class.getName());
    JPanel panelButtons;

    public DialogAlert(Dialog parent, String title, String alertMsg, boolean html) {
        super(parent, true);

        if (html) {
            createHTMLDialog(parent, title, alertMsg);
        } else {
            createOneMessageAlert(title, alertMsg);
        }
    }

    public DialogAlert(Frame parent, String title, String alertMsg, boolean html) {
        super(parent, true);

        if (html) {
            createHTMLDialog(parent, title, alertMsg);
        } else {
            createOneMessageAlert(title, alertMsg);
        }
    }

    private void createHTMLDialog(Window parent, String title, String alertMsg) {

        try {

            setTitle(title);

            /*
             * All stuff together...
             */
            JPanel panelAlert = new JPanel(new BorderLayout());
            panelAlert.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 10));

            JLabel labelIcon = new JLabel();
            labelIcon.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 8));
            labelIcon.setIcon(new ImageIcon(FileUtil.getImage("/images/alert.png").getScaledInstance(50, 50, Image.SCALE_SMOOTH))); //$NON-NLS-1$

            JTextPane area = new JTextPane();
            area.setOpaque(false);
            area.setBorder(null);
            area.setEditable(false);
            area.setContentType("text/html"); //$NON-NLS-1$
            area.setText(alertMsg);

            area.setFocusable(true);

            JScrollPane scrollPane = new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            panelAlert.add(labelIcon, BorderLayout.WEST);
            panelAlert.add(scrollPane, BorderLayout.EAST);

            if (scrollPane.getPreferredSize().getHeight() > 200) {
                scrollPane.setPreferredSize(new Dimension((int) scrollPane.getPreferredSize().getWidth(), 200));
            }

            makeRest(parent, panelAlert);

        } catch (Exception e) {
            log.error("Exception:" + e.getMessage()); //$NON-NLS-1$
        }
    }

    private void createOneMessageAlert(String title, String alertMsg) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void makeRest(Window parent, JComponent panelAlert) {
        /*
         * Dialog properties...
         */

        setModal(true);
        setResizable(false);

        GUIUtil.enableDisposeOnEscapeKey(this);

        /*
         * Buttons panel...
         */
        panelButtons = new JPanel();
        panelButtons.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        //panelButtons.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelButtons.setLayout(new BorderLayout());

        JButton buttonOk = new JButton("OK");
        buttonOk.setActionCommand("Alert - OK"); //$NON-NLS-1$
        buttonOk.addActionListener(new CommandDialogDispose(this));
        buttonOk.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    dispose();
                }
            }
        });


        panelButtons.add(buttonOk, BorderLayout.EAST);

        /*
         * Adds all and buttonsPanel...
         */
        getContentPane().add(panelAlert, BorderLayout.NORTH);
        getContentPane().add(panelButtons, BorderLayout.SOUTH);

        /*
         * Packs and sets location...
         */
        pack();

        if (parent != null) {

            if ((parent.getLocation().getX() != 0) && (parent.getLocation().getY() != 0)) {
                setLocation((int) parent.getLocation().getX() + (parent.getWidth() - getWidth()) / 2,
                        (int) parent.getLocation().getY() + (parent.getHeight() - getHeight()) / 2);
            } else {
                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                setLocation((int) (dim.getWidth() - getWidth()) / 2, (int) (dim.getHeight() - getHeight()) / 2);
            }
        }
        buttonOk.requestFocusInWindow();

    }
}

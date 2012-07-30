/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.swing.util;

import com.mudounet.swing.util.KeyboardShortcutManager;
import com.mudounet.gui.DialogAlert;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import javax.swing.*;
import org.slf4j.LoggerFactory;

/**
 *
 * @author isabelle
 */
public class GUIUtil {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(GUIUtil.class.getName());

    public static void show(final java.awt.Container container, final boolean visible) {
        show(container, visible, null);
    }

    public static void show(final Container container, final boolean visible, Window appearOnLeftSide) {

        if (visible) {
            adjustLocation(container, appearOnLeftSide);
        }

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                container.setVisible(visible);
            }
        });
    }

    public static void showAndWait(final Container container, final boolean visible) {
        showAndWait(container, visible, null);
    }

    public static void showAndWait(final Container container, final boolean visible, Window appearOnLeftSide) {

        try {

            if (visible) {
                adjustLocation(container, appearOnLeftSide);
            }

            if (SwingUtilities.isEventDispatchThread()) {
                container.setVisible(visible);
            } else {
                SwingUtilities.invokeAndWait(new Runnable() {

                    public void run() {
                        container.setVisible(visible);
                    }
                });
            }
        } catch (InterruptedException i) {
            log.error("InterruptedException:" + i.getMessage(), i);
        } catch (java.lang.reflect.InvocationTargetException i) {
            log.error("InvocationTargetException:" + i.getMessage(), i);
        } catch (Exception i) {
            log.error("Exception:" + i.getMessage(), i);
        }
    }

    private static void adjustLocation(Container container, Window appearOnLeftSide) {

        Point p = container.getLocation();

        Dimension size = container.getSize();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int widthLocation = (int) (p.getX() + size.getWidth());
        int heightLocation = (int) (p.getY() + size.getHeight());

        if (appearOnLeftSide != null) {

            Point appearLeft = appearOnLeftSide.getLocation();

            int wSize = (int) size.getWidth();
            p.setLocation(appearLeft.getX() - wSize, p.getY());
        }

        if (widthLocation > screenSize.getWidth()) {
            int diff = (int) (widthLocation - screenSize.getWidth());
            p.setLocation((p.getX() - diff), p.getY());
        }

        if (heightLocation > screenSize.getHeight()) {
            int diff = (int) (heightLocation - screenSize.getHeight());
            p.setLocation(p.getX(), (p.getY() - diff));
        }

        if (p.getX() < 0) {
            p.setLocation(0.0, p.getY());
        }

        if (p.getY() < 10) {
            p.setLocation(p.getX(), 15.0);
        }

        container.setLocation(p);
    }

    public static void invokeLater(Runnable runnable) {

        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(runnable);
        } else {
            runnable.run();
        }
    }

    public static void invokeAndWait(Runnable r) throws InterruptedException, InvocationTargetException {

        if (SwingUtilities.isEventDispatchThread()) {
            r.run();
        } else {
            SwingUtilities.invokeAndWait(r);
        }

    }

    public static void enableDisposeOnEscapeKey(final JDialog dialog) {
        enableDisposeOnEscapeKey(dialog, null);
    }

    public static void enableDisposeOnEscapeKey(final JDialog dialog, final Action escapeAction) {
        Action disposeAction = enableDisposeOnAction(dialog, escapeAction);
        enableActionOnEscapeKey(dialog.getRootPane(), disposeAction);
    }

    public static void enableActionOnEscapeKey(JRootPane rootPane, final Action escapeAction) {
        KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        KeyboardShortcutManager.registerKeyboardShortcut(key, escapeAction, rootPane);
    }

    public static Action enableDisposeOnAction(final JDialog dialog, final Action escapeAction) {
        return enableDisposeOnAction(null, dialog, escapeAction);
    }

    public static Action enableDisposeOnAction(KeyboardShortcutManager shortcutManager, final Action escapeAction) {
        return enableDisposeOnAction(shortcutManager, null, escapeAction);
    }

    public static Action enableDisposeOnAction(final KeyboardShortcutManager shortcutManager, final JDialog dialog, final Action escapeAction) {

        Action disposeAction = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {

                if (escapeAction != null) {
                    escapeAction.actionPerformed(e);
                }

                // Hide shortcut panel if it's visible
                if (shortcutManager != null) {
                    if (shortcutManager.isShortCutPanelVisible()) {
                        shortcutManager.hideShortCutPanel();
                    } else {
                        shortcutManager.getJDialog().dispose();
                    }
                } else {
                    dialog.dispose();
                }
            }
        };
        return disposeAction;
    }
}

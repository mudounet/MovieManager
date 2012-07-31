/* 
 * This file is part of Quelea, free projection software for churches.
 * Copyright (C) 2011 Michael Berry
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mudounet.utils;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map.Entry;
import java.util.*;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * General utility class containing a bunch of static methods.
 *
 * @author Michael
 */
public final class Utils {

    protected static final Logger logger = LoggerFactory.getLogger(Utils.class.getName());

    /**
     * Don't instantiate me. I bite.
     */
    private Utils() {
        throw new AssertionError();
    }

    /**
     * Sleep ignoring the exception.
     *
     * @param millis milliseconds to sleep.
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            //Nothing
        }
    }

    public static String readableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static String readableDuration(long millis) {
        return String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(millis),  TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    /**
     * Get an image icon from the location of a specified file.
     *
     * @param location the location of the image to use.
     * @return the icon formed from the image, or null if an IOException
     * occured.
     */
    public static ImageIcon getImageIcon(String location) {
        return getImageIcon(location, -1, -1);
    }

    /**
     * Get an image icon from the location of a specified file.
     *
     * @param location the location of the image to use.
     * @param width the width of the given image icon.
     * @param height the height of the given image icon.
     * @return the icon formed from the image, or null if an IOException
     * occured.
     */
    public static ImageIcon getImageIcon(String location, int width, int height) {
        Image image = getImage(location, width, height);
        if (image == null) {
            return null;
        }
        return new ImageIcon(image);
    }

    /**
     * Get an image from the location of a specified file.
     *
     * @param location the location of the image to use.
     * @return the icon formed from the image, or null if an IOException
     * occured.
     */
    public static BufferedImage getImage(String location) {
        return getImage(location, -1, -1);
    }

    /**
     * Get an image from the location of a specified file.
     *
     * @param location the location of the image to use.
     * @param width the width of the returned image.
     * @param height the height of the returned image.
     * @return the icon formed from the image, or null if an IOException
     * occured.
     */
    public static BufferedImage getImage(String location, int width, int height) {
        try {
            File f = getFileFromClasspath(location);
            BufferedImage image = ImageIO.read(f);
            if (width > 0 && height > 0) {
                return resizeImage(image, width, height);
            } else {
                return image;
            }
        } catch (IOException ex) {
            logger.error("Couldn't get image: " + location, ex);
            return null;
        }
    }

    public static File getFileFromClasspath(String s) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL url = cl.getResource(s);
        File f;
        try {
            f = new File(url.toURI());
        } catch (URISyntaxException ex) {
            f = new File(url.getPath());
        }

        return f;
    }

    /**
     * Resize a given image to the given width and height.
     *
     * @param image the image to resize.
     * @param width the width of the new image.
     * @param height the height of the new image.
     * @return the resized image.
     */
    public static BufferedImage resizeImage(BufferedImage image, int width, int height) {
        if (width > 0 && height > 0 && (image.getWidth() != width || image.getHeight() != height)) {
            BufferedImage bdest = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = bdest.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.drawImage(image, 0, 0, width, height, null);
            return bdest;
        } else {
            return image;
        }
    }

    /**
     * Convert the given icon to an image.
     *
     * @param icon the icon to convert.
     * @return the converted icon.
     */
    public static BufferedImage iconToImage(Icon icon) {
        BufferedImage ret = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
        icon.paintIcon(new JLabel(), ret.createGraphics(), 0, 0);
        return ret;
    }
}

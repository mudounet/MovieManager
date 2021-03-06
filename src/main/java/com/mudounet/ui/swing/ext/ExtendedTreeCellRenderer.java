/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.ui.swing.ext;

import com.mudounet.MovieManager;
import com.mudounet.gui.MovieManagerConfig;
import com.mudounet.models.ModelEntry;
import com.mudounet.utils.FileUtil;
import com.mudounet.utils.events.NewDatabaseLoadedEvent;
import com.mudounet.utils.events.NewDatabaseLoadedEventListener;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author gmanciet
 */
public class ExtendedTreeCellRenderer extends JLabel implements TreeCellRenderer, NewDatabaseLoadedEventListener {

    private static final long serialVersionUID = 1L;
    Logger log = LoggerFactory.getLogger(getClass());
    private HashMap<String, Icon> coverCache = new HashMap<String, Icon>();
    private Icon defaultIconMovie;
    private Icon defaultIconSerie;
    private String folder;
    private MovieManager mm = MovieManager.getIt();
    private MovieManagerConfig config;
    private int lastRowHeight = -1;
    private boolean lastUseCovers = false;
    private Image movieImage;
    private Image serieImage;
    int coverWidth;
    StringBuffer coverTitleBuf = new StringBuffer();
    private static Color background;
    private static Color selectionBackground;
    JTree tree;
    Map<ModelEntry, Object> views = new HashMap<ModelEntry, Object>();

    public void removeNode(DefaultMutableTreeNode node) {

        if (views.remove(node.getUserObject()) == null) {
            log.warn("No mapping existed for entry:" + node.getUserObject());
        }
    }

    public void removeEntry(ModelEntry entry) {
        views.remove(entry);
    }

    /**
     * ExtendedTreeCellRenderer constructor
     *
     * @param mm MovieManager
     * @param scrollPane - JScrollPane containing JTree
     */
    public ExtendedTreeCellRenderer(JTree tree, JScrollPane scrollPane) {
        this(tree, scrollPane, MovieManager.getConfig());
        this.tree = tree;
    }

    /**
     * ExtendedTreeCellRenderer constructor
     *
     * @param dmm DialogMovieManager
     * @param scrollPane - JScrollPane containing JTree
     * @param config MovieManagerConfig
     */
    public ExtendedTreeCellRenderer(JTree tree, JScrollPane scrollPane, MovieManagerConfig config) {

        this.config = config;
        this.tree = tree;

        if (tree != null && tree.getModel() != null) {
            final JTree finalTree = tree;
            scrollPane.getViewport().addChangeListener(new ChangeListener() {

                public void stateChanged(ChangeEvent e) {
                    TreeNode node = (DefaultMutableTreeNode) finalTree.getLastSelectedPathComponent();

                    if (finalTree.getModel() != null) {
                        ((DefaultTreeModel) finalTree.getModel()).nodeChanged(node);
                    }
                }
            });
        }

        // load and scale default images
        movieImage = FileUtil.getImage("/images/movie.png");
        serieImage = FileUtil.getImage("/images/serie.png");
        setOpaque(true);

        setDefaultColors();
    }

    /**
     * Enable anti-aliasing on movie list text in Java 1.6
     */
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        super.paintComponent(g2);
    }

    public void newDatabaseLoaded(NewDatabaseLoadedEvent evt) {
        folder = null;
        clearCoverCache();
    }

    /**
     * Returns specialized JLabel for JTree node display
     *
     * @param tree JTree
     * @param value Object
     * @param selected boolean
     * @param expanded boolean
     * @param leaf boolean
     * @param row int
     * @param hasFocus boolean
     * @return specialized JLabel
     */
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {

        try {

            boolean useCovers = config.getUseJTreeCovers();
            boolean useIcons = config.getUseJTreeIcons();

            Object o = ((DefaultMutableTreeNode) value).getUserObject();

            setBackground(selected ? selectionBackground : background);

            if (o instanceof ModelEntry) {
                ModelEntry entry = (ModelEntry) o;

                // icon
                int h = config.getMovieListRowHeight();

                if (!useCovers) {
                    h += 8;
                }

                if ((h != lastRowHeight) || (useCovers != lastUseCovers)) {
                    /*
                     * Use height as width to obtain square default images when
                     * not showing covers.
                     */
                    int w = useCovers ? h * 32 / 44 : h;

                    coverWidth = w;
                    defaultIconMovie = new ImageIcon(movieImage.getScaledInstance(w, h, Image.SCALE_SMOOTH));
                    defaultIconSerie = new ImageIcon(serieImage.getScaledInstance(w, h, Image.SCALE_SMOOTH));
                    lastRowHeight = h;
                    lastUseCovers = useCovers;
                    clearCoverCache();
                }

                Icon icon = null;

                if (useCovers) {

                    if (entry.getKey() != -1) {

                        if (entry.getCover() != null && entry.getCover().length() > 0) {
                            icon = (Icon) coverCache.get(entry.getCover());

                            if (icon == null) {
                                icon = loadCover(entry);

                                if (icon != null) {
                                    coverCache.put(entry.getCover(), icon);
                                }
                            }
                        }

                        if (icon == null) {
                            icon = leaf ? defaultIconMovie : defaultIconSerie;
                        }
                    }
                } else if (useIcons) {
                    icon = leaf ? defaultIconMovie : defaultIconSerie;
                }

                setIcon(icon);

                useCovers = true;

                if (useCovers) {

                    Object view = views.get(entry);

                    if (view != null) {
                        putClientProperty("html", view);
                    } else {

                        int fontSize = 3 + h / 40;

                        String coverTitle = null;

                        // No date
                        if (fontSize < 4) {

                            if (noDatePrefix == null || lastFontSize != fontSize) {
                                noDatePrefix = "<html><font size='" + fontSize + "'><b>";
                                noDatepostfix = "</b></font></html>";
                            }

                            coverTitle = noDatePrefix
                                    + entry.getTitle()
                                    + noDatepostfix;
                        } else {

                            if (withDatePrefix == null || lastFontSize != fontSize) {
                                withDatePrefix = "<html><font size='" + fontSize + "'><b>";
                                withDatepostfix = "</b></font><br><font size='" + (fontSize - 1) + "'>";
                            }

                            coverTitle = withDatePrefix
                                    + entry.getTitle()
                                    + withDatepostfix + entry.getDate() + "</font></html>";
                        }

                        setText(coverTitle);
                        views.put(entry, getClientProperty("html"));
                    }
                } else {
                    setText(entry.getTitle());
                }

            } else {
                setText(o.toString());
            }

        } catch (Exception e) {
            log.error("Exception:" + e.getMessage(), e);
        }

        javax.swing.JPanel p = new javax.swing.JPanel();
        p.setLayout(new java.awt.BorderLayout());
        p.add(this, BorderLayout.CENTER);

        return p;
    }
    String noDatePrefix = null;
    String noDatepostfix = null;
    String withDatePrefix = null;
    String withDatepostfix = null;
    int lastFontSize = 0;

    public static void setDefaultColors() {

        if (UIManager.getColor("ScrollPane.background") != null) {
            background = UIManager.getColor("ScrollPane.background");
        }

        if (background == null) {
            background = UIManager.getColor("TaskPane.background");
        }

        if (UIManager.getColor("Tree.selectionBackground") != null) {
            selectionBackground = UIManager.getColor("Tree.selectionBackground");
        }

    }

    /**
     * loadCover load cover from disk or database
     *
     * @param entry ModelEntry - movie to load cover for
     * @return Icon - loaded cover
     */
    private Icon loadCover(ModelEntry entry) {

        if (folder == null) {
            folder = config.getCoversPath();
        }

        int h = config.getMovieListRowHeight();
        coverWidth = h * 32 / 44; // hardcoded aspect ratio

        if (mm.getDatabase().isMySQL()) {
            if (config.getStoreCoversLocally() && new File(folder, entry.getCover()).exists()) {
                return new ImageIcon(FileUtil.getImage(folder + File.separator + entry.getCover()).getScaledInstance(coverWidth, h, Image.SCALE_SMOOTH));
            } else {
                byte[] coverData = entry.getCoverData();

                if (coverData != null) {
                    return new ImageIcon(Toolkit.getDefaultToolkit().createImage(coverData).getScaledInstance(coverWidth, h, Image.SCALE_SMOOTH));
                } else {
                    return null;
                }
            }
        } else if ((new File(folder, entry.getCover()).exists())) {
            /*
             * Loads the image...
             */
            return new ImageIcon(FileUtil.getImage(folder + File.separator + entry.getCover()).getScaledInstance(coverWidth, h, Image.SCALE_SMOOTH));
        } else {
            return null;
        }
    }

    /**
     * Clear cached covers and the html view cache
     */
    public void clearCoverCache() {
        views.clear();
        coverCache.clear();
    }

    /**
     * removeCoverFromCache - remove specified cover from cache
     *
     * @param cover - as represented in Model- or MovieEntry cover field
     */
    public void removeCoverFromCache(String cover) {
        coverCache.remove(cover);
    }
}

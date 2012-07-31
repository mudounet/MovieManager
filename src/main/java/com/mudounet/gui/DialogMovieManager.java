/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.gui;

import com.mudounet.MovieManager;
import com.mudounet.models.ModelMovie;
import com.mudounet.ui.swing.ext.ExtendedJTree;
import com.mudounet.ui.swing.ext.ExtendedTreeCellRenderer;
import com.mudounet.utils.FileUtil;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author isabelle
 */
public class DialogMovieManager extends JFrame implements ComponentListener {

    private static final long serialVersionUID = 1L;
    private static Logger log = LoggerFactory.getLogger(DialogMovieManager.class.getName());
    private int movieListWidth = 0;
    public int fontSize = 12;
    public static MovieManagerConfig config = MovieManager.getConfig();
    MovieManagerMenuBar menuBar = null;
    private JTree moviesList;
    ArrayList<ModelMovie> currentMovieList;

    public static JApplet getApplet() {
        log.warn("JApplet is not defined");
        return null;
    }
    private JSplitPane mainWindowSplitPane;
    private JSplitPane movieInfoSplitPane;
    private JSplitPane additionalInfoNotesSplitPane;

    public void componentResized(ComponentEvent ce) {
        movieListWidth = (int) getMoviesList().getSize().getWidth();

        /*
         * Maximized
         */
        if (getExtendedState() == JFrame.MAXIMIZED_BOTH) {
            config.setMainMaximized(true);
        } else {
            config.setMainSize(getMainSize());
            config.setMainMaximized(false);
        }
    }

    public void componentMoved(ComponentEvent ce) {
        if (isShowing()) {
            config.setScreenLocation(getLocationOnScreen());
        }
    }

    public Dimension getMainSize() {
        return this.getSize();
    }

    public void componentShown(ComponentEvent ce) {
    }

    public void componentHidden(ComponentEvent ce) {
    }

    /**
     * Gets the Movie List.
     *
     * @return JList that displays the MovieList.
     *
     */
    public JTree getMoviesList() {
        return moviesList;
    }

    public void setCurrentMovieList(ArrayList<ModelMovie> currentMovieList) {
        this.currentMovieList = currentMovieList;
    }

    public ArrayList<ModelMovie> getCurrentMoviesList() {
        return currentMovieList;
    }

    @Override
    public void finalize() {
        dispose();
    }

    public void setUp() {
        /*
         * Starts other inits.
         */
        log.debug("Start setting up the MovieManager."); //$NON-NLS-1$

        Toolkit.getDefaultToolkit().setDynamicLayout(true);
        // Tooltip delay
        ToolTipManager.sharedInstance().setDismissDelay(100000);

        if (!MovieManager.isApplet()) {
            System.setProperty("sun.awt.noerasebackground", "true"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        setTitle(config.sysSettings.getAppTitle()); //$NON-NLS-1$
        setIconImage(FileUtil.getImage("/images/film.png").getScaledInstance(16, 16, Image.SCALE_SMOOTH)); //$NON-NLS-1$

        setJMenuBar(createMenuBar());

        getContentPane().add(createWorkingArea(), BorderLayout.CENTER);

        setResizable(true);

        setHotkeyModifiers();

        /*
         * Hides database related components.
         */
        menuBar.setDatabaseComponentsEnable(false);

        updateJTreeIcons();

        addComponentListener(this);

        /*
         * All done, pack.
         */
        pack();

        setSize(config.mainSize);
        if (config.getMainMaximized()) {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        }


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Point location = config.getScreenLocation();

        if (location != null && location.getX() < screenSize.getWidth() && location.getY() < screenSize.getHeight()) {
            setLocation(location);
        } else {
            setLocation((int) (screenSize.getWidth() - getSize().getWidth()) / 2,
                    (int) (screenSize.getHeight() - getSize().getHeight()) / 2 - 12);
        }


        /*
         * Setting Main Window slider position
         */
        if (config.mainWindowSliderPosition == -1) {
            getMainWindowSplitPane().setDividerLocation(0.537);
            getMainWindowSplitPane().setLastDividerLocation(getMainWindowSplitPane().getDividerLocation());
        } else {
            getMainWindowSplitPane().setDividerLocation(config.mainWindowSliderPosition);
            if (config.mainWindowLastSliderPosition != -1) {
                getMainWindowSplitPane().setLastDividerLocation(config.mainWindowLastSliderPosition);
            }
        }

        /*
         * Setting Movie Info slider position
         */
        if (config.movieInfoSliderPosition == -1) {
            getMovieInfoSplitPane().setDividerLocation(0.5);
            getMovieInfoSplitPane().setLastDividerLocation(getMovieInfoSplitPane().getDividerLocation());
        } else if (getMovieInfoSplitPane() != null) {
            getMovieInfoSplitPane().setDividerLocation(config.movieInfoSliderPosition);

            if (config.movieInfoLastSliderPosition != -1) {
                getMovieInfoSplitPane().setLastDividerLocation(config.movieInfoLastSliderPosition);
            }
        }


        if (getAdditionalInfoNotesSplitPane() != null) {

            /*
             * Setting Additional Info / Notes slider position
             */
            if (config.additionalInfoNotesSliderPosition == -1) {
                getAdditionalInfoNotesSplitPane().setDividerLocation(0.5);
                getAdditionalInfoNotesSplitPane().setLastDividerLocation(getAdditionalInfoNotesSplitPane().getDividerLocation());
            } else {
                getAdditionalInfoNotesSplitPane().setDividerLocation(config.additionalInfoNotesSliderPosition);

                if (config.additionalInfoNotesLastSliderPosition != -1) {
                    getAdditionalInfoNotesSplitPane().setLastDividerLocation(config.additionalInfoNotesLastSliderPosition);
                }
            }
        }

        resetInfoFieldsDisplay();


        log.debug("MovieManager SetUp done!"); //$NON-NLS-1$
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public JSplitPane getMainWindowSplitPane() {
        return mainWindowSplitPane;
    }

    public JSplitPane getMovieInfoSplitPane() {
        return movieInfoSplitPane;
    }

    public JSplitPane getAdditionalInfoNotesSplitPane() {
        return additionalInfoNotesSplitPane;
    }

    public void showDialog() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void setHotkeyModifiers() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void resetInfoFieldsDisplay() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private JMenuBar createMenuBar() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private Component createWorkingArea() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void updateJTreeIcons() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
        /**
     * Creates the list of movies.
     *
     * @return The listofmovies.
     **/
    protected JScrollPane createList() {
    
    	log.debug("Start creation of the List."); //$NON-NLS-1$

    	moviesList = new ExtendedJTree();
        
        ExtendedTreeCellRenderer.setDefaultColors();
         
        moviesList.setModel(new DefaultTreeModel(new DefaultMutableTreeNode(new ModelMovie()))); //$NON-NLS-1$
        
        moviesList.setRootVisible(false);
        moviesList.setDragEnabled(false);
        moviesList.setLargeModel(true);
             
        
        // Gives error on some versions of substance L&F.
        moviesList.setFont(new Font(moviesList.getFont().getName(),Font.PLAIN,fontSize));
        
        MovieManagerCommandSelect listener = new MovieManagerCommandSelect();
        
        /* Adding listeners to the movie list */
        moviesList.addTreeSelectionListener(listener);
        moviesList.addMouseListener(listener);
        moviesList.addKeyListener(listener);
        
        final JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(moviesList);
        
        /*
         // Testing purposes
        scrollPane.getViewport().addChangeListener(new ChangeListener() {
        	public void stateChanged(ChangeEvent e) {						
        		ExtendedTreeCellRenderer.minViewWidth = scrollPane.getSize().width;
        	}
		});
        */
        
        moviesList.setOpaque(false);
        
        //Avoids NullPointer on Synthetica L&F.
        scrollPane.getViewport().setBackground(UIManager.getColor("ScrollPane.background"));
        
        treeCellRenderer = new ExtendedTreeCellRenderer(MovieManager.getDialog().getMoviesList(), scrollPane);
        extendedTreeCellRenderer = treeCellRenderer;
        
        moviesList.setCellRenderer(treeCellRenderer);
        MovieManager.getDatabaseHandler().getNewDatabaseLoadedHandler().addNewDatabaseLoadedEventListener(treeCellRenderer);
       
        new FileDrop(moviesList, new FileDrop.Listener() {
        	public void filesDropped(final java.io.File[] files ) {   
        		
        		Point p = moviesList.getMousePosition();
        		
        		final JTree movieList = getMoviesList();
        		
        		JPopupMenu popupMenu = new JPopupMenu();
        		        		
        		final JMenuItem addNewEntry = new JMenuItem("Add new entry");
        		popupMenu.add(addNewEntry); //$NON-NLS-1$
        		
        		final JMenuItem addToCurrent = new JMenuItem("Add to selected entry");
        		        		
        		if (movieList.getLastSelectedPathComponent() != null &&
        				((DefaultMutableTreeNode) movieList.getLastSelectedPathComponent()).getUserObject() != null &&
        				MovieManager.getIt().getDialog().getEntries() > 0)
            		popupMenu.add(addToCurrent); //$NON-NLS-1$
        		            		        		
        		ActionListener listener = new ActionListener() {
        			public void actionPerformed(ActionEvent event) {
        				        		
        				ModelMovieInfo modelMovieInfo = null;
        				boolean addNew = true;
        				
        				if (event.getSource().equals(addToCurrent)) {

        					addNew = false;
        					
        					if (movieList.getLastSelectedPathComponent() == null)
        						return;

        					/* The currently visible entry */
        					ModelEntry selected = (ModelEntry) ((DefaultMutableTreeNode) movieList.getLastSelectedPathComponent()).getUserObject();

        					if (selected.getKey() == -1)
        						return;

        					modelMovieInfo = new ModelMovieInfo(selected, true);
        				}
        				else if (event.getSource().equals(addNewEntry)) {
        					
        					DialogMovieInfo dialogMovieInfo = new DialogMovieInfo();
        					modelMovieInfo = dialogMovieInfo.movieInfoModel;
        					GUIUtil.show(dialogMovieInfo, true);
        				}
        				
        				boolean success = false;
        				
        				try {
							modelMovieInfo.getFileInfo(files);
							success = true;
						} catch (FileNotFoundException fe) {
							log.warn("Could not find file " + fe.getMessage());
							
							DialogAlert alert = new DialogAlert(MovieManager.getDialog(), "Error occured", "<html>An error occured while retrieving file info:<br>File not found:" + fe.getMessage() + "</html>", true);
							GUIUtil.show(alert, true);
							
						} catch (Exception e) {
							log.warn("Exception:" + e.getMessage(), e);
							
							DialogAlert alert = new DialogAlert(MovieManager.getDialog(), "Error occured", "<html>An error occured while retrieving file info:<br>" + e.getMessage() + "</html>", true);
							GUIUtil.show(alert, true);
						}
    				
						if (!addNew && success) {
							try {
								modelMovieInfo.saveToDatabase();
								
								MovieManagerCommandSelect.reloadCurrentModel();
							} catch (Exception e) {
								log.warn("Exception:" + e.getMessage(), e);
								
								DialogAlert alert = new DialogAlert(MovieManager.getDialog(), "Error occured", "An error occured while saving data to database");
								GUIUtil.show(alert, true);
							}
						}
        			}
        		};
        		        		
        		addToCurrent.addActionListener(listener);
        		addNewEntry.addActionListener(listener);
        		
        		popupMenu.setLocation(p);
        		popupMenu.show(movieList, p.x, p.y);
        	}   
        });
        
        /* All done. */
        log.debug("Creation of the List done."); //$NON-NLS-1$
        return scrollPane;
    }
}

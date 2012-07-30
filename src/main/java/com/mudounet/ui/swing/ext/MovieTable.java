/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.ui.swing.ext;

import com.mudounet.GlobalVariables;
import com.mudounet.hibernate.Movie;
import com.mudounet.utils.Utils;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import org.slf4j.LoggerFactory;

/**
 *
 * @author isabelle
 */
public class MovieTable extends JPanel {

    protected static org.slf4j.Logger logger = LoggerFactory.getLogger(MovieTable.class.getName());
    private static final long serialVersionUID = 1L;
    ListSelectionModel listSelectionModel;

    public MovieTable() {
        super(new GridLayout(1, 0));
        

        final JTable table = new JTable(new MovieTableModel(GlobalVariables.getListOfMovies()));
        table.getColumnModel().getColumn(1).setCellRenderer(new DurationRenderer());
        table.getColumnModel().getColumn(3).setCellRenderer(new FileSizeRenderer());
        listSelectionModel = table.getSelectionModel();
        listSelectionModel.addListSelectionListener(new SharedListSelectionHandler());
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);



        //Add the scroll pane to this panel.
        add(scrollPane);
    }

    class MovieTableModel extends AbstractTableModel {

        private static final long serialVersionUID = 1L;
        private String[] columnNames = {"Movie title", "Duration", "Dimensions", "Size", "Type", ""};
        private List<Movie> listOfMovies = new ArrayList<Movie>();

        public MovieTableModel(List<Movie> listOfMovies) {
            this.listOfMovies = listOfMovies;
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[col].toString();
        }

        public int getRowCount() {
            return listOfMovies.size();
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public Object getValueAt(int row, int col) {
            if (this.listOfMovies == null) {
                return null;
            }
            Movie m = this.listOfMovies.get(row);
            switch (col) {
                case 0:
                    return m.getTitle();
                case 1:
                    return m.getMediaInfo().getPlayTime();
                case 2:
                    return m.getMediaInfo().getVideoWidth() + " x " + m.getMediaInfo().getVideoHeight();
                case 3:
                    return m.getSize();
                case 4:
                    return m.getMediaInfo().getVideoCodec().toUpperCase();
                case 5:
                    return "View";
            }

            return null;
        }

        private void removeRow(int modelRow) {
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    class DurationRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 1L;

        @Override
        protected void setValue(Object value) {
            if (value instanceof Long) {
                this.setText(Utils.readableDuration(Long.parseLong(value.toString())));
            }
        }
    }

    class FileSizeRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 1L;

        @Override
        protected void setValue(Object value) {
            if (value instanceof Long) {
                this.setText(Utils.readableFileSize(Long.parseLong(value.toString())));
            }
        }
    }

    class SharedListSelectionHandler implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
            ListSelectionModel lsm = (ListSelectionModel) e.getSource();

            int firstIndex = e.getFirstIndex();
            int lastIndex = e.getLastIndex();
            //logger.debug(e);
//        boolean isAdjusting = e.getValueIsAdjusting();
//        logger.debug("Event for indexes "
//                      + firstIndex + " - " + lastIndex
//                      + "; isAdjusting is " + isAdjusting
//                      + "; selected indexes:");
//
//        if (lsm.isSelectionEmpty()) {
//            logger.debug(" <none>");
//        } else {
//            // Find out which indexes are selected.
//            int minIndex = lsm.getMinSelectionIndex();
//            int maxIndex = lsm.getMaxSelectionIndex();
//            for (int i = minIndex; i <= maxIndex; i++) {
//                if (lsm.isSelectedIndex(i)) {
//                    logger.debug(" " + i);
//                }
//            }
//        }
//        output.append(newline);
        }
    }
}

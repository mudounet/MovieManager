/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.ui.swing.ext;

import com.mudounet.hibernate.Movie;
import com.mudounet.utils.Utils;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author isabelle
 */
public class MovieTable extends JPanel {
    private static final long serialVersionUID = 1L;


    public MovieTable(List<Movie> listOfMovies) {
        super(new GridLayout(1, 0));

        final JTable table = new JTable(new MovieTableModel(listOfMovies));
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);



        //Add the scroll pane to this panel.
        add(scrollPane);
    }

    class MovieTableModel extends AbstractTableModel {
        private static final long serialVersionUID = 1L;

        private String[] columnNames = {"Movie title", "Width", "Height", "size", "duration", "Type"};
        private List<Movie> listOfMovies;

        public MovieTableModel(List<Movie> listOfMovies) {
            this.listOfMovies = listOfMovies;
        }
        
        public int getRowCount() {
            return listOfMovies.size();
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public Object getValueAt(int row, int col) {
            if(this.listOfMovies == null) {
                return null;
            }
            Movie m = this.listOfMovies.get(row);
            switch (col) {
            case 0:  return m.getTitle();
            case 1:  return m.getMediaInfo().getVideoWidth();
            case 2:  return m.getMediaInfo().getVideoHeight();
            case 3:  return Utils.readableFileSize(m.getSize());
            case 4:  return Utils.readableDuration(m.getMediaInfo().getPlayTime());
            case 5:  return m.getMediaInfo().getVideoCodec();
        }
            
            return null;
        }
    }
}

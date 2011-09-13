/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.dbunit;
import java.sql.ResultSet;  
import java.sql.ResultSetMetaData;  
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableMetaData;

/**
 *
 * @author gmanciet
 */
public class ResultSetReporter {
    
    protected static Logger logger = Logger.getLogger(ResultSetReporter.class.getName());

    public static void dump(ITable rs) throws SQLException, DataSetException {

        // the order of the rows in a cursor
        // are implementation dependent unless you use the SQL ORDER statement
        ITableMetaData meta   = rs.getTableMetaData();
        Column[] columns = meta.getColumns();
        int rowCount = rs.getRowCount();
        int colCount = columns.length;
        ArrayList<String> colList = new ArrayList<String>();

        for(int i = 0; i < colCount;i++) {
            colList.add(columns[i].getColumnName());
        }
        
        for (int i = 0; i < rowCount; i++) {
            
          
            for (int j = 0; j < colCount; ++j) {
                String column = colList.get(j);
                
                logger.debug(column+"["+(i+1)+"] : \""+rs.getValue(i, column)+"\"");
            }

            System.out.println(" ");
        }
    }
    
    public static void dump(ResultSet rs) throws SQLException {

        // the order of the rows in a cursor
        // are implementation dependent unless you use the SQL ORDER statement
        ResultSetMetaData meta   = rs.getMetaData();
        int               colmax = meta.getColumnCount();
        int               i;
        Object            o = null;

        // the result set is a cursor into the data.  You can only
        // point to one row at a time
        // assume we are pointing to BEFORE the first row
        // rs.next() points to next row and returns true
        // or false if there is no next row, which breaks the loop
        for (; rs.next(); ) {
            for (i = 0; i < colmax; ++i) {
                o = rs.getObject(i + 1);    // Is SQL the first column is indexed
                                            // with 1 not 0
                System.out.print(o.toString() + " ");
            }

            System.out.println(" ");
        }
    }
}  
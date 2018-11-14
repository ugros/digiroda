package ussoft;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.scene.control.TableView;

public class USDataResult {

    private final List<String> columnNames;
    private final List<List<Object>> data;
    private Integer nextRow=0;
    
    public HashMap<String,Object> nextRow() {
        if (nextRow<getNumRows()) {
          HashMap<String,Object> r = new HashMap();
          for (int i=0;i<getNumOfColumns();i++)   {
              r.put(getColumnName(i),data.get(nextRow).get(i));             
          }
          nextRow++;  
          return r;
        }       
        return null;
    }
    
    public HashMap<String,Object> getRow(int row) {
        if (row<getNumRows()) {
          HashMap<String,Object> r = new HashMap();
          for (int i=0;i<getNumOfColumns();i++)   {
              r.put(getColumnName(i),data.get(row).get(i));             
          }          
          return r;
        }       
        return null;
    }
    
    public void resetRowCounter() {
        this.nextRow=0;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    /**
     * It converts a ResultSet to a USDataResult, so you can simply get columns
     * and fields
     *
     * @param rs	ResultSet - for example: executeSql(sql);
     * @throws SQLException
     */
    public USDataResult(ResultSet rs) throws SQLException {

        this.data = new ArrayList<>();
        this.columnNames = new ArrayList<>();

        int columnCount = rs.getMetaData().getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            this.columnNames.add(rs.getMetaData().getColumnName(i));
        }

        while (rs.next()) {
            List<Object> row = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                row.add(rs.getObject(i));
            }
            this.data.add(row);
        }
    }

    /**
     * It creates a USDataResult from parameters, so you can simply get columns
     * and fields
     *
     * @param columnNames	a list of Strings
     * @param data	a two dimensions list of Objects
     * @throws SQLException
     */
    public USDataResult(List<String> columnNames, List<List<Object>> data) {
        this.columnNames = columnNames;
        this.data = data;
    }

    public int getNumOfColumns() {
        return columnNames.size();
    }

    public String getColumnName(int index) {
        return columnNames.get(index);
    }

    public int getNumRows() {
        return data.size();
    }

    public Object getData(int column, int row) {
        return data.get(row).get(column);
    }

    public Integer getColumnIndex(String columnName) {
        for (int i = 0; i < this.getNumOfColumns(); i++) {
            if (columnNames.get(i).equals(columnName)) {
                return i;
            }
        }
        return null;
    }

    public Object getData(String columnName, int row) {
        if (row < getNumRows()) {
            return data.get(row).get(getColumnIndex(columnName));
        } else {
            return null;
        }
    }

    public List<List<Object>> getData() {
        return data;
    }

    public TableView getTableView() {
        return null;
    }

}

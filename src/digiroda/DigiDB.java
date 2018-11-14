package digiroda;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static digiroda.DigiController.LOGGER;
import static digiroda.DigiController.language;
import java.sql.Statement;
import java.util.logging.Level;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import ussoft.USDataResult;
import ussoft.USDialogs;

/**
 * The class takes care about your MySQL database connections and queries
 *
 * @author ugros
 *
 */
public class DigiDB {

    private Connection localDB;
    final TableView table = new TableView();
    public ResultSet rs;
    ObservableList<String[]> dataRows = FXCollections.observableArrayList();

    /**
     * It returns with a connection
     *
     * @return	Connection
     */
    public Connection getConnect() {
        return localDB;
    }

    
    /**
     * Constructor for class, it opens a connection.
     *
     * @param host	URL or hostname like for example 'localhost'
     * @param database	database or schema name
     * @param user	username to login
     * @param password	password to login
     */
    public DigiDB(String host, String database, String user, String password) {
       
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();                
            localDB = DriverManager.getConnection(
                      "jdbc:derby:" + database + ";create=true;" + "user=" + user + ";password=" + password);
                    //"jdbc:derby:" + database + ";create=true;");
            LOGGER.log(Level.INFO, "Connected to Derby database");
        } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "SQL error: "+ex.getMessage());
                System.exit(0);
        } catch (InstantiationException ex) {
                LOGGER.log(Level.SEVERE, "InstantiationException"+ex.getMessage());
                System.exit(0);
        } catch (IllegalAccessException ex) {
                LOGGER.log(Level.SEVERE, "Illegal access: "+ex.getMessage());
                USDialogs.error(language.getProperty("LOGINERROR"), ex.getMessage());
                System.exit(0);
        } catch (ClassNotFoundException ex) {
                LOGGER.log(Level.SEVERE, "Class not found: "+ex.getMessage());
                System.exit(0);
        }


    }

    
    /**
     * This method closes the opened connection.
     *
     * @throws SQLException
     */
    public void close() throws SQLException {
        localDB.close();
    }

    
    /**
     * It executes a query and returns with the result, if it is;
     *
     * @param sql	String, which contains an sql query
     * @return	USDataResult - you can simply get columns and fields
     */
    public USDataResult executeSqlToDataResult(String sql) {
        try {
            Statement stmt;
            stmt = (Statement) localDB.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            return new USDataResult(rs);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
            return null;
        }
        
    }

    
    /**
     * It executes a query and returns with the result, if it is;
     *
     * @param sql	String, which contains an sql query
     * @return	ResultSet
     */
    public ResultSet executeSql(String sql) {
        try {
            Statement stmt;
            stmt = (Statement) localDB.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            return rs;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
        }
        return null;
    }

    
    /**
     * It executes a query and returns with the result, if it is;
     *
     * @param sql	String, which contains an sql query
     * @return	javafx.scene.control.TableView - you can add it to a javafx root
     */
    public TableView executeSqlToTable(String sql){
       return executeSqlToTable(sql, false);        
    }

    
    /**
     * It is a private method for executeSqlToTable method, it executes a query
     * and returns with the result, if it is;
     *
     * @param sql
     * @param editable
     * @return
     */
    private TableView executeSqlToTable(String sql, boolean editable){

        try {
            rs = executeSql(sql);
            USDataResult data = new USDataResult(rs);
            table.setEditable(editable);
            for (int i = 0; i < data.getNumOfColumns(); i++) {
                TableColumn<List<Object>, Object> column = new TableColumn<>(data.getColumnName(i));
                int columnIndex = i;
                column.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get(columnIndex)));
                table.getColumns().add(column);
            }
            
            table.getItems().setAll(data.getData());
            table.setEditable(editable);
            return table;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
        }
        return null;
    }

}

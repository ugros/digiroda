package digiroda;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import java.sql.Statement;
import java.util.logging.Level;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import ussoft.USDataResult;
import ussoft.USDialogs;

import static digiroda.DigiController.LOGGER;
import static digiroda.DigiController.language;
import static digiroda.DigiController.config;

/**
 * The DigiDB class takes care about two database connections (localDB and mainDB) and creates queries 
 *
 * @author ugros
 *
 */
public class DigiDB {

    private final Connection localDB;
    private final Connection mainDB;
    private final String LOCALHOST;
    private final String LOCALSCHEMA;
    private final String MAINHOST;
    private final String MAINSCHEMA;
    private final TableView table = new TableView();
    public ResultSet rs;
    ObservableList<String[]> dataRows = FXCollections.observableArrayList();
    
    /**
     * It returns with a connection of localDB
     *
     * @return	Connection
     */
    public Connection getLocalDB() {
        return localDB;
    }
    
    /**
     * It returns with a connection of mainDB
     *
     * @return	Connection
     */
    public Connection getMainDB() {
        return mainDB;
    }

    /**
     * Constructor for class, it opens two connections 
     * - connection one: local database
     * - connection two: main database.
     * @param user	username to main database
     * @param password	password to main database
     */
    public DigiDB(String user, String password) {
        
        MAINHOST = config.getProperty("MAINHOST");
        MAINSCHEMA = config.getProperty("MAINSCHEMA");        
        mainDB= setMainDB(MAINHOST,MAINSCHEMA,user,password);
        if (mainDB!=null) 
            LOGGER.log(Level.INFO, "Connected to the main database"); 
        else 
            System.exit(0);
        
        LOCALHOST = config.getProperty("LOCALHOST");
        LOCALSCHEMA = config.getProperty("LOCALSCHEMA");        
        localDB= setLocalDB(LOCALHOST,LOCALSCHEMA);
        if (localDB!=null) 
            LOGGER.log(Level.INFO, "Connected to the local database"); 
        else 
            System.exit(0);
    }
    
    
        /** Settig up the localDB field
     * @param host	URL or hostname like for example 'localhost'
     * @param database	database or schema name
     */
    private Connection setMainDB(String host, String database, String user, String password) {       
        try {
            LOGGER.log(Level.INFO, "Trying to connect to main database."); 
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();                
            return DriverManager.getConnection("jdbc:derby://"+host+"/" + database + ";create=true;" + "user=" + user + ";password=" + password);
        } catch (SQLException ex ) {
                LOGGER.log(Level.SEVERE, "SQL error while setting up main database: </br>"+ex.getMessage());
        } catch (InstantiationException ex) {
                LOGGER.log(Level.SEVERE, "InstantiationException while setting up main database: </br>"+ex.getMessage());
        } catch (IllegalAccessException ex) {
                LOGGER.log(Level.SEVERE, "Illegal access while setting up main database: </br>"+ex.getMessage());
                USDialogs.error(language.getProperty("LOGINERROR"), ex.getMessage());
        } catch (ClassNotFoundException ex) {
                LOGGER.log(Level.SEVERE, "Class not found while setting up main database: </br>"+ex.getMessage());
        }
        return null;
    }
    
    
     /** Settig up the localDB field
     * @param host	URL or hostname like for example 'localhost'
     * @param database	database or schema name
     */
    private Connection setLocalDB(String host, String database) {       
        try {
            LOGGER.log(Level.INFO, "Trying to connect to local database.");
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();                
            return DriverManager.getConnection("jdbc:derby:" + database + ";create=true;");      
        } catch (SQLException ex ) {
                LOGGER.log(Level.SEVERE, "SQL error while setting up local database: </br>"+ex.getMessage());
        } catch (InstantiationException ex) {
                LOGGER.log(Level.SEVERE, "InstantiationException  while setting up local database: </br>"+ex.getMessage());
        } catch (IllegalAccessException ex) {
                LOGGER.log(Level.SEVERE, "Illegal access while setting up local database: </br>"+ex.getMessage());
                USDialogs.error(language.getProperty("LOGINERROR while setting up local database: </br>"), ex.getMessage());
        } catch (ClassNotFoundException ex) {
                LOGGER.log(Level.SEVERE, "Class not found while setting up local database: </br>"+ex.getMessage());
        }
        return null;
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

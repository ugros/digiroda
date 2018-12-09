//<editor-fold defaultstate="collapsed" desc="license">
/*
 * Copyright 2018 ugros.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//</editor-fold>
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
import static digiroda.DigiController.user;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import javafx.scene.control.ToggleGroup;

/**
 * The DigiDB class takes care about two database connections (localDB and
 * mainDB) and creates queries
 *
 * @author ugros
 *
 */
public class DigiDB {
    
    class StringAndId {
        private String text;
        private Integer id;
        public StringAndId(String text, Integer id) {
            this.text=text;
            this.id=id;
        }
        public String getText() {
            return this.text;
        }
        public Integer getId() {
            return this.id;
        }
    }

    private final  String[] checkList = {"digidb", "digischema", "files", "companies", "contactpersons", "rights", "userrights", "users", "divisions"};
    private final Connection localDB;
    private final Connection mainDB;
    private final String LOCALHOST;
    private final String LOCALSCHEMA;
    private final String MAINHOST;
    private final String MAINDB;
    private final TableView table = new TableView();
    private ResultSet rs;
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
     * Constructor for class, it opens two connections - connection one: local
     * database - connection two: main database.
     *
     * @param user	username to main database
     * @param password	password to main database
     */
    public DigiDB(String user, String password) {

        MAINHOST = config.getProperty("MAINHOST");
        MAINDB = config.getProperty("MAINDB");
        mainDB = setMainDB(MAINHOST, MAINDB, user, password);
        if (mainDB != null) {
            LOGGER.log(Level.FINE, "Connected to the main database");
            if (!checkDBTables()) {
                close();
                System.exit(0);
            }
        } else {
            close();
            System.exit(0);
        }

        LOCALHOST = config.getProperty("LOCALHOST");
        LOCALSCHEMA = config.getProperty("LOCALSCHEMA");
        localDB = setLocalDB(LOCALHOST, LOCALSCHEMA);
        if (localDB != null) {
            LOGGER.log(Level.FINE, "Connected to the local database");
        } else {
            close();
            System.exit(0);
        }
    }

    /**
     * Settig up the localDB field
     *
     * @param host	URL or hostname like for example 'localhost'
     * @param database	database or schema name
     */
    private Connection setMainDB(String host, String database, String user, String password) {
        try {
            LOGGER.log(Level.FINE, "Trying to connect to main database.");
            //Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();                
            //return DriverManager.getConnection("jdbc:derby://"+host+"/" + database + ";create=true;" + "user=" + user + ";password=" + password);
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection("jdbc:postgresql://" + host + "/" + database, user, password);

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "SQL error while setting up main database: </br>" + ex.getMessage());
            /*} catch (InstantiationException ex) {
                LOGGER.log(Level.SEVERE, "InstantiationException while setting up main database: </br>"+ex.getMessage());
        } catch (IllegalAccessException ex) {
                LOGGER.log(Level.SEVERE, "Illegal access while setting up main database: </br>"+ex.getMessage());
                USDialogs.error(language.getProperty("LOGINERROR"), ex.getMessage());*/
        } catch (ClassNotFoundException ex) {
            LOGGER.log(Level.SEVERE, "Class not found while setting up main database: </br>" + ex.getMessage());
        }
        return null;
    }

    /**
     * Settig up the localDB field
     *
     * @param host	URL or hostname like for example 'localhost'
     * @param database	database or schema name
     */
    private Connection setLocalDB(String host, String database) {
        try {
            LOGGER.log(Level.FINE, "Trying to connect to local database.");
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            return DriverManager.getConnection("jdbc:derby:" + database + ";create=true;");
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "SQL error while setting up local database: </br>" + ex.getMessage());
        } catch (InstantiationException ex) {
            LOGGER.log(Level.SEVERE, "InstantiationException  while setting up local database: </br>" + ex.getMessage());
        } catch (IllegalAccessException ex) {
            LOGGER.log(Level.SEVERE, "Illegal access while setting up local database: </br>" + ex.getMessage());
            USDialogs.error(language.getProperty("LOGINERROR while setting up local database: </br>"), ex.getMessage());
        } catch (ClassNotFoundException ex) {
            LOGGER.log(Level.SEVERE, "Class not found while setting up local database: </br>" + ex.getMessage());
        }
        return null;
    }

    /**
     * This method closes the opened connections.
     *
     */
    public void close() {
        try {
            if (mainDB != null) {
                mainDB.close();
            }
            LOGGER.log(Level.FINE, "Connection to mainDB is closed.");
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        try {
            if (localDB != null) {
                localDB.close();
            }
            LOGGER.log(Level.FINE, "Connection to localDB is closed.");
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
    public List<StringAndId> getRadioButtonsOFDivisions() {
        List<StringAndId> radioButtons = new ArrayList<>();
        String sql = "SELECT users.id, users.familyname, users.firstname, divisions.name as division"
                + " FROM digischema.users, digischema.divisions "
                + " WHERE users.id=divisions.bossid;";
        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.selectedToggleProperty().addListener((observable, oldVal, newVal) -> System.out.println(newVal + " was selected"));
        try {
            PreparedStatement st = getMainDB().prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                radioButtons.add(new StringAndId(rs.getString("division")+"\n"
                      +rs.getString("familyname")+" "+rs.getString("firstname"),
                        rs.getInt("id")));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error while reading divisions: " + ex.getMessage());
        }
        return radioButtons;
    }
    
    public void storeInArchive(String fileName, String timeStamp, Integer userId) {
        String sql="";
        try {
            sql = "INSERT INTO digischema.archive("
                + "	\"timestamp\", filename)"
                + "	VALUES (?, ?);";
        
            PreparedStatement st = getMainDB().prepareStatement(sql);
            st.setString(1, timeStamp);
            st.setString(2, fileName);
            st.executeUpdate();
        
            sql = " SELECT id from digischema.archive where \"timestamp\"='"+timeStamp+"';";
            PreparedStatement st1 = getMainDB().prepareStatement(sql);
            ResultSet rs = st1.executeQuery();
            Integer id;
            while (rs.next()) {
                id=rs.getInt("id");
                sql = "INSERT INTO digischema.flows(" +
                        "	userid, docid, status, \"timestamp\")" +
                        "	VALUES (?, ?, ?, ?);";
        
                PreparedStatement st2 = getMainDB().prepareStatement(sql);
                st2.setInt(1, userId);
                st2.setInt(2, id);
                st2.setInt(3, 1);
                st2.setString(4, timeStamp);
                st2.executeUpdate();
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error while stored "+fileName+" in archive: "+ ex.getMessage());
            USDialogs.warning(language.getProperty("TEXT_NOTSTORED_HEAD"), language.getProperty("TEXT_NOTSTORED_TEXT"));
        }
    }

    /**
     * This method checks th main database (digidb), schema (digischema) and all
     * of tables.
     */
    public boolean checkDBTables() {
        String sql = "SELECT datname as name FROM pg_catalog.pg_database WHERE datname='digidb' union all \n"
                + "SELECT schema_name FROM information_schema.schemata WHERE schema_name = 'digischema' union all \n"
                + "SELECT table_name FROM information_schema.tables WHERE  table_schema = 'digischema';";
        try {
            PreparedStatement st = getMainDB().prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            List<String> dbList = new ArrayList<>();
            while (rs.next()) {
                dbList.add(rs.getString("name"));
            }

            if (dbList.containsAll(Arrays.asList(checkList))) {
                LOGGER.log(Level.FINE, "The main database and tables are ready for use.");
                return true;
            } else {
                LOGGER.log(Level.SEVERE, "The main database, schema or tables don't exist.");
                USDialogs.error(language.getProperty("TEXT_MAINERROR_TITLE"),language.getProperty("TEXT_MAINERROR_TEXT"));
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error while checked main database and its tables: " + ex.getMessage());
            USDialogs.error(language.getProperty("TEXT_MAINERROR_TITLE"),language.getProperty("TEXT_MAINERROR_TEXT"),ex.getMessage());
        }
        
        return false;
    }

    /**
     * It executes a query and returns with the result, if it is;
     *
     * @param sql	String, which contains an sql query
     * @return	USDataResult - you can simply get columns and fields
     */
    private USDataResult executeSqlToDataResult(String sql) {
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
     * @param sql String, which contains an sql query
     * @return	ResultSet
     */
    private ResultSet executeSql(String sql) {
        try {
            Statement stmt;
            stmt = (Statement) mainDB.createStatement();
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
    private TableView executeSqlToTable(String sql) {
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
    private TableView executeSqlToTable(String sql, boolean editable) {
        try {
            rs = executeSql(sql);
            if (rs != null) {
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
            } else {
                if (LOGGER.getLevel().equals(Level.FINEST)) {
                    LOGGER.log(Level.INFO, "The next query returned with empty result: </br>" + sql);
                }
                return null;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
        }
        return null;
    }

    /**
     * It is a method for get a set of user's rights and returns with the
     * result, if it is;
     *
     * @param userName
     * @return Set<String> it contains all names of user's rights.
     */
    public HashSet<String> getUserRights(String userName) {
        String sql = "select rightname from digiSCHEMA.rights where rights.id in ("
                + "select rightid from digiSCHEMA.userrights where userid=("
                + "select id from digiSCHEMA.users where username=?))";
        //String sql = "select rightname from digiSCHEMA.rights where rights.id in (select rightid from digiSCHEMA.userrights where userid=(select id from digiSCHEMA.users where username='ugros'))";
        HashSet<String> result = new HashSet<>();
        try {
            PreparedStatement pst = null;
            pst = mainDB.prepareStatement(sql);
            pst.setString(1, userName);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                result.add(rs.getString(1));
            }
            return result;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
            if (LOGGER.getLevel().equals(Level.FINEST)) {
                LOGGER.log(Level.INFO, sql);
            }
            return null;
        }
    }

    /**
     * It is a method for get a list of contacts and returns with the result in
     * an editable TableView, if it is;
     *
     * @return ObservableList<DigiContacts>: it contains all of contacts.
     */
    public ObservableList<DigiContacts> getContacts() {
        String sql = "SELECT contactpersons.id, company, contactpersons.familyname, contactpersons.firstname, contactpersons.phonenumber, contactpersons.email, contactpersons.country, contactpersons.city, contactpersons.postalcode, contactpersons.adress FROM digischema.companies, digischema.contactpersons where contactpersons.companyid=companies.id;";
        LOGGER.log(Level.FINE, "User " + user.getUserName() + " checked contacts.");
        ResultSet rs = executeSql(sql);
        ObservableList<DigiContacts> oList = FXCollections.observableArrayList();
        try {
            while (rs.next()) {
                oList.add(new DigiContacts(
                        rs.getInt("id"),
                        rs.getString("company"),
                        rs.getString("familyname"),
                        rs.getString("firstname"),
                        rs.getString("phonenumber"),
                        rs.getString("email"),
                        rs.getString("country"),
                        rs.getString("city"),
                        rs.getString("postalcode"),
                        rs.getString("adress")
                ));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, ex.toString());
        }
        return oList;
    }

    /**
     * Set familyname of a contact
     *
     * @param id
     * @param newValue
     */
    public void setFamilyNameOfContact(int id, String newValue) {
        String sql = "UPDATE digischema.contactpersons SET familyname =? WHERE id=" + id + ";";
        try {
            PreparedStatement st = mainDB.prepareStatement(sql);
            st.setString(1, newValue);
            st.executeUpdate();
            LOGGER.log(Level.FINE, "User " + user.getUserName() + " changed familyname of contact: " + id);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "User " + user.getUserName() + " tried to change familyname of contact: " + id + "</br>" + ex.getMessage());
        }
    }

    /**
     * Set city of a contact
     *
     * @param id
     * @param newValue
     */
    public void setCityOfContact(int id, String newValue) {
        String sql = "UPDATE digischema.contactpersons SET city =? WHERE id=" + id + ";";
        try {
            PreparedStatement st = mainDB.prepareStatement(sql);
            st.setString(1, newValue);
            st.executeUpdate();
            LOGGER.log(Level.FINE, "User " + user.getUserName() + " changed city of contact: " + id);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "User " + user.getUserName() + " tried to change city of contact: " + id + "</br>" + ex.getMessage());
        }
    }

    /**
     * Set country of a contact
     *
     * @param id
     * @param newValue
     */
    public void setCountryOfContact(int id, String newValue) {
        String sql = "UPDATE digischema.contactpersons SET country =? WHERE id=" + id + ";";
        try {
            PreparedStatement st = mainDB.prepareStatement(sql);
            st.setString(1, newValue);
            st.executeUpdate();
            LOGGER.log(Level.FINE, "User " + user.getUserName() + " changed country of contact: " + id);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "User " + user.getUserName() + " tried to change country of contact: " + id + "</br>" + ex.getMessage());
        }
    }

    /**
     * Set familyname of a contact
     *
     * @param id
     * @param newValue
     */
    public void setPostalCOfContact(int id, String newValue) {
        String sql = "UPDATE digischema.contactpersons SET postalcode =? WHERE id=" + id + ";";
        try {
            PreparedStatement st = mainDB.prepareStatement(sql);
            st.setString(1, newValue);
            st.executeUpdate();
            LOGGER.log(Level.FINE, "User " + user.getUserName() + " changed postal code of contact: " + id);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "User " + user.getUserName() + " tried to change postal code of contact: " + id + "</br>" + ex.getMessage());
        }
    }

    /**
     * Set firstname of a contact
     *
     * @param id
     * @param newValue
     */
    public void setFirstNameOfContact(int id, String newValue) {
        String sql = "UPDATE digischema.contactpersons SET firstname =? WHERE id=" + id + ";";
        try {
            PreparedStatement st = mainDB.prepareStatement(sql);
            st.setString(1, newValue);
            st.executeUpdate();
            LOGGER.log(Level.FINE, "User " + user.getUserName() + " changed firstname of contact: " + id);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "User " + user.getUserName() + " tried to change firstname of contact: " + id + "</br>" + ex.getMessage());
        }
    }

    /**
     * Set familyname of a contact
     *
     * @param id
     * @param newValue
     */
    public void setAdressOfContact(int id, String newValue) {
        String sql = "UPDATE digischema.contactpersons SET adress =? WHERE id=" + id + ";";
        try {
            PreparedStatement st = mainDB.prepareStatement(sql);
            st.setString(1, newValue);
            st.executeUpdate();
            LOGGER.log(Level.FINE, "User " + user.getUserName() + " changed adress of contact: " + id);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "User " + user.getUserName() + " tried to change adress of contact: " + id + "</br>" + ex.getMessage());
        }
    }

    /**
     * Set phonenumber of a contact
     *
     * @param id
     * @param newValue
     */
    public void setPhoneNumberOfContact(int id, String newValue) {
        String sql = "UPDATE digischema.contactpersons SET phonenumber =? WHERE id=" + id + ";";
        try {
            PreparedStatement st = mainDB.prepareStatement(sql);
            st.setString(1, newValue);
            st.executeUpdate();
            LOGGER.log(Level.FINE, "User " + user.getUserName() + " changed phonenumber of contact: " + id);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "User " + user.getUserName() + " tried to change phonenumber of contact: " + id + "</br>" + ex.getMessage());
        }
    }

    /**
     * Set email of a contact
     *
     * @param id
     * @param newValue
     */
    public void setEmailOfContact(int id, String newValue) {
        String sql = "UPDATE digischema.contactpersons SET email =? WHERE id=" + id + ";";
        try {
            PreparedStatement st = mainDB.prepareStatement(sql);
            st.setString(1, newValue);
            st.executeUpdate();
            LOGGER.log(Level.FINE, "User " + user.getUserName() + " changed email of contact: " + id);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "User " + user.getUserName() + " tried to change email of contact: " + id + "</br>" + ex.getMessage());
        }
    }
}

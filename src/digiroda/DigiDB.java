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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.scene.control.CheckBox;

/**
 * The DigiDB class takes care about two database connections (localDB and
 * mainDB) and creates queries
 *
 * @author ugros
 *
 */
public class DigiDB {

    class StringAndId {
        //<editor-fold defaultstate="collapsed" desc="code">

        private String text;
        private Integer id;

        public StringAndId(String text, Integer id) {
            this.text = text;
            this.id = id;
        }

        public String getText() {
            return this.text;
        }

        public Integer getId() {
            return this.id;
        }
    }

    //</editor-fold>
    class CheckBoxStringAndId extends StringAndId {
        //<editor-fold defaultstate="collapsed" desc="code">    

        private CheckBox checkBox;

        public CheckBoxStringAndId(String text, Integer id) {
            super(text, id);
            this.checkBox = new CheckBox(text);
        }

        public CheckBox getCheckBox() {
            return this.checkBox;
        }
    }
    //</editor-fold>

    private final String[] checkList = {"digischema", "archive", "flows", "relations", "statuscodes", "files", "companies", "contactpersons", "rights", "userrights", "users", "divisions"};
    private final String[] rightList = {"opencontacts", "addcontact", "manageoptions", "addusers", "checklog"};
    //<editor-fold defaultstate="collapsed" desc="+fields">
    private final Connection localDB;
    private final Connection mainDB;
    private final String LOCALHOST;
    private final String LOCALSCHEMA;
    private final String MAINHOST;
    private final String MAINDB;
    private List<String> dbList;
    private final TableView table = new TableView();
    private ResultSet rs;
    ObservableList<String[]> dataRows = FXCollections.observableArrayList();
    //</editor-fold>

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
        //<editor-fold defaultstate="collapsed" desc="code">

        String sql = "";
        MAINHOST = config.getProperty("MAINHOST");
        MAINDB = config.getProperty("MAINDB");
        mainDB = setMainDB(MAINHOST, MAINDB, user, password);
        if (mainDB != null) {
            LOGGER.log(Level.FINE, "Connected to the main database");
            if (!checkDBTables()) {
                if (USDialogs.confirmation(language.getProperty("CREATEDB_HEAD"), language.getProperty("CREATEDB_TEXT")).getText().equalsIgnoreCase("ok")) {

                    if (!dbList.contains(MAINDB)) {
                        try {
                            sql = " CREATE DATABASE " + MAINDB + " "
                                    + " WITH "
                                    + " OWNER = postgres"
                                    + " ENCODING = 'UTF8'"
                                    + " LC_COLLATE = 'Hungarian_Hungary.1250'"
                                    + " LC_CTYPE = 'Hungarian_Hungary.1250'"
                                    + " TABLESPACE = pg_default"
                                    + " CONNECTION LIMIT = -1;"
                                    + " GRANT ALL ON DATABASE digidb TO postgres;"
                                    + " GRANT TEMPORARY, CONNECT ON DATABASE digidb TO PUBLIC;"
                                    + " ALTER DEFAULT PRIVILEGES GRANT ALL ON TABLES TO postgres;"
                                    + " ALTER DEFAULT PRIVILEGES GRANT USAGE, SELECT ON SEQUENCES TO postgres;";
                            Statement st = getMainDB().createStatement();
                            st.executeUpdate(sql);
                        } catch (SQLException ex) {
                            LOGGER.log(Level.SEVERE, "Error while created database: " + ex.getMessage());
                            if (LOGGER.getLevel().equals(Level.FINEST)) {
                                LOGGER.log(Level.INFO, sql);
                            }
                        }
                    }

                    if (!dbList.contains("digischema")) {
                        try {
                            sql = " CREATE SCHEMA digischema AUTHORIZATION postgres;"
                                    + " GRANT ALL ON SCHEMA digischema TO postgres;";
                            Statement st = getMainDB().createStatement();
                            st.executeUpdate(sql);
                        } catch (SQLException ex) {
                            LOGGER.log(Level.SEVERE, "Error while created schema: " + ex.getMessage());
                            if (LOGGER.getLevel().equals(Level.FINEST)) {
                                LOGGER.log(Level.INFO, sql);
                            }
                        }
                    }

                    if (!dbList.contains("archive")) {
                        try {
                            sql = " CREATE TABLE digischema.archive"
                                    + " ( id bigserial NOT NULL,"
                                    + "    filename character varying COLLATE pg_catalog.\"default\" NOT NULL,"
                                    + "    \"timestamp\" timestamp with time zone,"
                                    + "    CONSTRAINT archive_pkey PRIMARY KEY (id) )"
                                    + " WITH (OIDS = FALSE)"
                                    + " TABLESPACE pg_default;"
                                    + " ALTER TABLE digischema.archive OWNER to postgres;"
                                    + " GRANT ALL ON TABLE digischema.archive TO postgres;";
                            Statement st = getMainDB().createStatement();
                            st.executeUpdate(sql);
                        } catch (SQLException ex) {
                            LOGGER.log(Level.SEVERE, "Error while created table of archive: " + ex.getMessage());
                            if (LOGGER.getLevel().equals(Level.FINEST)) {
                                LOGGER.log(Level.INFO, sql);
                            }
                        }
                    }

                    if (!dbList.contains("companies")) {
                        try {
                            sql = " CREATE TABLE digischema.companies"
                                    + "("
                                    + "    id bigserial NOT NULL,"
                                    + "    company character varying(200) COLLATE pg_catalog.\"default\" NOT NULL,"
                                    + "    country character varying(200) COLLATE pg_catalog.\"default\" NOT NULL,"
                                    + "    city character varying(50) COLLATE pg_catalog.\"default\" NOT NULL,"
                                    + "    adress character varying(200) COLLATE pg_catalog.\"default\" NOT NULL,"
                                    + "    phonenumber character varying(200) COLLATE pg_catalog.\"default\" NOT NULL,"
                                    + "    email character varying(200) COLLATE pg_catalog.\"default\" NOT NULL,"
                                    + "    boss character varying(200) COLLATE pg_catalog.\"default\" NOT NULL,"
                                    + "    CONSTRAINT companies_pkey PRIMARY KEY (id)"
                                    + ")"
                                    + "WITH ("
                                    + "    OIDS = FALSE"
                                    + ")"
                                    + "TABLESPACE pg_default;"
                                    + ""
                                    + "ALTER TABLE digischema.companies"
                                    + "    OWNER to postgres;";

                            Statement st = getMainDB().createStatement();
                            st.executeUpdate(sql);
                        } catch (SQLException ex) {
                            LOGGER.log(Level.SEVERE, "Error while created table of companies: " + ex.getMessage());
                            if (LOGGER.getLevel().equals(Level.FINEST)) {
                                LOGGER.log(Level.INFO, sql);
                            }
                        }
                    }

                    if (!dbList.contains("contactpersons")) {
                        try {
                            sql = " CREATE TABLE digischema.contactpersons"
                                    + "("
                                    + "    id bigserial NOT NULL,"
                                    + "    companyid integer,"
                                    + "    familyname character varying(200) COLLATE pg_catalog.\"default\" NOT NULL,"
                                    + "    firstname character varying(200) COLLATE pg_catalog.\"default\" NOT NULL,"
                                    + "    country character varying(50) COLLATE pg_catalog.\"default\" NOT NULL,"
                                    + "    city character varying(50) COLLATE pg_catalog.\"default\" NOT NULL,"
                                    + "    adress character varying(200) COLLATE pg_catalog.\"default\" NOT NULL,"
                                    + "    phonenumber character varying(20) COLLATE pg_catalog.\"default\" NOT NULL,"
                                    + "    email character varying(200) COLLATE pg_catalog.\"default\" NOT NULL,"
                                    + "    postalcode character varying COLLATE pg_catalog.\"default\","
                                    + "    CONSTRAINT contactpersons_pkey PRIMARY KEY (id)"
                                    + ")"
                                    + "WITH ("
                                    + "    OIDS = FALSE"
                                    + ")"
                                    + "TABLESPACE pg_default;"
                                    + ""
                                    + "ALTER TABLE digischema.contactpersons"
                                    + "    OWNER to postgres;";

                            Statement st = getMainDB().createStatement();
                            st.executeUpdate(sql);
                        } catch (SQLException ex) {
                            LOGGER.log(Level.SEVERE, "Error while created table of contactpersons: " + ex.getMessage());
                            if (LOGGER.getLevel().equals(Level.FINEST)) {
                                LOGGER.log(Level.INFO, sql);
                            }
                        }
                    }

                    if (!dbList.contains("divisions")) {
                        try {
                            sql = " CREATE TABLE digischema.divisions"
                                    + "("
                                    + "    id bigserial NOT NULL,"
                                    + "    shortname character varying(10) COLLATE pg_catalog.\"default\" NOT NULL,"
                                    + "    name character varying(200) COLLATE pg_catalog.\"default\" NOT NULL,"
                                    + "    bossid integer NOT NULL,"
                                    + "    hierarchycode character varying(10) COLLATE pg_catalog.\"default\" NOT NULL,"
                                    + "    CONSTRAINT divisions_name_key UNIQUE (name)"
                                    + ","
                                    + "    CONSTRAINT divisions_shortname_key UNIQUE (shortname)"
                                    + ""
                                    + ")"
                                    + "WITH ("
                                    + "    OIDS = FALSE"
                                    + ")"
                                    + "TABLESPACE pg_default;"
                                    + ""
                                    + "ALTER TABLE digischema.divisions"
                                    + "    OWNER to postgres;"
                                    + ""
                                    + "GRANT ALL ON TABLE digischema.divisions TO postgres;";

                            Statement st = getMainDB().createStatement();
                            st.executeUpdate(sql);
                        } catch (SQLException ex) {
                            LOGGER.log(Level.SEVERE, "Error while created table of divisions: " + ex.getMessage());
                            if (LOGGER.getLevel().equals(Level.FINEST)) {
                                LOGGER.log(Level.INFO, sql);
                            }
                        }
                    }

                    if (!dbList.contains("files")) {
                        try {
                            sql = " CREATE TABLE digischema.files"
                                    + "("
                                    + "    docid bigint NOT NULL,"
                                    + "    partnerid integer NOT NULL,"
                                    + "    subject character varying(400) COLLATE pg_catalog.\"default\" NOT NULL,"
                                    + "    url character varying(50) COLLATE pg_catalog.\"default\" NOT NULL,"
                                    + "    contactpersonid integer,"
                                    + "    deadline date,"
                                    + "    archivecategory character varying(4) COLLATE pg_catalog.\"default\","
                                    + "    instructions text COLLATE pg_catalog.\"default\" NOT NULL,"
                                    + "    CONSTRAINT files_pkey PRIMARY KEY (docid)"
                                    + ")"
                                    + "WITH ("
                                    + "    OIDS = FALSE"
                                    + ")"
                                    + "TABLESPACE pg_default;"
                                    + ""
                                    + "ALTER TABLE digischema.files"
                                    + "    OWNER to postgres;"
                                    + ""
                                    + "GRANT ALL ON TABLE digischema.files TO postgres;";

                            Statement st = getMainDB().createStatement();
                            st.executeUpdate(sql);
                        } catch (SQLException ex) {
                            LOGGER.log(Level.SEVERE, "Error while created table of files: " + ex.getMessage());
                            if (LOGGER.getLevel().equals(Level.FINEST)) {
                                LOGGER.log(Level.INFO, sql);
                            }
                        }
                    }

                    if (!dbList.contains("flows")) {
                        try {
                            sql = " CREATE TABLE digischema.flows"
                                    + "("
                                    + "    id bigserial NOT NULL,"
                                    + "    userid bigint NOT NULL,"
                                    + "    docid bigint NOT NULL,"
                                    + "    status bigint NOT NULL,"
                                    + "    \"timestamp\" timestamp with time zone,"
                                    + "    CONSTRAINT flows_pkey PRIMARY KEY (id)"
                                    + ")"
                                    + "WITH ("
                                    + "    OIDS = FALSE"
                                    + ")"
                                    + "TABLESPACE pg_default;"
                                    + ""
                                    + "ALTER TABLE digischema.flows"
                                    + "    OWNER to postgres;"
                                    + ""
                                    + "GRANT ALL ON TABLE digischema.flows TO postgres;";

                            Statement st = getMainDB().createStatement();
                            st.executeUpdate(sql);
                        } catch (SQLException ex) {
                            LOGGER.log(Level.SEVERE, "Error while created table of flows: " + ex.getMessage());
                            if (LOGGER.getLevel().equals(Level.FINEST)) {
                                LOGGER.log(Level.INFO, sql);
                            }
                        }
                    }

                    if (!dbList.contains("relations")) {
                        try {
                            sql = " CREATE TABLE digischema.relations"
                                    + "("
                                    + "    id bigserial NOT NULL,"
                                    + "    antecedentdocid bigint NOT NULL,"
                                    + "    followindocid bigint NOT NULL,"
                                    + "    CONSTRAINT relations_pkey PRIMARY KEY (id)"
                                    + ")"
                                    + "WITH ("
                                    + "    OIDS = FALSE"
                                    + ")"
                                    + "TABLESPACE pg_default;"
                                    + ""
                                    + "ALTER TABLE digischema.relations"
                                    + "    OWNER to postgres;"
                                    + ""
                                    + "GRANT ALL ON TABLE digischema.relations TO postgres;";

                            Statement st = getMainDB().createStatement();
                            st.executeUpdate(sql);
                        } catch (SQLException ex) {
                            LOGGER.log(Level.SEVERE, "Error while created table of relations: " + ex.getMessage());
                            if (LOGGER.getLevel().equals(Level.FINEST)) {
                                LOGGER.log(Level.INFO, sql);
                            }
                        }
                    }

                    if (!dbList.contains("rights")) {
                        try {
                            sql = " CREATE TABLE digischema.rights"
                                    + "("
                                    + "    id bigserial NOT NULL,"
                                    + "    rightname character varying(20) COLLATE pg_catalog.\"default\" NOT NULL,"
                                    + "    CONSTRAINT rights_pkey PRIMARY KEY (id)"
                                    + ")"
                                    + "WITH ("
                                    + "    OIDS = FALSE"
                                    + ")"
                                    + "TABLESPACE pg_default;"
                                    + ""
                                    + "ALTER TABLE digischema.rights"
                                    + "    OWNER to postgres;"
                                    + "INSERT INTO digischema.rights(rightname)"
                                    + "	VALUES ";
                            for (int i = 0; i < rightList.length; i++) {
                                sql += "('" + rightList[i] + "')";
                                if (i < rightList.length - 1) {
                                    sql += ", ";
                                } else {
                                    sql += ";";
                                }
                            }

                            Statement st = getMainDB().createStatement();
                            st.executeUpdate(sql);
                        } catch (SQLException ex) {
                            LOGGER.log(Level.SEVERE, "Error while created table of rights: " + ex.getMessage());
                            if (LOGGER.getLevel().equals(Level.FINEST)) {
                                LOGGER.log(Level.INFO, sql);
                            }
                        }
                    }

                    if (!dbList.contains("statuscodes")) {
                        try {
                            sql = " CREATE TABLE digischema.statuscodes"
                                    + "("
                                    + "    id bigserial NOT NULL,"
                                    + "    status character varying COLLATE pg_catalog.\"default\" NOT NULL,"
                                    + "    CONSTRAINT statuscodes_pkey PRIMARY KEY (id)"
                                    + ")"
                                    + "WITH ("
                                    + "    OIDS = FALSE"
                                    + ")"
                                    + "TABLESPACE pg_default;"
                                    + ""
                                    + "ALTER TABLE digischema.statuscodes"
                                    + "    OWNER to postgres;"
                                    + ""
                                    + "GRANT ALL ON TABLE digischema.statuscodes TO postgres;";

                            Statement st = getMainDB().createStatement();
                            st.executeUpdate(sql);
                        } catch (SQLException ex) {
                            LOGGER.log(Level.SEVERE, "Error while created table of statuscodes: " + ex.getMessage());
                            if (LOGGER.getLevel().equals(Level.FINEST)) {
                                LOGGER.log(Level.INFO, sql);
                            }
                        }
                    }

                    if (!dbList.contains("users")) {
                        try {
                            sql = " CREATE TABLE digischema.users"
                                    + "("
                                    + "    id bigserial NOT NULL,"
                                    + "    familyname character varying(200) COLLATE pg_catalog.\"default\" NOT NULL,"
                                    + "    firstname character varying(200) COLLATE pg_catalog.\"default\" NOT NULL,"
                                    + "    username character varying(50) COLLATE pg_catalog.\"default\" NOT NULL,"
                                    + "    divisionid bigint,"
                                    + "    CONSTRAINT users_pkey PRIMARY KEY (id)"
                                    + ")"
                                    + "WITH ("
                                    + "    OIDS = FALSE"
                                    + ")"
                                    + "TABLESPACE pg_default;"
                                    + ""
                                    + "ALTER TABLE digischema.users"
                                    + "    OWNER to postgres;"
                                    + "INSERT INTO digischema.users("
                                    + "	familyname, firstname, username, divisionid)"
                                    + "	VALUES ('Rendszergazda', '#1', 'postgres', 0);";

                            Statement st = getMainDB().createStatement();
                            st.executeUpdate(sql);
                        } catch (SQLException ex) {
                            LOGGER.log(Level.SEVERE, "Error while created table of users: " + ex.getMessage());
                            if (LOGGER.getLevel().equals(Level.FINEST)) {
                                LOGGER.log(Level.INFO, sql);
                            }
                        }
                    }

                    if (!dbList.contains("userrights")) {
                        try {
                            sql = " CREATE TABLE digischema.userrights"
                                    + "("
                                    + "    id bigserial NOT NULL,"
                                    + "    userid integer,"
                                    + "    rightid integer,"
                                    + "    CONSTRAINT userrights_pkey PRIMARY KEY (id)"
                                    + ")"
                                    + "WITH ("
                                    + "    OIDS = FALSE"
                                    + ")"
                                    + "TABLESPACE pg_default;"
                                    + ""
                                    + "ALTER TABLE digischema.userrights"
                                    + "    OWNER to postgres;";

                            Statement st = getMainDB().createStatement();
                            st.executeUpdate(sql);

                            sql = "SELECT id FROM digischema.users where username='postgres'";

                            Statement st2 = getMainDB().createStatement();
                            ResultSet rs2 = st2.executeQuery(sql);
                            int userId = 0;
                            while (rs2.next()) {
                                userId = rs2.getInt("id");
                            }
                            sql = "SELECT id FROM digischema.rights";

                            Statement st3 = getMainDB().createStatement();
                            ResultSet rs3 = st3.executeQuery(sql);
                            while (rs3.next()) {
                                sql = "INSERT INTO digischema.userrights("
                                        + "	userid, rightid)"
                                        + "	VALUES (" + userId + ", " + rs3.getInt("id") + ");";

                                Statement st4 = getMainDB().createStatement();
                                st4.executeUpdate(sql);
                            }

                        } catch (SQLException ex) {
                            LOGGER.log(Level.SEVERE, "Error while created table of userrights: " + ex.getMessage());
                            if (LOGGER.getLevel().equals(Level.FINEST)) {
                                LOGGER.log(Level.INFO, sql);
                            }
                        }
                    }
                } else {
                    close();
                    Platform.exit();
                }
            }
        } else {
            close();
            Platform.exit();
        }

        LOCALHOST = config.getProperty("LOCALHOST");
        LOCALSCHEMA = config.getProperty("LOCALSCHEMA");
        localDB = setLocalDB(LOCALHOST, LOCALSCHEMA);
        if (localDB != null) {
            LOGGER.log(Level.FINE, "Connected to the local database");
        } else {
            close();
            Platform.exit();
        }
    }
    //</editor-fold>

    /**
     * Settig up the localDB field
     *
     * @param host	URL or hostname like for example 'localhost'
     * @param database	database or schema name
     */
    private Connection setMainDB(String host, String database, String user, String password) {
        //<editor-fold defaultstate="collapsed" desc="code">    
        try {
            LOGGER.log(Level.FINE, "Trying to connect to main database.");
            //Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();                
            //return DriverManager.getConnection("jdbc:derby://"+host+"/" + database + ";create=true;" + "user=" + user + ";password=" + password);
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection("jdbc:postgresql://" + host + "/" + database, user, password);

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "SQL error while setting up main database: </br>" + ex.getMessage());
            USDialogs.warning(language.getProperty("DATABASE_ERROR_TITLE"), ex.getMessage());
        } catch (ClassNotFoundException ex) {
            LOGGER.log(Level.SEVERE, "Class not found while setting up main database: </br>" + ex.getMessage());
            USDialogs.warning(language.getProperty("DATABASE_ERROR_TITLE"), ex.getMessage());
        }
        return null;
    }

    //</editor-fold>
    /**
     * Settig up the localDB field
     *
     * @param host	URL or hostname like for example 'localhost'
     * @param database	database or schema name
     */
    private Connection setLocalDB(String host, String database) {
        //<editor-fold defaultstate="collapsed" desc="code">    
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

    //</editor-fold>
    /**
     * This method closes the opened connections.
     *
     */
    public void close() {
        //<editor-fold defaultstate="collapsed" desc="code">
        try {
            if (mainDB != null) {
                mainDB.close();
            }
            LOGGER.log(Level.FINE, "Connection to mainDB is closed.");
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
        }
        try {
            if (localDB != null) {
                localDB.close();
            }
            LOGGER.log(Level.FINE, "Connection to localDB is closed.");
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
        }
    }
    //</editor-fold>

    /**
     * This method returns with a list of rights.
     *
     * @return List<CheckBoxStringAndId>
     *
     */
    public List<CheckBoxStringAndId> getAllOfRights() {
        //<editor-fold defaultstate="collapsed" desc="code">
        List<CheckBoxStringAndId> allOfRights = new ArrayList<>();
        String sql = "SELECT id, rightname FROM digischema.rights;";
        PreparedStatement st=null;
        try {
            st = getMainDB().prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                allOfRights.add(new CheckBoxStringAndId(rs.getString("rightname"), rs.getInt("id")));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error while reading rights: " + ex.getMessage());
            if (LOGGER.getLevel().equals(Level.FINEST)) {
                LOGGER.log(Level.INFO, st.toString());
            }
            return null;
        }
        return allOfRights;
    }
    //</editor-fold>

    /**
     * This method adds a list of rights to a user.
     *
     *
     */
    public boolean addRights(Integer userId, List<CheckBoxStringAndId> list) {
        //<editor-fold defaultstate="collapsed" desc="code">
        String sql = "INSERT INTO digischema.userrights (userid, rightid) VALUES ( ?, ?);";
        PreparedStatement st=null;
        try {
            for (CheckBoxStringAndId cb : list) {               
                if (cb.checkBox.isSelected()) {
                    Integer rightId = cb.getId();
                    st = getMainDB().prepareStatement(sql);
                    st.setInt(1, userId);
                    st.setInt(2, rightId);
                    st.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error while added rights: " + ex.getMessage());
            if (LOGGER.getLevel().equals(Level.FINEST)) {
                LOGGER.log(Level.INFO, st.toString());
            }
            return false;
        }
        return true;
    }
    //</editor-fold>

        /**
     * This method deletes all rights of user.
     *
     *
     */
    public void deleteRights(Integer userId) {
        //<editor-fold defaultstate="collapsed" desc="code">
        String sql = "DELETE FROM digischema.userrights WHERE userid= ?;";
        PreparedStatement st=null;
        try {
            st = getMainDB().prepareStatement(sql);
            st.setInt(1, userId);
            st.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error while deleted rights: " + ex.getMessage());
            if (LOGGER.getLevel().equals(Level.FINEST)) {
                LOGGER.log(Level.INFO, st.toString());
            }
        }
    }
    //</editor-fold>
    
    /**
     * This method returns with the id of userName.
     *
     * @return Integer
     *
     */
    public int getUserId(String userName) {
        //<editor-fold defaultstate="collapsed" desc="code">
        String sql = "SELECT id FROM digischema.users WHERE username=?;";
        Integer userId = null;
        PreparedStatement st=null;
        try {
            st = getMainDB().prepareStatement(sql);
            st.setString(1, userName);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                userId = rs.getInt("id");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
            if (LOGGER.getLevel().equals(Level.FINEST)) {
                LOGGER.log(Level.INFO, st.toString());
            }
        }
        return userId;
    }
    //</editor-fold>

    /**
     * This function creates a single user with all fields
     *
     */
    public Integer createUser(String familyName, String firstName, String userName, Integer divisionId, String password) {
        //<editor-fold defaultstate="collapsed" desc="code">
        String sql = "";
        Integer userId = null;
        try {
            Pattern pu = Pattern.compile("^[a-zA-Z0-9_]*$");
            Matcher mu = pu.matcher(userName);
            Pattern pp = Pattern.compile("^[a-zA-Z0-9_öüóőúéáűÖÜÓŐÚÉÁŰíÍ+-@&#{}$ß<>łŁđĐ€×÷]*$");
            Matcher mp = pp.matcher(password);
            if (mu.find() && (mp.find())) {
                sql = "CREATE USER " + userName + " WITH PASSWORD '" + password + "'   LOGIN"
                        + "  NOSUPERUSER"
                        + "  INHERIT"
                        + "  CREATEDB"
                        + "  NOCREATEROLE"
                        + "  REPLICATION;";
                Statement st = getMainDB().createStatement();
                st.executeUpdate(sql);
                sql = "GRANT postgres TO " + userName + " WITH ADMIN OPTION;";
                Statement st2 = getMainDB().createStatement();
                st2.executeUpdate(sql);

                LOGGER.log(Level.INFO, "User " + userName + " is created.");
            } else {
                USDialogs.warning(language.getProperty("TEXT_NOTALLOWED_HEAD"), language.getProperty("TEXT_INVALIDCHARACTER"));
                return null;
            }
            sql = "INSERT INTO digischema.users(familyname, firstname, username, divisionid) VALUES (?, ?, ?, ?);";
            PreparedStatement st = getMainDB().prepareStatement(sql);
            st.setString(1, familyName);
            st.setString(2, firstName);
            st.setString(3, userName);
            st.setInt(4, divisionId);
            sql=st.toString();
            st.executeUpdate();

            return getUserId(userName);

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
            if (LOGGER.getLevel().equals(Level.FINEST)) {
                LOGGER.log(Level.INFO, sql);
            }
            return null;
        }
    }
    //</editor-fold>

    /**
     * This function returned with a list of string and id's list for
     * radiobuttons in arrive's panel
     *
     */
    public List<StringAndId> getRadioButtonsOFDivisions() {
        //<editor-fold defaultstate="collapsed" desc="code">

        List<StringAndId> radioButtons = new ArrayList<>();
        String sql = "SELECT users.id, users.familyname, users.firstname, divisions.name as division"
                + " FROM digischema.users, digischema.divisions "
                + " WHERE users.id=divisions.bossid;";
        PreparedStatement st = null;
        try {
            st=getMainDB().prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                radioButtons.add(new StringAndId(rs.getString("division") + "\n>"
                        + rs.getString("familyname") + " " + rs.getString("firstname"),
                        rs.getInt("id")));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error while reading divisions: " + ex.getMessage());
            if (LOGGER.getLevel().equals(Level.FINEST)) {
                LOGGER.log(Level.INFO, st.toString());
            }
        }
        return radioButtons;
    }
    //</editor-fold>

    /**
     * This method makes arrived a documentum, stores in archive and creates
     * first step of flow.
     *
     */
    public void storeInArchive(String fileName, Timestamp timeStamp, Integer userId) {
        //<editor-fold defaultstate="collapsed" desc="code">

        String sql = "INSERT INTO digischema.archive("
                    + "	\"timestamp\", filename)"
                    + "	VALUES (?, ?);";
        PreparedStatement st = null;
        try {
            st=getMainDB().prepareStatement(sql);
            st.setTimestamp(1, timeStamp);
            st.setString(2, fileName);
            st.executeUpdate();

            sql = " SELECT id from digischema.archive where \"timestamp\"='" + timeStamp + "';";
            st = getMainDB().prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            Integer id;
            while (rs.next()) {
                id = rs.getInt("id");
                sql = "INSERT INTO digischema.flows("
                        + "	userid, docid, status, \"timestamp\")"
                        + "	VALUES (?, ?, ?, ?);";

                st = getMainDB().prepareStatement(sql);
                st.setInt(1, userId);
                st.setInt(2, id);
                st.setInt(3, 1);
                st.setTimestamp(4, timeStamp);
                st.executeUpdate();
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error while stored " + fileName + " in archive: " + ex.getMessage());
            if (LOGGER.getLevel().equals(Level.FINEST)) {
                LOGGER.log(Level.INFO, st.toString());
            }
            USDialogs.warning(language.getProperty("TEXT_NOTSTORED_HEAD"), language.getProperty("TEXT_NOTSTORED_TEXT"));
        }
    }
    //</editor-fold>  

    /**
     * This method checks th main database (digidb), schema (digischema) and all
     * of tables.
     */
    public boolean checkDBTables() {
        //<editor-fold defaultstate="collapsed" desc="code">

        String sql = "SELECT datname as name FROM pg_catalog.pg_database WHERE datname='" + MAINDB + "' union all "
                + "SELECT schema_name FROM information_schema.schemata WHERE schema_name = 'digischema' union all "
                + "SELECT table_name FROM information_schema.tables WHERE  table_schema = 'digischema';";
        PreparedStatement st= null;
        try {
            st = getMainDB().prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            dbList = new ArrayList<>();
            while (rs.next()) {
                dbList.add(rs.getString("name"));
            }

            if (dbList.containsAll(Arrays.asList(checkList))) {
                LOGGER.log(Level.FINE, "The main database and tables are ready for use.");
                return true;
            } else {
                LOGGER.log(Level.SEVERE, "The main database, schema or tables don't exist.");
                //USDialogs.error(language.getProperty("TEXT_MAINERROR_TITLE"), language.getProperty("TEXT_MAINERROR_TEXT"));
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error while checked main database and its tables: " + ex.getMessage());
            if (LOGGER.getLevel().equals(Level.FINEST)) {
                LOGGER.log(Level.INFO, st.toString());
            }
            //USDialogs.error(language.getProperty("TEXT_MAINERROR_TITLE"), language.getProperty("TEXT_MAINERROR_TEXT"), ex.getMessage());
        }

        return false;
    }//</editor-fold>

    /**
     * It executes a query and returns with the result, if it is;
     *
     * @param sql	String, which contains an sql query
     * @return	USDataResult - you can simply get columns and fields
     */
    private USDataResult executeSqlToDataResult(String sql) {
        //<editor-fold defaultstate="collapsed" desc="code"> 
        Statement stmt=null;         
        try {
            
            stmt = (Statement) localDB.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            return new USDataResult(rs);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
            if (LOGGER.getLevel().equals(Level.FINEST)) {
                LOGGER.log(Level.INFO, sql);
            }
            return null;
        }
    }
//</editor-fold> 

    /**
     * It executes a query and returns with the result, if it is;
     *
     * @param sql String, which contains an sql query
     * @return	ResultSet
     */
    private ResultSet executeSql(String sql) {
        //<editor-fold defaultstate="collapsed" desc="code">

        try {
            Statement stmt;
            stmt = (Statement) mainDB.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            return rs;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
            if (LOGGER.getLevel().equals(Level.FINEST)) {
                LOGGER.log(Level.INFO, sql);
            }
        }
        return null;
    }//</editor-fold>

    /**
     * It executes a query and returns with the result, if it is;
     *
     * @param sql	String, which contains an sql query
     * @return	javafx.scene.control.TableView - you can add it to a javafx root
     */
    private TableView executeSqlToTable(String sql) {
        //<editor-fold defaultstate="collapsed" desc="code">

        return executeSqlToTable(sql, false);
    }//</editor-fold>

    /**
     * It is a private method for executeSqlToTable method, it executes a query
     * and returns with the result, if it is;
     *
     * @param sql
     * @param editable
     * @return
     */
    private TableView executeSqlToTable(String sql, boolean editable) {
        //<editor-fold defaultstate="collapsed" desc="code">
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
    }//</editor-fold>

    /**
     * It is a method for get a set of user's rights and returns with the
     * result, if it is;
     *
     * @param userName
     * @return Set<String> it contains all names of user's rights.
     */
    public List<String> getUserRights(String userName) {
        //<editor-fold defaultstate="collapsed" desc="code">

        String sql = "select rightname from digiSCHEMA.rights where rights.id in ("
                + "select rightid from digiSCHEMA.userrights where userid=("
                + "select id from digiSCHEMA.users where username=?))";
        List<String> result = new ArrayList();
        PreparedStatement st = null;
        try {
            
            st = mainDB.prepareStatement(sql);
            st.setString(1, userName);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                result.add(rs.getString(1));
            }
            return result;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
            if (LOGGER.getLevel().equals(Level.FINEST)) {
                LOGGER.log(Level.INFO, st.toString());
            }
            return null;
        }
    }//</editor-fold>

    /**
     * It is a method for get a set of user's rights and returns with the
     * result, if it is;
     *
     * @return List<String> it contains all names of user's.
     */
    public List<String> getAllOfUserNames() {
        //<editor-fold defaultstate="collapsed" desc="code">

        String sql = "select username from digiSCHEMA.users;";
        List<String> result = new ArrayList(); 
        PreparedStatement st = null;
        try {
           
            st = mainDB.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                result.add(rs.getString(1));
            }
            return result;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
            if (LOGGER.getLevel().equals(Level.FINEST)) {
                LOGGER.log(Level.INFO, st.toString());
            }
            return null;
        }
    }//</editor-fold>

    /**
     * It is a method for get a list of contacts and returns with the result in
     * an editable TableView, if it is;
     *
     * @return ObservableList<DigiContacts>: it contains all of contacts.
     */
    public ObservableList<DigiContacts> getContacts() {
        //<editor-fold defaultstate="collapsed" desc="code">

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
            if (LOGGER.getLevel().equals(Level.FINEST)) {
                LOGGER.log(Level.INFO, sql);
            }
        }
        return oList;
    }//</editor-fold>

    /**
     * Set familyname of a contact
     *
     * @param id
     * @param newValue
     */
    public void setFamilyNameOfContact(int id, String newValue) {
        //<editor-fold defaultstate="collapsed" desc="code">

        String sql = "UPDATE digischema.contactpersons SET familyname =? WHERE id=" + id + ";";
        try {
            PreparedStatement st = mainDB.prepareStatement(sql);
            st.setString(1, newValue);
            st.executeUpdate();
            LOGGER.log(Level.FINE, "User " + user.getUserName() + " changed familyname of contact: " + id);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "User " + user.getUserName() + " tried to change familyname of contact: " + id + "</br>" + ex.getMessage());
            if (LOGGER.getLevel().equals(Level.FINEST)) {
                LOGGER.log(Level.INFO, sql);
            }
        }
    }//</editor-fold>

    /**
     * Set city of a contact
     *
     * @param id
     * @param newValue
     */
    public void setCityOfContact(int id, String newValue) {
        //<editor-fold defaultstate="collapsed" desc="code">

        String sql = "UPDATE digischema.contactpersons SET city =? WHERE id=" + id + ";";
        PreparedStatement st=null;
        try {
             st = mainDB.prepareStatement(sql);
            st.setString(1, newValue);
            st.executeUpdate();
            LOGGER.log(Level.FINE, "User " + user.getUserName() + " changed city of contact: " + id);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "User " + user.getUserName() + " tried to change city of contact: " + id + "</br>" + ex.getMessage());
            if (LOGGER.getLevel().equals(Level.FINEST)) {
                LOGGER.log(Level.INFO, st.toString());
            }
        }
    }
    //</editor-fold>

    /**
     * Set country of a contact
     *
     * @param id
     * @param newValue
     */
    public void setCountryOfContact(int id, String newValue) {
        //<editor-fold defaultstate="collapsed" desc="code">
        String sql = "UPDATE digischema.contactpersons SET country =? WHERE id=" + id + ";";
        PreparedStatement st=null;
        try {
             st = mainDB.prepareStatement(sql);
            st.setString(1, newValue);
            st.executeUpdate();
            LOGGER.log(Level.FINE, "User " + user.getUserName() + " changed country of contact: " + id);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "User " + user.getUserName() + " tried to change country of contact: " + id + "</br>" + ex.getMessage());
            if (LOGGER.getLevel().equals(Level.FINEST)) {
                LOGGER.log(Level.INFO, st.toString());
            }
        }
    }

    //</editor-fold>
    /**
     * Set familyname of a contact
     *
     * @param id
     * @param newValue
     */
    public void setPostalCOfContact(int id, String newValue) {
        //<editor-fold defaultstate="collapsed" desc="code">    
        String sql = "UPDATE digischema.contactpersons SET postalcode =? WHERE id=" + id + ";";
        PreparedStatement st=null;
        try {
            st = mainDB.prepareStatement(sql);
            st.setString(1, newValue);
            st.executeUpdate();
            LOGGER.log(Level.FINE, "User " + user.getUserName() + " changed postal code of contact: " + id);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "User " + user.getUserName() + " tried to change postal code of contact: " + id + "</br>" + ex.getMessage());
            if (LOGGER.getLevel().equals(Level.FINEST)) {
                LOGGER.log(Level.INFO, st.toString());
            }
        }
    }
    //</editor-fold>

    /**
     * Set firstname of a contact
     *
     * @param id
     * @param newValue
     */
    public void setFirstNameOfContact(int id, String newValue) {
        //<editor-fold defaultstate="collapsed" desc="code">

        String sql = "UPDATE digischema.contactpersons SET firstname =? WHERE id=" + id + ";";
        PreparedStatement st=null;
        try {
            st = mainDB.prepareStatement(sql);
            st.setString(1, newValue);
            st.executeUpdate();
            LOGGER.log(Level.FINE, "User " + user.getUserName() + " changed firstname of contact: " + id);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "User " + user.getUserName() + " tried to change firstname of contact: " + id + "</br>" + ex.getMessage());
            if (LOGGER.getLevel().equals(Level.FINEST)) {
                LOGGER.log(Level.INFO, st.toString());
            }
        }
    }//</editor-fold>

    /**
     * Set familyname of a contact
     *
     * @param id
     * @param newValue
     */
    public void setAdressOfContact(int id, String newValue) {
        //<editor-fold defaultstate="collapsed" desc="code">

        String sql = "UPDATE digischema.contactpersons SET adress =? WHERE id=" + id + ";";
        PreparedStatement st=null;
        try {
            st = mainDB.prepareStatement(sql);
            st.setString(1, newValue);
            st.executeUpdate();
            LOGGER.log(Level.FINE, "User " + user.getUserName() + " changed adress of contact: " + id);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "User " + user.getUserName() + " tried to change adress of contact: " + id + "</br>" + ex.getMessage());
            if (LOGGER.getLevel().equals(Level.FINEST)) {
                LOGGER.log(Level.INFO, st.toString());
            }
        }
    }//</editor-fold>

    /**
     * Set phonenumber of a contact
     *
     * @param id
     * @param newValue
     */
    public void setPhoneNumberOfContact(int id, String newValue) {
        //<editor-fold defaultstate="collapsed" desc="code">
        String sql = "UPDATE digischema.contactpersons SET phonenumber =? WHERE id=" + id + ";";
        PreparedStatement st=null;
        try {
            st= mainDB.prepareStatement(sql);
            st.setString(1, newValue);
            st.executeUpdate();
            LOGGER.log(Level.FINE, "User " + user.getUserName() + " changed phonenumber of contact: " + id);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "User " + user.getUserName() + " tried to change phonenumber of contact: " + id + "</br>" + ex.getMessage());
            if (LOGGER.getLevel().equals(Level.FINEST)) {
                LOGGER.log(Level.INFO, st.toString());
            }
        }
    }//</editor-fold>

    /**
     * Set email of a contact
     *
     * @param id
     * @param newValue
     */
    public void setEmailOfContact(int id, String newValue) {
        //<editor-fold defaultstate="collapsed" desc="code">

        String sql = "UPDATE digischema.contactpersons SET email =? WHERE id=" + id + ";";
        PreparedStatement st = null;
        try {
            st = mainDB.prepareStatement(sql);
            st.setString(1, newValue);
            st.executeUpdate();
            LOGGER.log(Level.FINE, "User " + user.getUserName() + " changed email of contact: " + id);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "User " + user.getUserName() + " tried to change email of contact: " + id + "</br>" + ex.getMessage());
            if (LOGGER.getLevel().equals(Level.FINEST)) {
                LOGGER.log(Level.INFO, st.toString());
            }
        }
    }
//</editor-fold>
}

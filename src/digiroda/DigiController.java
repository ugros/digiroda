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

import static digiroda.DigiMenuListener.MENU;
import static digiroda.DigiMenuListener.MENU_ACTUALLOG;
import static digiroda.DigiMenuListener.MENU_ARRIVE;
import static digiroda.DigiMenuListener.MENU_CONTACTS;
import static digiroda.DigiMenuListener.MENU_EXPORT;
import static digiroda.DigiMenuListener.MENU_LOGS;
import static digiroda.DigiMenuListener.MENU_QUIT;
import static digiroda.DigiMenuListener.MENU_SETTINGS;
import static digiroda.DigiMenuListener.MENU_SHOWCONTACTS;
import static digiroda.DigiMenuListener.MENU_CALENDAR;
import static digiroda.DigiMenuListener.MENU_CREATEUSER;
import static digiroda.DigiMenuListener.MENU_OPTIONS;
import static digiroda.DigiMenuListener.MENU_SETRIGHTS;
import static digiroda.DigiMenuListener.MENU_USERS;
import static digiroda.DigiMenuListener.connects;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import ussoft.USDialogs;
import ussoft.USLogger;

public class DigiController implements Initializable {

    //<editor-fold defaultstate="collapsed" desc="@FXML annotations">    
    @FXML
    AnchorPane root;
    @FXML
    SplitPane mainSplitPane;
    @FXML
    StackPane menuPane;
    @FXML
    StackPane dataPane;
    @FXML
    SplitPane contactsSplitPane;
    @FXML
    AnchorPane contactsDataPane;
    @FXML
    AnchorPane contactsAddPane;
    @FXML
    StackPane cleanStackFXML;  
    @FXML
    AnchorPane cleanAnchorFXML;  
    @FXML
    ScrollPane cleanScrollFXML;  
    @FXML
    Label label;
    @FXML
    TextField familyName;
    @FXML
    TextField firstName;
    @FXML
    TextField phoneNumber;
    @FXML
    Button addContactBtn;
    @FXML
    TableView table;
    @FXML
    TextField filterText;
    static SplitPane contactsSplitP;
    static ScrollPane cleanScrollPane;
    static StackPane cleanStackPane;
    static AnchorPane cleanAnchorPane;
    static TableView contactsTable;
    static StackPane dataP;
    static TextField filterT;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Field's declarations">
    static TreeItem<String> rootItem, treeItem1, treeItem2, treeItem3, treeItem4, treeItem5, treeItem31, treeItem32, treeItem41, treeItem42, treeItem43, treeItem44,
            treeItem421, treeItem422;
    final static Properties language = new Properties();
    public final static Properties config = new Properties();
    static DigiUser user=null;
    public static Level loggerLevel; 						// This is for set up logging level. Use "normal" in "_logLevel" to set up normal level.
    final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME); 	// Logger is an API for logging what you/ want
    static String lg; 															// The logger's filename
    public boolean isLogStored = false;
    private boolean external=false;
    //</editor-fold>
    
    public void readProperties() {
        
        InputStream input = null;
        InputStream input2 = null;
        
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            //resource file should be in /src/resources folder and 
            //that folder should be added in "Project/Properties" menu to "Sources" and to "Libraries" too  
            input = loader.getResourceAsStream("resources/hungarian.lang.properties");           
            language.load(input);
 

            File f=new File("./config.xml");
            if (f.exists()) 
            {
            	FileInputStream in=new FileInputStream(f);
                config.loadFromXML(in); 
                external=true;                
            } else {
            	input2 = loader.getResourceAsStream("resources/config.properties");           
            	config.load(input2);  
                f.createNewFile();
                OutputStream os = new FileOutputStream(f);
                config.storeToXML(os, "Beállítások");
            }
            
        } catch (IOException e) {
            USDialogs.error("ERROR", e.getMessage());
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    USDialogs.error("ERROR", e.getMessage());
                }
            }
            if (input2 != null) {
                try {
                    input2.close();
                } catch (IOException e) {
                    USDialogs.error("ERROR", e.getMessage());
                }
            }
        }

    }

    private void setMenuItems() {
        
        //<editor-fold defaultstate="collapsed" desc="Adding menu items">
        rootItem = new TreeItem<>(MENU);
        TreeView<String> treeView = new TreeView<>(rootItem);
        treeView.setShowRoot(true);
        treeItem1 = new TreeItem<>(MENU_ARRIVE);
        
        treeItem2 = new TreeItem<>(MENU_CALENDAR); 
        
        treeItem3 = new TreeItem<>(MENU_CONTACTS);        
            treeItem31 = new TreeItem<>(MENU_SHOWCONTACTS);
            treeItem32 = new TreeItem<>(MENU_EXPORT);
            treeItem3.getChildren().addAll(treeItem31, treeItem32);  
        
        treeItem4= new TreeItem<>(MENU_SETTINGS);
            treeItem41=new TreeItem<>(MENU_OPTIONS);
            treeItem42=new TreeItem<>(MENU_USERS);
                treeItem421=new TreeItem<>(MENU_CREATEUSER);
                treeItem422=new TreeItem<>(MENU_SETRIGHTS);
            treeItem43= new TreeItem<>(MENU_LOGS);
            treeItem44= new TreeItem<>(MENU_ACTUALLOG);
        
        treeItem42.getChildren().addAll(treeItem421, treeItem422);
        treeItem4.getChildren().addAll(treeItem41, treeItem42, treeItem43, treeItem44);  
        
        treeItem5 = new TreeItem<>(MENU_QUIT);
        
        rootItem.getChildren().addAll(treeItem1, treeItem2, treeItem3, treeItem4, treeItem5);
        menuPane.getChildren().add(treeView);
        
        digiroda.DigiController.rootItem.setExpanded(true);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Adding menu listener">
        DigiMenuListener menuListener= new DigiMenuListener();
        treeView.getSelectionModel().selectedItemProperty().addListener(menuListener.menuListener());
        //</editor-fold>
       
    }

    private void setFXML() {
                
        contactsSplitP=new SplitPane();
        contactsSplitP=contactsSplitPane;
        
        cleanScrollPane=new ScrollPane();        
        cleanScrollPane=cleanScrollFXML;
        
        cleanStackPane=new StackPane();        
        cleanStackPane=cleanStackFXML;
        
        cleanAnchorPane=new AnchorPane();        
        cleanAnchorPane=cleanAnchorFXML;
        
        contactsTable=new TableView();
        contactsTable=table;
        
        dataP=new StackPane();
        dataP=dataPane;
        
        filterT=new TextField();
        filterT=filterText;
        
        
    }   
    
    private void setLogger() {
        String level=config.getProperty("LOGLEVEL");
        switch (level) 
        {
            case "Developer": loggerLevel=Level.FINEST; break;
            case "Everything" : loggerLevel=Level.FINER; break;
            case "Normal": loggerLevel=Level.FINE; break;
            case "OnlyReds": loggerLevel=Level.WARNING; break;
            case "OnlyErrors": loggerLevel=Level.SEVERE; break;
            default : loggerLevel=Level.FINE; 
        }			
        // Set the logger level. Don't overwrite this line, if you want change level, change the value of 'LOGLEVEL' in config.properties.
        LOGGER.setLevel(loggerLevel); 
                
        try {
            lg = USLogger.setup(config.getProperty("LOGDIR"));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE,"Can't create log file "+lg);
            LOGGER.log(Level.FINEST,e.getLocalizedMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {  
    	
        readProperties();        
        setLogger();  
        LOGGER.log(Level.INFO,"Program started normally.");
        
        if (external) 
        	LOGGER.log(Level.FINE, "External settings are loaded from: config.xml"); 
        else
        	LOGGER.log(Level.FINE, "External settings are loaded from basic config.properties");
        
        user= new DigiUser(null,null);
        if (user.getChecked()) {            
            setMenuItems();
            setFXML();
            root.getStylesheets().add(getClass().getResource("digi.css").toExternalForm());
        } else {
           LOGGER.log(Level.SEVERE,"System exit (1): Error while checking user.");
           USDialogs.warning(language.getProperty("DATABASE_ERROR_TITLE"), language.getProperty("USERCHECK_ERROR_TEXT"));
           connects.close();
           System.exit(1);
        }
    }
}

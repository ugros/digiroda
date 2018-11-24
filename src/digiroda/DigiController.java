package digiroda;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import ussoft.USDialogs;
import ussoft.USLogger;


public class DigiController implements Initializable {

    //<editor-fold defaultstate="collapsed" desc="Field's declarations">
    static TreeItem<String> treeItem1, treeItem2, treeItem3, treeItem4, treeItem11, treeItem12;
    static Properties language = new Properties();
    static Properties config = new Properties();
    static DigiUser user=null;
    public static Level loggerLevel; 						// This is for set up logging level. Use "normal" in "_logLevel" to set up normal level.
    final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME); 	// Logger is an API for logging what you/ want
    static String lg; 															// The logger's filename
    public boolean isLogStored = false;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="@FXML annotations">
    @FXML
    SplitPane contactsP;
    @FXML
    Pane logP;
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
    StackPane menuPane;
    @FXML
    Pane dataPane;
    @FXML
    Pane contactsPane;
    @FXML
    SplitPane data2Pane;
    @FXML
    TableView table;
    @FXML
    TextField filterText;
    static SplitPane cP;
    static Pane lP;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private String MENU_QUIT;
    private String MENU_EXPORT;
    private String MENU_CONTACTS;
    private String MENU_LIST;
    private String MENU_LOGS;
    private String COLUMN_FIRSTNAME;
    private String COLUMN_FAMILYNAME;
    private String COLUMN_PHONENUMBER; 
    private String COLUMN_EMAIL;
    private String MENU_SETTINGS;
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
            MENU_QUIT = language.getProperty("MENU_QUIT");
            MENU_EXPORT = language.getProperty("MENU_EXPORT");
            MENU_CONTACTS = language.getProperty("MENU_CONTACTS");
            MENU_LIST = language.getProperty("MENU_LIST");
            MENU_LOGS = language.getProperty("MENU_LOGS");
            MENU_SETTINGS = language.getProperty("MENU_SETTINGS");
            
            COLUMN_FIRSTNAME = language.getProperty("COLUMN_FIRSTNAME");
            COLUMN_FAMILYNAME = language.getProperty("COLUMN_FAMILYNAME");
            COLUMN_PHONENUMBER = language.getProperty("COLUMN_PHONENUMBER");
            COLUMN_EMAIL = language.getProperty("COLUMN_EMAIL");
            
            input2 = loader.getResourceAsStream("resources/config.properties");           
            config.load(input2);
            
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
        TreeItem<String> rootItem = new TreeItem<>("Menü");
        TreeView<String> treeView = new TreeView<>(rootItem);
        treeView.setShowRoot(false);
        treeItem1 = new TreeItem<>(MENU_CONTACTS);        
            treeItem11 = new TreeItem<>(MENU_LIST);
            treeItem12 = new TreeItem<>(MENU_EXPORT);
            treeItem1.getChildren().addAll(treeItem11, treeItem12);  
            
        treeItem2= new TreeItem<>(MENU_SETTINGS);
        
        treeItem3= new TreeItem<>(MENU_LOGS);
        
        treeItem4 = new TreeItem<>(MENU_QUIT);
        
        rootItem.getChildren().addAll(treeItem1, treeItem2, treeItem3, treeItem4);
        menuPane.getChildren().add(treeView);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Adding menu listener">
        treeView.getSelectionModel().selectedItemProperty().addListener(DigiListeners.menuListener());
        //</editor-fold>
       
    }

    private void setTableData() {
        //ObservableList<DigiContacts> tableList=FXCollections.observableArrayList();
        ObservableList<DigiContacts> tableList=user.getConnects().getContacts();
         
        TableColumn familyN = new TableColumn(COLUMN_FAMILYNAME);
        familyN.setMinWidth(150);
        familyN.setCellFactory(TextFieldTableCell.forTableColumn());
        familyN.setCellValueFactory(new PropertyValueFactory<>("familyName"));
        familyN.setOnEditCommit(DigiHandlers.familyNOnEditCommit());

        TableColumn firstN = new TableColumn(COLUMN_FIRSTNAME);
        firstN.setMinWidth(150);
        firstN.setCellFactory(TextFieldTableCell.forTableColumn());
        firstN.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        firstN.setOnEditCommit(DigiHandlers.firstNOnEditCommit());

        TableColumn phoneN = new TableColumn(COLUMN_PHONENUMBER);
        phoneN.setMinWidth(100);
        phoneN.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneN.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        phoneN.setOnEditCommit(DigiHandlers.phoneNOnEditCommit());
        
        TableColumn email = new TableColumn(COLUMN_EMAIL);
        email.setMinWidth(100);
        email.setCellFactory(TextFieldTableCell.forTableColumn());
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        email.setOnEditCommit(DigiHandlers.emailOnEditCommit());

        table.getColumns().addAll(familyN, firstN, phoneN,email);
        table.setEditable(true);

        FilteredList<DigiContacts> filteredList = new FilteredList<>(tableList);
        filterText.textProperty().addListener((observable, oldValue, newValue) -> {
        filteredList.setPredicate(
            new Predicate<DigiContacts>(){
                public boolean test(DigiContacts t){
                    if (t.getFamilyName().toUpperCase().contains(filterText.getText().toUpperCase())
                        || t.getFirstName().toUpperCase().contains(filterText.getText().toUpperCase())    
                            ) 
                        return true;
                    else 
                        return false;
                }});});
       /*filterText.textProperty().addListener((observable, oldValue, newValue) -> {
           filteredList.setPredicate( t->  
           { if (   t.getFamilyName().toUpperCase().contains(filterText.getText().toUpperCase())
                 || t.getFirstName().toUpperCase().contains(filterText.getText().toUpperCase()) ) 
                return true;
            else 
                return false;
           });
       });*/
       
       
       table.setItems(filteredList);
       table.blendModeProperty();
               
       table.minWidthProperty().bind(contactsPane.widthProperty());
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
            lg = USLogger.setup();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE,"Can't create log file "+lg);
            LOGGER.log(Level.FINEST,e.getLocalizedMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {  
        readProperties();
        cP=new SplitPane();
        lP=new Pane();
        cP=contactsP;
        lP=logP;
        
        setLogger();      
        LOGGER.log(Level.INFO,"Program started normally.");
        
        user= new DigiUser(null,null);
        if (user.getChecked()) {            
            setMenuItems();
            setTableData();
        } else {
           LOGGER.log(Level.SEVERE,"System exit (1): Error while checking user.");
           user.getConnects().close();
           System.exit(1);
        }
    }
}

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

import static digiroda.DigiController.LOGGER;
import static digiroda.DigiController.cleanAnchorPane;
import static digiroda.DigiController.cleanScrollPane;
import static digiroda.DigiController.cleanStackPane;
import static digiroda.DigiController.config;
import static digiroda.DigiController.contactsSplitP;
import static digiroda.DigiController.contactsTable;
import static digiroda.DigiController.dataP;
import static digiroda.DigiController.filterT;
import static digiroda.DigiController.language;
import static digiroda.DigiController.user;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import ussoft.USDialogs;

/**
 *
 * @author ugros
 */
class DigiMenuListener {

    private File[] fileList;
    private Double width;
    private GridPane gridPane;
    private ImageView imageView;
    private Integer cols;

    //<editor-fold defaultstate="collapsed" desc="Declaration of menu's constants">
    final static String MENU = language.getProperty("MENU");
    final static String MENU_LOGS = language.getProperty("MENU_LOGS");
    final static String MENU_ACTUALLOG = language.getProperty("MENU_ACTUALLOG");
    final static String MENU_QUIT = language.getProperty("MENU_QUIT");
    final static String MENU_EXPORT = language.getProperty("MENU_EXPORT");
    final static String MENU_CONTACTS = language.getProperty("MENU_CONTACTS");
    final static String MENU_SHOWCONTACTS = language.getProperty("MENU_SHOWCONTACTS");
    final static String MENU_SETTINGS = language.getProperty("MENU_SETTINGS");
    final static String MENU_OPTIONS = language.getProperty("MENU_OPTIONS");
    final static String MENU_USERS = language.getProperty("MENU_USERS");
    final static String MENU_CREATEUSER = language.getProperty("MENU_CREATEUSER");
    final static String MENU_SETRIGHTS = language.getProperty("MENU_SETRIGHTS");
    final static String MENU_ARRIVE = language.getProperty("MENU_ARRIVE");
    final static String MENU_CALENDAR = language.getProperty("MENU_CALENDAR");
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Constants of columnnames">
    final static String COLUMN_COMPANYNAME = language.getProperty("COLUMN_COMPANYNAME");
    final static String COLUMN_FIRSTNAME = language.getProperty("COLUMN_FIRSTNAME");
    final static String COLUMN_FAMILYNAME = language.getProperty("COLUMN_FAMILYNAME");
    final static String COLUMN_PHONENUMBER = language.getProperty("COLUMN_PHONENUMBER");
    final static String COLUMN_EMAIL = language.getProperty("COLUMN_EMAIL");
    final static String COLUMN_COUNTRY = language.getProperty("COLUMN_COUNTRY");
    final static String COLUMN_CITY = language.getProperty("COLUMN_CITY");
    final static String COLUMN_POSTALCODE = language.getProperty("COLUMN_POSTALCODE");
    final static String COLUMN_ADRESS = language.getProperty("COLUMN_ADRESS");
    //</editor-fold>     
    //<editor-fold defaultstate="collapsed" desc="Other constants">
    final static String TEXT_NOTALLOWED_HEAD=language.getProperty("TEXT_NOTALLOWED_HEAD");
    final static String TEXT_NOTALLOWED_TEXT=language.getProperty("TEXT_NOTALLOWED_TEXT");
    final static String ARCHIVE1=config.getProperty("ARCHIVE1");
    final static String ARCHIVE2=config.getProperty("ARCHIVE2");
    //</editor-fold>
    
    static String readFile(String path, Charset encoding) throws IOException {            
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
    
    public EventHandler colValueCommit() {
        return new EventHandler<TableColumn.CellEditEvent<Settings, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Settings, String> t) {
                ((Settings) t.getTableView().getItems().get(t.getTablePosition().getRow())).setValue(t.getNewValue());
                String key = ((Settings) t.getTableView().getItems().get(t.getTablePosition().getRow())).getKey();
                String value = t.getNewValue();
                try {
                    config.setProperty(key, value);
                    File f=new File("./config.xml");
                    if (!f.exists()) 
                        f.createNewFile();
                    OutputStream os=new FileOutputStream(f);
                    config.storeToXML(os, "Beállítások");
                    LOGGER.log(Level.INFO, "User " + user.getUserName() + " edited options.");
                } catch (IOException ex) {
                    LOGGER.log(Level.SEVERE, ex.getMessage());
                } 
            }
        };
    }

    public EventHandler colKeyCommit() {
        return new EventHandler<TableColumn.CellEditEvent<Settings, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Settings, String> t) {
                USDialogs.warning("Ez Nem megy", "Ezt sejnos nem teheted meg!");
            }
        };
    }
    
    public class Settings {
                        private SimpleStringProperty key;
                        private SimpleStringProperty value;
                        public Settings() {
                            this.key = new SimpleStringProperty("");
                            this.value = new SimpleStringProperty("");
                        }
                        public Settings(String key, String value) {
                            this.key = new SimpleStringProperty(key);
                            this.value = new SimpleStringProperty(value);
                        }
                        public String getKey() {
                            return this.key.getValue();
                        }
                        public String getValue() {
                            return this.value.getValue();
                        }
                        public void setKey(String newValue) {
                            this.key = new SimpleStringProperty(newValue);
                        }                        
                        public void setValue(String newValue) {
                            this.value = new SimpleStringProperty(newValue);
                        }                        
                    }

    public ChangeListener menuListener() {
        return (ChangeListener) new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                TreeItem item = (TreeItem) newValue;
                String selected = item.getValue().toString();
                if (selected.equals(MENU)) {
                    //<editor-fold defaultstate="collapsed" desc="MENU_ROOT">
                    digiroda.DigiController.rootItem.setExpanded(true);
                    //</editor-fold>
                } else if (selected.equals(MENU_CONTACTS)) {
                    //<editor-fold defaultstate="collapsed" desc="MENU_CONTACTS">
                    
                    digiroda.DigiController.treeItem3.setExpanded(!digiroda.DigiController.treeItem3.isExpanded());
                    //</editor-fold>
                } else if (selected.equals(MENU_EXPORT)) {
                    //<editor-fold defaultstate="collapsed" desc="MENU_EXPORT">
                    LOGGER.log(Level.FINE, "Az exportálás menüpontot választottad");
                    //</editor-fold>
                } else if (selected.equals(MENU_SHOWCONTACTS)) {
                    //<editor-fold defaultstate="collapsed" desc="MENU_SHOWCONTACTS">
                    cleanScrollPane.setVisible(false);
                    cleanAnchorPane.setVisible(false);
                    cleanStackPane.setVisible(false);
                    contactsSplitP.setVisible(true);
                    contactsTable.getColumns().clear();
                    
                    ObservableList<DigiContacts> tableList = user.getConnects().getContacts();

                    TableColumn companyN = new TableColumn(COLUMN_COMPANYNAME);
                    companyN.setMinWidth(150);
                    companyN.setCellFactory(TextFieldTableCell.forTableColumn());
                    companyN.setCellValueFactory(new PropertyValueFactory<>("companyName"));
                    companyN.setOnEditCommit(DigiHandlers.companyNOnEditCommit());

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

                    TableColumn country = new TableColumn(COLUMN_COUNTRY);
                    country.setMinWidth(150);
                    country.setCellFactory(TextFieldTableCell.forTableColumn());
                    country.setCellValueFactory(new PropertyValueFactory<>("country"));
                    country.setOnEditCommit(DigiHandlers.countryOnEditCommit());

                    TableColumn city = new TableColumn(COLUMN_CITY);
                    city.setMinWidth(150);
                    city.setCellFactory(TextFieldTableCell.forTableColumn());
                    city.setCellValueFactory(new PropertyValueFactory<>("city"));
                    city.setOnEditCommit(DigiHandlers.cityOnEditCommit());

                    TableColumn postalC = new TableColumn(COLUMN_POSTALCODE);
                    postalC.setMinWidth(150);
                    postalC.setCellFactory(TextFieldTableCell.forTableColumn());
                    postalC.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
                    postalC.setOnEditCommit(DigiHandlers.postalCOnEditCommit());

                    TableColumn adress = new TableColumn(COLUMN_ADRESS);
                    adress.setMinWidth(150);
                    adress.setCellFactory(TextFieldTableCell.forTableColumn());
                    adress.setCellValueFactory(new PropertyValueFactory<>("adress"));
                    adress.setOnEditCommit(DigiHandlers.adressOnEditCommit());

                    contactsTable.getColumns().addAll(companyN, familyN, firstN, phoneN, email, country, city, postalC, adress);
                    contactsTable.setEditable(true);

                    FilteredList<DigiContacts> filteredList = new FilteredList<>(tableList);
                    filterT.textProperty().addListener((observable2, oldValue2, newValue2) -> {
                        filteredList.setPredicate(
                                new Predicate<DigiContacts>() {
                            public boolean test(DigiContacts t) {
                                if (t.getFamilyName().toUpperCase().contains(filterT.getText().toUpperCase())
                                        || t.getFirstName().toUpperCase().contains(filterT.getText().toUpperCase())) {
                                    return true;
                                } else {
                                    return false;
                                }
                            }
                        });
                    });

                    contactsTable.setItems(filteredList);
                    contactsTable.blendModeProperty();

                    contactsTable.minWidthProperty().bind(dataP.widthProperty());
                    contactsTable.maxWidthProperty().bind(dataP.widthProperty());
                    LOGGER.log(Level.FINE, "User " + user.getUserName() + " checked contacts.");
                    //</editor-fold>
                 } else if (selected.equals(MENU_SETTINGS)) {
                    //<editor-fold defaultstate="collapsed" desc="MENU_SETTINGS">
                    digiroda.DigiController.treeItem4.setExpanded(!digiroda.DigiController.treeItem4.isExpanded());
                    //</editor-fold>  
                } else if (selected.equals(MENU_USERS)) {
                    //<editor-fold defaultstate="collapsed" desc="MENU_USERS">
                    digiroda.DigiController.treeItem42.setExpanded(!digiroda.DigiController.treeItem42.isExpanded());
                    //</editor-fold>  
                } else if (selected.equals(MENU_CREATEUSER)) {
                    //<editor-fold defaultstate="collapsed" desc="MENU_CREATEUSER">
                        contactsSplitP.setVisible(false);
                        cleanScrollPane.setVisible(false);
                        cleanAnchorPane.setVisible(false);
                        cleanStackPane.setVisible(true);
                        cleanStackPane.getChildren().clear();
                        GridPane grid=new GridPane();
                        Label label1=new Label("Felhasználónév");
                        grid.add(label1, 1, 1);
                        TextField userName=new TextField();
                        grid.add(userName, 2, 1);
                        Label label2=new Label("Jelszó");
                        grid.add(label2, 1, 2);
                        TextField password=new TextField();
                        grid.add(password, 2, 2);
                        
                        Button btn = new Button("Létrehoz");
                        btn.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                user.getConnects().createUser(userName.getText(), password.getText());
                            }
                        });
                        grid.add(btn, 4, 2);
                        cleanStackPane.getChildren().add(grid);
                    
                    //</editor-fold>  
                }  else if (selected.equals(MENU_SETRIGHTS)) {
                    //<editor-fold defaultstate="collapsed" desc="MENU_SETRIGHTS">
                    
                    
                    //</editor-fold>  
                } else if (selected.equals(MENU_OPTIONS)) {
                    //<editor-fold defaultstate="collapsed" desc="MENU_OPTIONS">
                    cleanAnchorPane.setVisible(false);
                    cleanStackPane.setVisible(false);
                    contactsSplitP.setVisible(false);
                    cleanScrollPane.setVisible(true);
                    VBox vBox=new VBox();
                    TableView table = new TableView();
                   
                    ObservableList<Settings> tableList = FXCollections.observableArrayList();
                    config.forEach((key, value) -> {
                        tableList.add(new Settings((String)key,(String)value));
                    });
                    
                    TableColumn colKey = new TableColumn(language.getProperty("COLUMN_KEY"));
                    colKey.setMinWidth(150);
                    colKey.setCellFactory(TextFieldTableCell.forTableColumn());
                    colKey.setCellValueFactory(new PropertyValueFactory<>("key"));
                    colKey.setOnEditCommit(colKeyCommit());

                    TableColumn colValue = new TableColumn(language.getProperty("COLUMN_VALUE"));
                    colValue.setMinWidth(150);
                    colValue.setCellFactory(TextFieldTableCell.forTableColumn());
                    colValue.setCellValueFactory(new PropertyValueFactory<>("value"));
                    colValue.setOnEditCommit(colValueCommit());   
                    
                    table.getColumns().addAll(colKey,colValue);
                    table.setEditable(true);
                    Label label=new Label(language.getProperty("TEXT_FILTER"));
                    TextField filter=new TextField();
                    FilteredList<Settings> filteredList = new FilteredList<>(tableList);
                    filter.textProperty().addListener((observable2, oldValue2, newValue2) -> {
                        filteredList.setPredicate(
                                new Predicate<Settings>() {
                            public boolean test(Settings t) {
                                if (t.getKey().toUpperCase().contains(filter.getText().toUpperCase())
                                        || t.getValue().toUpperCase().contains(filter.getText().toUpperCase())) {
                                    return true;
                                } else {
                                    return false;
                                }
                            }
                        });
                    });
                    table.setItems(filteredList);                    
                    HBox hBox=new HBox();
                    hBox.getChildren().addAll(label,filter);
                    vBox.getChildren().addAll(hBox,table);
                    hBox.getStyleClass().add("hbox");
                    vBox.getStyleClass().add("vbox");
                    label.getStyleClass().add("padding10right");
                    cleanScrollPane.setContent(vBox);
                    
                    table.minWidthProperty().bind(cleanScrollPane.widthProperty());
                    table.maxWidthProperty().bind(cleanScrollPane.widthProperty());
                    LOGGER.log(Level.FINE, "User " + user.getUserName() + " checked options.");
                    //</editor-fold>
                } else if (selected.equals(MENU_LOGS)) {
                    //<editor-fold defaultstate="collapsed" desc="MENU_LOGS">
                    if (user.checkRight("checklog")) {
                        WebView webView = new WebView();
                        final WebEngine webEngine = webView.getEngine();
                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setTitle(language.getProperty("TITLE_LOG"));
                        fileChooser.setInitialDirectory(new File(config.getProperty("LOGDIR")));
                        fileChooser.getExtensionFilters().add(new ExtensionFilter(language.getProperty("TEXT_LOGFILES"), "*.log"));
                        File f = fileChooser.showOpenDialog(null);
                        if (f != null) {
                            try {
                                //webEngine.load(f.toURI().toString());
                                String content = readFile(f.getPath(), StandardCharsets.UTF_8);
                                webView.getEngine().loadContent(content, "text/html");
                                contactsSplitP.setVisible(false);
                                cleanScrollPane.setVisible(false);
                                cleanAnchorPane.setVisible(false);
                                cleanStackPane.setVisible(true);
                                cleanStackPane.getChildren().add(webView);
                                cleanStackPane.autosize();
                                LOGGER.log(Level.FINE, "User " + user.getUserName() + " checked log file: " + f.getAbsolutePath());
                            } catch (IOException ex) {
                                Logger.getLogger(DigiMenuListener.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } else {
                        LOGGER.log(Level.WARNING, user.getUserName() + " tried to check log files.");
                        USDialogs.warning(TEXT_NOTALLOWED_HEAD, TEXT_NOTALLOWED_TEXT);
                    }
                    //</editor-fold>
                } else if (selected.equals(MENU_ACTUALLOG)) {
                    //<editor-fold defaultstate="collapsed" desc="MENU_ACTUALLOG">
                    if (user.checkRight("checklog")) {
                        WebView webView = new WebView();
                        final WebEngine webEngine = webView.getEngine();
                        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                        Date date = new Date();
                        String fileName = config.getProperty("LOGDIR")+"/LOG_" + dateFormat.format(date).toString() + ".log";
                        File f = new File(fileName);
                        if (f != null) {
                            try {
                                //webEngine.load(f.toURI().toString());
                                String content = readFile(f.getPath(), StandardCharsets.UTF_8);
                                webView.getEngine().loadContent(content, "text/html");
                                contactsSplitP.setVisible(false);
                                cleanScrollPane.setVisible(false);
                                cleanAnchorPane.setVisible(false);
                                cleanStackPane.setVisible(true);
                                cleanStackPane.getChildren().add(webView);
                                cleanStackPane.autosize();
                                LOGGER.log(Level.FINE, "User " + user.getUserName() + " checked log file: " + f.getAbsolutePath());
                            } catch (IOException ex) {
                                Logger.getLogger(DigiMenuListener.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } else {
                        LOGGER.log(Level.WARNING, user.getUserName() + " tried to check log files.");
                        USDialogs.warning(TEXT_NOTALLOWED_HEAD, TEXT_NOTALLOWED_TEXT);
                    }
                    //</editor-fold>
                } else if (selected.equals(MENU_ARRIVE)) {
                    //<editor-fold defaultstate="collapsed" desc="MENU_ARRIVE">
                    contactsSplitP.setVisible(false);
                    cleanAnchorPane.setVisible(false);
                    cleanStackPane.setVisible(false);
                    cleanScrollPane.setVisible(true);

                    VBox vBox = new VBox();
                    vBox.getStyleClass().add("vbox");
                    gridPane = new GridPane();
                    gridPane.getStyleClass().add("gridpane");
                    
                    cols= Integer.parseInt(config.getProperty("PREVIEW_COLUMNS"));  
                    if (cols>4) cols=4; 
                        else if (cols<1) cols=1;
                    Double fullWidth=cleanScrollPane.getWidth();
                    width= (fullWidth/cols)-195;
                    final File directory = new File(config.getProperty("READFROM"));

                    fileList = directory.listFiles(new FileFilter() {
                        @Override
                        public boolean accept(File pathname) {
                            if (pathname.isFile()
                                    && pathname.getName().toLowerCase().endsWith(".pdf")) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });

                    vBox.getChildren().addAll(gridPane);
                    cleanScrollPane.setContent(vBox);
                    

                    Task<Void> longRunningTask = new Task<Void>() {
                        private int counter;
                        private List<DigiDB.StringAndId> radioButtons = user.getConnects().getRadioButtonsOFDivisions();

                        @Override
                        protected Void call() {

                            for (File file : fileList) {
                                try {
                                    BufferedImage bufferedImge;
                                    
                                    PDDocument document = PDDocument.load(file);
                                    PDFRenderer renderer = new PDFRenderer(document);
                                    bufferedImge = renderer.renderImage(0);
                                    document.close();
                                
                                    Image image = SwingFXUtils.toFXImage(bufferedImge, null);
                                    Double ratio =  width / image.getWidth();

                                    imageView = new ImageView();
                                    imageView = new ImageView();
                                    imageView.setImage(image);
                                    imageView.setFitWidth(width);
                                    imageView.setFitHeight(ratio * image.getHeight());
                                   
                                    Platform.runLater(() -> {
                                        HBox hBox = new HBox();
                                        HBox hBox1 = new HBox();
                                        HBox hBox2 = new HBox();
                                        hBox1.getChildren().addAll(imageView);
                                        hBox1.getStyleClass().add("img");
                                        GridPane grid = new GridPane();
                                        grid.getStyleClass().add("radiobuttons");
                                        Button btn = new Button("Érkeztet");
                                        class PreViewGroup extends ToggleGroup {
                                            public File file;
                                            public PreViewGroup(File file) {
                                                super();
                                                this.file=file;
                                            }
                                        }
                                        PreViewGroup tg = new PreViewGroup(file);
                                        int i;
                                        for (i = 0; i < radioButtons.size(); i++) {
                                            RadioButton rb = new RadioButton(radioButtons.get(i).getText());
                                            rb.setMinWidth(150);
                                            rb.setMaxWidth(150);
                                            rb.setUserData(radioButtons.get(i).getId());
                                            rb.setToggleGroup(tg);
                                            grid.add(rb, 0, i);
                                        }
                                        btn.setOnMousePressed(new EventHandler<MouseEvent>() {
                                            @Override
                                            public void handle(MouseEvent mouseEvent) {
                                                if (mouseEvent.isPrimaryButtonDown() && tg.getSelectedToggle()!=null) {
                                                    File f= new File(ARCHIVE1);
                                                    try {
                                                        Timestamp timeStamp=new Timestamp(System.currentTimeMillis());                                                        
                                                        if (!f.exists()) f.mkdir();
                                                        f=new File(ARCHIVE1+"/"+timeStamp.getTime()+".dg");
                                                        Files.copy(tg.file.toPath(), f.toPath());                                        
                                                        f= new File(ARCHIVE2);
                                                        if (!f.exists()) f.mkdir();
                                                        f=new File(ARCHIVE2);
                                                        if (!f.exists()) f.mkdir();
                                                        f=new File(ARCHIVE2+"/"+timeStamp.getTime()+".dg");
                                                        Files.copy(tg.file.toPath(), f.toPath());               
                                                        user.getConnects().storeInArchive(
                                                                tg.file.getName(), 
                                                                timeStamp, 
                                                                Integer.parseInt(tg.getSelectedToggle().getUserData().toString()));

                                                        hBox.setVisible(false);
                                                        hBox.getChildren().clear();
                                                        hBox.setStyle("-fx-border-insets:0px; -fx-background-insets:0px; -fx-padding:0;");
                                                        Files.delete(tg.file.toPath());               
                                                        LOGGER.log(Level.INFO, f.getAbsolutePath()+" is stored.");
                                                    } catch (IOException ex) {
                                                        LOGGER.log(Level.SEVERE, f.getAbsolutePath()+" isn't stored: "+ex.getMessage());
                                                    }
                                                }
                                            }
                                        });
                                       
                                        grid.add(btn, 0, i + 1);

                                        hBox2.getChildren().addAll(grid);
                                        hBox.getChildren().addAll(hBox1, hBox2);

                                        hBox.getStyleClass().add("hbox");
                                        hBox1.getStyleClass().add("hbox1");
                                        hBox1.addEventFilter(EventType.ROOT, DigiHandlers.clickOnPDFPreview(file));

                                        hBox2.getStyleClass().add("hbox2");
                                        gridPane.add(hBox, counter % cols, ((Integer) counter / cols));
                                        counter++;

                                    });
                                    } catch (IOException e) {
                                    LOGGER.log(Level.SEVERE, e.getMessage());
                                } 


                            }
                            return null;
                        }
                    ;
                    };
                    new Thread(longRunningTask).start();
                    //</editor-fold>
                } else if (selected.equals(MENU_CALENDAR)) {
                    //<editor-fold defaultstate="collapsed" desc="MENU_CALENDAR">
                    contactsSplitP.setVisible(false);
                    cleanAnchorPane.setVisible(false);
                    cleanStackPane.setVisible(false);
                    cleanScrollPane.setVisible(true);

                    VBox vBox = new VBox();
                    vBox.getStyleClass().add("vbox");
                    DigiCalendar calendar=new DigiCalendar(YearMonth.now());
                    vBox.getChildren().add(calendar.getView());
                    cleanScrollPane.setContent(vBox);
                    //</editor-fold>
                }else if (selected.equals(MENU_QUIT)) {
                    //<editor-fold defaultstate="collapsed" desc="MENU_QUIT">
                    LOGGER.log(Level.INFO, "Program terminated.");
                    user.getConnects().close();
                    System.exit(0);
                    //</editor-fold>
                }
            }
        };
    }

}

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
import static digiroda.DigiController.user;
import static digiroda.DigiController.config;
import static digiroda.DigiController.contactsSplitP;
import static digiroda.DigiController.contactsTable;
import static digiroda.DigiController.dataP;
import static digiroda.DigiController.filterT;
import static digiroda.DigiController.language;
import static digiroda.DigiController.cleanScrollPane;
import static digiroda.DigiController.cleanStackPane;

import java.util.logging.Level;
import java.util.function.Predicate;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import java.io.File;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.io.FileFilter;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventType;
import javafx.scene.control.ProgressBar;

/**
 *
 * @author ugros
 */
class DigiMenuListener {

    private int counter = 0;
    private File[] list;
    private File fileEntry;
    private Integer h;
    private GridPane gridPane;
    private ProgressBar pb;
    private ImageView imageView;
    private static Thread thread;

    //<editor-fold defaultstate="collapsed" desc="Declaration of menu's constants">
    final static String MENU = language.getProperty("MENU");
    final static String MENU_LOGS = language.getProperty("MENU_LOGS");
    final static String MENU_QUIT = language.getProperty("MENU_QUIT");
    final static String MENU_EXPORT = language.getProperty("MENU_EXPORT");
    final static String MENU_CONTACTS = language.getProperty("MENU_CONTACTS");
    final static String MENU_SHOWCONTACTS = language.getProperty("MENU_SHOWCONTACTS");
    final static String MENU_SETTINGS = language.getProperty("MENU_SETTINGS");
    final static String MENU_ARRIVE = language.getProperty("MENU_ARRIVE");
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
                    digiroda.DigiController.treeItem1.setExpanded(true);
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
                    LOGGER.log(Level.FINE, "A beállítások menüpontot választottad");
                    //</editor-fold>
                } else if (selected.equals(MENU_LOGS)) {
                    //<editor-fold defaultstate="collapsed" desc="MENU_LOGS">
                    if (user.checkRight("checklog")) {
                        WebView webView = new WebView();
                        final WebEngine webEngine = webView.getEngine();
                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setTitle(language.getProperty("TITLE_LOG"));
                        fileChooser.setInitialDirectory(new File(config.getProperty("LOGDIR")));
                        fileChooser.getExtensionFilters().add(new ExtensionFilter("Naplófájlok", "*.html"));
                        // File f = new File("./LOG/LOG_20181125.html");
                        File f = fileChooser.showOpenDialog(null);
                        if (f != null) {
                            webEngine.load(f.toURI().toString());
                            contactsSplitP.setVisible(false);
                            cleanScrollPane.setVisible(false);
                            cleanAnchorPane.setVisible(false);
                            cleanStackPane.setVisible(true);
                            cleanStackPane.getChildren().add(webView);
                            cleanStackPane.autosize();
                            LOGGER.log(Level.FINE, "User " + user.getUserName() + " checked log file: " + f.getAbsolutePath());
                        }
                    } else {
                        LOGGER.log(Level.WARNING, user.getUserName() + " tried to check log files.");
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

                    h = Integer.parseInt(config.getProperty("PREVIEW_HEIGHT"));
                    final File directory = new File(config.getProperty("READFROM"));

                    list = directory.listFiles(new FileFilter() {
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

                        @Override
                        protected Void call() throws Exception {
                            for (File fileEntry : list) {
                                try {
                                    PDDocument document = PDDocument.load(fileEntry);

                                    PDFRenderer renderer = new PDFRenderer(document);
                                    BufferedImage bufferedImge = renderer.renderImage(0);
                                    document.close();
                                    Image image = SwingFXUtils.toFXImage(bufferedImge, null);
                                    Float ratio = h / (float) image.getHeight();

                                    imageView = new ImageView();
                                    imageView = new ImageView();
                                    imageView.setImage(image);
                                    imageView.setFitHeight(h);
                                    imageView.setFitWidth(ratio * image.getWidth());

                                    Platform.runLater(() -> {

                                        HBox hBox = new HBox();
                                        HBox hBox1 = new HBox();
                                        HBox hBox2 = new HBox();

                                        hBox1.getChildren().addAll(imageView);
                                        hBox1.getStyleClass().add("img");
                                        GridPane grid = new GridPane();
                                        Button btn = new Button("Érkeztet");
                                        //btn.getStyleClass().add("btn");
                                        //btn.setId("xxx");
                                        grid.add(btn, 0, 0);

                                        hBox2.getChildren().addAll(grid);
                                        hBox.getChildren().addAll(hBox1, hBox2);

                                        hBox.getStyleClass().add("hbox");
                                        hBox1.getStyleClass().add("hbox1");
                                        hBox1.addEventFilter(EventType.ROOT, DigiHandlers.clickOnPDFPreview(fileEntry));

                                        hBox2.getStyleClass().add("hbox2");
                                        gridPane.add(hBox, counter % 2, ((Integer) counter / 2));
                                        counter++;

                                    });

                                } catch (IOException e) {
                                    LOGGER.log(Level.SEVERE, e.getMessage());
                                    //}
                                }
//                            if (counter == list.length) {
//                                this.cancel();
//                            }
//                            
                            }
                            return null;
                        }
                    ;
                    };
                    new Thread(longRunningTask).start();
                    //</editor-fold>
                } else if (selected.equals(MENU_QUIT)) {
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

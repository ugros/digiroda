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

package digiroda;

import static digiroda.DigiController.LOGGER;
import static digiroda.DigiController.user;

import java.util.logging.Level;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import java.io.File;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import static digiroda.DigiController.logP;
import static digiroda.DigiController.config;
import static digiroda.DigiController.contactsSplitP;
import static digiroda.DigiController.language;

/**
 *
 * @author ugros
 */
class DigiMenuListener {
    //<editor-fold defaultstate="collapsed" desc="Declaration of menutitles">
    final static String MENU_LOGS=language.getProperty("MENU_LOGS");
    final static String MENU_QUIT = language.getProperty("MENU_QUIT");
    final static String MENU_EXPORT = language.getProperty("MENU_EXPORT");
    final static String MENU_CONTACTS = language.getProperty("MENU_CONTACTS");
    final static String MENU_SHOWCONTACTS = language.getProperty("MENU_LIST");
    final static String MENU_SETTINGS = language.getProperty("MENU_SETTINGS");
    //</editor-fold>

    public static ChangeListener menuListener() {
        return (ChangeListener) (ObservableValue observable, Object oldValue, Object newValue) -> {
            TreeItem item = (TreeItem) newValue;
            String selected=item.getValue().toString();
            if (selected.equals(MENU_CONTACTS)) {
                //<editor-fold defaultstate="collapsed" desc="MENU_CONTACTS">
                digiroda.DigiController.treeItem1.setExpanded(true);
                //</editor-fold>
            } else if (selected.equals(MENU_EXPORT)) {
                //<editor-fold defaultstate="collapsed" desc="MENU_EXPORT">
                LOGGER.log(Level.FINE, "Az exportálás menüpontot választottad");
                //</editor-fold>
            } else if (selected.equals(MENU_SHOWCONTACTS)) {
                //<editor-fold defaultstate="collapsed" desc="MENU_SHOWCONTACTS">
                logP.setVisible(false);
                contactsSplitP.setVisible(true);
                LOGGER.log(Level.FINE, user.getUserName()+" checked contacts.");
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
                    fileChooser.getExtensionFilters().add(new ExtensionFilter("Naplófájlok","*.html"));
                    // File f = new File("./LOG/LOG_20181125.html");
                    File f=fileChooser.showOpenDialog(null);
                    if (f!=null) {
                        webEngine.load(f.toURI().toString());
                        contactsSplitP.setVisible(false);
                        logP.setVisible(true);
                        logP.getChildren().add(webView);
                        logP.autosize();
                    }
                    LOGGER.log(Level.FINE, user.getUserName()+" checked log file: "+ f.getAbsolutePath());
                } else LOGGER.log(Level.WARNING, user.getUserName()+" tried to check log files.");
                //</editor-fold>
            } else if (selected.equals(MENU_QUIT)) {
                //<editor-fold defaultstate="collapsed" desc="MENU_QUIT">
                LOGGER.log(Level.INFO, "Program terminated.");
                user.getConnects().close();
                System.exit(0);
                //</editor-fold>
            }
        };
    }

}

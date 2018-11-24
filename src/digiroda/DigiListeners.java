/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digiroda;

import static digiroda.DigIroda.root;
import static digiroda.DigiController.LOGGER;
import static digiroda.DigiController.user;

import java.util.logging.Level;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;
import static digiroda.DigiController.lP;
import static digiroda.DigiController.cP;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
//import static digiroda.DigiController.menuPane;
/**
 *
 * @author ugros
 */
class DigiListeners {
    
   
    public static ChangeListener menuListener() {
        return new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                    TreeItem selected = (TreeItem) newValue;
                    switch (selected.getValue().toString()) {
                        case "Kapcsolatok":
                            digiroda.DigiController.treeItem1.setExpanded(true);
                            break;
                        case "Exportálás":
                            System.out.println("Az exportálás menüpontot választottad");
                            break;
                        case "Megmutat":
                            //System.out.println("A lista menüpontot választottad");
                            lP.setVisible(false);
                            cP.setVisible(true); 
                            //menuPane.setVisible(false) ;
                            break;
                        case "Naplók":
                            
                            
                            WebView webView = new WebView();
                            final WebEngine webEngine = webView.getEngine();
                            webEngine.load("C:/xxx/L.html");
                            
                            lP.getChildren().add(webView);
                            cP.setVisible(false); 
                            lP.setVisible(true);  
                            break;
                        case "Kilépés":   
                            LOGGER.log(Level.INFO,"Program terminated.");
                            user.getConnects().close();
                            System.exit(0);
                            break;
                    }
                }
            };
    }
    
}

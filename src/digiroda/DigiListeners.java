/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digiroda;

import static digiroda.DigiController.LOGGER;
import static digiroda.DigiController.user;

import java.util.logging.Level;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;

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
                        case "Lista":
                            System.out.println("A lista menüpontot választottad");
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

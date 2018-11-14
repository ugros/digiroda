package digiroda;


import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ugros
 */
public class DigiHandlers {
    
    public static EventHandler familyNOnEditCommit(){
        return new EventHandler<TableColumn.CellEditEvent<DigiContacts,String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<DigiContacts, String> t) {
                ((DigiContacts) t.getTableView().getItems().get(t.getTablePosition().getRow())).setFamilyName(t.getNewValue());                 
            }
        }; 
    }
    
    public static EventHandler firstNOnEditCommit(){
        return new EventHandler<TableColumn.CellEditEvent<DigiContacts,String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<DigiContacts, String> t) {
                ((DigiContacts) t.getTableView().getItems().get(t.getTablePosition().getRow())).setFirstName(t.getNewValue());                 
            }
        };  
    }
    
    public static EventHandler phoneNOnEditCommit(){
        return new EventHandler<TableColumn.CellEditEvent<DigiContacts,String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<DigiContacts, String> t) {
               ((DigiContacts) t.getTableView().getItems().get(t.getTablePosition().getRow())).setPhoneNumber(t.getNewValue());                 
            }
        };   
    }
    
}

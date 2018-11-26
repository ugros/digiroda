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

import static digiroda.DigiController.user;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;

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
                int id=((DigiContacts) t.getTableView().getItems().get(t.getTablePosition().getRow())).getId();
                user.getConnects().setFamilyNameOfContact(id, t.getNewValue());
            }
        }; 
    }
    
    public static EventHandler emailOnEditCommit(){
        return new EventHandler<TableColumn.CellEditEvent<DigiContacts,String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<DigiContacts, String> t) {
                ((DigiContacts) t.getTableView().getItems().get(t.getTablePosition().getRow())).setEmail(t.getNewValue());  
                int id=((DigiContacts) t.getTableView().getItems().get(t.getTablePosition().getRow())).getId();
                user.getConnects().setEmailOfContact(id, t.getNewValue());
            }
        }; 
    }
    
    public static EventHandler firstNOnEditCommit(){
        return new EventHandler<TableColumn.CellEditEvent<DigiContacts,String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<DigiContacts, String> t) {
                ((DigiContacts) t.getTableView().getItems().get(t.getTablePosition().getRow())).setFirstName(t.getNewValue());  
                int id=((DigiContacts) t.getTableView().getItems().get(t.getTablePosition().getRow())).getId();
                user.getConnects().setFirstNameOfContact(id, t.getNewValue());
            }
        };  
    }
    
    public static EventHandler phoneNOnEditCommit(){
        return new EventHandler<TableColumn.CellEditEvent<DigiContacts,String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<DigiContacts, String> t) {
               ((DigiContacts) t.getTableView().getItems().get(t.getTablePosition().getRow())).setPhoneNumber(t.getNewValue()); 
               int id=((DigiContacts) t.getTableView().getItems().get(t.getTablePosition().getRow())).getId();
                user.getConnects().setPhoneNumberOfContact(id, t.getNewValue());
            }
        };   
    }
    
}

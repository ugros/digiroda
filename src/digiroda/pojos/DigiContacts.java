/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digiroda;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author ugros
 */
    public class DigiContacts {

        private SimpleStringProperty familyName;
        private SimpleStringProperty firstName;
        private SimpleStringProperty phoneNumber;
        private SimpleStringProperty email;

        public DigiContacts() {
            this.familyName = new SimpleStringProperty("");
            this.firstName = new SimpleStringProperty("");
            this.phoneNumber = new SimpleStringProperty("");
            this.email = new SimpleStringProperty("");
        }

        public DigiContacts(String familyName, String firstName, String phoneNumber, String email) {
            this.familyName = new SimpleStringProperty(familyName);
            this.firstName = new SimpleStringProperty(firstName);
            this.phoneNumber = new SimpleStringProperty(phoneNumber);
            this.email = new SimpleStringProperty(email);
        }

        public String getFamilyName() {
            return familyName.getValue();
        }

        public String getFirstName() {
            return firstName.getValue();
        }

        public String getPhoneNumber() {
            return phoneNumber.getValue();
        }

        public void setFamilyName(String familyName) {
            this.familyName = new SimpleStringProperty(familyName);
        }

        public void setFirstName(String firstName) {
            this.firstName = new SimpleStringProperty(firstName);
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = new SimpleStringProperty(phoneNumber);
        }

        public String getEmail() {
            return email.getValue();
        }
        
        public void setEmail(String email) {
            this.email = new SimpleStringProperty(email);
        }
        
        
    }

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

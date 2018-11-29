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

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author ugros
 */
public class DigiContacts {

    private SimpleStringProperty companyName;
    private SimpleStringProperty familyName;
    private SimpleStringProperty firstName;
    private SimpleStringProperty phoneNumber;
    private SimpleStringProperty country;
    private SimpleStringProperty city;
    private SimpleStringProperty postalCode;
    private SimpleStringProperty adress;
    private SimpleStringProperty email;
    private int id;

    public DigiContacts() {
        this.familyName = new SimpleStringProperty("");
        this.firstName = new SimpleStringProperty("");
        this.phoneNumber = new SimpleStringProperty("");
        this.email = new SimpleStringProperty("");
        this.companyName = new SimpleStringProperty("");
        this.country = new SimpleStringProperty("");
        this.city = new SimpleStringProperty("");
        this.postalCode = new SimpleStringProperty("");
        this.adress = new SimpleStringProperty("");
        this.id = 0;
    }

    public DigiContacts(int id, String companyName, String familyName, String firstName, String phoneNumber, String email, String country, String city, String postalCode, String adress) {
        this.familyName = new SimpleStringProperty(familyName);
        this.firstName = new SimpleStringProperty(firstName);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.email = new SimpleStringProperty(email);
        this.companyName = new SimpleStringProperty(companyName);
        this.country = new SimpleStringProperty(country);
        this.city = new SimpleStringProperty(city);
        this.postalCode = new SimpleStringProperty(postalCode);
        this.adress = new SimpleStringProperty(adress);
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public String getCompanyName() {
        return companyName.getValue();
    }

    public String getCountry() {
        return country.getValue();
    }

    public String getCity() {
        return city.getValue();
    }

    public String getPostalCode() {
        return postalCode.getValue();
    }

    public String getAdress() {
        return adress.getValue();
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

    public void setCompanyName(String newValue) {
        this.companyName = new SimpleStringProperty(newValue);
    }

    public void setCity(String newValue) {
        this.city = new SimpleStringProperty(newValue);
    }

    public void setCountry(String newValue) {
        this.country = new SimpleStringProperty(newValue);
    }

    public void setPostalCode(String newValue) {
        this.postalCode = new SimpleStringProperty(newValue);
    }

    public void setAdress(String newValue) {
        this.adress = new SimpleStringProperty(newValue);
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

package com.risjavafx.pages.referrals;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

@SuppressWarnings("unused")
public class ReferralData {

    SimpleIntegerProperty patientIdData;
    SimpleStringProperty dateOfBirthData;
    SimpleStringProperty lastNameData;
    SimpleStringProperty firstNameData;

    public ReferralData(int patientId, String dateOfBirth, String lastName, String firstName) {
        this.patientIdData = new SimpleIntegerProperty(patientId);
        this.dateOfBirthData = new SimpleStringProperty(dateOfBirth);
        this.lastNameData = new SimpleStringProperty(lastName);
        this.firstNameData = new SimpleStringProperty(firstName);
    }

    public int getPatientIdData() {
        return patientIdData.get();
    }

    public void setPatientIdData(int patientId) {
        this.patientIdData.set(patientId);
    }

    public String getDateOfBirthData() {
        return dateOfBirthData.get();
    }

    public void setDateOfBirthData(String dateOfBirth) {
        this.dateOfBirthData.set(dateOfBirth);
    }

    public String getLastNameData() {
        return lastNameData.get();
    }

    public void setLastNameData(String lastNameData) {
        this.lastNameData.set(lastNameData);
    }

    public String getFirstNameData() {
        return firstNameData.get();
    }

    public void setFirstNameData(String firstNameData) {
        this.firstNameData.set(firstNameData);
    }
}

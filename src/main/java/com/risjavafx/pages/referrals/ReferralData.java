package com.risjavafx.pages.referrals;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

@SuppressWarnings("unused")
public class ReferralData {

    SimpleIntegerProperty patientIdData;
    SimpleStringProperty dateOfBirthData;
    SimpleStringProperty lastNameData;
    SimpleStringProperty firstNameData;
    SimpleStringProperty sexData;
    SimpleStringProperty raceData;
    SimpleStringProperty ethnicityData;

    public ReferralData(int patientId, String dateOfBirth, String lastName, String firstName, String sex, String race, String ethnicity) {
        this.patientIdData = new SimpleIntegerProperty(patientId);
        this.dateOfBirthData = new SimpleStringProperty(dateOfBirth);
        this.lastNameData = new SimpleStringProperty(lastName);
        this.firstNameData = new SimpleStringProperty(firstName);
        this.sexData = new SimpleStringProperty(sex);
        this.raceData = new SimpleStringProperty(race);
        this.ethnicityData = new SimpleStringProperty(ethnicity);
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

    public String getSexData() {
        return sexData.get();
    }

    public void setSexData(String sexData) {
        this.sexData.set(sexData);
    }

    public String getRaceData() {
        return raceData.get();
    }

    public void setRaceData(String raceData) {
        this.raceData.set(raceData);
    }

    public String getEthnicityData() {
        return ethnicityData.get();
    }

    public void setEthnicityData(String ethnicityData) {
        this.ethnicityData.set(ethnicityData);
    }
}

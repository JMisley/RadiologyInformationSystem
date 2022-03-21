package com.risjavafx.pages.appointments;

import javafx.beans.property.SimpleStringProperty;
@SuppressWarnings("unused")
public class AppointmentData {
    SimpleStringProperty patient;
    SimpleStringProperty modality;
    SimpleStringProperty price;
    SimpleStringProperty date;
    SimpleStringProperty radiologist;
    SimpleStringProperty technician;
    SimpleStringProperty closedFlag;

    public AppointmentData(String patient, String modality, String price, String date, String radiologist, String technician, String closedFlag) {
        this.patient = new SimpleStringProperty(patient);
        this.modality = new SimpleStringProperty(modality);
        this.price = new SimpleStringProperty(price);
        this.date = new SimpleStringProperty(date);
        this.radiologist = new SimpleStringProperty(radiologist);
        this.technician = new SimpleStringProperty(technician);
        this.closedFlag = new SimpleStringProperty(closedFlag);
    }

    public String getPatient() {
        return patient.get();
    }

    public SimpleStringProperty patientProperty() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient.set(patient);
    }

    public String getModality() {
        return modality.get();
    }


    public SimpleStringProperty modalityProperty() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality.set(modality);
    }

    public String getPrice() {
        return price.get();
    }

    public SimpleStringProperty priceProperty() {
        return price;
    }

    public void setPrice(String price) {
        this.price.set(price);
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getRadiologist() {
        return radiologist.get();
    }

    public SimpleStringProperty radiologistProperty() {
        return radiologist;
    }

    public void setRadiologist(String radiologist) {
        this.radiologist.set(radiologist);
    }

    public String getTechnician() {
        return technician.get();
    }

    public SimpleStringProperty technicianProperty() {
        return technician;
    }

    public void setTechnician(String technician) {
        this.technician.set(technician);
    }

    public String getClosedFlag() {
        return closedFlag.get();
    }

    public SimpleStringProperty closedFlagProperty() {
        return closedFlag;
    }

    public void setClosedFlag(String closedFlag) {
        this.closedFlag.set(closedFlag);
    }
}

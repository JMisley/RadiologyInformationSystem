package com.risjavafx.pages.appointments;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
@SuppressWarnings("unused")
public class AppointmentData {

    SimpleIntegerProperty patientId;
    SimpleIntegerProperty appointmentId;
    SimpleStringProperty patient;
    SimpleStringProperty modality;
    SimpleStringProperty price;
    SimpleStringProperty date;
    SimpleStringProperty radiologist;
    SimpleStringProperty technician;
    SimpleStringProperty closedFlag;

    public SimpleIntegerProperty patientIdProperty() {
        return patientId;
    }

    public AppointmentData(int appointmentId, String patient, String modality, String price, String dateTime, String radiologist, String technician, String closedFlag) {
        this.appointmentId = new SimpleIntegerProperty(appointmentId);
        this.patient = new SimpleStringProperty(patient);
        this.modality = new SimpleStringProperty(modality);
        this.price = new SimpleStringProperty(price);
        this.date = new SimpleStringProperty(dateTime);
        this.radiologist = new SimpleStringProperty(radiologist);
        this.technician = new SimpleStringProperty(technician);
        this.closedFlag = new SimpleStringProperty(closedFlag);
    }

    public int getPatientId() {
        return patientId.get();
    }

    public void setPatientId(int patientId) {
        this.patientId.set(patientId);
    }

    public int getAppointmentId() {
        return appointmentId.get();
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId.set(appointmentId);
    }

    public String getPatient() {
        return patient.get();
    }

    public void setPatient(String patient) {
        this.patient.set(patient);
    }

    public String getModality() {
        return modality.get();
    }

    public void setModality(String modality) {
        this.modality.set(modality);
    }

    public String getPrice() {
        return price.get();
    }

    public void setPrice(String price) {
        this.price.set(price);
    }

    public String getDate() {
        return date.get();
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getRadiologist() {
        return radiologist.get();
    }

    public void setRadiologist(String radiologist) {
        this.radiologist.set(radiologist);
    }

    public String getTechnician() {
        return technician.get();
    }

    public void setTechnician(String technician) {
        this.technician.set(technician);
    }

    public String getClosedFlag() {
        return closedFlag.get();
    }

    public void setClosedFlag(String closedFlag) {
        this.closedFlag.set(closedFlag);
    }
}
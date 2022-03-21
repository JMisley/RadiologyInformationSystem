package com.risjavafx.pages.orders;

import javafx.beans.property.SimpleStringProperty;


public class OrdersData {
    SimpleStringProperty orderIdData;
    SimpleStringProperty patientData;
    SimpleStringProperty referralMdData;
    SimpleStringProperty modalityData;
    SimpleStringProperty appointmentData;
    SimpleStringProperty notesData;
    SimpleStringProperty statusData;
    SimpleStringProperty reportData;

    public OrdersData(String orderId, String patient, String referralMd, String modality, String appointment, String notes, String status, String report) {
        this.orderIdData = new SimpleStringProperty(orderId);
        this.patientData = new SimpleStringProperty(patient);
        this.referralMdData = new SimpleStringProperty(referralMd);
        this.modalityData = new SimpleStringProperty(modality);
        this.appointmentData = new SimpleStringProperty(appointment);
        this.notesData = new SimpleStringProperty(notes);
        this.statusData = new SimpleStringProperty(status);
        this.reportData = new SimpleStringProperty(report);
    }

    public String getOrderIdData() {return orderIdData.get();}

    public void setOrderIdData(String ORDERID) {this.orderIdData.set(ORDERID);}

    public String getPatientData() {
        return patientData.get();
    }

    public void setPatientData(String patientData) {
        this.patientData.set(patientData);
    }

    public String getReferralMdDataData() {
        return referralMdData.get();
    }

    public void setReferralMdData(String referralMdData) {
        this.referralMdData.set(referralMdData);
    }

    public String getModalityData() {
        return modalityData.get();
    }

    public void setModalityData(String modalityData) {
        this.modalityData.set(modalityData);
    }

    public String getNotesData() {
        return notesData.get();
    }

    public void setNotesData(String notesData) {
        this.notesData.set(notesData);
    }

    public String getStatusData() {
        return statusData.get();
    }

    public void setStatusData(String statusData) {
        this.statusData.set(statusData);
    }

    public String getReportData() {
        return reportData.get();
    }

    public void setReportData(String reportData) {
        this.reportData.set(reportData);
    }
}

package com.risjavafx;

import javafx.beans.property.SimpleStringProperty;


public class OrdersData {
    SimpleStringProperty orderIdData;
    SimpleStringProperty patientData;
    SimpleStringProperty referralMdData;
//    SimpleStringProperty modalityData;
//    SimpleStringProperty appointmentData;
//    SimpleStringProperty notesData;
//    SimpleStringProperty statusData;
//    SimpleStringProperty reportData;

    public OrdersData(String orderId, String patient, String referralMd /*String modality, String appointment, String notes, String status, String */) {
        this.orderIdData = new SimpleStringProperty(orderId);
        this.patientData = new SimpleStringProperty(patient);
        this.referralMdData = new SimpleStringProperty(referralMd);
//        this.modalityData = new SimpleStringProperty(modality);
//        this.appointmentData = new SimpleStringProperty(appointment);
//        this.notesData = new SimpleStringProperty(notes);
//        this.statusData = new SimpleStringProperty(status);
//        this.reportData = new SimpleStringProperty(report);
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

//    public String getEmailAddressData() {
//        return emailAddressData.get();
//    }
//
//    public void setEmailAddressData(String emailAddressData) {
//        this.emailAddressData.set(emailAddressData);
//    }
//
//    public String getSystemRoleData() {
//        return systemRoleData.get();
//    }
//
//    public void setSystemRoleData(String systemRoleData) {
//        this.systemRoleData.set(systemRoleData);
//    }
}

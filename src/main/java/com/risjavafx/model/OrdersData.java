package com.risjavafx.model;

import com.risjavafx.controller.TableSearchBar;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class OrdersData {
    SimpleIntegerProperty orderIdData;
    SimpleStringProperty displayNameData;
    SimpleIntegerProperty patientIdData;
    SimpleIntegerProperty docIdData;
    SimpleIntegerProperty modalityData;
    SimpleStringProperty notesData;

    TableSearchBar tableSearchBar = new TableSearchBar();

    public OrdersData(int orderId, String displayName, int patientId, int docId, int modality, String notes) {
        this.orderIdData = new SimpleIntegerProperty(orderId);
        this.displayNameData = new SimpleStringProperty(displayName);
        this.patientIdData = new SimpleIntegerProperty(patientId);
        this.docIdData = new SimpleIntegerProperty(docId);
        this.modalityData = new SimpleIntegerProperty(modality);
        this.notesData = new SimpleStringProperty(notes);
    }
    public int getOrderIdData() {return orderIdData.get();}

    public void setOrderIdData(int orderId) {this.orderIdData.set(orderId);}

    public String getDisplayNameData() {
        return displayNameData.get();
    }

    public void setDisplayNameData(String displayNameData) {
        this.displayNameData.set(displayNameData);
    }

    public int getPatientIdData() {
        return patientIdData.get();
    }

    public void setPatientIdData(int patientIdData) {
        this.patientIdData.set(patientIdData);
    }

    public int getDocIdData() {
        return docIdData.get();
    }

    public void setDocId(int docIdData) {
        this.docIdData.set(docIdData);
    }

    public int getModalityData() {
        return modalityData.get();
    }

    public void setModalityData(int modalityData) {
        this.modalityData.set(modalityData);
    }

    public String getNotesData() {
        return notesData.get();
    }

    public void setNotesData(String notesData) {
        this.notesData.set(notesData);
    }
}

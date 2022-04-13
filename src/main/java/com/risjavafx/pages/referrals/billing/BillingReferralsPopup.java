package com.risjavafx.pages.referrals.billing;

import com.risjavafx.Driver;
import com.risjavafx.Miscellaneous;
import com.risjavafx.PromptButtonCell;
import com.risjavafx.pages.PageManager;
import com.risjavafx.popups.PopupManager;
import com.risjavafx.popups.Popups;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class BillingReferralsPopup implements Initializable {
    public VBox popupContainer;
    public Label insuranceBillLabel;
    public Label patientBillLabel;
    public Label totalBillLabel;
    public Button closeButton;
    public ComboBox<String> insuranceComboBox;

    private static int patientId;
    private double totalCost;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resizeElements();
        populateComboBox();

        Popups.BILLING.getPopup().showingProperty().addListener((observableValue, aBoolean, t1) -> {
            PageManager.getRoot().setDisable(!aBoolean);
            refreshElements();
            totalCost = queryTotalCost();
            insuranceComboBox.valueProperty().addListener(observable -> calculatePatientBill());
        });
    }

    private double queryTotalCost() {
        int totalCost = 0;
        try {
            Driver driver = new Driver();
            String sql = """
                    SELECT price
                    FROM modalities, appointments
                    WHERE appointments.patient = ? AND appointments.modality = modality_id
                    """;
            PreparedStatement preparedStatement = driver.connection.prepareStatement(sql);
            preparedStatement.setInt(1, patientId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                totalCost += resultSet.getInt("price");
            }
        } catch (
                Exception exception) {
            exception.printStackTrace();
        }
        return totalCost;
    }

    private double calculateTotalBill() {
        double tax = .07;
        double bill = totalCost * (1 + tax);
        double roundedBill = Math.round(bill * 100.00) / 100.00;
        totalBillLabel.setText(String.valueOf(roundedBill));
        return roundedBill;
    }

    private double[] calculateInsuranceBill() {
        double insurancePercent = 0;
        for (InsuranceCompanies insuranceCompany : InsuranceCompanies.values()) {
            if (insuranceCompany.toString().equals(insuranceComboBox.getValue())) {
                insurancePercent = insuranceCompany.getPercent();
            }
        }

        double totalCost = calculateTotalBill();
        double insuranceCoverage = totalCost * insurancePercent;
        double roundedBill = Math.round(insuranceCoverage * 100.00) / 100.00;
        insuranceBillLabel.setText(String.valueOf(roundedBill));
        return new double[]{totalCost, roundedBill};
    }

    private void calculatePatientBill() {
        double[] costs = calculateInsuranceBill();
        double bill = costs[0] - costs[1];
        double roundedBill = Math.round(bill * 100.00) / 100.00;
        patientBillLabel.setText(String.valueOf(roundedBill));
    }

    private void populateComboBox() {
        ObservableList<String> insurance = FXCollections.observableArrayList();
        insurance.add("None");
        for (InsuranceCompanies insuranceCompany : InsuranceCompanies.values()) {
            insurance.add(insuranceCompany.toString());
        }
        insuranceComboBox.setItems(insurance);
    }

    private void resizeElements() {
        Miscellaneous misc = new Miscellaneous();
        popupContainer.setPrefHeight(Popups.getMenuDimensions()[0]);
        popupContainer.setPrefWidth(Popups.getMenuDimensions()[1]);
        popupContainer.setMaxHeight(Popups.getMenuDimensions()[0]);
        popupContainer.setMaxWidth(Popups.getMenuDimensions()[1]);

        closeButton.setPrefHeight(misc.getScreenWidth() * .033);
        closeButton.setPrefWidth(misc.getScreenWidth() * .11);
        closeButton.setStyle("-fx-font-size: 16px");

        insuranceComboBox.setButtonCell(new PromptButtonCell<>("Insurance"));
    }

    private void refreshElements() {
        insuranceComboBox.getSelectionModel().clearSelection();
        patientBillLabel.setText("");
        insuranceBillLabel.setText("");
        totalBillLabel.setText("");
    }

    public static void setPatientId(int patientId) {
        BillingReferralsPopup.patientId = patientId;
    }

    // Button OnClicks
    public void closePopup() {
        PopupManager.removePopup("MENU");
    }
}

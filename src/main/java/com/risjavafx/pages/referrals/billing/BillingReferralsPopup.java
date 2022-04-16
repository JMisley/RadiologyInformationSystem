package com.risjavafx.pages.referrals.billing;

import com.risjavafx.Driver;
import com.risjavafx.Miscellaneous;
import com.risjavafx.PromptButtonCell;
import com.risjavafx.components.InfoTable;
import com.risjavafx.popups.PopupManager;
import com.risjavafx.popups.Popups;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BillingReferralsPopup implements Initializable {
    public VBox popupContainer;
    public Button closeButton;
    public ComboBox<String> insuranceComboBox;
    public StackPane tableViewContainer;
    public Label subtotalLabel;
    public Label taxLabel;
    public Label totalBillLabel;
    public Label insuranceBillLabel;
    public Label patientBillLabel;

    private static int patientId;
    private double subtotal;
    private double tax;
    private double totalBill;
    private double insuranceBill;
    private double patientBill;

    private final ObservableList<BillingUtils.BillingData> observableList = FXCollections.observableArrayList();
    private final TableColumn<BillingUtils.BillingData, String>
            modalityColumn = new TableColumn<>("Modality"),
            dateTimeColumn = new TableColumn<>("Date"),
            priceColumn = new TableColumn<>("Price");
    private final ArrayList<TableColumn<BillingUtils.BillingData, String>> tableColumnList = new ArrayList<>() {{
        add(modalityColumn);
        add(dateTimeColumn);
        add(priceColumn);
    }};

    InfoTable<BillingUtils.BillingData, String> infoTable = new InfoTable<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resizeElements();
        populateComboBox();
        createTable();

        Popups.BILLING.getPopup().showingProperty().addListener((observableValue, aBoolean, t1) -> {
            observableList.clear();
            refreshElements();
            queryTotalCost();

            insuranceComboBox.valueProperty().addListener(observable -> {
                if (insuranceComboBox.getSelectionModel().getSelectedItem() != null)
                    setCostLabels();
            });
        });
    }

    private void createTable() {
        try {
            setCellFactoryValues();

            infoTable.setColumns(tableColumnList);
            infoTable.addColumnsToTable();
            infoTable.tableView.setId("smallTableContent");

            tableViewContainer.getChildren().add(infoTable.tableView);

            infoTable.setCustomColumnWidth(modalityColumn, .3);
            infoTable.setCustomColumnWidth(dateTimeColumn, .4);
            infoTable.setCustomColumnWidth(priceColumn, .3);

            infoTable.tableView.setItems(observableList);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void setCellFactoryValues() {
        modalityColumn.setCellValueFactory(new PropertyValueFactory<>("modalityData"));
        dateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dateTimeData"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("priceData"));
    }

    private void queryTotalCost() {
        try {
            String sql = """
                    SELECT name, date_time, price
                    FROM modalities, appointments
                    WHERE appointments.patient = ?
                        AND appointments.modality = modality_id
                        AND appointments.closed = 0
                    """;
            PreparedStatement preparedStatement = Driver.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, patientId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                observableList.add(new BillingUtils.BillingData(
                        resultSet.getString("name"),
                        resultSet.getString("date_time"),
                        resultSet.getInt("price")
                ));
            }
        } catch (
                Exception exception) {
            exception.printStackTrace();
        }
    }

    private void setCostLabels() {
        runCalculations();
        subtotalLabel.setText(String.valueOf(subtotal));
        taxLabel.setText(String.valueOf(Math.round(tax * 100.0) / 100.0));
        totalBillLabel.setText("$" + Math.round(totalBill * 100.0) / 100.0);
        insuranceBillLabel.setText("$" + Math.round(insuranceBill * 100.0) / 100.0);
        patientBillLabel.setText("$" + Math.round(patientBill * 100.0) / 100.0);
    }

    private void runCalculations() {
        subtotal = calculateSubtotal();
        tax = calculateTax();
        totalBill = calculateTotalBill();
        insuranceBill = calculateInsuranceBill();
        patientBill = calculatePatientBill();
    }

    private double calculateSubtotal() {
        double subtotal = 0;
        for (BillingUtils.BillingData billingData : observableList)
            subtotal += billingData.priceData.get();
        return subtotal;
    }

    private double calculateTax() {
        return subtotal * .07;
    }

    private double calculateTotalBill() {
        return subtotal + tax;
    }

    private double calculateInsuranceBill() {
        double insurancePercentage = 0;
        for (BillingUtils.InsuranceCompanies insuranceCompany : BillingUtils.InsuranceCompanies.values()) {
            if (insuranceCompany.toString().equals(insuranceComboBox.getSelectionModel().getSelectedItem()))
                insurancePercentage = insuranceCompany.getPercent();
        }
        return totalBill * insurancePercentage;
    }

    private double calculatePatientBill() {
        return totalBill - insuranceBill;
    }

    private void populateComboBox() {
        ObservableList<String> insurance = FXCollections.observableArrayList();
        insurance.add("None");
        for (BillingUtils.InsuranceCompanies insuranceCompany : BillingUtils.InsuranceCompanies.values()) {
            insurance.add(insuranceCompany.toString());
        }
        insuranceComboBox.setItems(insurance);
    }

    private void resizeElements() {
        Miscellaneous misc = new Miscellaneous();
        popupContainer.setPrefHeight(Popups.getLargeMenuDimensions()[0]);
        popupContainer.setPrefWidth(Popups.getLargeMenuDimensions()[1]);
        popupContainer.setMaxHeight(Popups.getLargeMenuDimensions()[0]);
        popupContainer.setMaxWidth(Popups.getLargeMenuDimensions()[1]);

        closeButton.setPrefHeight(misc.getScreenWidth() * .033);
        closeButton.setPrefWidth(misc.getScreenWidth() * .11);
        closeButton.setStyle("-fx-font-size: 16px");

        insuranceComboBox.setButtonCell(new PromptButtonCell<>("Insurance"));
    }

    private void refreshElements() {
        insuranceComboBox.getSelectionModel().clearSelection();
        subtotalLabel.setText("");
        taxLabel.setText("");
        totalBillLabel.setText("");
        insuranceBillLabel.setText("");
        patientBillLabel.setText("");
    }

    public static void setPatientId(int patientId) {
        BillingReferralsPopup.patientId = patientId;
    }

    // Button OnClicks
    public void closePopup() {
        PopupManager.removePopup();
    }
}

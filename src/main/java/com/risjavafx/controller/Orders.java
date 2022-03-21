package com.risjavafx.controller;

import com.risjavafx.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Orders implements Initializable {

    public HBox topContent;
    public BorderPane mainContainer;
    public HBox titleBar, tableSearchBar;
    public StackPane centerContent;
    public SplitPane centerContentContainer;
    public static SortedList<OrdersData> sortedList;
    public static FilteredList<OrdersData> filteredList;
    public static ObservableList<OrdersData> observableList = FXCollections.observableArrayList();

    public TableColumn<OrdersData, String>
            orderId = new TableColumn<>("Order ID"),
            displayName = new TableColumn<>("Full Name"),
            patientId = new TableColumn<>("Patient ID"),
            docId = new TableColumn<>("Referral Doctor"),
            modality = new TableColumn<>("Modality"),
            notes = new TableColumn<>("Notes");

    public ArrayList<TableColumn<OrdersData, String>> tableColumnsList = new ArrayList<>() {
        {
            add(orderId);
            add(displayName);
            add(patientId);
            add(docId);
            add(modality);
            add(notes);
        }};

    public static InfoTable<OrdersData, String> infoTable = new InfoTable<>();
    Miscellaneous misc = new Miscellaneous();

    // Load NavigationBar component into home-page.fxml
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Pages.setPage(Pages.ORDERS);
        TitleBar.createTitleBar(mainContainer, titleBar, this.getClass());
        NavigationBar.createNavBar(topContent, this.getClass());
        TableSearchBar.createSearchBar(tableSearchBar, this.getClass());
        setComboBoxItems();
        createTable();
        filterData();

    }
    public void createTable() {
        try {
            setCellFactoryValues();

            infoTable.setColumns(tableColumnsList);
            infoTable.addColumnsToTable();

            infoTable.setCustomColumnWidth(orderId, .1);
            infoTable.setCustomColumnWidth(displayName, .1);
            infoTable.setCustomColumnWidth(patientId, .1);
            infoTable.setCustomColumnWidth(docId, .1);
            infoTable.setCustomColumnWidth(modality, .1);
            infoTable.setCustomColumnWidth(notes, .1);

            centerContentContainer.setMaxWidth(misc.getScreenWidth() * .85);
            centerContentContainer.setMaxHeight(misc.getScreenHeight() * .85);
            centerContent.getChildren().add(infoTable.tableView);

            queryData(getAllDataStringQuery());
            infoTable.tableView.setItems(observableList);
            infoTable.tableView.refresh();
        } catch (
                Exception exception) {
            exception.printStackTrace();
        }

    }

    public static void queryData(String sql) throws SQLException {
        Driver driver = new Driver();
        ResultSet resultSet = driver.connection.createStatement().executeQuery(sql);

        while (resultSet.next()) {
            observableList.add(new OrdersData(
                    resultSet.getInt("order_id"),
                    resultSet.getString("full_name"),
                    resultSet.getInt("patient"),
                    resultSet.getInt("referral_md"),
                    resultSet.getInt("modality"),
                    resultSet.getString("notes")

            ));
        }
    }

    public String getAllDataStringQuery() {
        return """
                SELECT *
                FROM orders;
                
                """;
    }

    public static String getLastRowStringQuery() {
        return """
                SELECT *
                FROM users, roles INNER JOIN users_roles
                ON roles.role_id = users_roles.role_id
                WHERE users.user_id = users_roles.user_id
                ORDER BY users.user_id DESC LIMIT 1;
                """;
    }

    public void setCellFactoryValues() {
        orderId.setCellValueFactory(new PropertyValueFactory<>("orderIdData"));
        displayName.setCellValueFactory(new PropertyValueFactory<>("displayNameData"));
        patientId.setCellValueFactory(new PropertyValueFactory<>("patientIdData"));
        docId.setCellValueFactory(new PropertyValueFactory<>("docIdData"));
        modality.setCellValueFactory(new PropertyValueFactory<>("modalityData"));
        notes.setCellValueFactory(new PropertyValueFactory<>("notesData"));
    }

    public static void filterData() {
        try {
            filteredList = new FilteredList<>(observableList);

            TableSearchBar.usableTextField.textProperty().addListener((observable, oldValue, newValue) -> filteredList.setPredicate(orderData -> {
                System.out.println("listening");
                if (TableSearchBar.usableComboBox.getValue() == null) {
                    TableSearchBar.usableErrorLabel.setText("Please select a filter");
                } else {
                    TableSearchBar.usableErrorLabel.setText(null);
                }

                if (newValue.isEmpty() || newValue.isBlank()) {
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();
                int searchKeyInt = -1;
                try {
                    searchKeyInt = Integer.parseInt(newValue);
                } catch (Exception ignored) {
                }

                if (orderData.getOrderIdData() == searchKeyInt && getComboBoxItem("Order ID")) {
                    return true;
                } else if (orderData.getDisplayNameData().toLowerCase().contains(searchKeyword) && getComboBoxItem("Full name")) {
                    return true;
                } else if (orderData.getPatientIdData() == searchKeyInt && getComboBoxItem("Username")) {
                    return true;
                } else if (orderData.getDocIdData() == searchKeyInt && getComboBoxItem ("Email address")) {
                    return true;
                } else if (orderData.getModalityData() == searchKeyInt && getComboBoxItem("dgdg"))
                    return true;
                    return orderData.getNotesData().toLowerCase().contains(searchKeyword) && getComboBoxItem("System role");
            }));

            sortedList = new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(infoTable.tableView.comparatorProperty());
            infoTable.tableView.setItems(sortedList);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void setComboBoxItems() {
        ObservableList<String> oblist = FXCollections.observableArrayList(
                "All",
                "Order ID",
                "Full name",
                "Patient ID",
                "Referral Doctor",
                "Modality",
                "Notes"
        );
        TableSearchBar.usableComboBox.setItems(oblist);
    }

    public static boolean getComboBoxItem(String string) {
        String selectedComboValue = TableSearchBar.usableComboBox.getValue();
        return string.equals(selectedComboValue) || "All".equals(selectedComboValue);
    }
        }

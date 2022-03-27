package com.risjavafx.controller;

import com.risjavafx.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;


import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Admin implements Initializable {

    public BorderPane mainContainer;
    public HBox topContent, titleBar, tableSearchBar;
    //public VBox imgVBox;
    public StackPane centerContent;
    public SplitPane centerContentContainer;
    public static SortedList<AdminData> sortedList;
    public static FilteredList<AdminData> filteredList;
    public static ObservableList<AdminData> observableList = FXCollections.observableArrayList();



    public TableColumn<AdminData, String>
            userId = new TableColumn<>("User ID"),
            displayName = new TableColumn<>("Full Name"),
            username = new TableColumn<>("Username"),
            emailAdr = new TableColumn<>("Email Address"),
            systemRole = new TableColumn<>("System Role");


    public ArrayList<TableColumn<AdminData, String>> tableColumnsList = new ArrayList<>() {{
        add(userId);
        add(displayName);
        add(username);
        add(emailAdr);
        add(systemRole);

    }};

    public static InfoTable<AdminData, String> infoTable = new InfoTable<>();
    Miscellaneous misc = new Miscellaneous();





    public void initialize(URL url, ResourceBundle resourceBundle) {
        Pages.setPage(Pages.ADMIN);
        TitleBar.createTitleBar(mainContainer, titleBar, this.getClass());
        NavigationBar.createNavBar(topContent, this.getClass());
        TableSearchBar.createSearchBar(tableSearchBar, this.getClass());
        setComboBoxItems();
        createTable();
        filterData();
        //uploadImages();



    }

    //public void uploadImages() {
        //primaryStage.setTitle("upload new image");
        //FileChooser fileChooser = new FileChooser();
        //VBox imgBox = new VBox(imgVBox);
        //Button fileButton = new Button("Select File");
        //imgBox.getChildren().add(fileButton);
        //imgBox.setAlignment(Pos.CENTER);
       // fileButton.setOnAction(e -> {
            //File selectedFile = fileChooser.showOpenDialog(new Stage());
       // });

        //VBox imgVBox = new VBox(fileButton);
        //Scene imgscene = new Scene(imgBox, 250, 250);
        //imgBox.setAlignment(Pos.CENTER);

        //primaryStage.setScene(imgscene);
        //primaryStage.show();


    //}




    public void createTable() {
        try {
            setCellFactoryValues();

            infoTable.setColumns(tableColumnsList);
            infoTable.addColumnsToTable();

            infoTable.setCustomColumnWidth(userId, .12);
            infoTable.setCustomColumnWidth(displayName, .2);
            infoTable.setCustomColumnWidth(username, .18);
            infoTable.setCustomColumnWidth(emailAdr, .3);
            infoTable.setCustomColumnWidth(systemRole, .2);


            centerContentContainer.setMaxWidth(misc.getScreenWidth() * .85);
            centerContentContainer.setMaxHeight(misc.getScreenHeight() * .85);
            centerContent.getChildren().add(infoTable.tableView);
            //centerContent.getChildren().add(imgVBox);



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
            observableList.add(new AdminData(
                    resultSet.getInt("user_id"),
                    resultSet.getString("full_name"),
                    resultSet.getString("username"),
                    resultSet.getString("email"),
                    resultSet.getString("name")
            ));
        }
    }

    public String getAllDataStringQuery() {
        return """
                SELECT *
                FROM users, roles INNER JOIN users_roles
                ON roles.role_id = users_roles.role_id
                WHERE users.user_id = users_roles.user_id
                ORDER BY users.user_id;
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
        userId.setCellValueFactory(new PropertyValueFactory<>("userIdData"));
        displayName.setCellValueFactory(new PropertyValueFactory<>("displayNameData"));
        username.setCellValueFactory(new PropertyValueFactory<>("usernameData"));
        emailAdr.setCellValueFactory(new PropertyValueFactory<>("emailAddressData"));
        systemRole.setCellValueFactory(new PropertyValueFactory<>("systemRoleData"));
    }

    public static void filterData() {
        try {
            filteredList = new FilteredList<>(observableList);

            TableSearchBar.usableTextField.textProperty().addListener((observable, oldValue, newValue) -> filteredList.setPredicate(adminData -> {
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

                if (adminData.getUserIdData() == searchKeyInt && getComboBoxItem("User ID")) {
                    return true;
                } else if (adminData.getDisplayNameData().toLowerCase().contains(searchKeyword) && getComboBoxItem("Full name")) {
                    return true;
                } else if (adminData.getUsernameData().toLowerCase().contains(searchKeyword) && getComboBoxItem("Username")) {
                    return true;
                } else if (adminData.getEmailAddressData().toLowerCase().contains(searchKeyword) && getComboBoxItem("Email address")) {
                    return true;
                } else
                    return adminData.getSystemRoleData().toLowerCase().contains(searchKeyword) && getComboBoxItem("System role");
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
                "User ID",
                "Full name",
                "Username",
                "Email address",
                "System role"
        );
        TableSearchBar.usableComboBox.setItems(oblist);
    }

    public static boolean getComboBoxItem(String string) {
        String selectedComboValue = TableSearchBar.usableComboBox.getValue();
        return string.equals(selectedComboValue) || "All".equals(selectedComboValue);
    }


}
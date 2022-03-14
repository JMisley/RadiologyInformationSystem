package com.risjavafx.controller;

import com.risjavafx.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Admin implements Initializable {

    public BorderPane mainContainer;
    public HBox topContent, titleBar, tableSearchBar;
    public StackPane centerContent;
    public SplitPane centerContentContainer;
    public FilteredList<AdminData> filteredList;
    public SortedList<AdminData> sortedList;

    public TableColumn<AdminData, String>
            userId = new TableColumn<>("User ID"),
            username = new TableColumn<>("Username"),
            displayName = new TableColumn<>("Full Name"),
            emailAdr = new TableColumn<>("Email Address"),
            systemRole = new TableColumn<>("System Role");

    public ArrayList<TableColumn<AdminData, String>> tableColumnsList = new ArrayList<>() {{
        add(userId);
        add(username);
        add(displayName);
        add(emailAdr);
        add(systemRole);
    }};

    InfoTable<AdminData, String> infoTable = new InfoTable<>();

    public void initialize(URL url, ResourceBundle resourceBundle) {
        Pages.setPage(Pages.ADMIN);
        TitleBar.createTitleBar(mainContainer, titleBar, this.getClass());
        NavigationBar.createNavBar(topContent, this.getClass());
        TableSearchBar.createSearchBar(tableSearchBar, this.getClass());
        setComboBoxItems();
        createTable();
        filterData();
    }

    public void createTable() {
        try {
            Miscellaneous misc = new Miscellaneous();

            queryData();
            setCellFactoryValues();

            infoTable.setColumns(tableColumnsList);
            infoTable.addColumnsToTable();

            infoTable.setCustomColumnWidth(userId, .12);
            infoTable.setCustomColumnWidth(username, .18);
            infoTable.setCustomColumnWidth(displayName, .2);
            infoTable.setCustomColumnWidth(emailAdr, .3);
            infoTable.setCustomColumnWidth(systemRole, .2);

            centerContentContainer.setMaxWidth(misc.getScreenWidth() * .85);
            centerContentContainer.setMaxHeight(misc.getScreenHeight() * .85);
            centerContent.getChildren().add(infoTable.tableView);
            infoTable.tableView.setItems(queryData());
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    public ObservableList<AdminData> queryData() throws SQLException {
        ObservableList<AdminData> observableList = FXCollections.observableArrayList();
        Driver driver = new Driver();
        String sql = """
                select *
                from users, roles inner join users_roles
                on roles.role_id = users_roles.role_id
                where users.user_id = users_roles.user_id
                order by users.user_id;
                """;
        ResultSet resultSet = driver.connection.createStatement().executeQuery(sql);

        while (resultSet.next()) {
            observableList.add(new AdminData(
                    resultSet.getInt("user_id"),
                    resultSet.getString("username"),
                    resultSet.getString("full_name"),
                    resultSet.getString("email"),
                    resultSet.getString("name")
            ));
        }
        return observableList;
    }

    public void setCellFactoryValues() {
        userId.setCellValueFactory(new PropertyValueFactory<>("userIdData"));
        username.setCellValueFactory(new PropertyValueFactory<>("usernameData"));
        displayName.setCellValueFactory(new PropertyValueFactory<>("displayNameData"));
        emailAdr.setCellValueFactory(new PropertyValueFactory<>("emailAddressData"));
        systemRole.setCellValueFactory(new PropertyValueFactory<>("systemRoleData"));
    }

    public void filterData() {
        try {
            filteredList = new FilteredList<>(queryData());

            TableSearchBar.usableTextField.textProperty().addListener((observable, oldValue, newValue) -> filteredList.setPredicate(adminData -> {
                if (newValue.isEmpty() || newValue.isBlank()) {
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();
                int searchKeyInt = -1;
                try {searchKeyInt = Integer.parseInt(newValue);} catch (Exception ignored) {}

                if (adminData.getUserIdData() == searchKeyInt && getComboBoxItem("User ID")) {
                    return true;
                } else if (adminData.getUsernameData().toLowerCase().contains(searchKeyword) && getComboBoxItem("Username")) {
                    return true;
                } else if (adminData.getDisplayNameData().toLowerCase().contains(searchKeyword) && getComboBoxItem("Full name")) {
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
                "Username",
                "Full name",
                "Email address",
                "System role"
        );
        TableSearchBar.usableComboBox.setItems(oblist);
    }

    public boolean getComboBoxItem(String string) {
        String selectedComboValue = TableSearchBar.usableComboBox.getValue();
        return string.equals(selectedComboValue) || "All".equals(selectedComboValue);
    }
}
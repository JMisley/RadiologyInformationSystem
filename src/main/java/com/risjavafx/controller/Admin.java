package com.risjavafx.controller;

import com.risjavafx.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
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

        try {
            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable() throws SQLException {
        Miscellaneous misc = new Miscellaneous();

        queryData();
        setCellFactoryValues();

        infoTable.setColumns(tableColumnsList);
        infoTable.addColumnsToTable();

        infoTable.setCustomColumnWidth(userId, .105);
        infoTable.setCustomColumnWidth(username, .18);
        infoTable.setCustomColumnWidth(displayName, .2);
        infoTable.setCustomColumnWidth(emailAdr, .3);
        infoTable.setCustomColumnWidth(systemRole, .2);

        centerContentContainer.setMaxWidth(misc.getScreenWidth() * .85);
        centerContentContainer.setMaxHeight(misc.getScreenHeight() * .85);
        centerContent.getChildren().add(infoTable.tableView);
        infoTable.tableView.setItems(queryData());
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
}

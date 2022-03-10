package com.risjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    public HBox topContent;
    public HBox titleBar;
    public StackPane centerContent;

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

    // Load NavigationBar component into home-page.fxml
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TitleBar.createTitleBar(mainContainer, titleBar, this.getClass());
        NavigationMenu.createNavBar(topContent, this.getClass());

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
        InfoTable<AdminData> infoTable = new InfoTable<>() {{
            setColumns(tableColumnsList);
            addColumnsToTable();

            setCustomColumnWidth(userId, .12);
            setCustomColumnWidth(username, .18);
            setCustomColumnWidth(displayName, .2);
            setCustomColumnWidth(emailAdr, .3);
            setCustomColumnWidth(systemRole, .2);

            centerContent.setMaxWidth(misc.getScreenWidth() * .85);
            centerContent.setMaxHeight(misc.getScreenHeight() * .75);
        }};
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

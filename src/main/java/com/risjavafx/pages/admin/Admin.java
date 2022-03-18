package com.risjavafx.pages.admin;

import com.risjavafx.components.InfoTable;
import com.risjavafx.Driver;
import com.risjavafx.Miscellaneous;
import com.risjavafx.components.NavigationBar;
import com.risjavafx.components.TableSearchBar;
import com.risjavafx.components.TitleBar;
import com.risjavafx.pages.Pages;
import com.risjavafx.popups.Popups;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Admin implements Initializable {

    public BorderPane mainContainer;
    public HBox topContent, titleBar, tableSearchBar;
    public StackPane centerContent;
    public SplitPane centerContentContainer;
    public static ObservableList<AdminData> observableList = FXCollections.observableArrayList();
    SortedList<AdminData> sortedList;
    FilteredList<AdminData> filteredList;

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

    InfoTable<AdminData, String> infoTable = new InfoTable<>();
    Miscellaneous misc = new Miscellaneous();

    public void initialize(URL url, ResourceBundle resourceBundle) {
        Pages.setPage(Pages.ADMIN);
        Popups.setPopupEnum(Popups.ADMIN);

        TitleBar.createTitleBar(mainContainer, titleBar);
        NavigationBar.createNavBar(topContent);
        TableSearchBar.createSearchBar(tableSearchBar);
        setComboBoxItems();
        createTable();
        filterData();
        tableViewListener();
        manageRowSelection();
    }

    public void createTable() {
        try {
            setCellFactoryValues();

            infoTable.setColumns(tableColumnsList);
            infoTable.addColumnsToTable();

            infoTable.setCustomColumnWidth(userId, .12);
            infoTable.setCustomColumnWidth(displayName, .24);
            infoTable.setCustomColumnWidth(username, .17);
            infoTable.setCustomColumnWidth(emailAdr, .27);
            infoTable.setCustomColumnWidth(systemRole, .2);

            centerContentContainer.setMaxWidth(misc.getScreenWidth() * .9);
            centerContentContainer.setMaxHeight(misc.getScreenHeight() * .85);
            centerContent.getChildren().add(infoTable.tableView);

            queryData(getAllDataStringQuery());
            infoTable.tableView.setItems(observableList);
            infoTable.tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
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

    @SuppressWarnings("SqlWithoutWhere")
    public void deleteSelectedItemsQuery(String table) throws SQLException {
        Driver driver = new Driver();
        ObservableList<AdminData> selectedItems = infoTable.tableView.getSelectionModel().getSelectedItems();
        for (AdminData selectedItem : selectedItems) {
            String sql = """
                    DELETE FROM %$
                    WHERE user_id = ?
                    """.replace("%$", table);
            PreparedStatement preparedStatement = driver.connection.prepareStatement(sql);
            preparedStatement.setInt(1, selectedItem.getUserIdData());
            preparedStatement.execute();
        }
    }

    public void setCellFactoryValues() {
        userId.setCellValueFactory(new PropertyValueFactory<>("userIdData"));
        displayName.setCellValueFactory(new PropertyValueFactory<>("displayNameData"));
        username.setCellValueFactory(new PropertyValueFactory<>("usernameData"));
        emailAdr.setCellValueFactory(new PropertyValueFactory<>("emailAddressData"));
        systemRole.setCellValueFactory(new PropertyValueFactory<>("systemRoleData"));
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

    // Listener for Admin TextField and ComboBox
    public void filterData() {
        try {
            filteredList = new FilteredList<>(Admin.observableList);

            TableSearchBar.usableTextField.textProperty().addListener((observable, oldValue, newValue) ->
                    filteredList.setPredicate(adminData -> filterDataEvent(newValue, adminData)));

            TableSearchBar.usableComboBox.valueProperty().addListener((newValue) ->
                    filteredList.setPredicate(adminData -> {
                        if (newValue != null) {
                            return filterDataEvent(TableSearchBar.usableTextField.getText(), adminData);
                        }
                        return false;
                    }));

            sortedList = new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(infoTable.tableView.comparatorProperty());
            infoTable.tableView.setItems(sortedList);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    // Action to be performed for Admin TextField and ComboBox listener
    public static boolean filterDataEvent(String newValue, AdminData adminData) {
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

        if (adminData.getUserIdData() == searchKeyInt && Admin.getComboBoxItem("User ID")) {
            return true;
        } else if (adminData.getDisplayNameData().toLowerCase().contains(searchKeyword) && Admin.getComboBoxItem("Full name")) {
            return true;
        } else if (adminData.getUsernameData().toLowerCase().contains(searchKeyword) && Admin.getComboBoxItem("Username")) {
            return true;
        } else if (adminData.getEmailAddressData().toLowerCase().contains(searchKeyword) && Admin.getComboBoxItem("Email address")) {
            return true;
        } else
            return adminData.getSystemRoleData().toLowerCase().contains(searchKeyword) && Admin.getComboBoxItem("System role");
    }

    // Listener for Admin TableView
    public void tableViewListener() {
        infoTable.tableView.getSelectionModel().selectedItemProperty().addListener((observableValue, adminData, t1) -> {
            if (t1 != null) {
                TableSearchBar.toggleButtons(false);
                TableSearchBar.usableDeleteButton.setOnAction(actionEvent -> {
                    try {
                        deleteSelectedItemsQuery("users_roles");
                        deleteSelectedItemsQuery("users");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    TableSearchBar.deleteItems(infoTable, observableList);
                });
            }
        });
    }

    // If a selected row is clicked again, it will unselect. TableSearchBar Buttons will also adjust appropriately
    public void manageRowSelection() {
        infoTable.tableView.setRowFactory(tableView2 -> {
            final TableRow<AdminData> row = new TableRow<>();
            row.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                final int index = row.getIndex();
                if (index >= 0 && index < infoTable.tableView.getItems().size() &&
                    infoTable.tableView.getSelectionModel().isSelected(index)) {
                    infoTable.tableView.getSelectionModel().clearSelection();

                    TableSearchBar.toggleButtons(true);
                    event.consume();
                }
            });
            return row;
        });
    }
}
package com.risjavafx.pages.referrals;

import com.risjavafx.components.InfoTable;
import com.risjavafx.Driver;
import com.risjavafx.Miscellaneous;
import com.risjavafx.components.NavigationBar;
import com.risjavafx.components.TableSearchBar;
import com.risjavafx.components.TitleBar;
import com.risjavafx.pages.PageManager;
import com.risjavafx.pages.Pages;
import com.risjavafx.popups.models.PopupConfirmation;
import com.risjavafx.popups.PopupManager;
import com.risjavafx.popups.Popups;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

public class Referrals implements Initializable {

    public BorderPane mainContainer;
    public HBox topContent, titleBar, tableSearchBarContainer;
    public StackPane centerContent;
    public SplitPane centerContentContainer;

    public static ObservableList<ReferralData> observableList = FXCollections.observableArrayList();
    SortedList<ReferralData> sortedList;
    FilteredList<ReferralData> filteredList;

    InfoTable<ReferralData, String> infoTable = new InfoTable<>();
    TableSearchBar tableSearchBar = new TableSearchBar();
    Miscellaneous misc = new Miscellaneous();

    public TableColumn<ReferralData, String>
            patientId = new TableColumn<>("Patient ID"),
            dateOfBirth = new TableColumn<>("Date of Birth"),
            lastName = new TableColumn<>("Last Name"),
            firstName = new TableColumn<>("First Name"),
            sex = new TableColumn<>("Sex"),
            race = new TableColumn<>("Race"),
            ethnicity = new TableColumn<>("Ethnicity");

    public ArrayList<TableColumn<ReferralData, String>> tableColumnsList = new ArrayList<>() {{
        add(patientId);
        add(dateOfBirth);
        add(lastName);
        add(firstName);
        add(sex);
        add(race);
        add(ethnicity);
    }};

    public void initialize(URL url, ResourceBundle resourceBundle) {
        observableList.clear();

        // Miscellaneous components initialization
        Pages.setPage(Pages.REFERRALS);
        TitleBar.createTitleBar(mainContainer, titleBar);
        NavigationBar.createNavBar(topContent);

        // *TableView* initialization
        createTable();
        tableViewListener();
        manageRowSelection();

        // Overrides caching functionality and loads *TableSearchBar* every time page is opened
        PageManager.getScene().rootProperty().addListener(observable -> {
            if (Pages.getPage() == Pages.REFERRALS) {
                createTableSearchBar();
            }
        });
    }

    public void createTableSearchBar() {
        tableSearchBar.createSearchBar(tableSearchBarContainer);
        tableSearchBarAddButtonListener();
        setComboBoxItems();
        filterData();

        if (!infoTable.tableView.getSelectionModel().getSelectedItems().isEmpty()) {
            tableSearchBar.toggleButtons(false);
        }
    }

    public void createTable() {
        try {
            setCellFactoryValues();

            infoTable.setColumns(tableColumnsList);
            infoTable.addColumnsToTable();

            infoTable.setCustomColumnWidth(patientId, .142);
            infoTable.setCustomColumnWidth(dateOfBirth, .142);
            infoTable.setCustomColumnWidth(lastName, .142);
            infoTable.setCustomColumnWidth(firstName, .142);
            infoTable.setCustomColumnWidth(sex, .142);
            infoTable.setCustomColumnWidth(race, .142);
            infoTable.setCustomColumnWidth(ethnicity, .142);

            centerContentContainer.setMaxWidth(misc.getScreenWidth() * .75);
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
            observableList.add(new ReferralData(
                    resultSet.getInt("patient_id"),
                    resultSet.getString("dob"),
                    resultSet.getString("last_name"),
                    resultSet.getString("first_name"),
                    resultSet.getString("sex"),
                    resultSet.getString("race"),
                    resultSet.getString("ethnicity")
            ));
        }
    }

    public String getAllDataStringQuery() {
        return """
                SELECT patient_id, dob, first_name, last_name, sex, race, ethnicity
                FROM patients
                ORDER BY patient_id
                """;
    }

    public static String getLastRowStringQuery() {
        return """
                SELECT patient_id, dob, first_name, last_name, sex, race, ethnicity
                FROM patients
                ORDER BY patient_id DESC LIMIT 1;
                """;
    }

    @SuppressWarnings("SqlWithoutWhere")
    public void deleteSelectedItemsQuery(String table) throws SQLException {
        Driver driver = new Driver();
        ObservableList<ReferralData> selectedItems = infoTable.tableView.getSelectionModel().getSelectedItems();
        for (ReferralData selectedItem : selectedItems) {
            String sql = """
                    DELETE FROM %$
                    WHERE patient_id = ?
                    """.replace("%$", table);
            PreparedStatement preparedStatement = driver.connection.prepareStatement(sql);
            preparedStatement.setInt(1, selectedItem.getPatientIdData());
            preparedStatement.execute();
        }
    }

    public void setCellFactoryValues() {
        patientId.setCellValueFactory(new PropertyValueFactory<>("patientIdData"));
        dateOfBirth.setCellValueFactory(new PropertyValueFactory<>("dateOfBirthData"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastNameData"));
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstNameData"));
        sex.setCellValueFactory(new PropertyValueFactory<>("sexData"));
        race.setCellValueFactory(new PropertyValueFactory<>("raceData"));
        ethnicity.setCellValueFactory(new PropertyValueFactory<>("ethnicityData"));
    }

    public void setComboBoxItems() {
        ObservableList<String> oblist = FXCollections.observableArrayList(
                "All",
                "Patient ID",
                "Date of birth",
                "Last name",
                "First name",
                "Sex",
                "Race",
                "Ethnicity"
        );
        tableSearchBar.getComboBox().setItems(oblist);
    }

    public boolean getComboBoxItem(String string) {
        String selectedComboValue = tableSearchBar.getComboBox().getValue();
        return string.equals(selectedComboValue) || "All".equals(selectedComboValue);
    }

    // Listener for Admin TextField and ComboBox
    public void filterData() {
        try {
            filteredList = new FilteredList<>(observableList);

            tableSearchBar.getTextField().textProperty().addListener((observable, oldValue, newValue) ->
                    filteredList.setPredicate(referralData -> filterDataEvent(newValue, referralData)));

            tableSearchBar.getComboBox().valueProperty().addListener((newValue) -> filteredList.setPredicate(referralData -> {
                if (newValue != null) {
                    return filterDataEvent(tableSearchBar.getTextField().getText(), referralData);
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
    public boolean filterDataEvent(String newValue, ReferralData referralData) {
        if (tableSearchBar.getComboBox().getValue() == null) {
            tableSearchBar.getErrorLabel().setText("Please select a filter");
        } else {
            tableSearchBar.getErrorLabel().setText(null);
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

        if (referralData.getPatientIdData() == searchKeyInt && getComboBoxItem("Patient ID")) {
            return true;
        } else if (referralData.getDateOfBirthData().toLowerCase().contains(searchKeyword) ||
                referralData.getDateOfBirthData().toLowerCase().contains(String.valueOf(searchKeyInt)) &&
                        getComboBoxItem("Date of birth")) {
            return true;
        } else if (referralData.getLastNameData().toLowerCase().contains(searchKeyword) && getComboBoxItem("Last name")) {
            return true;
        } else
            return referralData.getFirstNameData().toLowerCase().contains(searchKeyword) && getComboBoxItem("First name");
    }

    // Listener for Admin TableView
    public void tableViewListener() {
        infoTable.tableView.getSelectionModel().selectedItemProperty().addListener((observableValue, ReferralData, t1) -> {
            if (t1 != null) {
                tableSearchBar.toggleButtons(false);
                tableSearchBar.getDeleteButton().setOnAction(actionEvent ->
                        customConfirmationPopup(confirm -> confirmDeletion(), cancel -> PopupManager.removePopup("ALERT")));
            } else {
                tableSearchBar.toggleButtons(true);
            }
        });
    }

    // If a selected row is clicked again, it will unselect. TableSearchBar Buttons will also adjust appropriately
    public void manageRowSelection() {
        infoTable.tableView.setRowFactory(tableView -> {
            final TableRow<ReferralData> row = new TableRow<>();
            row.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                final int index = row.getIndex();
                if (index >= 0 && index < infoTable.tableView.getItems().size() &&
                        infoTable.tableView.getSelectionModel().isSelected(index)) {
                    infoTable.tableView.getSelectionModel().clearSelection();

                    tableSearchBar.toggleButtons(true);
                    event.consume();
                }
            });
            return row;
        });
    }

    public void customConfirmationPopup(EventHandler<ActionEvent> confirm, EventHandler<ActionEvent> cancel) {
        PopupManager.createPopup(Popups.CONFIRMATION);
        new PopupConfirmation() {{
            setConfirmButtonLabel("Continue");
            setExitButtonLabel("Cancel");
            setHeaderLabel("Warning");
            setContentLabel("This data will be permanently deleted");
            getConfirmationButton().setOnAction(confirm);
            getCancelButton().setOnAction(cancel);
        }};
    }

    public void confirmDeletion() {
        try {
            deleteSelectedItemsQuery("patients");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        observableList.removeAll(infoTable.tableView.getSelectionModel().getSelectedItems());
        PopupManager.removePopup("ALERT");
    }

    public void tableSearchBarAddButtonListener() {
        tableSearchBar.getAddButton().setOnAction(event -> PopupManager.createPopup(Popups.REFERRALS));
    }
}
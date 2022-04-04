package com.risjavafx.pages.appointments;

import com.risjavafx.Driver;
import com.risjavafx.Miscellaneous;
import com.risjavafx.components.InfoTable;
import com.risjavafx.components.NavigationBar;
import com.risjavafx.components.TableSearchBar;
import com.risjavafx.components.TitleBar;
import com.risjavafx.pages.PageManager;
import com.risjavafx.pages.Pages;
import com.risjavafx.popups.PopupConfirmation;
import com.risjavafx.popups.PopupManager;
import com.risjavafx.popups.Popups;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Appointments implements Initializable {
    public BorderPane mainContainer;
    public HBox topContent, titleBar, tableSearchBarContainer;
    public StackPane centerContent;
    public SplitPane centerContentContainer;
    public static ObservableList<AppointmentData> observableList = FXCollections.observableArrayList();
    SortedList<AppointmentData> sortedList;
    FilteredList<AppointmentData> filteredList;


    InfoTable<AppointmentData, String> infoTable = new InfoTable<>();
    TableSearchBar tableSearchBar = new TableSearchBar();
    Miscellaneous misc = new Miscellaneous();

    public TableColumn<AppointmentData, String>
            patientId = new TableColumn<>("ID"),
            patient = new TableColumn<>("Patient"),
            modality = new TableColumn<>("Modality"),
            price = new TableColumn<>("Price"),
            dateTime = new TableColumn<>("Date"),
            radiologist = new TableColumn<>("Radiologist"),
            technician = new TableColumn<>("Technician"),
            closedFlag = new TableColumn<>("Closed");

    public ArrayList<TableColumn<AppointmentData, String>> tableColumnsList = new ArrayList<>() {{
        add(patientId);
        add(patient);
        add(modality);
        add(price);
        add(dateTime);
        add(radiologist);
        add(technician);
        add(closedFlag);
    }};


    // Load NavigationBar component into home-page.fxml
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        observableList.clear();
        Pages.setPage(Pages.APPOINTMENTS);
        TitleBar.createTitleBar(mainContainer, titleBar);
        NavigationBar.createNavBar(topContent);
        createTable();
        tableViewListener();
        manageRowSelection();

        // Overrides caching functionality and loads *TableSearchBar* every time page is opened
        PageManager.getScene().rootProperty().addListener(observable -> {
            if (Pages.getPage() == Pages.APPOINTMENTS) {
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

            infoTable.setCustomColumnWidth(patientId, .07);
            infoTable.setCustomColumnWidth(patient, .15);
            infoTable.setCustomColumnWidth(modality, .13);
            infoTable.setCustomColumnWidth(price, .09);
            infoTable.setCustomColumnWidth(dateTime, .15);
            infoTable.setCustomColumnWidth(radiologist, .15);
            infoTable.setCustomColumnWidth(technician, .15);
            infoTable.setCustomColumnWidth(closedFlag, .11);


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
        // ObservableList<AppointmentData> observableList = FXCollections.observableArrayList();
        Driver driver = new Driver();
        ResultSet resultSet = driver.connection.createStatement().executeQuery(sql);

        while (resultSet.next()) {
            String name = resultSet.getString("first_name") + " " + resultSet.getString("last_name");
            String checkIn;
            if (resultSet.getByte("closed") == 1) {
                checkIn = "concluded";
            } else {
                checkIn = "In Progress";
            }
            observableList.add(new AppointmentData(
                    resultSet.getInt("patient"),
                    name,
                    resultSet.getString("name"),
                    "$" + resultSet.getString("price"),
                    resultSet.getString("date_time"),
                    resultSet.getString(7),
                    resultSet.getString(8),
                    checkIn
            ));
        }
        //  return observableList;
    }

    public String getAllDataStringQuery() {
        return """
                SELECT appointments.patient,
                       patients.first_name,
                       patients.last_name,
                       modalities.name,
                       modalities.price,
                       appointments.date_time,
                       u1.full_name,
                       u2.full_name,
                       appointments.closed
                FROM appointments,
                     users as u1,
                     users as u2,
                     patients,
                     modalities
                WHERE u1.user_id = appointments.radiologist
                  AND u2.user_id = appointments.technician
                  AND patients.patient_id = appointments.patient
                  AND modalities.modality_id = appointments.modality
                ORDER BY appointments.patient;
                """;
    }

    public static String getLastRowStringQuery() {
        return """
                   SELECT appointments.patient,
                       patients.first_name,
                       patients.last_name,
                       modalities.name,
                       modalities.price,
                       appointments.date_time,
                       u1.full_name,
                       u2.full_name,
                       appointments.closed
                FROM appointments,
                     users as u1,
                     users as u2,
                     patients,
                     modalities
                WHERE u1.user_id = appointments.radiologist
                  AND u2.user_id = appointments.technician
                  AND patients.patient_id = appointments.patient
                  AND modalities.modality_id = appointments.modality
                ORDER BY appointments.patient  DESC LIMIT 1;
                """;
    }

    @SuppressWarnings("SqlWithoutWhere")
    public void deleteSelectedItemsQuery(String table) throws SQLException {
        Driver driver = new Driver();
        ObservableList<AppointmentData> selectedItems = infoTable.tableView.getSelectionModel().getSelectedItems();
        for (AppointmentData selectedItem : selectedItems) {
            String sql = """
                    DELETE FROM %$
                    WHERE appointment_id = ?
                    """.replace("%$", table);
            PreparedStatement preparedStatement = driver.connection.prepareStatement(sql);
            preparedStatement.setInt(1, selectedItem.getPatientId());
            preparedStatement.execute();
        }
    }

    public void setCellFactoryValues() {
        patientId.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        patient.setCellValueFactory(new PropertyValueFactory<>("patient"));
        modality.setCellValueFactory(new PropertyValueFactory<>("modality"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        dateTime.setCellValueFactory(new PropertyValueFactory<>("date"));
        radiologist.setCellValueFactory(cellData -> cellData.getValue().radiologist);
        technician.setCellValueFactory(cellData -> cellData.getValue().technician);
        closedFlag.setCellValueFactory(new PropertyValueFactory<>("closedFlag"));
    }
    /////////////

    public void setComboBoxItems() {
        ObservableList<String> oblist = FXCollections.observableArrayList(
                "All",
                "Patient ",
                "Modality",
                "Price",
                "Date",
                "Radiologist",
                "Technician",
                "Closed"
        );
        tableSearchBar.getComboBox().setItems(oblist);
    }

    //////
    public boolean getComboBoxItem(String string) {
        String selectedComboValue = tableSearchBar.getComboBox().getValue();
        return string.equals(selectedComboValue) || "All".equals(selectedComboValue);
    }

    // Listener for Admin TextField and ComboBox
    public void filterData() {
        try {
            filteredList = new FilteredList<>(Appointments.observableList);

            tableSearchBar.getTextField().textProperty().addListener((observable, oldValue, newValue) ->
                    filteredList.setPredicate(appointmentData -> filterDataEvent(newValue, appointmentData)));

            tableSearchBar.getComboBox().valueProperty().addListener((newValue) -> filteredList.setPredicate(appointmentData -> {
                if (newValue != null) {
                    return filterDataEvent(tableSearchBar.getTextField().getText(), appointmentData);
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

    public boolean filterDataEvent(String newValue, AppointmentData appointmentData) {
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

        if (appointmentData.getPatientId() == searchKeyInt && getComboBoxItem("ID")) {
            return true;
        } else if (appointmentData.getPatient().toLowerCase().contains(searchKeyword) && getComboBoxItem("Patient")) {
            return true;
        } else if (appointmentData.getModality().toLowerCase().contains(searchKeyword) && getComboBoxItem("Modality")) {
            return true;
        } else if (appointmentData.getDate().toLowerCase().contains(searchKeyword) && getComboBoxItem("Date")) {
            return true;
        } else
            return appointmentData.getRadiologist().toLowerCase().contains(searchKeyword) && getComboBoxItem("Radiologist");
    }


    // Listener for Admin TableView
    public void tableViewListener() {
        infoTable.tableView.getSelectionModel().selectedItemProperty().addListener((observableValue, adminData, t1) -> {
            if (t1 != null) {
                tableSearchBar.toggleButtons(false);
                tableSearchBar.getDeleteButton().setOnAction(actionEvent ->
                        customConfirmationPopup(confirm -> confirmDeletion(), cancel -> Popups.getAlertPopupEnum().getPopup().hide()));
            } else {
                tableSearchBar.toggleButtons(true);
            }
        });
    }

    // If a selected row is clicked again, it will unselect. TableSearchBar Buttons will also adjust appropriately
    public void manageRowSelection() {
        infoTable.tableView.setRowFactory(tableView -> {
            final TableRow<AppointmentData> row = new TableRow<>();
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
            setConfirmationImage(new Image("file:C:/Users/johnn/IdeaProjects/RISJavaFX/src/main/resources/com/risjavafx/images/warning.png"));
            getConfirmationButton().setOnAction(confirm);
            getCancelButton().setOnAction(cancel);
        }};
    }

    public void confirmDeletion() {
        /*
        try {
            deleteSelectedItemsQuery("users_roles");
            deleteSelectedItemsQuery("users");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        */

        observableList.removeAll(infoTable.tableView.getSelectionModel().getSelectedItems());
        Popups.getAlertPopupEnum().getPopup().hide();
    }

    public void tableSearchBarAddButtonListener() {

        tableSearchBar.getAddButton().setOnAction(event -> PopupManager.createPopup(Popups.APPOINTMENT));

    }
}
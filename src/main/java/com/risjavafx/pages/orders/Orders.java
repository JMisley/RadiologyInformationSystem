package com.risjavafx.pages.orders;

import com.risjavafx.Driver;
import com.risjavafx.Miscellaneous;
import com.risjavafx.components.InfoTable;
import com.risjavafx.components.NavigationBar;
import com.risjavafx.components.TableSearchBar;
import com.risjavafx.components.TitleBar;
import com.risjavafx.pages.LoadingService;
import com.risjavafx.pages.PageManager;
import com.risjavafx.pages.Pages;
import com.risjavafx.pages.TableManager;
import com.risjavafx.popups.models.Notification;
import com.risjavafx.popups.models.PopupConfirmation;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class Orders implements Initializable {
    public BorderPane mainContainer;
    public HBox topContent, titleBar, tableSearchBarContainer;
    public StackPane centerContent;
    public SplitPane centerContentContainer;
    private File file;

    public static ObservableList<OrdersData> observableList = FXCollections.observableArrayList();
    SortedList<OrdersData> sortedList;
    FilteredList<OrdersData> filteredList;

    InfoTable<OrdersData, String> infoTable = new InfoTable<>();
    TableSearchBar tableSearchBar = new TableSearchBar();
    Miscellaneous misc = new Miscellaneous();

    public TableColumn<OrdersData, String>
            orderId = new TableColumn<>("Order ID"),
            patient = new TableColumn<>("Patient"),
            referralMd = new TableColumn<>("Referral MD"),
            modality = new TableColumn<>("Modality"),
            notes = new TableColumn<>("Notes"),
            status = new TableColumn<>("Status");
    public ArrayList<TableColumn<OrdersData, String>> tableColumnsList = new ArrayList<>() {{
        add(orderId);
        add(patient);
        add(referralMd);
        add(modality);
        add(notes);
    }};

    // Load NavigationBar component into home-page.fxml
    public void initialize(URL url, ResourceBundle resourceBundle) {
        observableList.clear();
        Pages.setPage(Pages.ORDERS);
        TitleBar.createTitleBar(mainContainer, titleBar);
        NavigationBar.createNavBar(topContent);

        createTable();
        tableViewListener();
        manageRowSelection();

        // Overrides caching functionality and loads *TableSearchBar* every time page is opened
        PageManager.getScene().rootProperty().addListener(observable -> {
            if (Pages.getPage() == Pages.ORDERS) {
                createTableSearchBar();
                refreshTable();
            }
        });

    }

    public void createTableSearchBar() {
        tableSearchBar.createSearchBar(tableSearchBarContainer);
        tableSearchBarAddButtonListener();
        tableSearchBarAddImageButtonListener();
        tableSearchBarEditButtonListener();
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

            infoTable.setCustomColumnWidth(orderId, .12);
            infoTable.setCustomColumnWidth(patient, .2);
            infoTable.setCustomColumnWidth(referralMd, .17);
            infoTable.setCustomColumnWidth(modality, .17);
            infoTable.setCustomColumnWidth(notes, .35);

            centerContentContainer.setMaxWidth(misc.getScreenWidth() * .75);
            centerContentContainer.setMaxHeight(misc.getScreenHeight() * .85);
            centerContent.getChildren().add(infoTable.tableView);

            queryData(getAllDataStringQuery());
            infoTable.tableView.setItems(observableList);
            infoTable.tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            TableManager.setOrdersTable(infoTable.tableView);
        } catch (
                Exception exception) {
            exception.printStackTrace();
        }
    }

    private void refreshTable() {
        if (!centerContent.getChildren().contains(TableManager.getOrdersTable())) {
            centerContent.getChildren().add(TableManager.getOrdersTable());
        }
    }

    public static void queryData(String sql) throws SQLException {

        ResultSet resultSet = Driver.getConnection().createStatement().executeQuery(sql);

        while (resultSet.next()) {
            observableList.add(new OrdersData(
                    resultSet.getInt("order_id"),
                    resultSet.getString("patient"),
                    resultSet.getString("referral_md"),
                    resultSet.getString("modality"),
                    resultSet.getString("notes"),
                    resultSet.getString("status")
            ));
        }
    }

    public String getAllDataStringQuery() {
        return """
                SELECT *
                FROM orders
                """;
    }

    public static String getLastRowStringQuery() {
        return """
                SELECT *
                FROM orders
                ORDER BY order_id
                DESC LIMIT 1;
                """;
    }

    @SuppressWarnings("SqlWithoutWhere")
    public void deleteSelectedItemsQuery(String table) throws SQLException {

        ObservableList<OrdersData> selectedItems = infoTable.tableView.getSelectionModel().getSelectedItems();
        for (OrdersData selectedItem : selectedItems) {
            String sql = """
                    DELETE FROM %$
                    WHERE order_id = ?
                    """.replace("%$", table);
            PreparedStatement preparedStatement = Driver.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, selectedItem.getOrderIdData());
            preparedStatement.execute();
        }
    }

    public void setCellFactoryValues() {
        orderId.setCellValueFactory(new PropertyValueFactory<>("orderIdData"));
        patient.setCellValueFactory(new PropertyValueFactory<>("patientData"));
        referralMd.setCellValueFactory(new PropertyValueFactory<>("referralMdData"));
        modality.setCellValueFactory(new PropertyValueFactory<>("modalityData"));
        notes.setCellValueFactory(new PropertyValueFactory<>("notesData"));
        status.setCellValueFactory(new PropertyValueFactory<>("statusData"));
    }

    public void setComboBoxItems() {
        ObservableList<String> oblist = FXCollections.observableArrayList(
                "Patient",
                "Referral MD",
                "Modality"
        );
        tableSearchBar.getComboBox().setItems(oblist);
    }

    public boolean getComboBoxItem(String string) {
        String selectedComboValue = tableSearchBar.getComboBox().getValue();
        return string.equals(selectedComboValue) || "All".equals(selectedComboValue);
    }

    // Listener for Orders TextField and ComboBox
    public void filterData() {
        try {
            filteredList = new FilteredList<>(Orders.observableList);

            tableSearchBar.getTextField().textProperty().addListener((observable, oldValue, newValue) ->
                    filteredList.setPredicate(ordersData -> filterDataEvent(newValue, ordersData)));

            tableSearchBar.getComboBox().valueProperty().addListener((newValue) -> filteredList.setPredicate(ordersData -> {
                if (newValue != null) {
                    return filterDataEvent(tableSearchBar.getTextField().getText(), ordersData);
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

    // Action to be performed for Orders TextField and ComboBox listener
    public boolean filterDataEvent(String newValue, OrdersData ordersData) {
        if (tableSearchBar.getComboBox().getValue() == null) {
            tableSearchBar.getErrorLabel().setText("Please select a filter");
        } else {
            tableSearchBar.getErrorLabel().setText(null);
        }

        if (newValue.isEmpty() || newValue.isBlank()) {
            return true;
        }

        String searchKeyword = newValue.toLowerCase();

        if (ordersData.getPatientData().toLowerCase().contains(searchKeyword) && getComboBoxItem("Patient")) {
            return true;
        } else if (ordersData.getReferralMdData().toLowerCase().contains(searchKeyword) && getComboBoxItem("Referral MD")) {
            return true;
        } else return ordersData.getModalityData().toLowerCase().contains(searchKeyword) && getComboBoxItem("Modality");
    }

    // Listener for Orders TableView
    public void tableViewListener() {
        infoTable.tableView.getSelectionModel().selectedItemProperty().addListener((observableValue, ordersData, t1) -> {
            if (t1 != null) {
                tableSearchBar.toggleButtons(false);
                tableSearchBar.getDeleteButton().setOnAction(actionEvent ->
                        customConfirmationPopup(confirm -> confirmDeletion(), cancel -> PopupManager.removePopup()));
            } else {
                tableSearchBar.toggleButtons(true);
            }
            OrdersEditPopup.setOrderClickedId(infoTable.tableView.getSelectionModel().getSelectedItem().orderIdData.get());
        });
    }

    // If a selected row is clicked again, it will unselect. TableSearchBar Buttons will also adjust appropriately
    public void manageRowSelection() {
        infoTable.tableView.setRowFactory(tableView -> {
            final TableRow<OrdersData> row = new TableRow<>();
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
            //deleteSelectedItemsQuery("order_id");
            deleteSelectedItemsQuery("orders");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        observableList.removeAll(infoTable.tableView.getSelectionModel().getSelectedItems());
        PopupManager.removePopup();
    }

    public void tableSearchBarAddButtonListener() {
        tableSearchBar.getAddButton().setOnAction(event -> PopupManager.createPopup(Popups.ORDERS));
    }

    public void tableSearchBarAddImageButtonListener() {
        FileChooser fileChooser = new FileChooser();
        tableSearchBar.getAddImageButton().setOnAction(e -> {
            file = fileChooser.showOpenDialog(new Stage());
            if (file != null)
                createConfirmationPopup(confirm -> uploadImageToDatabase(), cancel -> PopupManager.removePopup());
        });
    }

    private void createConfirmationPopup(EventHandler<ActionEvent> confirm, EventHandler<ActionEvent> cancel) {
        PopupManager.createPopup(Popups.CONFIRMATION);
        new PopupConfirmation() {{
            setConfirmationImage(new Image(String.valueOf(file)));
            getHeaderLabel().setManaged(false);
            getConfirmationImageView().setFitHeight(150);
            getConfirmationImageView().setFitWidth(200);
            setConfirmButtonLabel("Yes");
            setExitButtonLabel("No");
            setHeaderLabel("");
            setContentLabel("Is this the image you wish to add?");
            getConfirmationButton().setOnAction(confirm);
            getCancelButton().setOnAction(cancel);
        }};
    }
    public void tableSearchBarEditButtonListener() {
        tableSearchBar.getEditButton().setOnAction(event -> PopupManager.createPopup(Popups.ORDERS_EDIT));
    }
    private void uploadImageToDatabase() {
        try {


            String query;
            if (checkForImages()) {
                query = """
                        UPDATE imaging_info
                        SET Image = ?
                        WHERE order_id = ?;
                        """;
            } else {
                query = """
                        INSERT INTO imaging_info (Image, order_id)
                        VALUES (?, ?)
                        """;
            }

            PreparedStatement preparedStatement = Driver.getConnection().prepareStatement(query);
            FileInputStream fileInputStream = new FileInputStream(file);
            preparedStatement.setBinaryStream(1, fileInputStream, (int) file.length());
            preparedStatement.setInt(2, infoTable.tableView.getSelectionModel().getSelectedItem().getOrderIdData());
            preparedStatement.execute();
            preparedStatement.close();
            PopupManager.removePopup();

            String notiHeader = "Submission Complete";
            String notiBody = "You have successfully added an image";
            Notification.createNotification(notiHeader, notiBody);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    // Returns true if order_id already has an image
    private boolean checkForImages() {
        try {

            String query = """
                    SELECT order_id
                    FROM imaging_info
                    WHERE order_id = ?
                    """;

            PreparedStatement preparedStatement = Driver.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, infoTable.tableView.getSelectionModel().getSelectedItem().getOrderIdData());
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }
}
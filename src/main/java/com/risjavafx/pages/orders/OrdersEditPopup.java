package com.risjavafx.pages.orders;

import com.risjavafx.popups.models.PopupAlert;
import com.risjavafx.popups.models.Notification;
import com.risjavafx.pages.PageManager;
import com.risjavafx.popups.PopupManager;
import com.risjavafx.Driver;
import com.risjavafx.Miscellaneous;
import com.risjavafx.popups.Popups;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class OrdersEditPopup implements Initializable{
    public VBox popupContainer;
    public static VBox usablePopupContainer;

    public Label orderIDLabel;
    public ComboBox<String> patientNameComboBox;
    public ComboBox<String> referralMdComboBox;
    public ComboBox<String> modalityComboBox;
    public ComboBox<String> appointmentComboBox;
    public TextArea notesTextArea;
    public ComboBox<String> statusComboBox;
    public TextArea reportTextArea;

    public Button cancelButton;
    public Button submitButton;
    public static int clickedOrderId;

    Driver driver = new Driver();
    Miscellaneous misc = new Miscellaneous();

    public OrdersEditPopup() throws SQLException {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resizeElements();
        populateComboBoxPatient();
        populateComboBoxReferralMd();
        populateComboBoxModality();
        populateComboBoxAppointment();
        populateComboBoxStatus();

        Popups.ORDERSEDIT.getPopup().showingProperty().addListener((observableValue, aBoolean, t1) -> {
            PageManager.getRoot().setDisable(!aBoolean);
            populateComboBoxPatient();
            populateComboBoxReferralMd();
            populateComboBoxModality();
            populateComboBoxAppointment();
            populateComboBoxStatus();
            if (Popups.ORDERSEDIT.getPopup().isShowing()) {
                orderIDLabel.setText(String.valueOf(getOrderClickedId()));
                refreshTextFields();
            }
        });
        usablePopupContainer = popupContainer;
    }

    public void populateComboBoxPatient() {
        try {
            String sql = """
                  SELECT first_name , last_name
                    FROM patients;
                    """;
            ResultSet resultSet = driver.connection.createStatement().executeQuery(sql);
            ObservableList<String> oblist = FXCollections.observableArrayList();
            while (resultSet.next()) {
                oblist.add(resultSet.getString("first_name") +" " + resultSet.getString("last_name"));
            }
            patientNameComboBox.setItems(oblist);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void populateComboBoxReferralMd() {
        try {
            String sql = """               
                    SELECT full_name
                    FROM users, users_roles
                    where users_roles.role_id = 3 AND users_roles.user_id = users.user_id;
                    """;
            ResultSet resultSet = driver.connection.createStatement().executeQuery(sql);
            ObservableList<String> oblist = FXCollections.observableArrayList();
            while (resultSet.next()) {
                oblist.add(resultSet.getString("full_name"));
            }
            referralMdComboBox.setItems(oblist);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void populateComboBoxModality() {
        try {
            String sql = """
                    select name
                    from modalities;
                    """;
            ResultSet resultSet = driver.connection.createStatement().executeQuery(sql);
            ObservableList<String> oblist = FXCollections.observableArrayList();
            while (resultSet.next()) {
                oblist.add(resultSet.getString("name"));
            }
            modalityComboBox.setItems(oblist);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void populateComboBoxAppointment() {
        try {
            String sql = """
                    select appointment_id
                    from appointments;
                    """;
            ResultSet resultSet = driver.connection.createStatement().executeQuery(sql);
            ObservableList<String> oblist = FXCollections.observableArrayList();
            while (resultSet.next()) {
                oblist.add(resultSet.getString("appointment_id"));
            }
            appointmentComboBox.setItems(oblist);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void populateComboBoxStatus() {
        try {
            String sql = """
                    select closed
                    from appointments;
                    """;
            ResultSet resultSet = driver.connection.createStatement().executeQuery(sql);
            ObservableList<String> oblist = FXCollections.observableArrayList();
            while (resultSet.next()) {
                oblist.add(resultSet.getString("closed"));
            }
            statusComboBox.setItems(oblist);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void setOrderClickedId(int userClickedId) {
        OrdersEditPopup.clickedOrderId = userClickedId;
    }

    public static int getOrderClickedId() {
        return OrdersEditPopup.clickedOrderId;
    }

    public void updateOrdersQuery() throws SQLException {
        String sql = """
                    UPDATE orders
                    SET patient = ?, referral_md = ?, modality = ?, appointment = ?, notes = ?, status = ?, report = ?
                    WHERE orders.order_id = ?;
                    """;
        PreparedStatement preparedStatement = driver.connection.prepareStatement(sql);
        preparedStatement.setString(1, patientNameComboBox.getValue());
        preparedStatement.setString(2, referralMdComboBox.getValue());
        preparedStatement.setString(3, modalityComboBox.getValue());
        preparedStatement.setString(4, appointmentComboBox.getValue());
        preparedStatement.setString(5, this.notesTextArea.getText());
        preparedStatement.setString(6, statusComboBox.getValue());
        preparedStatement.setString(7, this.reportTextArea.getText());
        preparedStatement.setInt(8, getOrderClickedId());
        preparedStatement.execute();
    }

    public boolean validInput() {
        return patientNameComboBox.getValue() != null &&
                referralMdComboBox.getValue() != null &&
                modalityComboBox.getValue() != null &&
                appointmentComboBox.getValue() != null &&
                appointmentComboBox.getValue() != null &&
                !notesTextArea.getText().isBlank() &&
                statusComboBox.getValue() != null &&
                !reportTextArea.getText().isBlank();
    }

    private void resizeElements() {
        popupContainer.setPrefHeight(Popups.getMenuDimensions()[0]);
        popupContainer.setPrefWidth(Popups.getMenuDimensions()[1]);
        popupContainer.setMaxHeight(Popups.getMenuDimensions()[0]);
        popupContainer.setMaxWidth(Popups.getMenuDimensions()[1]);

        cancelButton.setPrefHeight(misc.getScreenWidth() * .033);
        cancelButton.setPrefWidth(misc.getScreenWidth() * .11);
        submitButton.setPrefHeight(misc.getScreenWidth() * .033);
        submitButton.setPrefWidth(misc.getScreenWidth() * .11);

        double fontSize;
        if ((misc.getScreenWidth() / 80) < 20) {
            fontSize = misc.getScreenWidth() / 80;
        } else {
            fontSize = 20;
        }
        cancelButton.setStyle("-fx-font-size: " + fontSize);
        submitButton.setStyle("-fx-font-size: " + fontSize);
    }

    private void refreshTextFields() {
        patientNameComboBox.getSelectionModel().clearSelection();
        modalityComboBox.getSelectionModel().clearSelection();
        referralMdComboBox.getSelectionModel().clearSelection();
        statusComboBox.getSelectionModel().clearSelection();
        appointmentComboBox.getSelectionModel().clearSelection();
        reportTextArea.clear();
        notesTextArea.clear();
    }

    public void submitButtonOnclick() {
        boolean exception = false;
        if (validInput()) {
            try {
                updateOrdersQuery();
                Orders.queryData(Orders.getLastRowStringQuery());
                PopupManager.removePopup("MENU");
                Notification.createNotification("Submission Complete", "You successfully updated an order");
            } catch (Exception e) {
                exception = true;
            }

        }
        if (!validInput() || exception) {
            PopupManager.createPopup(Popups.ALERT);
            new PopupAlert() {{
                setHeaderLabel("Submission Failed");
                setContentLabel("Please make sure you filled out all fields");
                setExitButtonLabel("Retry");
            }};
        }
    }

    public void cancelButtonOnclick() {
        try {
            PopupManager.removePopup("MENU");
        } catch (Exception ignore) {
        }
    }

}

package com.risjavafx.pages.appointments;

import com.risjavafx.PromptButtonCell;
import com.risjavafx.Driver;
import com.risjavafx.Miscellaneous;
import com.risjavafx.popups.models.Notification;
import com.risjavafx.popups.models.PopupAlert;
import com.risjavafx.popups.PopupManager;
import com.risjavafx.popups.Popups;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AppointmentPopUp implements Initializable {
    public VBox popupContainer;
    public static VBox usablePopupContainer;

    public Label appointmentIdLabel;
    public ComboBox<String> roleComboBoxTech;
    public ComboBox<String> roleComboBoxRad;
    public ComboBox<String> roleComboBoxPatient;
    public ComboBox<Integer> orderIdComboBox;
    public TextField dateTextField;
    public TextField phoneNumberTextField;
    public TextField emailTextField;
    public Button cancelButton;
    public Button submitButton;

    Miscellaneous misc = new Miscellaneous();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resizeElements();
        setAppointmentIdLabel();
        populateRadTechCombos(roleComboBoxRad);
        populateRadTechCombos(roleComboBoxTech);
        populateComboBoxPatient();
        setPatientComboBoxListener();
        populateComboBoxOrderFromPatient();

        Popups.APPOINTMENT.getPopup().showingProperty().addListener((observableValue, aBoolean, t1) -> {
            if (Popups.APPOINTMENT.getPopup().isShowing()) {
                appointmentIdLabel.setText(String.valueOf(setAppointmentIdLabel()));
                refreshElements();
            }
        });
        usablePopupContainer = popupContainer;
    }

    public void refreshElements() {
        roleComboBoxRad.getSelectionModel().clearSelection();
        roleComboBoxTech.getSelectionModel().clearSelection();
        orderIdComboBox.getSelectionModel().clearSelection();
        roleComboBoxPatient.getSelectionModel().clearSelection();
        phoneNumberTextField.clear();
        emailTextField.clear();
        dateTextField.clear();
    }

    public int setAppointmentIdLabel() {
        try {
            String sql = """
                    select MAX(appointment_id)
                    from appointments;
                    """;
            ResultSet resultSet = Driver.getConnection().createStatement().executeQuery(sql);
            if (resultSet.next()) {
                return (resultSet.getInt("MAX(appointment_id)") + 1);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return -1;
    }

    public void populateComboBoxPatient() {
        try {
            String sql = """
                    SELECT first_name , last_name
                    FROM patients;
                      """;
            ResultSet resultSet = Driver.getConnection().createStatement().executeQuery(sql);
            ObservableList<String> oblist = FXCollections.observableArrayList();
            while (resultSet.next()) {
                oblist.add(resultSet.getString("first_name") + " " + resultSet.getString("last_name"));
            }
            roleComboBoxPatient.setItems(oblist);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void populateRadTechCombos(ComboBox<String> comboBox) {
        int roleId = -1;
        if (comboBox.equals(roleComboBoxRad)) roleId = 5;
        else if (comboBox.equals(roleComboBoxTech)) roleId = 4;

        try {
            String sql = """           
                    SELECT full_name
                    FROM users, users_roles
                    where users_roles.role_id = ? AND users_roles.user_id = users.user_id;
                    """;

            PreparedStatement preparedStatement = Driver.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, roleId);
            ResultSet resultSet = preparedStatement.executeQuery();

            ObservableList<String> oblist = FXCollections.observableArrayList();
            while (resultSet.next()) {
                oblist.add(resultSet.getString("full_name"));
            }
            comboBox.setItems(oblist);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void insertAppointmentQuery() throws SQLException {
        String sql = """
                insert into appointments
                values (?, ?,  ?, ?, ?, ?, ?, ?,? ,0);
                """;


        PreparedStatement preparedStatement = Driver.getConnection().prepareStatement(sql);
        preparedStatement.setInt(1, Integer.parseInt(appointmentIdLabel.getText()));
        preparedStatement.setInt(2, pullPatientComboboxId(roleComboBoxPatient.getValue()));
        preparedStatement.setInt(3, orderIdComboBox.getValue());
        preparedStatement.setInt(4, pullModalityComboboxId());
        preparedStatement.setString(5, dateTextField.getText());
        preparedStatement.setInt(6, pullDocComboboxId(roleComboBoxRad.getValue()));
        preparedStatement.setInt(7, pullDocComboboxId(roleComboBoxTech.getValue()));
        preparedStatement.setString(8, phoneNumberTextField.getText());
        preparedStatement.setString(9, emailTextField.getText());
        preparedStatement.execute();

    }

    public int pullPatientComboboxId(String name) {
        try {
            String patientIdFromCombo = """
                    SELECT patient_id
                    FROM patients
                    WHERE ? = CONCAT(first_name, " ", last_name)
                    """;
            PreparedStatement preparedStatementPatientCombo = Driver.getConnection().prepareStatement(patientIdFromCombo);
            preparedStatementPatientCombo.setString(1, name);
            ResultSet resultSetPatientCombo = preparedStatementPatientCombo.executeQuery();

            int i = 0;
            while (resultSetPatientCombo.next()) {
                i = resultSetPatientCombo.getInt(("patient_id"));
            }
            return i;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return -1;
    }

    public int pullDocComboboxId(String name) {
        try {
            String techIdFromCombo = """
                    SELECT user_id
                    FROM users
                    WHERE ? =  full_name
                    """;
            PreparedStatement preparedStatementTechCombo = Driver.getConnection().prepareStatement(techIdFromCombo);
            preparedStatementTechCombo.setString(1, name);
            ResultSet resultSetTechCombo = preparedStatementTechCombo.executeQuery();

            int i = 0;
            while (resultSetTechCombo.next()) {
                i = resultSetTechCombo.getInt(("user_id"));
            }
            return i;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return -1;
    }

    public void populateComboBoxOrderFromPatient() {
        roleComboBoxPatient.valueProperty().addListener(observable -> {
            try {
                String sql = """
                        SELECT orders.order_id
                        FROM orders
                        WHERE orders.patient = ?
                         """;
                ObservableList<Integer> oblist = FXCollections.observableArrayList();
                PreparedStatement preparedStatement = Driver.getConnection().prepareStatement(sql);
                preparedStatement.setString(1, roleComboBoxPatient.getValue());
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    oblist.add(resultSet.getInt("order_id"));
                }
                orderIdComboBox.setItems(oblist);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public int pullModalityComboboxId() {
        try {
            String modalityIdFromCombo = """
                    SELECT modality_id
                    FROM modalities, orders
                    WHERE modalities.name = orders.modality AND orders.order_id = ?
                    """;
            PreparedStatement preparedStatementModalityCombo = Driver.getConnection().prepareStatement(modalityIdFromCombo);
            preparedStatementModalityCombo.setInt(1, orderIdComboBox.getValue());
            ResultSet resultSetTechCombo = preparedStatementModalityCombo.executeQuery();

            int i = 0;
            while (resultSetTechCombo.next()) {
                i = resultSetTechCombo.getInt(("modality_id"));
            }
            return i;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return -1;
    }


    // Returns false if any input field is invalid
    public boolean validInput() {
        return orderIdComboBox.getValue() != null &&
               roleComboBoxRad.getValue() != null &&
               roleComboBoxTech.getValue() != null &&
               roleComboBoxPatient.getValue() != null &&
               !dateTextField.getText().isBlank() &&
               !phoneNumberTextField.getText().isBlank() &&
               !emailTextField.getText().isBlank();
    }

    public void resizeElements() {
        popupContainer.setPrefHeight(Popups.getMenuDimensions()[0]);
        popupContainer.setPrefWidth(Popups.getMenuDimensions()[1]);
        popupContainer.setMaxHeight(Popups.getMenuDimensions()[0]);
        popupContainer.setMaxWidth(Popups.getMenuDimensions()[1]);

        cancelButton.setPrefHeight(misc.getScreenWidth() * .033);
        cancelButton.setPrefWidth(misc.getScreenWidth() * .11);
        submitButton.setPrefHeight(misc.getScreenWidth() * .033);
        submitButton.setPrefWidth(misc.getScreenWidth() * .11);

        roleComboBoxPatient.setButtonCell(new PromptButtonCell<>("Patient"));
        orderIdComboBox.setButtonCell(new PromptButtonCell<>("Order ID"));
        roleComboBoxTech.setButtonCell(new PromptButtonCell<>("Technician"));
        roleComboBoxRad.setButtonCell(new PromptButtonCell<>("Radiologist"));

        double fontSize;
        if ((misc.getScreenWidth() / 80) < 20) {
            fontSize = misc.getScreenWidth() / 80;
        } else {
            fontSize = 20;
        }
        cancelButton.setStyle("-fx-font-size: " + fontSize);
        submitButton.setStyle("-fx-font-size: " + fontSize);
    }

    //Button Onclicks
    // Onclick for submit button
    public void submitButtonOnclick() throws SQLException {
        if (validInput()) {
            insertAppointmentQuery();
            Appointments.queryData(Appointments.getLastRowStringQuery());
            PopupManager.removePopup();
            Notification.createNotification("Submission Complete", "You successfully added a new user");
        } else if (!validInput()) {
            PopupManager.createPopup(Popups.ALERT);
            new PopupAlert() {{
                setHeaderLabel("Submission Failed");
                setContentLabel("Please make sure you filled out all fields");
                setExitButtonLabel("Retry");
            }};
        }
    }

    // Onclick for cancel button
    public void cancelButtonOnclick() {
        try {
            PopupManager.removePopup();
        } catch (Exception ignore) {
        }
    }

    public void setPatientComboBoxListener() {
        roleComboBoxPatient.valueProperty().addListener(observable -> {
            String str = roleComboBoxPatient.getValue();
            System.out.println(str);
        });
    }
}
package com.risjavafx.pages.appointments;

import com.risjavafx.pages.PageManager;
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
    public ComboBox<String> roleComboBoxModality;
    public ComboBox<String> roleComboBoxRad;
    public ComboBox<String> roleComboBoxTech;
    public ComboBox<String> roleComboBoxPatient;
    public TextField dateTextField;
    public TextField phoneNumberTextField;
    public TextField emailTextField;
    public Button cancelButton;
    public Button submitButton;

    Driver driver = new Driver();
    Miscellaneous misc = new Miscellaneous();

    public AppointmentPopUp() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resizeElements();
        setAppointmentIdLabel();
        populateComboBoxModality();
        populateComboBoxRadiologist();
        populateComboBoxTechnician();
        populateComboBoxPatient();
        Popups.APPOINTMENT.getPopup().showingProperty().addListener((observableValue, aBoolean, t1) -> {
            PageManager.getRoot().setDisable(!aBoolean);

            if (Popups.APPOINTMENT.getPopup().isShowing()) {
                appointmentIdLabel.setText(String.valueOf(setAppointmentIdLabel()));
                refreshElements();
            }
        });
        usablePopupContainer = popupContainer;
    }

    public void refreshElements() {
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
            ResultSet resultSet = driver.connection.createStatement().executeQuery(sql);
            if (resultSet.next()) {
                return (resultSet.getInt("MAX(appointment_id)") + 1);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return -1;
    }


    public void populateComboBoxTechnician() {
        try {
            String sql = """
                    SELECT full_name
                      FROM users, users_roles
                      where users_roles.role_id = 5 AND users_roles.user_id = users.user_id;
                      """;
            ResultSet resultSet = driver.connection.createStatement().executeQuery(sql);
            ObservableList<String> oblist = FXCollections.observableArrayList();
            while (resultSet.next()) {
                oblist.add(resultSet.getString("full_name"));
            }
            roleComboBoxTech.setItems(oblist);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
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
                oblist.add(resultSet.getString("first_name") + " " + resultSet.getString("last_name"));
            }
            roleComboBoxPatient.setItems(oblist);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void populateComboBoxRadiologist() {
        try {
            String sql = """
                                        
                    SELECT full_name
                    FROM users, users_roles
                    where users_roles.role_id = 6 AND users_roles.user_id = users.user_id;
                    """;
            ResultSet resultSet = driver.connection.createStatement().executeQuery(sql);
            ObservableList<String> oblist = FXCollections.observableArrayList();
            while (resultSet.next()) {
                oblist.add(resultSet.getString("full_name"));
            }
            roleComboBoxRad.setItems(oblist);
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
            roleComboBoxModality.setItems(oblist);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    public void insertAppointmentQuery() throws SQLException {
        String sql = """
                insert into appointments
                values (?, ?, null, ?, ?, ?, ?, ?, ?, null,null,null,1,1);
                """;


        PreparedStatement preparedStatement = driver.connection.prepareStatement(sql);
        preparedStatement.setInt(1, Integer.parseInt(appointmentIdLabel.getText()));
        preparedStatement.setInt(2, pullPatientComboboxId(roleComboBoxPatient.getValue()));
        preparedStatement.setInt(3, pullModalityComboboxId(roleComboBoxModality.getValue()));
        preparedStatement.setString(4, dateTextField.getText());
        preparedStatement.setInt(5, pullDocComboboxId(roleComboBoxRad.getValue()));
        preparedStatement.setInt(6, pullDocComboboxId(roleComboBoxTech.getValue()));
        preparedStatement.setString(7, phoneNumberTextField.getText());
        preparedStatement.setString(8, emailTextField.getText());
        preparedStatement.execute();
    }

    public int pullPatientComboboxId(String name) {
        try {
            String patientIdFromCombo = """
                    SELECT patient_id
                    FROM patients
                    WHERE ? = CONCAT(first_name, " ", last_name)
                    """;
            PreparedStatement preparedStatementPatientCombo = driver.connection.prepareStatement(patientIdFromCombo);
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
            PreparedStatement preparedStatementTechCombo = driver.connection.prepareStatement(techIdFromCombo);
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

    public int pullModalityComboboxId(String insertName) {
        try {
            String modalityIdFromCombo = """
                    SELECT modality_id
                    FROM modalities
                    WHERE name = ?
                    """;
            PreparedStatement preparedStatementModalityCombo = driver.connection.prepareStatement(modalityIdFromCombo);
            preparedStatementModalityCombo.setString(1, insertName);
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
        return roleComboBoxModality.getValue() != null &&
               roleComboBoxTech.getValue() != null &&
               roleComboBoxRad.getValue() != null &&
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
            PopupManager.removePopup("MENU");
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
            PopupManager.removePopup("MENU");
        } catch (Exception ignore) {
        }
    }
}
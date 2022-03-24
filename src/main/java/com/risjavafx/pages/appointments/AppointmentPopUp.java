package com.risjavafx.pages.appointments;

import com.risjavafx.popups.PopupAlert;
import com.risjavafx.popups.Notification;
import com.risjavafx.pages.PageManager;
import com.risjavafx.popups.PopupManager;
import com.risjavafx.Driver;
import com.risjavafx.Miscellaneous;
import com.risjavafx.popups.Popups;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AppointmentPopUp implements Initializable {
    public VBox popupContainer;
    public static VBox usablePopupContainer;

    public Label patientIdLabel;
    public ComboBox<String> roleComboBox;
    public TextField FirstNameLabel;
    public TextField lastNameLabel;
    public TextField priceTextField;
    public TextField dateTextField;
    public TextField radiologistTextField;
    public TextField technicianTextField;
    public Button cancelButton;
    public Button submitButton;

    Driver driver = new Driver();
    Miscellaneous misc = new Miscellaneous();

    public AppointmentPopUp() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resizeElements();
        setUserIDLabel();
        populateComboBox();
        Popups.APPOINTMENT.getPopup().showingProperty().addListener((observableValue, aBoolean, t1) ->
                PageManager.getRoot().setDisable(!aBoolean));
        usablePopupContainer = popupContainer;
    }

    public void setUserIDLabel() {
        try {
            String sql = """
                    select MAX(patient_id)
                    from patients;
                    """;
            ResultSet resultSet = driver.connection.createStatement().executeQuery(sql);
            while (resultSet.next()) {
                patientIdLabel.setText(String.valueOf(resultSet.getInt("MAX(patient_id)") + 1));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    public void populateComboBox() {
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
            roleComboBox.setItems(oblist);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void insertUserQuery() throws SQLException {
        String sql = """
                insert into appointments
                values (?, ?, ?, ?, ?, ?);
                """;
        PreparedStatement preparedStatement = driver.connection.prepareStatement(sql);
        preparedStatement.setInt(1, Integer.parseInt(patientIdLabel.getText()));
        preparedStatement.setString(2, lastNameLabel.getText().toLowerCase());
        preparedStatement.setString(3, FirstNameLabel.getText());
        preparedStatement.setString(4, priceTextField.getText());
        preparedStatement.setString(5, dateTextField.getText());
        preparedStatement.setInt(6, 1);
        preparedStatement.execute();
    }

    public void insertRoleIdQuery() throws SQLException {
        String sql = """
                insert into users_roles
                values (?, ?, ?);
                """;
        PreparedStatement preparedStatement = driver.connection.prepareStatement(sql);
        preparedStatement.setInt(1, Integer.parseInt(patientIdLabel.getText()));
        preparedStatement.setInt(2, getRoleId(roleComboBox.getValue()));
        preparedStatement.setInt(3, Integer.parseInt(patientIdLabel.getText()));
        preparedStatement.execute();
    }

    public int getRoleId(String role) throws SQLException {
        String sql = """
                select role_id
                from roles
                where name = ?;
                """;
        PreparedStatement preparedStatement = driver.connection.prepareStatement(sql);
        preparedStatement.setString(1, role);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt("role_id");
    }

    // Returns false if any input field is invalid
    public boolean validInput() {
        return roleComboBox.getValue() != null &&
                !FirstNameLabel.getText().isBlank() &&
                !lastNameLabel.getText().isBlank() &&
                !priceTextField.getText().isBlank() &&
                !dateTextField.getText().isBlank();
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
        if ((misc.getScreenWidth()/80) < 20) {
            fontSize = misc.getScreenWidth()/80;
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
            insertUserQuery();
            insertRoleIdQuery();
            Appointments.queryData(Appointments.getLastRowStringQuery());
            Popups.getMenuPopupEnum().getPopup().hide();
            Notification.createNotification();
        } else if (!validInput()) {
            PopupManager.createPopup(Popups.ALERT);
            new PopupAlert() {{
                setAlertImage(new Image("file:C:/Users/johnn/IdeaProjects/RISJavaFX/src/main/resources/com/risjavafx/images/error.png"));
                setHeaderLabel("Submission Failed");
                setContentLabel("Please make sure you filled out all fields");
                setExitButtonLabel("Retry");
            }};

        }
    }

    // Onclick for cancel button
    public void cancelButtonOnclick() {
        Popups.APPOINTMENT.getPopup().hide();
        try {
            Popups.ALERT.getPopup().hide();} catch (Exception ignore) {}
    }
}

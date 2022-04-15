package com.risjavafx.pages.referrals;

import com.risjavafx.pages.LoadingService;
import com.risjavafx.popups.models.PopupAlert;
import com.risjavafx.popups.PopupManager;
import com.risjavafx.Driver;
import com.risjavafx.Miscellaneous;
import com.risjavafx.popups.Popups;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ReferralPopup implements Initializable {
    public VBox popupContainer;
    public Label patientIDLabel;
    public TextField firstNameTextField;
    public TextField lastNameTextField;
    public TextField birthDateTextField;
    public TextField sexTextField;
    public TextField raceTextField;
    public TextField ethnicityTextField;
    public Button cancelButton;
    public Button submitButton;

    Miscellaneous misc = new Miscellaneous();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resizeElements();

        Popups.REFERRALS.getPopup().showingProperty().addListener((observableValue, aBoolean, t1) -> {
            if (Popups.REFERRALS.getPopup().isShowing()) {
                patientIDLabel.setText(String.valueOf(setPatientIDLabel()));
                refreshElements();
            }
        });
    }

    public void refreshElements() {
        firstNameTextField.clear();
        lastNameTextField.clear();
        birthDateTextField.clear();
        raceTextField.clear();
        sexTextField.clear();
        ethnicityTextField.clear();
    }

    public int setPatientIDLabel() {
        try {
            String sql = """
                    select MAX(patient_id)
                    from patients;
                    """;
            ResultSet resultSet = Driver.getConnection().createStatement().executeQuery(sql);
            if (resultSet.next()) {
                return (resultSet.getInt("MAX(patient_id)") + 1);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return -1;
    }


    public void insertPatientQuery() throws SQLException {
        String sql = """
                insert into patients
                values (?, ?, ?, ?, ?, ?, ?);
                """;
        PreparedStatement preparedStatement = Driver.getConnection().prepareStatement(sql);
        preparedStatement.setInt(1, Integer.parseInt(patientIDLabel.getText()));
        preparedStatement.setString(2, firstNameTextField.getText());
        preparedStatement.setString(3, lastNameTextField.getText());
        preparedStatement.setString(4, birthDateTextField.getText());
        preparedStatement.setString(5, sexTextField.getText());
        preparedStatement.setString(6, raceTextField.getText());
        preparedStatement.setString(7, ethnicityTextField.getText());
        preparedStatement.execute();
    }


    // Returns false if any input field is invalid
    public boolean validInput() {
        return !firstNameTextField.getText().isBlank() &&
               !birthDateTextField.getText().isBlank() &&
               !lastNameTextField.getText().isBlank() &&
               !sexTextField.getText().isBlank() &&
               !raceTextField.getText().isBlank() &&
               !ethnicityTextField.getText().isBlank();
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

    //Button OnClicks
    // Onclick for submit button
    public void submitButtonOnclick() throws SQLException {
        if (validInput()) {
            insertPatientQuery();
            Referrals.queryData(Referrals.getLastRowStringQuery());
            PopupManager.removePopup();
            String notiHeader = "Submission Complete";
            String notiText = "You have successfully changed your information";
            LoadingService.CustomReload defaultReset = new LoadingService.CustomReload(notiHeader, notiText);
            defaultReset.start();
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
}
package com.risjavafx.pages.orders;

import com.risjavafx.Driver;
import com.risjavafx.Miscellaneous;
import com.risjavafx.pages.PageManager;
import com.risjavafx.popups.PopupManager;
import com.risjavafx.popups.Popups;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class OrderAlert implements Initializable {

    public VBox popupContainer;
    public ListView<String> listView;
    public Button confirmButton;
    public Button cancelButton;

    private static boolean clicked;
    private static int patientID;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resizeElements();

        Popups.ORDER_ALERT.getPopup().showingProperty().addListener((observableValue, aBoolean, t1) -> {
            if (Popups.ORDER_ALERT.getPopup().isShowing()) {
                populateListView();
            }
        });
    }

    private void populateListView() {
        try {
            String sql = """
                SELECT installed_devices, family_illness, allergies, previous_surgeries, current_medication
                FROM patient_background
                WHERE patient_id = ?
                """;

            PreparedStatement preparedStatement = Driver.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, patientID);
            ResultSet resultSet = preparedStatement.executeQuery();

            ObservableList<String> observableList = FXCollections.observableArrayList();
            while (resultSet.next()) {
                for (int i = 1; i < 6; i++) {
                    if (resultSet.getString(i) != null)
                        observableList.add(resultSet.getString(i));
                }
            }
            listView.setItems(observableList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resizeElements() {
        Miscellaneous misc = new Miscellaneous();
        popupContainer.setPrefHeight(Popups.getCustomDimensions()[0]);
        popupContainer.setPrefWidth(Popups.getCustomDimensions()[1]);

        cancelButton.setPrefWidth(misc.getScreenWidth() * .08);
        confirmButton.setPrefWidth(misc.getScreenWidth() * .08);
    }

    public static void patientNameToID(String Patient) {
        try {
            String sql = """
                SELECT patient_id
                FROM patients
                WHERE first_name = ? AND last_name = ?
                """;

            String[] splitName = splitPatientName(Patient);
            PreparedStatement preparedStatement = Driver.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, splitName[0]);
            preparedStatement.setString(2, splitName[1]);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
                patientID = resultSet.getInt("patient_id");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String[] splitPatientName(String name) {
        return name.split("\\s+");
    }

    public static boolean isClicked() {
        return clicked;
    }

    public static void resetClicked() {
        clicked = false;
    }

    public static void toggleClicked() {
        clicked = !clicked;
    }

    public void cancel() {
        PopupManager.removePopup(Popups.ORDER_ALERT);
        PageManager.getRoot().setDisable(true);
    }

    public void confirm() {
        toggleClicked();
        PopupManager.removePopup(Popups.ORDER_ALERT);
        PageManager.getRoot().setDisable(true);
    }
}

package com.risjavafx.pages.referrals.background;

import com.risjavafx.Driver;
import com.risjavafx.pages.Loadable;
import com.risjavafx.pages.LoadingService;
import com.risjavafx.pages.referrals.ViewReferralsPopup;
import com.risjavafx.popups.PopupManager;
import com.risjavafx.popups.Popups;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class PatientBackgroundPopup implements Initializable, Loadable {


    static int clickedPatientId;

    @FXML
    private ListView<String> CurrentMedicationList;

    @FXML
    private ListView<String> FamilyIllnessList;

    @FXML
    private ListView<String> FoodMedicineAllergyList;

    @FXML
    private ListView<String> InstalledMedicalDevicesList;

    @FXML
    private ListView<String> PreviousSurgeriesList;

    @FXML
    private TextField addFamilyIllnessTextArea;

    @FXML
    private TextField addFoodMedicineAllergyTextArea;

    @FXML
    private TextField addTextAreaCurrentMedication;

    @FXML
    private TextField addTextAreaMedicalDevices;

    @FXML
    private TextField addTextAreaPreviousSurgeries;

    @FXML
    private StackPane popupContainer;

    @FXML
    private Label familyIllnessLabel;

    @FXML
    private Label patientNameLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        Popups.PATIENTBACKGROUND.getPopup().showingProperty().addListener((observableValue, aBoolean, t1) -> {
            if (Popups.PATIENTBACKGROUND.getPopup().isShowing()) {
                refreshElements();

                setPatientClickedId(clickedPatientId);
                patientNameLabel.setText(String.valueOf(setPatientNameLabel()));


            }
        });
        // usablePopupContainer = popupContainer;
    }

    private void refreshElements() {
        patientNameLabel.setText("");
    }

    public static void setPatientClickedId(int clickedPatientId) {

        PatientBackgroundPopup.clickedPatientId = clickedPatientId;
        System.out.println(clickedPatientId);

    }

    public String setPatientNameLabel() {
        String answer = "broken";
        try {
            String sql = """
                    SELECT first_name, last_name
                    FROM  patients
                    WHERE patients.patient_id = ?;
                    """;

            PreparedStatement preparedStatement = Driver.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, (clickedPatientId));
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.executeQuery();


            //System.out.println(clickedPatientId);

            if (resultSet.next()) {
                answer = ((resultSet.getString("first_name") + (" ") + resultSet.getString("last_name")));
                return answer;

            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return answer;
    }


    @FXML
    void addListItemFamilyIllness(MouseEvent event) throws SQLException {
        addListItem(FamilyIllnessList, addFamilyIllnessTextArea);
        insertDataToList("family_illness", addFamilyIllnessTextArea, FamilyIllnessList);
    }

    @FXML
    void addListNameCurrentMedication(MouseEvent event) throws SQLException {
        addListItem(CurrentMedicationList, addTextAreaCurrentMedication);
        insertDataToList("current_medication", addTextAreaCurrentMedication, CurrentMedicationList);

    }

    @FXML
    void addListNameFoodMedicine(MouseEvent event) throws SQLException {
        addListItem(FoodMedicineAllergyList, addFoodMedicineAllergyTextArea);
        insertDataToList("allergies", addFoodMedicineAllergyTextArea, FoodMedicineAllergyList);
    }

    @FXML
    void addListNameInstalledMedicalDevices(MouseEvent event) throws SQLException {
        addListItem(InstalledMedicalDevicesList, addTextAreaMedicalDevices);
        insertDataToList("installed_devices", addTextAreaMedicalDevices, InstalledMedicalDevicesList);
    }

    @FXML
    void addListNamePreviousSurgeries(MouseEvent event) throws SQLException {
        addListItem(PreviousSurgeriesList, addTextAreaPreviousSurgeries);
        insertDataToList("previous_surgeries", addTextAreaPreviousSurgeries, PreviousSurgeriesList);
    }

    @FXML
    void removeListFoodMedicine(MouseEvent event) {
        removeListItem(FoodMedicineAllergyList);

    }

    @FXML
    void removeListItemFamilyIllness(MouseEvent event) {
        removeListItem(FamilyIllnessList);

    }

    @FXML
    void removeListNameCurrentMedication(MouseEvent event) {
        removeListItem(CurrentMedicationList);
    }

    @FXML
    void removeListNameInstalledMedicalDevices(MouseEvent event) {
        removeListItem(InstalledMedicalDevicesList);
    }

    @FXML
    void removeListNamePreviousSurgeries(MouseEvent event) {
        removeListItem(PreviousSurgeriesList);
    }

    public void cancelButtonOnclick(ActionEvent actionEvent) {
        try {
            PopupManager.removePopup();
        } catch (Exception ignore) {
        }

    }


    void removeListItem(ListView<String> list) {
        int selectedID = list.getSelectionModel().getSelectedIndex();
        System.out.println(selectedID);
        list.getItems().remove(selectedID);

    }

    void addListItem(ListView<String> list, TextField textField) {
        list.getItems().add(textField.getText());
        int selectedID = list.getSelectionModel().getSelectedIndex();
    }

    public void insertDataToList(String columnName, TextField textArea, ListView<String> list) throws SQLException {
        String sql = " ";
        if (getIsNull(columnName, list)) {
            sql = """
                    INSERT INTO patient_background (%$ , index_id, patient_id)
                    VALUES (?, ?, ?)
                    """.replace("%$", columnName);
            PreparedStatement preparedStatement = Driver.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, textArea.getText());
            preparedStatement.setInt(2, (list.getItems().size()  ));
            preparedStatement.setInt(3, clickedPatientId);
            preparedStatement.execute();
            textArea.clear();
        } else {
            sql = """
                      UPDATE patient_background
                      SET %$ = ?
                      WHERE index_id = ?;
                    """.replace("%$", columnName);
            PreparedStatement preparedStatement = Driver.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, textArea.getText());
            preparedStatement.setInt(2, list.getItems().size() );
            preparedStatement.execute();
            textArea.clear();
        }

    }

    public boolean getIsNull(String columnName, ListView<String> list) throws SQLException {
        String sql = """
                SELECT %$
                    FROM patient_background
                    WHERE index_id = ?
                    """.replace("%$", columnName);
        PreparedStatement preparedStatement = Driver.getConnection().prepareStatement(sql);
        preparedStatement.setInt(1, list.getItems().size());
        ResultSet result = preparedStatement.executeQuery();
        //returns true if it is null
        String stringResult = "";
        if (result.next()){
            stringResult = result.getString(columnName);
        }
        System.out.println(stringResult.equals("-1"));
        return !stringResult.equals("-1");
    }
}

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


        setPatientNameLabel();
        setPatientClickedId(clickedPatientId);

        Popups.PATIENTBACKGROUND.getPopup().showingProperty().addListener((observableValue, aBoolean, t1) -> {
            if (Popups.PATIENTBACKGROUND.getPopup().isShowing()) {
                refreshElements();
                patientNameLabel.setText(String.valueOf(setPatientNameLabel()));



            }
        });
        // usablePopupContainer = popupContainer;
    }

    private void refreshElements() {
        patientNameLabel.setText("");
    }

    public static void setPatientClickedId(int clickedPatientId) {
        Popups.PATIENTBACKGROUND.getPopup().showingProperty().addListener((observableValue, aBoolean, t1) -> {
            PatientBackgroundPopup.clickedPatientId = clickedPatientId;
            System.out.println(clickedPatientId);
        });
    }

    public String setPatientNameLabel() {
        String answer ="broken";
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
                answer =  ((resultSet.getString("first_name") + (" ") + resultSet.getString("last_name")));
                return  answer;

            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return answer;
    }

    public void insertAppointmentQuery() throws SQLException {
        String sql = """
                insert into patient_background
                values (?, ?,  ?, ?, ?, ?);
                """;

        System.out.println(clickedPatientId);
        PreparedStatement preparedStatement = Driver.getConnection().prepareStatement(sql);
        preparedStatement.setInt(1, clickedPatientId);
        preparedStatement.setString(2, addFoodMedicineAllergyTextArea.getText());
        preparedStatement.setString(3, addTextAreaPreviousSurgeries.getText());
        preparedStatement.setString(4, addTextAreaCurrentMedication.getText());
        preparedStatement.setString(5, addTextAreaMedicalDevices.getText());
        preparedStatement.setString(6, addFamilyIllnessTextArea.getText());
        preparedStatement.execute();

    }

    @FXML
    void addListItemFamilyIllness(MouseEvent event) throws SQLException{
        addListItem(FamilyIllnessList, addFamilyIllnessTextArea);
        insertDataToList("family_illness", addFamilyIllnessTextArea);
    }

    @FXML
    void addListNameCurrentMedication(MouseEvent event) throws SQLException{
        addListItem(CurrentMedicationList, addTextAreaCurrentMedication);
        insertDataToList("current_medication", addTextAreaCurrentMedication);

    }

    @FXML
    void addListNameFoodMedicine(MouseEvent event) throws SQLException{
        addListItem(FoodMedicineAllergyList, addFoodMedicineAllergyTextArea);
       insertDataToList("allergies", addFoodMedicineAllergyTextArea);
    }

    @FXML
    void addListNameInstalledMedicalDevices(MouseEvent event) throws SQLException{
        addListItem(InstalledMedicalDevicesList, addTextAreaMedicalDevices);
        insertDataToList("installed_devices", addTextAreaMedicalDevices);
    }

    @FXML
    void addListNamePreviousSurgeries(MouseEvent event) throws SQLException {
        addListItem(PreviousSurgeriesList, addTextAreaPreviousSurgeries);
        insertDataToList("previous_surgeries", addTextAreaPreviousSurgeries);
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
        } catch (Exception ignore) {}

    }

    public void submitButtonOnclick(ActionEvent actionEvent) throws SQLException  {
    insertAppointmentQuery();

    }

    void removeListItem(ListView<String> list) {
        int selectedID = list.getSelectionModel().getSelectedIndex();
        list.getItems().remove(selectedID);
    }

    void addListItem(ListView<String> list, TextField textField) {
        list.getItems().add(textField.getText());

    }

    public  void insertDataToList(String columnName, TextField textArea) throws  SQLException{
        String sql = """
                UPDATE patient_background
                SET %$ = ?
                WHERE patient_background.patient_id = ? 
                """.replace("%$", columnName);


        PreparedStatement preparedStatement = Driver.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, textArea.getText());
        preparedStatement.setInt(2, clickedPatientId);
        preparedStatement.execute();
        textArea.clear();

    }
}

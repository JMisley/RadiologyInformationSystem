package com.risjavafx.pages.referrals.background;

import com.risjavafx.pages.Loadable;
import com.risjavafx.pages.LoadingService;
import com.risjavafx.pages.referrals.ViewReferralsPopup;
import com.risjavafx.popups.PopupManager;
import com.risjavafx.popups.Popups;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;


public class PatientBackgroundPopup {

    private static int clickedPatientId;

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

    public static void setPatientClickedId(int clickedPatientId) {
        Popups.PATIENTBACKGROUND.getPopup().showingProperty().addListener((observableValue, aBoolean, t1) -> {
            PatientBackgroundPopup.clickedPatientId = clickedPatientId;
        });

    }

    @FXML
    void addListItemFamilyIllness(MouseEvent event) {
        FamilyIllnessList.getItems().add(addFamilyIllnessTextArea.getText());

    }

    @FXML
    void addListNameCurrentMedication(MouseEvent event) {
        CurrentMedicationList.getItems().add(addTextAreaCurrentMedication.getText());

    }

    @FXML
    void addListNameFoodMedicine(MouseEvent event) {
        FoodMedicineAllergyList.getItems().add(addFoodMedicineAllergyTextArea.getText());

    }

    @FXML
    void addListNameInstalledMedicalDevices(MouseEvent event) {
        InstalledMedicalDevicesList.getItems().add(addTextAreaMedicalDevices.getText());

    }

    @FXML
    void addListNamePreviousSurgeries(MouseEvent event) {
        PreviousSurgeriesList.getItems().add(addTextAreaPreviousSurgeries.getText());
    }

    @FXML
    void removeListFoodMedicine(MouseEvent event) {
        int selectedID = FoodMedicineAllergyList.getSelectionModel().getSelectedIndex();
        FoodMedicineAllergyList.getItems().remove(selectedID);
    }

    @FXML
    void removeListItemFamilyIllness(MouseEvent event) {
        int selectedID = FamilyIllnessList.getSelectionModel().getSelectedIndex();
        FamilyIllnessList.getItems().remove(selectedID);
    }

    @FXML
    void removeListNameCurrentMedication(MouseEvent event) {
        int selectedID = CurrentMedicationList.getSelectionModel().getSelectedIndex();
        CurrentMedicationList.getItems().remove(selectedID);
    }

    @FXML
    void removeListNameInstalledMedicalDevices(MouseEvent event) {
        int selectedID = InstalledMedicalDevicesList.getSelectionModel().getSelectedIndex();
        InstalledMedicalDevicesList.getItems().remove(selectedID);
    }

    @FXML
    void removeListNamePreviousSurgeries(MouseEvent event) {
        int selectedID = PreviousSurgeriesList.getSelectionModel().getSelectedIndex();
        PreviousSurgeriesList.getItems().remove(selectedID);
    }

    public void returnToPage(ActionEvent actionEvent) {
        try {
            PopupManager.removePopup();
        } catch (Exception ignore) {}

    }

    public void submitChanges(ActionEvent actionEvent) {


    }
}

package com.risjavafx.pages.referrals.background;
import com.risjavafx.Driver;
import com.risjavafx.pages.Loadable;
import com.risjavafx.popups.PopupManager;
import com.risjavafx.popups.Popups;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Popups.PATIENTBACKGROUND.getPopup().showingProperty().addListener((observableValue, aBoolean, t1) -> {
            if (Popups.PATIENTBACKGROUND.getPopup().isShowing()) {
                refreshElements();
                setPatientClickedId(clickedPatientId);
                try {
                    populateList(FamilyIllnessList, "family_illness");
                    populateList(InstalledMedicalDevicesList, "installed_devices");
                    populateList(CurrentMedicationList, "current_medication");
                    populateList(FoodMedicineAllergyList, "allergies");
                    populateList(PreviousSurgeriesList, "previous_surgeries");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void refreshElements() {
    }

    public static void setPatientClickedId(int clickedPatientId) {
        PatientBackgroundPopup.clickedPatientId = clickedPatientId;
        System.out.println(clickedPatientId);
    }

    void removeListItem(ListView<String> list, String columnName) throws SQLException {
        int selectedID = list.getSelectionModel().getSelectedIndex();
        String sql = """
                UPDATE patient_background
                SET %$ = null
                WHERE (index_id = ? AND patient_id = ?)
                """.replace("%$", columnName);
        PreparedStatement preparedStatement = Driver.getConnection().prepareStatement(sql);
        preparedStatement.setInt(1, list.getItems().size());
        preparedStatement.setInt(2, clickedPatientId);

        shiftColumnUp(columnName, selectedID);
        preparedStatement.execute();
        deleteRow(list);

        list.getItems().remove(selectedID);
    }

    void shiftColumnUp(String columnName, int currentIndex) throws SQLException {
        String sql = """
                UPDATE patient_background p1
                LEFT OUTER JOIN patient_background p2
                ON p1.index_id = p2.index_id - 1 AND p1.patient_id = ? AND p2.patient_id = ?
                SET p1.%$ = p2.%$
                WHERE p1.patient_id = ? AND p1.index_id > ?;
                """.replace("%$", columnName);
        PreparedStatement preparedStatement = Driver.getConnection().prepareStatement(sql);
        preparedStatement.setInt(1, clickedPatientId);
        preparedStatement.setInt(2, clickedPatientId);
        preparedStatement.setInt(3, clickedPatientId);
        preparedStatement.setInt(4, currentIndex);

        preparedStatement.execute();
    }

    private void deleteRow(ListView<String> list) throws SQLException {
        String sql1 =  """
                SELECT *
                FROM patient_background
                WHERE index_id = ? AND patient_id = ?
                """;
        PreparedStatement preparedStatement = Driver.getConnection().prepareStatement(sql1);
        preparedStatement.setInt(1, list.getItems().size());
        preparedStatement.setInt(2, clickedPatientId);
        ResultSet resultSet = preparedStatement.executeQuery();

        int col = 0, count = 0;
        while (resultSet.next()) {
            col++;
            if (resultSet.getString(col) == (null))
                count++;

        }
        preparedStatement.close();

        if (count == 5) {
            String sql2 = """
                DELETE FROM patient_background
                WHERE index_id = ? AND patient_id = ?
                """;
            preparedStatement = Driver.getConnection().prepareStatement(sql2);
            preparedStatement.setInt(1, list.getItems().size());
            preparedStatement.setInt(2, clickedPatientId);
            preparedStatement.execute();
        }
    }

    void addListItem(ListView<String> list, TextField textField) {
        list.getItems().add(textField.getText());

    }

    public void insertDataToList(String columnName, TextField textArea, ListView<String> list) throws SQLException {
        String sql;
        if (getIsNull(columnName, list)) {
            sql = """
                    INSERT INTO patient_background (%$ , index_id, patient_id)
                    VALUES (?, ?, ?)
                    """.replace("%$", columnName);
            PreparedStatement preparedStatement = Driver.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, textArea.getText());
            preparedStatement.setInt(2, (list.getItems().size()));
            preparedStatement.setInt(3, clickedPatientId);
            preparedStatement.execute();
            textArea.clear();
        } else {
            sql = """
                      UPDATE patient_background
                      SET %$ = ?
                      WHERE index_id = ? AND patient_id = ?;
                    """.replace("%$", columnName);
            PreparedStatement preparedStatement = Driver.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, textArea.getText());
            preparedStatement.setInt(2, list.getItems().size());
            preparedStatement.setInt(3, clickedPatientId);
            preparedStatement.execute();
            textArea.clear();
        }
    }

    public boolean getIsNull(String columnName, ListView<String> list) throws SQLException {
        String sql = """
                SELECT %$
                    FROM patient_background
                    WHERE index_id = ? AND patient_id = ?
                    """.replace("%$", columnName);
        PreparedStatement preparedStatement = Driver.getConnection().prepareStatement(sql);
        preparedStatement.setInt(1, list.getItems().size());
        preparedStatement.setInt(2, clickedPatientId);
        ResultSet result = preparedStatement.executeQuery();
        String stringResult = "";
        if (result.next()) {
            stringResult = result.getString(columnName);
        }
        System.out.println(stringResult == (null));
        return stringResult != (null);
    }

    public void populateList(ListView<String> list, String columnName) throws SQLException {
        String sql = """
                SELECT %$
                FROM patient_background
                WHERE patient_id = ?
                """.replace("%$", columnName);
        PreparedStatement preparedStatement = Driver.getConnection().prepareStatement(sql);
        preparedStatement.setInt(1, clickedPatientId);
        ResultSet result = preparedStatement.executeQuery();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        while (result.next()) {
            if (result.getString(columnName) != (null)) {
                observableList.add(result.getString(columnName));
            }
        }
        list.setItems(observableList);
    }

    @FXML
    void addListItemFamilyIllness() throws SQLException {
        addListItem(FamilyIllnessList, addFamilyIllnessTextArea);
        insertDataToList("family_illness", addFamilyIllnessTextArea, FamilyIllnessList);
    }

    @FXML
    void addListNameCurrentMedication() throws SQLException {
        addListItem(CurrentMedicationList, addTextAreaCurrentMedication);
        insertDataToList("current_medication", addTextAreaCurrentMedication, CurrentMedicationList);
    }

    @FXML
    void addListNameFoodMedicine() throws SQLException {
        addListItem(FoodMedicineAllergyList, addFoodMedicineAllergyTextArea);
        insertDataToList("allergies", addFoodMedicineAllergyTextArea, FoodMedicineAllergyList);
    }

    @FXML
    void addListNameInstalledMedicalDevices() throws SQLException {
        addListItem(InstalledMedicalDevicesList, addTextAreaMedicalDevices);
        insertDataToList("installed_devices", addTextAreaMedicalDevices, InstalledMedicalDevicesList);
    }

    @FXML
    void addListNamePreviousSurgeries() throws SQLException {
        addListItem(PreviousSurgeriesList, addTextAreaPreviousSurgeries);
        insertDataToList("previous_surgeries", addTextAreaPreviousSurgeries, PreviousSurgeriesList);
    }

    @FXML
    void removeListFoodMedicine() throws SQLException {
        removeListItem(FoodMedicineAllergyList, "allergies");
    }

    @FXML
    void removeListItemFamilyIllness() throws SQLException {
        removeListItem(FamilyIllnessList, "family_illness");
    }

    @FXML
    void removeListNameCurrentMedication() throws SQLException {
        removeListItem(CurrentMedicationList, "current_medication");
    }

    @FXML
    void removeListNameInstalledMedicalDevices() throws SQLException {
        removeListItem(InstalledMedicalDevicesList, "installed_devices");
    }

    @FXML
    void removeListNamePreviousSurgeries() throws SQLException {
        removeListItem(PreviousSurgeriesList, "previous_surgeries");
    }

    public void cancelButtonOnclick() {
        try {
            PopupManager.removePopup();
        } catch (Exception ignore) {
        }
    }
}

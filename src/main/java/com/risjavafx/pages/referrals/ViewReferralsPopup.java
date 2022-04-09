package com.risjavafx.pages.referrals;

import com.risjavafx.Driver;
import com.risjavafx.Miscellaneous;
import com.risjavafx.pages.PageManager;
import com.risjavafx.pages.appointments.AppointmentData;
import com.risjavafx.popups.PopupManager;
import com.risjavafx.popups.Popups;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.sql.SQLException;

public class ViewReferralsPopup implements Initializable  {

    public VBox popupContainer;
    public Button returnButton;
    public Button submitButton;
    public Button viewImagesButton;
    public ComboBox<String> appointmentsComboBox;
    public static int  clickedPatientId;

    Driver driver = new Driver();
    Miscellaneous misc = new Miscellaneous();

    public ViewReferralsPopup() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resizeElements();

        Popups.VIEW_REFERRALS.getPopup().showingProperty().addListener((observableValue, aBoolean, t1) -> {
            PageManager.getRoot().setDisable(!aBoolean);
            populateComboBoxAppointment();
        });
    }

    private void resizeElements() {
        Miscellaneous misc = new Miscellaneous();

        popupContainer.setPrefHeight(Popups.getLargeMenuDimensions()[0]);
        popupContainer.setPrefWidth(Popups.getLargeMenuDimensions()[1]);
        popupContainer.setMaxHeight(Popups.getLargeMenuDimensions()[0]);
        popupContainer.setMaxWidth(Popups.getLargeMenuDimensions()[1]);

        for (Control control : new Control[] {returnButton, submitButton, viewImagesButton, appointmentsComboBox}) {
            control.setPrefHeight(40);
            control.setPrefWidth(misc.getScreenWidth() * .15);
            control.setStyle("-fx-font-size: 14px");
        }
    }
 public  static void setPatientClickedId(int clickedPatientId){
        ViewReferralsPopup.clickedPatientId = clickedPatientId;
 }
 public static int getPatientClickedId() {
 return ViewReferralsPopup.clickedPatientId;
 }

    public void populateComboBoxAppointment(){
        try {
            String sql = """
                    SELECT date_time
                    FROM  appointments
                    WHERE  appointments.patient = ?
                     """;
            ObservableList<String> oblist = FXCollections.observableArrayList();
            PreparedStatement preparedStatement = driver.connection.prepareStatement(sql);
            System.out.print(getPatientClickedId());
            preparedStatement.setInt(1, getPatientClickedId());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                oblist.add(resultSet.getString("date_time"));
            }
            appointmentsComboBox.setItems(oblist);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void returnToPage() {
        try {
            PopupManager.removePopup("MENU");
        }
        catch (Exception ignore) {}
    }

    public void submitChanges() {
        try {
            PopupManager.removePopup("MENU");
        }
        catch (Exception ignore) {}
    }
}

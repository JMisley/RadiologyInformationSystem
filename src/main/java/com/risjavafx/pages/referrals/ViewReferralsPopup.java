package com.risjavafx.pages.referrals;

import com.risjavafx.Driver;
import com.risjavafx.Miscellaneous;
import com.risjavafx.pages.PageManager;
import com.risjavafx.popups.PopupManager;
import com.risjavafx.popups.Popups;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.sql.SQLException;

public class ViewReferralsPopup implements Initializable {

    public VBox popupContainer;
    public Button returnButton;
    public Button submitButton;
    public Button viewImagesButton;
    public ComboBox<String> appointmentsComboBox;
    public ComboBox<Integer> ordersComboBox;
    public static int clickedPatientId;
    public TextArea notesTextArea;
    public TextArea reportTextArea;

    Driver driver = new Driver();

    public ViewReferralsPopup() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resizeElements();
        setAppointmentsComboBoxListener();
        Popups.VIEW_REFERRALS.getPopup().showingProperty().addListener((observableValue, aBoolean, t1) -> {
            PageManager.getRoot().setDisable(!aBoolean);
            populateComboBoxAppointment();
            populateComboBoxOrder();
            populateNotesTextArea();
            populateReportTextArea();
        });
    }

    private void resizeElements() {
        Miscellaneous misc = new Miscellaneous();

        popupContainer.setPrefHeight(Popups.getLargeMenuDimensions()[0]);
        popupContainer.setPrefWidth(Popups.getLargeMenuDimensions()[1]);
        popupContainer.setMaxHeight(Popups.getLargeMenuDimensions()[0]);
        popupContainer.setMaxWidth(Popups.getLargeMenuDimensions()[1]);

        for (Control control : new Control[]{returnButton, submitButton, viewImagesButton, appointmentsComboBox}) {
            control.setPrefHeight(40);
            control.setPrefWidth(misc.getScreenWidth() * .15);
            control.setStyle("-fx-font-size: 14px");
        }
    }

    public static void setPatientClickedId(int clickedPatientId) {
        ViewReferralsPopup.clickedPatientId = clickedPatientId;
    }

    public static int getPatientClickedId() {
        return ViewReferralsPopup.clickedPatientId;
    }

    public void populateComboBoxAppointment() {
        try {
            String sql = """
                    SELECT date_time
                    FROM  appointments
                    WHERE  appointments.patient = ?
                     """;
            ObservableList<String> oblist = FXCollections.observableArrayList();
            PreparedStatement preparedStatement = driver.connection.prepareStatement(sql);
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

    public void populateComboBoxOrder() {
        appointmentsComboBox.valueProperty().addListener(observable -> {
            try {
                String sql = """
                        SELECT orders.order_id
                        FROM orders, appointments
                        WHERE orders.appointment = appointment_id AND appointments.date_time = ?
                         """;
                ObservableList<Integer> oblist = FXCollections.observableArrayList();
                PreparedStatement preparedStatement = driver.connection.prepareStatement(sql);
                preparedStatement.setString(1, appointmentsComboBox.getValue());
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    oblist.add(resultSet.getInt("order_id"));
                }
                ordersComboBox.setItems(oblist);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public void populateNotesTextArea() {
        ordersComboBox.valueProperty().addListener(observable -> {
            try {
                String sql = """
                        SELECT notes
                        FROM orders
                        WHERE orders.order_id = ?
                        """;
                PreparedStatement preparedStatement = driver.connection.prepareStatement(sql);
                preparedStatement.setInt(1, ordersComboBox.getValue());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    notesTextArea.setText(resultSet.getString("notes"));
                }
            } catch (Exception ignored) {
            }
        });
    }

    public void populateReportTextArea() {
        ordersComboBox.valueProperty().addListener(observable -> {
            try {
                String sql = """
                        SELECT report
                        FROM orders
                        WHERE orders.order_id = ?
                        """;
                PreparedStatement preparedStatement = driver.connection.prepareStatement(sql);
                preparedStatement.setInt(1, ordersComboBox.getValue());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    reportTextArea.setText(resultSet.getString("report"));
                }
            } catch (Exception ignored) {
            }
        });
    }

    public String[] getDataToInsert() throws SQLException {
        String sql = """
                    SELECT patients.first_name, patients.last_name, modalities.name
                FROM appointments,
                     patients,
                     modalities
                WHERE appointments.date_time =  ?
                  AND appointments.patient = patients.patient_id
                  AND appointments.modality = modalities.modality_id;
                """;
        PreparedStatement preparedStatement = this.driver.connection.prepareStatement(sql);
        preparedStatement.setString(1, appointmentsComboBox.getValue());
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return new String[]{resultSet.getString("first_name") + " " + resultSet.getString("last_name"), resultSet.getString("name")};
        }
        return null;
    }

    public void setAppointmentsComboBoxListener() {
        appointmentsComboBox.valueProperty().addListener(observable -> {
            String str = appointmentsComboBox.getValue();
            System.out.println(str);
        });
    }

    public void returnToPage() {
        try {
            PopupManager.removePopup("MENU");
        } catch (Exception ignore) {
        }
    }

    public void submitChanges() {
        try {
            PopupManager.removePopup("MENU");
        } catch (Exception ignore) {
        }
    }
}

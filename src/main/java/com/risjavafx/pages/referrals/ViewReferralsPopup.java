package com.risjavafx.pages.referrals;

import com.risjavafx.Driver;
import com.risjavafx.Miscellaneous;
import com.risjavafx.PromptButtonCell;
import com.risjavafx.components.main.Main;
import com.risjavafx.pages.Loadable;
import com.risjavafx.pages.LoadingService;
import com.risjavafx.pages.Pages;
import com.risjavafx.pages.images.ImagesPage;
import com.risjavafx.popups.PopupManager;
import com.risjavafx.popups.Popups;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.sql.SQLException;

public class ViewReferralsPopup implements Initializable, Loadable {

    @FXML private VBox popupContainer;
    @FXML private Button returnButton;
    @FXML private Button submitButton;
    @FXML private Button viewImagesButton;
    @FXML private ComboBox<String> appointmentsComboBox;
    @FXML private ComboBox<Integer> ordersComboBox;
    @FXML private TextArea notesTextArea;
    @FXML private TextArea reportTextArea;

    private static int clickedPatientId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resizeElements();
        Popups.VIEW_REFERRALS.getPopup().showingProperty().addListener((observableValue, aBoolean, t1) -> {
            refreshComboBoxes();
            refreshTextAndButtons();
            populateComboBoxAppointment();
            populateComboBoxOrder();
            queryNotesAndReportText();
        });
    }

    public static void setPatientClickedId(int clickedPatientId) {
        ViewReferralsPopup.clickedPatientId = clickedPatientId;
    }

    private void populateComboBoxAppointment() {
        try {
            String sql = """
                    SELECT date_time
                    FROM  appointments
                    WHERE  appointments.patient = ?
                     """;
            ObservableList<String> oblist = FXCollections.observableArrayList();
            PreparedStatement preparedStatement = Driver.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, clickedPatientId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                oblist.add(resultSet.getString("date_time"));
            }
            appointmentsComboBox.setItems(oblist);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void populateComboBoxOrder() {
        appointmentsComboBox.valueProperty().addListener(observable -> {
            try {
                String sql = """
                        SELECT order_id
                        FROM appointments
                        WHERE appointments.date_time = ? AND patient = ?
                         """;
                ObservableList<Integer> oblist = FXCollections.observableArrayList();
                PreparedStatement preparedStatement = Driver.getConnection().prepareStatement(sql);
                preparedStatement.setString(1, appointmentsComboBox.getValue());
                preparedStatement.setInt(2, clickedPatientId);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    oblist.add(resultSet.getInt("order_id"));
                }
                ordersComboBox.setItems(oblist);
                refreshTextAndButtons();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    private void queryNotesAndReportText() {
        ordersComboBox.valueProperty().addListener(observable -> {
            try {
                String sql = """
                        SELECT notes, report
                        FROM orders
                        WHERE orders.order_id = ?
                        """;
                PreparedStatement preparedStatement = Driver.getConnection().prepareStatement(sql);
                preparedStatement.setInt(1, ordersComboBox.getValue());
                ResultSet resultSet = preparedStatement.executeQuery();

                populateTextAreas(resultSet);

            } catch (Exception ignored) {
            }
        });
    }

    private void populateTextAreas(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            if (resultSet.getString("notes").isBlank())
                notesTextArea.setPromptText("No notes have been taken yet");
            else {
                String notesText = resultSet.getString("notes");
                notesTextArea.setText(notesText);
            }

            if (resultSet.getString("report").isBlank())
                reportTextArea.setPromptText("No report has been made yet");
            else {
                String reportText = resultSet.getString("report");
                reportTextArea.setText(reportText);
            }
        }

        submitButton.setDisable(false);
        viewImagesButton.setDisable(false);
    }

    private void resizeElements() {
        Miscellaneous misc = new Miscellaneous();

        popupContainer.setPrefHeight(Popups.getLargeMenuDimensions()[0]);
        popupContainer.setPrefWidth(Popups.getLargeMenuDimensions()[1]);
        popupContainer.setMaxHeight(Popups.getLargeMenuDimensions()[0]);
        popupContainer.setMaxWidth(Popups.getLargeMenuDimensions()[1]);

        ordersComboBox.setButtonCell(new PromptButtonCell<>("Select Order"));
        appointmentsComboBox.setButtonCell(new PromptButtonCell<>("Select Appointment"));

        for (Control control : new Control[]{returnButton, submitButton, viewImagesButton, appointmentsComboBox, ordersComboBox}) {
            control.setPrefHeight(40);
            control.setPrefWidth(misc.getScreenWidth() * .15);
            control.setStyle("-fx-font-size: 14px");
        }
    }

    private void refreshComboBoxes() {
        ordersComboBox.getSelectionModel().clearSelection();
        appointmentsComboBox.getSelectionModel().clearSelection();
    }

    private void refreshTextAndButtons() {
        reportTextArea.clear();
        reportTextArea.setPromptText("");
        notesTextArea.clear();
        notesTextArea.setPromptText("");
        viewImagesButton.setDisable(true);
        submitButton.setDisable(true);
    }

    private void updateTextAreaChanges() throws SQLException {
        
        PreparedStatement preparedStatement;
        final String sql = """
                UPDATE orders
                SET notes = ?, report = ?
                WHERE order_id = ?;
                """;
        preparedStatement = Driver.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, notesTextArea.getText());
        preparedStatement.setString(2, reportTextArea.getText());
        preparedStatement.setInt(3, ordersComboBox.getValue());
        preparedStatement.execute();
    }

    // OnClick listeners
    @FXML
    private void returnToPage() {
        try {
            PopupManager.removePopup();
        } catch (Exception ignore) {}
    }

    @FXML
    private void submitChanges() {
        try {
            updateTextAreaChanges();
            PopupManager.removePopup();

            Loadable loadable = new ViewReferralsPopup();
            String notiHeader = "Submission Complete";
            String notiBody = "You have successfully changed this patient's information";
            LoadingService.CustomReload customReload = new LoadingService.CustomReload(loadable, notiHeader, notiBody);
            customReload.start();
        } catch (Exception ignore) {}
    }

    @FXML
    private void viewImageButtonOnClick() {
        ImagesPage.setOrderId(ordersComboBox.getValue());
        Main.createNewWindow(Pages.IMAGE);
    }
}

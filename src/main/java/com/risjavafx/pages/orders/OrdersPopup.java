package com.risjavafx.pages.orders;

import com.risjavafx.Driver;
import com.risjavafx.Miscellaneous;
import com.risjavafx.pages.PageManager;
import com.risjavafx.popups.models.Notification;
import com.risjavafx.popups.PopupManager;
import com.risjavafx.popups.Popups;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.risjavafx.popups.models.PopupAlert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class OrdersPopup implements Initializable {
    public VBox popupContainer;
    public Label orderIDLabel;
    public ComboBox<String> referralMdComboBox;
    public ComboBox<String> modalityComboBox;
    public ComboBox<String> patientComboBox;
    public TextArea notesTextArea;
    public TextArea reportTextArea;
    public Button cancelButton;
    public Button submitButton;
    
    Miscellaneous misc = new Miscellaneous();

    public void initialize(URL url, ResourceBundle resourceBundle) {
        resizeElements();
        setOrderIDLabel();
        populateComboBoxReferralMd();
        // populateComboBoxAppointment();
        populateComboBoxPatient();
        populateComboBoxModality();
        //setAppointmentComboBoxListener();

        Popups.ORDERS.getPopup().showingProperty().addListener((observableValue, aBoolean, t1) -> {
            PageManager.getRoot().setDisable(!aBoolean);

            if (Popups.ORDERS.getPopup().isShowing()) {
                orderIDLabel.setText(String.valueOf(setOrderIDLabel()));
                refreshTextFields();


            }
        });
    }

    public int setOrderIDLabel() {
        try {
            String sql = """
                    select MAX(order_id)
                    from orders;
                    """;
            ResultSet resultSet = Driver.getConnection().createStatement().executeQuery(sql);

            if (resultSet.next()) {
                return resultSet.getInt("MAX(order_id)") + 1;
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }
        return -1;
    }

    public void populateComboBoxModality() {
        try {
            String sql = """
                    select name
                    from modalities;
                    """;
            ResultSet resultSet = Driver.getConnection().createStatement().executeQuery(sql);
            ObservableList<String> oblist = FXCollections.observableArrayList();
            while (resultSet.next()) {
                oblist.add(resultSet.getString("name"));
            }
            modalityComboBox.setItems(oblist);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void populateComboBoxReferralMd() {
        try {
            String sql = """               
                    SELECT full_name
                    FROM users, users_roles
                    where users_roles.role_id = 3 AND users_roles.user_id = users.user_id;
                    """;
            ResultSet resultSet = Driver.getConnection().createStatement().executeQuery(sql);
            ObservableList<String> oblist = FXCollections.observableArrayList();
            while (resultSet.next()) {
                oblist.add(resultSet.getString("full_name"));
            }
            referralMdComboBox.setItems(oblist);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void populateComboBoxPatient() {
        try {
            String sql = """               
                    SELECT first_name, last_name
                    FROM patients
                    """;
            ResultSet resultSet = Driver.getConnection().createStatement().executeQuery(sql);
            ObservableList<String> oblist = FXCollections.observableArrayList();
            while (resultSet.next()) {
                oblist.add(resultSet.getString("first_name") + " " + resultSet.getString("last_name"));
            }
            patientComboBox.setItems(oblist);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void insertOrderQuery() throws SQLException {
        String sql = """
                insert into orders
                values (?, ?, ?, ?, ?, ?, ?);
                                
                """;
        PreparedStatement preparedStatement = Driver.getConnection().prepareStatement(sql);
        preparedStatement.setInt(1, Integer.parseInt(this.orderIDLabel.getText()));
        preparedStatement.setString(2, patientComboBox.getValue());
        preparedStatement.setString(3, referralMdComboBox.getValue());
        preparedStatement.setString(4, modalityComboBox.getValue());
        preparedStatement.setString(5, this.notesTextArea.getText());
        preparedStatement.setString(6, null);
        preparedStatement.setString(7, this.reportTextArea.getText());
        preparedStatement.execute();
    /*
        String sqlAfter = """
                 update orders 
                 where orders.order_id = ? 
                 set
                 """
        PreparedStatement insertAfter = this.Driver.getConnection().prepareStatement((sqlAfter));
    */
    }

    public boolean validInput() {
        return referralMdComboBox.getValue() != null &&
               modalityComboBox.getValue() != null &&
               patientComboBox.getValue() != null;
    }

    public void resizeElements() {
        this.popupContainer.setPrefHeight(Popups.getMenuDimensions()[0]);
        this.popupContainer.setPrefWidth(Popups.getMenuDimensions()[1]);
        this.popupContainer.setMaxHeight(Popups.getMenuDimensions()[0]);
        this.popupContainer.setMaxWidth(Popups.getMenuDimensions()[1]);
        this.cancelButton.setPrefHeight(this.misc.getScreenWidth() * 0.033D);
        this.cancelButton.setPrefWidth(this.misc.getScreenWidth() * 0.11D);
        this.submitButton.setPrefHeight(this.misc.getScreenWidth() * 0.033D);
        this.submitButton.setPrefWidth(this.misc.getScreenWidth() * 0.11D);
        double fontSize = Math.min(this.misc.getScreenWidth() / 80.0D, 20.0D);

        this.cancelButton.setStyle("-fx-font-size: " + fontSize);
        this.submitButton.setStyle("-fx-font-size: " + fontSize);
    }

    private void refreshTextFields() {
        referralMdComboBox.getSelectionModel().clearSelection();
        // appointmentComboBox.getSelectionModel().clearSelection();
        reportTextArea.clear();
        notesTextArea.clear();
    }

    public void submitButtonOnclick() throws SQLException {
        if (this.validInput()) {
            this.insertOrderQuery();
            //this.insertOrderIdQuery();
            Orders.queryData(Orders.getLastRowStringQuery());
            PopupManager.removePopup("MENU");
            Notification.createNotification("Submission Complete", "You have successfully added a new order");
        } else if (!validInput()) {
            PopupManager.createPopup(Popups.ALERT);
            new PopupAlert() {{
                setHeaderLabel("Submission Failed");
                setContentLabel("Please make sure you filled out all fields");
                setExitButtonLabel("Retry");
            }};
        }
    }

    public void cancelButtonOnclick() {
        try {
            PopupManager.removePopup("MENU");
        } catch (Exception ignored) {}
    }
}
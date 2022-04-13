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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import com.risjavafx.popups.models.PopupAlert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class OrdersEditPopup implements Initializable {
    public VBox popupContainer;
    public static VBox usablePopupContainer;
    public Label orderIDLabel;
    public ComboBox<String> patientNameComboBox;
    public ComboBox<String> referralMdComboBox;
    public ComboBox<String> modalityComboBox;
    public ComboBox<String> appointmentComboBox;
    public TextArea notesTextArea;
    public ComboBox<String> statusComboBox;
    public TextArea reportTextArea;
    public Button cancelButton;
    public Button submitButton;
    public static int clickedOrderId;

    Driver driver = new Driver();
    Miscellaneous misc = new Miscellaneous();

    public OrdersEditPopup() throws SQLException {
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        resizeElements();
        setOrderIDLabel();
        populateComboBoxReferralMd();
        //populateComboBoxAppointment();
        //setAppointmentComboBoxListener();
        Popups.ORDERS_EDIT.getPopup().showingProperty().addListener((observableValue, aBoolean, t1) -> {
            PageManager.getRoot().setDisable(!aBoolean);

            if (Popups.ORDERS_EDIT.getPopup().isShowing()) {
                orderIDLabel.setText(String.valueOf(getOrderClickedId()));
                refreshTextFields();
            }
        });
        usablePopupContainer = this.popupContainer;
    }

    public int setOrderIDLabel() {
        try {
            String sql = """
                    select MAX(order_id)
                    from orders;
                    """;
            ResultSet resultSet = this.driver.connection.createStatement().executeQuery(sql);

            if (resultSet.next()) {
                return resultSet.getInt("MAX(order_id)") + 1;
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }
        return -1;
    }


    public void populateComboBoxReferralMd() {
        try {
            String sql = """               
                    SELECT full_name
                    FROM users, users_roles
                    where users_roles.role_id = 3 AND users_roles.user_id = users.user_id;
                    """;
            ResultSet resultSet = driver.connection.createStatement().executeQuery(sql);
            ObservableList<String> oblist = FXCollections.observableArrayList();
            while (resultSet.next()) {
                oblist.add(resultSet.getString("full_name"));
            }
            referralMdComboBox.setItems(oblist);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

//    public void populateComboBoxAppointment() {
//        try {
//            String sql = """
//                    SELECT last_name, date_time
//                                        FROM db_ris.patients, db_ris.appointments
//                                        where patients.patient_id = appointments.patient;
//                    """;
//
//            ResultSet resultSet = driver.connection.createStatement().executeQuery(sql);
//            ObservableList<String> oblist = FXCollections.observableArrayList();
//            while (resultSet.next()) {
//
//                oblist.add(resultSet.getString("last_name") + ": " + resultSet.getString("date_time"));
//            }
//            appointmentComboBox.setItems(oblist);
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
//    }

    public static void setOrderClickedId(int orderClickedId) {
        OrdersEditPopup.clickedOrderId = orderClickedId;
    }

    public static int getOrderClickedId() {
        return OrdersEditPopup.clickedOrderId;
    }


    public void updateOrderQuery() throws SQLException {
        String sql = """
                UPDATE orders
                SET referral_md = ?, notes = ?, report = ?
                WHERE orders.order_id = ?;
                """;
        PreparedStatement preparedStatement = this.driver.connection.prepareStatement(sql);
        preparedStatement.setString(1, referralMdComboBox.getValue());
        preparedStatement.setString(2, this.notesTextArea.getText());
        preparedStatement.setString(3, this.reportTextArea.getText());
        preparedStatement.setInt(4, getOrderClickedId());
        preparedStatement.execute();
    /*
        String sqlAfter = """
                 update orders
                 where orders.order_id = ?
                 set
                 """
        PreparedStatement insertAfter = this.driver.connection.prepareStatement((sqlAfter));
    */
    }

//    public String[] getDataToInsert() throws SQLException {
//
//
//
//        String sql = """
//                        SELECT patients.first_name, patients.last_name, modalities.name
//                    FROM appointments,
//                         patients,
//                         modalities
//                    WHERE appointments.date_time =  ?
//                      AND appointments.patient = patients.patient_id
//                      AND appointments.modality = modalities.modality_id;
//                    """;
//        PreparedStatement preparedStatement = this.driver.connection.prepareStatement(sql);
//        preparedStatement.setString(1, returnAppointmentComboBoxDate());
//        ResultSet resultSet = preparedStatement.executeQuery();
//
//
//        if (resultSet.next()) {
//            return new String[]{resultSet.getString("first_name") + " " + resultSet.getString("last_name"), resultSet.getString("name")};
//
//        }
//
//        return null;
//    }

//    public int getDataToInsertAppointmentId() throws SQLException {
//
//
//
//        String sql = """
//                        SELECT appointments.appointment_id
//                    FROM appointments
//
//                    WHERE appointments.date_time =  ?
//
//                    """;
//        PreparedStatement preparedStatement = this.driver.connection.prepareStatement(sql);
//        preparedStatement.setString(1, returnAppointmentComboBoxDate());
//        ResultSet resultSet = preparedStatement.executeQuery();
//
//
//        if (resultSet.next()) {
//            return resultSet.getInt("appointment_id");
//
//        }
//
//        return -1;
//    }




    public boolean validInput() {
        return  this.referralMdComboBox.getValue() != null && !this.notesTextArea.getText().isBlank() &&  !this.reportTextArea.getText().isBlank();
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
        appointmentComboBox.getSelectionModel().clearSelection();
        reportTextArea.clear();
        notesTextArea.clear();
    }

    public void submitButtonOnclick() throws SQLException {
        if (this.validInput()) {
            this.updateOrderQuery();
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
        } catch (Exception ignored) {
        }
    }
    public String returnAppointmentComboBoxDate(){

        String str = appointmentComboBox.getValue();
        String[] arrOfStr = str.split(": ", 2);


        return arrOfStr[1];
    }
    public void setAppointmentComboBoxListener() {
        appointmentComboBox.valueProperty().addListener(observable -> {
            /*
            String Date = appointmentComboBox.getValue();
            ArrayList<String> info = new ArrayList<String>();
            info.add(Arrays.toString(Date.split(": ")));
            System.out.println(info);
            */
            String str = appointmentComboBox.getValue();
            String[] arrOfStr = str.split(": ", 2);

            System.out.println(arrOfStr[1]);
        });
    }
}
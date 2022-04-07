package com.risjavafx.pages.orders;

import com.risjavafx.Driver;
import com.risjavafx.Miscellaneous;
import com.risjavafx.pages.PageManager;
import com.risjavafx.pages.orders.Orders;
import com.risjavafx.popups.models.Notification;
import com.risjavafx.popups.models.PopupAlert;
import com.risjavafx.popups.PopupManager;
import com.risjavafx.popups.Popups;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

public class OrdersPopup implements Initializable {
    public VBox popupContainer;
    public static VBox usablePopupContainer;
    public Label orderIDLabel;
    public ComboBox<String> patientNameComboBox;
    public ComboBox<String> referralMdComboBox;
    public ComboBox<String> modalityComboBox;
    public ComboBox<String> appointmentComboBox;
    public TextField notesTextField;
    public ComboBox<String> statusComboBox;
    public TextField reportTextField;
    public Button cancelButton;
    public Button submitButton;
    Driver driver = new Driver();
    Miscellaneous misc = new Miscellaneous();

    public OrdersPopup() throws SQLException {
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        resizeElements();
        setOrderIDLabel();
        populateComboBoxPatient();
        populateComboBoxreferralMd();
        populateComboBoxModality();
        populateComboBoxAppointment();
        populateComboBoxStatus();
        Popups.ORDERS.getPopup().showingProperty().addListener((observableValue, aBoolean, t1) -> PageManager.getRoot().setDisable(!aBoolean));
        usablePopupContainer = this.popupContainer;
    }

    public void setOrderIDLabel() {
        try {
            String sql = """
                    select MAX(order_id)
                    from orders;
                    """;
            ResultSet resultSet = this.driver.connection.createStatement().executeQuery(sql);

            while(resultSet.next()) {
                this.orderIDLabel.setText(String.valueOf(resultSet.getInt("MAX(order_id)") + 1));
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public void populateComboBoxPatient() {
        try {
            String sql = """
                  SELECT first_name , last_name
                    FROM patients;
                    """;
            ResultSet resultSet = driver.connection.createStatement().executeQuery(sql);
            ObservableList<String> oblist = FXCollections.observableArrayList();
            while (resultSet.next()) {
                oblist.add(resultSet.getString("first_name") +" " + resultSet.getString("last_name"));
            }
            patientNameComboBox.setItems(oblist);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void populateComboBoxreferralMd() {
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

    public void populateComboBoxModality() {
        try {
            String sql = """
                    select name
                    from modalities;
                    """;
            ResultSet resultSet = driver.connection.createStatement().executeQuery(sql);
            ObservableList<String> oblist = FXCollections.observableArrayList();
            while (resultSet.next()) {
                oblist.add(resultSet.getString("name"));
            }
            modalityComboBox.setItems(oblist);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void populateComboBoxAppointment() {
        try {
            String sql = """
                    select appointment_id
                    from appointments;
                    """;
            ResultSet resultSet = driver.connection.createStatement().executeQuery(sql);
            ObservableList<String> oblist = FXCollections.observableArrayList();
            while (resultSet.next()) {
                oblist.add(resultSet.getString("appointment_id"));
            }
            appointmentComboBox.setItems(oblist);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void populateComboBoxStatus() {
        try {
            String sql = """
                    select closed
                    from appointments;
                    """;
            ResultSet resultSet = driver.connection.createStatement().executeQuery(sql);
            ObservableList<String> oblist = FXCollections.observableArrayList();
            while (resultSet.next()) {
                oblist.add(resultSet.getString("closed"));
            }
            statusComboBox.setItems(oblist);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void insertOrderQuery() throws SQLException {
        String sql = """
        insert into orders
        values (?, ?, ?, ?, ?, ?, ?, ?);
        """;
        PreparedStatement preparedStatement = this.driver.connection.prepareStatement(sql);
        preparedStatement.setInt(1, Integer.parseInt(this.orderIDLabel.getText()));
        preparedStatement.setInt(2,  pullPatientComboboxId(patientNameComboBox.getValue()));
        preparedStatement.setInt(3, pullDocComboBoxId(referralMdComboBox.getValue()));
        preparedStatement.setInt(4, pullModalityComboBoxId(modalityComboBox.getValue()));
        preparedStatement.setInt(5, pullAppointmentComboBoxId(appointmentComboBox.getValue()));
        preparedStatement.setString(6, this.notesTextField.getText());
        preparedStatement.setInt(7, pullStatusComboBoxId(statusComboBox.getValue()));
        preparedStatement.setString(8, this.reportTextField.getText());
        preparedStatement.execute();
    }

//    public void insertOrderIdQuery() throws SQLException {
//        String sql = """
//                insert into orders
//                values (?, ?);
//                """;
//        PreparedStatement preparedStatement = driver.connection.prepareStatement(sql);
//        preparedStatement.setInt(1, Integer.parseInt(orderIDLabel.getText()));
//        preparedStatement.setInt(2, getOrderId(orderIDLabel.getValue()));
//        preparedStatement.setInt(2, Integer.parseInt(orderIDLabel.getText()));
//        preparedStatement.execute();
//    }
//
//    public int getOrderId(String order) throws SQLException {
//        String sql = """
//                select order_id
//                from orders;
//                """;
//        PreparedStatement preparedStatement = this.driver.connection.prepareStatement(sql);
//        preparedStatement.setString(1, order);
//        ResultSet resultSet = preparedStatement.executeQuery();
//        resultSet.next();
//        return resultSet.getInt("order_id");
//    }

    public int pullPatientComboboxId(String name) {
        try {
            String patientIdFromCombo = """
                    SELECT patient_id
                    FROM patients
                    WHERE ? = CONCAT(first_name, " ", last_name)
                    """;
            PreparedStatement preparedStatementPatientCombo = driver.connection.prepareStatement(patientIdFromCombo);
            preparedStatementPatientCombo.setString(1, name);
            ResultSet resultSetPatientCombo = preparedStatementPatientCombo.executeQuery();

            int i = 0;
            while (resultSetPatientCombo.next()) {
                i = resultSetPatientCombo.getInt(("patient_id"));
            }
            return i;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return -1;
    }

    public int pullDocComboBoxId(String name) {
        try {
            String techIdFromCombo = """
                    SELECT user_id
                    FROM users
                    WHERE ? =  full_name
                    """;
            PreparedStatement preparedStatementTechCombo = driver.connection.prepareStatement(techIdFromCombo);
            preparedStatementTechCombo.setString(1, name);
            ResultSet resultSetTechCombo = preparedStatementTechCombo.executeQuery();

            int i = 0;
            while (resultSetTechCombo.next()) {
                i = resultSetTechCombo.getInt(("user_id"));
            }
            return i;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return -1;
    }

    public int pullModalityComboBoxId(String insertName) {
        try {
            String modalityIdFromCombo = """
                    SELECT modality_id
                    FROM modalities
                    WHERE name = ?
                    """;
            PreparedStatement preparedStatementModalityCombo = driver.connection.prepareStatement(modalityIdFromCombo);
            preparedStatementModalityCombo.setString(1, insertName);
            ResultSet resultSetTechCombo = preparedStatementModalityCombo.executeQuery();

            int i = 0;
            while (resultSetTechCombo.next()) {
                i = resultSetTechCombo.getInt(("modality_id"));
            }
            return i;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return -1;
    }

    public int pullAppointmentComboBoxId(String insertName) {
        try {
            String modalityIdFromCombo = """
                    SELECT appointment_id
                    FROM appointments
                    WHERE name = ?
                    """;
            PreparedStatement preparedStatementModalityCombo = driver.connection.prepareStatement(modalityIdFromCombo);
            preparedStatementModalityCombo.setString(1, insertName);
            ResultSet resultSetTechCombo = preparedStatementModalityCombo.executeQuery();

            int i = 0;
            while (resultSetTechCombo.next()) {
                i = resultSetTechCombo.getInt(("appointment_id"));
            }
            return i;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return -1;
    }

    public int pullStatusComboBoxId(String insertName) {
        try {
            String modalityIdFromCombo = """
                    SELECT closed
                    FROM appointments
                    WHERE name = ?
                    """;
            PreparedStatement preparedStatementModalityCombo = driver.connection.prepareStatement(modalityIdFromCombo);
            preparedStatementModalityCombo.setString(1, insertName);
            ResultSet resultSetTechCombo = preparedStatementModalityCombo.executeQuery();

            int i = 0;
            while (resultSetTechCombo.next()) {
                i = resultSetTechCombo.getInt(("closed"));
            }
            return i;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return -1;
    }

    public boolean validInput() {
        return this.patientNameComboBox.getValue() != null && this.referralMdComboBox.getValue() != null && this.modalityComboBox.getValue() != null && !this.notesTextField.getText().isBlank() && this.statusComboBox != null && !this.reportTextField.getText().isBlank();
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

    public void submitButtonOnclick() throws SQLException {
        if (this.validInput()) {
            this.insertOrderQuery();
            //this.insertOrderIdQuery();
            Orders.queryData(Orders.getLastRowStringQuery());
            Popups.getMenuPopupEnum().getPopup().hide();
            Notification.createNotification("Submission Complete", "You have successfully added a new order");
        } else if (!this.validInput()) {
            PopupManager.createPopup(Popups.ALERT);
            PopupAlert var10001 = new PopupAlert() {
                {
                    this.setAlertImage(new Image("file:C:/Users/johnn/IdeaProjects/RISJavaFX/src/main/resources/com/risjavafx/images/error.png"));
                    this.setHeaderLabel("Submission Failed");
                    this.setContentLabel("Please make sure you filled out all fields");
                    this.setExitButtonLabel("Retry");
                }
            };
        }

    }

    public void cancelButtonOnclick() {
        Popups.ORDERS.getPopup().hide();

        try {
            PopupManager.removePopup("MENU");
        } catch (Exception ignored) {
        }

    }
}

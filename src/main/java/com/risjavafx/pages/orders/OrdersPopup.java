package com.risjavafx.pages.orders;

import com.risjavafx.Driver;
import com.risjavafx.Miscellaneous;
import com.risjavafx.pages.PageManager;
import com.risjavafx.pages.orders.Orders;
import com.risjavafx.popups.Notification;
import com.risjavafx.popups.PopupAlert;
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
    public TextField patientNameTextField;
    public TextField referralMdTextField;
    public TextField modalityTextField;
    public TextField appointmentTextField;
    public TextField notesTextField;
    public TextField statusTextField;
    public TextField reportTextField;
    public Button cancelButton;
    public Button submitButton;
    Driver driver = new Driver();
    Miscellaneous misc = new Miscellaneous();

    public OrdersPopup() throws SQLException {
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resizeElements();
        this.setOrderIDLabel();
        //this.populateComboBox();
        Popups.ORDERS.getPopup().showingProperty().addListener((observableValue, aBoolean, t1) -> {
            PageManager.getRoot().setDisable(!aBoolean);
        });
        usablePopupContainer = this.popupContainer;
    }

    public void setOrderIDLabel() {
        try {
            String sql = "select MAX(order_id)\nfrom orders;\n";
            ResultSet resultSet = this.driver.connection.createStatement().executeQuery(sql);

            while(resultSet.next()) {
                this.orderIDLabel.setText(String.valueOf(resultSet.getInt("MAX(order_id)") + 1));
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

//    public void populateComboBox() {
//        try {
//            String sql = "select name\nfrom roles;\n";
//            ResultSet resultSet = this.driver.connection.createStatement().executeQuery(sql);
//            ObservableList oblist = FXCollections.observableArrayList();
//
//            while(resultSet.next()) {
//                oblist.add(resultSet.getString("name"));
//            }
//
//            this.roleComboBox.setItems(oblist);
//        } catch (Exception var4) {
//            var4.printStackTrace();
//        }
//
//    }

    public void insertOrderQuery() throws SQLException {
        String sql = "insert into orders\nvalues (?, ?, ?, ?, ?, ?, ?, ?);\n";
        PreparedStatement preparedStatement = this.driver.connection.prepareStatement(sql);
        preparedStatement.setInt(1, Integer.parseInt(this.orderIDLabel.getText()));
        preparedStatement.setString(2, this.patientNameTextField.getText());
        preparedStatement.setString(3, this.referralMdTextField.getText());
        preparedStatement.setString(4, this.modalityTextField.getText());
        preparedStatement.setString(5, this.appointmentTextField.getText());
        preparedStatement.setString(6, this.notesTextField.getText());
        preparedStatement.setString(7, this.statusTextField.getText());
        preparedStatement.setString(8, this.reportTextField.getText());
        //preparedStatement.execute();
    }

    public void insertOrderIdQuery() throws SQLException {
        String sql = "insert into orders\nvalues (?, ?);\n";
        PreparedStatement preparedStatement = this.driver.connection.prepareStatement(sql);
        preparedStatement.setInt(1, Integer.parseInt(this.orderIDLabel.getText()));
        //preparedStatement.setInt(2, this.getRoleId((String)this.roleComboBox.getValue()));
        preparedStatement.setInt(2, Integer.parseInt(this.orderIDLabel.getText()));
        preparedStatement.execute();
    }

    public int getOrderId(String order) throws SQLException {
        String sql = "select order_id\nfrom orders\nwhere name = ?;\n";
        PreparedStatement preparedStatement = this.driver.connection.prepareStatement(sql);
        preparedStatement.setString(1, order);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt("order_id");
    }

    public boolean validInput() {
        return !this.patientNameTextField.getText().isBlank() && !this.referralMdTextField.getText().isBlank() && !this.modalityTextField.getText().isBlank() && !this.notesTextField.getText().isBlank() && !this.statusTextField.getText().isBlank() && !this.reportTextField.getText().isBlank();
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
        double fontSize;
        if (this.misc.getScreenWidth() / 80.0D < 20.0D) {
            fontSize = this.misc.getScreenWidth() / 80.0D;
        } else {
            fontSize = 20.0D;
        }

        this.cancelButton.setStyle("-fx-font-size: " + fontSize);
        this.submitButton.setStyle("-fx-font-size: " + fontSize);
    }

    public void submitButtonOnclick() throws SQLException {
        if (this.validInput()) {
            this.insertOrderQuery();
            this.insertOrderIdQuery();
            Orders.queryData(Orders.getLastRowStringQuery());
            Popups.getMenuPopupEnum().getPopup().hide();
            Notification.createNotification();
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
            Popups.ALERT.getPopup().hide();
        } catch (Exception var2) {
        }

    }
}

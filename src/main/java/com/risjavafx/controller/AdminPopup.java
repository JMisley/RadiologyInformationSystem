package com.risjavafx.controller;

import com.risjavafx.model.Driver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AdminPopup implements Initializable {
    public Label userIDLabel;
    public ComboBox<String> roleComboBox;
    public TextField fullNameTextField;
    public TextField emailTextField;
    public TextField usernameTextField;
    public TextField passwordTextField;

    Driver driver = new Driver();

    public AdminPopup() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUserIDLabel();
        populateComboBox();
    }

    public void setUserIDLabel() {
        try {
            String sql = """
                    select MAX(user_id)
                    from users;
                    """;
            ResultSet resultSet = driver.connection.createStatement().executeQuery(sql);
            while (resultSet.next()) {
                userIDLabel.setText(String.valueOf(resultSet.getInt("MAX(user_id)") + 1));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    public void populateComboBox() {
        try {
            String sql = """
                    select name
                    from roles;
                    """;
            ResultSet resultSet = driver.connection.createStatement().executeQuery(sql);
            ObservableList<String> oblist = FXCollections.observableArrayList();
            while (resultSet.next()) {
                oblist.add(resultSet.getString("name"));
            }
            roleComboBox.setItems(oblist);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    // Onclick for submit button
    public void createNewUser() throws SQLException {
        insertUserQuery();
        insertRoleIdQuery();
        Main.popup.hide();
    }

    public void insertUserQuery() throws SQLException {
        String sql = """
                insert into users
                values (?, ?, ?, ?, ?, ?);
                """;
        PreparedStatement preparedStatement = driver.connection.prepareStatement(sql);
        preparedStatement.setInt(1, Integer.parseInt(userIDLabel.getText()));
        preparedStatement.setString(2, emailTextField.getText().toLowerCase());
        preparedStatement.setString(3, fullNameTextField.getText());
        preparedStatement.setString(4, usernameTextField.getText());
        preparedStatement.setString(5, passwordTextField.getText());
        preparedStatement.setInt(6, 1);
        preparedStatement.execute();
    }

    public void insertRoleIdQuery() throws SQLException {
        String sql = """
                insert into users_roles
                values (?, ?, ?);
                """;
        PreparedStatement preparedStatement = driver.connection.prepareStatement(sql);
        preparedStatement.setInt(1, Integer.parseInt(userIDLabel.getText()));
        preparedStatement.setInt(2, getRoleId(roleComboBox.getValue()));
        preparedStatement.setInt(3, Integer.parseInt(userIDLabel.getText()));
        preparedStatement.execute();
    }

    public int getRoleId(String role) throws SQLException {
        String sql = """
                select role_id
                from roles
                where name = ?;
                """;
        PreparedStatement preparedStatement = driver.connection.prepareStatement(sql);
        preparedStatement.setString(1, role);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt("role_id");
    }
}

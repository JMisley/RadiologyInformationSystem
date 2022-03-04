package com.risjavafx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Login implements Initializable {

    public Button loginButton;
    @FXML
    public Label errorMessage;
    @FXML
    public TextField username;
    @FXML
    public PasswordField password;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        Pages.setPage(Pages.HOME);
    }

    // If entered credentials are authorized, open home page, else return an error message
    public void userLogin() throws IOException, SQLException {
        Main main = new Main();
        if (checkCredentials(username.getText(), password.getText())) {
            main.changeScene("navigation pages/home-page.fxml");
        } else if (username.getText().isBlank() || password.getText().isEmpty()) {
            errorMessage.setText("Please enter all information");
        } else {
            errorMessage.setText("Incorrect login information");
        }
    }

    // If input username and password combination exists in database, return true
    private boolean checkCredentials(String username, String password) throws SQLException {
        com.risjavafx.Driver driver = new Driver();
        PreparedStatement preparedStatement;
        final String sql = """
                SELECT *
                FROM users
                WHERE username = ? AND password = ?
                """;
        preparedStatement = driver.connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();

        return resultSet.next();
    }
}
package com.risjavafx.pages.login;

import com.risjavafx.components.TitleBar;
import com.risjavafx.Driver;
import com.risjavafx.Miscellaneous;
import com.risjavafx.pages.PageManager;
import com.risjavafx.pages.Pages;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Login implements Initializable {

    public BorderPane mainContainer;
    public Button loginButton;
    public Label errorMessage;
    public TextField username;
    public PasswordField password;
    public AnchorPane loginContainer;
    public HBox titleBar;
    Miscellaneous misc = new Miscellaneous();

    public void initialize(URL url, ResourceBundle resourceBundle) {
        TitleBar.createTitleBar(mainContainer, titleBar);
        resizeElements();

        loginContainer.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    userLogin();
                } catch (IOException | SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    // If entered credentials are authorized, open home page, else return an error message
    public void userLogin() throws IOException, SQLException {
        if (checkCredentials(username.getText(), password.getText())) {
            LoadingService loginService = new LoadingService();
            loginService.start();
        } else if (username.getText().isBlank() || password.getText().isEmpty()) {
            errorMessage.setText("Please enter all information");
        } else {
            errorMessage.setText("Incorrect login information");
        }
    }

    // If input username and password combination exists in database, return true
    private boolean checkCredentials(String username, String password) throws SQLException {
        Driver driver = new Driver();
        PreparedStatement preparedStatement;
        final String sql = """
                SELECT *
                FROM users
                WHERE BINARY username = ? AND BINARY password = ?
                """;
        preparedStatement = driver.connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();

        return resultSet.next();
    }

    private void resizeElements() {
        loginContainer.setPrefWidth(misc.getScreenWidth() * .3);
        loginContainer.setPrefHeight(misc.getScreenHeight() * .7);

        loginButton.setStyle("-fx-font-size: 36px");
    }

    public static class LoadingService extends Service<Void> {
        public Task<Void> createTask() {
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() {
                    PageManager.switchPage(Pages.PROGRESS);
                    PageManager.loadPagesToCache();
                    return null;
                }
            };
            task.setOnSucceeded(event -> PageManager.switchPage(Pages.HOME));
            return task;
        }
    }
}
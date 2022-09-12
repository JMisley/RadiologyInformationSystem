package com.risjavafx.pages.admin;

import com.risjavafx.PromptButtonCell;
import com.risjavafx.pages.Loadable;
import com.risjavafx.pages.LoadingService;
import com.risjavafx.pages.Pages;
import com.risjavafx.popups.models.Notification;
import com.risjavafx.popups.models.PopupAlert;
import com.risjavafx.popups.PopupManager;
import com.risjavafx.Driver;
import com.risjavafx.Miscellaneous;
import com.risjavafx.popups.Popups;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AdminPopup implements Initializable, Loadable {
    public VBox popupContainer;
    public static VBox usablePopupContainer;
    public Label userIDLabel;
    public ComboBox<String> roleComboBox;
    public TextField fullNameTextField;
    public TextField emailTextField;
    public TextField usernameTextField;
    public TextField passwordTextField;
    public Button cancelButton;
    public Button submitButton;

    Miscellaneous misc = new Miscellaneous();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resizeElements();
        populateComboBox();

        // Overrides caching functionality
        Popups.ADMIN.getPopup().showingProperty().addListener((observableValue, aBoolean, t1) -> {
            if (Popups.ADMIN.getPopup().isShowing()) {
                userIDLabel.setText(String.valueOf(getNextUserId()));
                refreshElements();
            }
        });
        usablePopupContainer = popupContainer;
    }

    public int getNextUserId() {
        try {
            String sql = """
                    select MAX(user_id)
                    from users;
                    """;
            ResultSet resultSet = Driver.getConnection().createStatement().executeQuery(sql);
            if (resultSet.next()) {
                return resultSet.getInt("MAX(user_id)") + 1;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return -1;
    }

    public void populateComboBox() {
        try {
            String sql = """
                    select name
                    from roles;
                    """;
            ResultSet resultSet = Driver.getConnection().createStatement().executeQuery(sql);
            ObservableList<String> oblist = FXCollections.observableArrayList();
            while (resultSet.next()) {
                oblist.add(resultSet.getString("name"));
            }
            roleComboBox.setItems(oblist);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void insertUserQuery() throws SQLException {
        String sql = """
                insert into users
                values (?, ?, ?, ?, ?, ?);
                """;
        PreparedStatement preparedStatement = Driver.getConnection().prepareStatement(sql);
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
        PreparedStatement preparedStatement = Driver.getConnection().prepareStatement(sql);
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
        PreparedStatement preparedStatement = Driver.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, role);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt("role_id");
    }

    // Returns false if any input field is invalid
    public boolean validInput() {
        return roleComboBox.getValue() != null &&
               !fullNameTextField.getText().isBlank() &&
               !emailTextField.getText().isBlank() &&
               !usernameTextField.getText().isBlank() &&
               !passwordTextField.getText().isBlank();
    }

    private void resizeElements() {
        popupContainer.setPrefHeight(Popups.getMenuDimensions()[0]);
        popupContainer.setPrefWidth(Popups.getMenuDimensions()[1]);
        popupContainer.setMaxHeight(Popups.getMenuDimensions()[0]);
        popupContainer.setMaxWidth(Popups.getMenuDimensions()[1]);

        cancelButton.setPrefHeight(misc.getScreenWidth() * .033);
        cancelButton.setPrefWidth(misc.getScreenWidth() * .11);
        submitButton.setPrefHeight(misc.getScreenWidth() * .033);
        submitButton.setPrefWidth(misc.getScreenWidth() * .11);

        roleComboBox.setButtonCell(new PromptButtonCell<>("Select Role"));

        double fontSize;
        if ((misc.getScreenWidth() / 80) < 20) {
            fontSize = misc.getScreenWidth() / 80;
        } else {
            fontSize = 20;
        }
        cancelButton.setStyle("-fx-font-size: " + fontSize);
        submitButton.setStyle("-fx-font-size: " + fontSize);
    }

    private void refreshElements() {
        roleComboBox.getSelectionModel().clearSelection();
        fullNameTextField.clear();
        emailTextField.clear();
        usernameTextField.clear();
        passwordTextField.clear();
    }

    //Button OnClicks
    // Onclick for submit button
    public void submitButtonOnclick() {
        if (validInput()) {
            try {
                insertUserQuery();
                insertRoleIdQuery();
                Admin.queryData(Admin.getLastRowStringQuery());
                PopupManager.removePopup();

                LoadingService.GlobalResetPageSwitch customReload = new LoadingService.GlobalResetPageSwitch(Pages.HOME);
                customReload.start();
                String notiHeader = "Submission Complete";
                String notiBody = "You have successfully added a new user";
                Notification.createNotification(notiHeader, notiBody);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            PopupManager.createPopup(Popups.ALERT);
            new PopupAlert() {{
                setHeaderLabel("Submission Failed");
                setContentLabel("Please make sure you filled out all fields");
                setExitButtonLabel("Retry");
            }};
        }
    }

    // Onclick for cancel button
    public void cancelButtonOnclick() {
        PopupManager.removePopup();
    }
}

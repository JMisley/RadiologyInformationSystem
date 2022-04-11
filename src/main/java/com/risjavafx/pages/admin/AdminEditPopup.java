package com.risjavafx.pages.admin;

import com.risjavafx.popups.models.PopupAlert;
import com.risjavafx.popups.models.Notification;
import com.risjavafx.pages.PageManager;
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

public class AdminEditPopup implements Initializable {
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
    public static int clickedUserId;

    Driver driver = new Driver();
    Miscellaneous misc = new Miscellaneous();

    public AdminEditPopup() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resizeElements();
        populateComboBox();

        // Overrides caching functionality
        Popups.ADMINEDIT.getPopup().showingProperty().addListener((observableValue, aBoolean, t1) -> {
            PageManager.getRoot().setDisable(!aBoolean);
            populateComboBox();
            if (Popups.ADMINEDIT.getPopup().isShowing()) {
                userIDLabel.setText(String.valueOf(getUserClickedId()));
                refreshElements();
            }
        });
        usablePopupContainer = popupContainer;
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

    public static void setUserClickedId(int userClickedId) {
        AdminEditPopup.clickedUserId = userClickedId;
    }

    public static int getUserClickedId() {
        return AdminEditPopup.clickedUserId;
    }

    public void updateUserQuery() throws SQLException {
        String sql = """
                    UPDATE users
                    SET email = ?, full_name = ?, username = ?, password = ?
                    WHERE users.user_id = ?;
                    """;
        PreparedStatement preparedStatement = driver.connection.prepareStatement(sql);
        preparedStatement.setString(1, emailTextField.getText().toLowerCase());
        preparedStatement.setString(2, fullNameTextField.getText());
        preparedStatement.setString(3, usernameTextField.getText());
        preparedStatement.setString(4, passwordTextField.getText());
        preparedStatement.setInt(5, getUserClickedId());
        preparedStatement.execute();
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
        roleComboBox.setPromptText("Select Role");
        fullNameTextField.clear();
        emailTextField.clear();
        usernameTextField.clear();
        passwordTextField.clear();
    }

    //Button Onclicks
    // Onclick for submit button
    public void submitButtonOnclick() {
        boolean exception = false;
        if (validInput()) {
            try {
                updateUserQuery();
                Admin.queryData(Admin.getLastRowStringQuery());
                PopupManager.removePopup("MENU");
                Notification.createNotification("Submission Complete", "You successfully added a new user");
            } catch (Exception e) {
                exception = true;
            }

        }
        if (!validInput() || exception) {
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
        try {
            PopupManager.removePopup("MENU");
        } catch (Exception ignore) {
        }
    }
}
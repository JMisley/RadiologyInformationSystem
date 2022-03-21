package com.risjavafx.components;

import com.risjavafx.Miscellaneous;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class TableSearchBar implements Initializable {

    @FXML private Label searchLabel;
    @FXML private Button addButton;
    @FXML private Button deleteButton;
    @FXML private Button editButton;
    @FXML private TextField textField;
    @FXML private ComboBox<String> comboBox;
    @FXML private Label errorLabel;

    private static Button usableAddButton;
    private static Button usableDeleteButton;
    private static Button usableEditButton;
    private static TextField usableTextField;
    private static ComboBox<String> usableComboBox;
    private static Label usableErrorLabel;

    static Miscellaneous misc = new Miscellaneous();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeUsables();
        resizeElements();
    }

    public void initializeUsables() {
        usableTextField = textField;
        usableComboBox = comboBox;
        usableErrorLabel = errorLabel;
        usableAddButton = addButton;
        usableEditButton = editButton;
        usableDeleteButton = deleteButton;
    }

    public void createSearchBar(HBox tableSearchBar) {
        ComponentsManager.createComponent(Components.TABLE_SEARCH_BAR, tableSearchBar);
    }

    public void resizeElements() {
        textField.setPrefHeight(misc.getScreenHeight() * .05);
        textField.setPrefWidth(misc.getScreenWidth() * .3);

        comboBox.setPrefHeight(misc.getScreenHeight() * .05);
        comboBox.setPrefWidth(misc.getScreenWidth() * .1);

        double fontSize;
        if ((misc.getScreenWidth() / 80) < 20) {
            fontSize = misc.getScreenWidth() / 80;
        } else {
            fontSize = 18;
        }
        searchLabel.setStyle("-fx-font-size: " + fontSize);
        textField.setStyle("-fx-font-size: " + (fontSize - 2) + "; -fx-font-family: 'Arial'");

        Button[] buttons = {addButton, editButton, deleteButton};
        for (Button button : buttons) {
            button.setPrefHeight(misc.getScreenHeight() * .05);
            button.setPrefWidth(misc.getScreenWidth() * .1);
            button.setStyle("-fx-font-size: " + (fontSize - 2) + "px");
        }
    }

    public void toggleButtons(boolean showAddButton) {
        int addOpacity, otherOpacity;
        if (showAddButton) {
            addOpacity = 1;
            otherOpacity = 0;
        } else {
            addOpacity = 0;
            otherOpacity = 1;
        }
        TableSearchBar.usableAddButton.setManaged(showAddButton);
        TableSearchBar.usableAddButton.setDisable(!showAddButton);
        TableSearchBar.usableAddButton.setOpacity(addOpacity);
        TableSearchBar.usableEditButton.setDisable(showAddButton);
        TableSearchBar.usableEditButton.setOpacity(otherOpacity);
        TableSearchBar.usableDeleteButton.setDisable(showAddButton);
        TableSearchBar.usableDeleteButton.setOpacity(otherOpacity);
    }

    public TextField getTextField() {return usableTextField;}

    public ComboBox<String> getComboBox() {return usableComboBox;}

    public Label getErrorLabel() {return usableErrorLabel;}

    public Button getAddButton() {return usableAddButton;}

    public Button getEditButton() {return usableEditButton;}

    public Button getDeleteButton() {return usableDeleteButton;}
}

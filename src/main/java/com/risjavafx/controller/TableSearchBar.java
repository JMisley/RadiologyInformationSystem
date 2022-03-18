package com.risjavafx.controller;

import com.risjavafx.model.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class TableSearchBar implements Initializable {

    public Label searchLabel;

    public Button addButton;
    public static Button usableAddButton;

    public Button deleteButton;
    public static Button usableDeleteButton;

    public Button editButton;
    public static Button usableEditButton;

    public TextField textField;
    public static TextField usableTextField;

    public ComboBox<String> comboBox;
    public static ComboBox<String> usableComboBox;

    public Label errorLabel;
    public static Label usableErrorLabel;

    static Miscellaneous misc = new Miscellaneous();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resizeElements();
        usableTextField = textField;
        usableComboBox = comboBox;
        usableErrorLabel = errorLabel;
        usableAddButton = addButton;
        usableEditButton = editButton;
        usableDeleteButton = deleteButton;
    }

    public static <E> void createSearchBar(HBox tableSearchBar, Class<E> thisClass) {
        try {
            URL navigationBarComponent = thisClass.getResource("components/TableSearchBar.fxml");
            tableSearchBar.getChildren().setAll((Node) FXMLLoader.load(Objects.requireNonNull(navigationBarComponent)));
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public static void toggleButtons(boolean showAddButton) {
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

    // Add Button OnClick Listener
    public void addItems() throws IOException {
        PopupManager.createPopup(Popups.getPopupEnum());
    }

    // Edit Button OnClick Listener
    public void editItems() {
    }

    // Delete Button OnClick Listener
    public static <E> void deleteItems(InfoTable<E, ?> infoTable, ObservableList<E> observableList) {
        ObservableList<E> selectedItems = infoTable.tableView.getSelectionModel().getSelectedItems();
        observableList.removeAll(selectedItems);
    }

    public void comboBoxValueChange() {
        usableComboBox = comboBox;
    }
}

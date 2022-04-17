package com.risjavafx.components;

import com.risjavafx.Miscellaneous;
import com.risjavafx.pages.Pages;
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
    @FXML private Button viewButton;
    @FXML private Button checkInButton;
    @FXML private Button billingButton;
    @FXML private Button addImageButton;
    @FXML private Button patientBackgroundButton;


    @FXML private TextField textField;
    @FXML private ComboBox<String> comboBox;
    @FXML private Label errorLabel;

    private static Button usableAddButton;
    private static Button usableDeleteButton;
    private static Button usableEditButton;
    private static Button usableCheckInButton;
    private static Button usableViewButton;
    private static Button usableBillingButton;
    private static Button usableAddImageButton;
    private static Button usablePatientBackgroundButton;

    private static TextField usableTextField;
    private static ComboBox<String> usableComboBox;
    private static Label usableErrorLabel;

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
        usableCheckInButton = checkInButton;
        usableBillingButton = billingButton;
        usablePatientBackgroundButton = patientBackgroundButton;
        usableViewButton = viewButton;
        usableAddImageButton = addImageButton;
    }

    public void createSearchBar(HBox tableSearchBar) {
        ComponentsManager.createComponent(Components.TABLE_SEARCH_BAR, tableSearchBar);
    }

    private void resizeElements() {
        Miscellaneous misc = new Miscellaneous();

        textField.setPrefHeight(misc.getScreenHeight() * .05);
        textField.setPrefWidth(misc.getScreenWidth() * .3);

        comboBox.setPrefHeight(misc.getScreenHeight() * .05);
        comboBox.setPrefWidth(misc.getScreenWidth() * .1);

        int fontSize = 16;

        searchLabel.setStyle("-fx-font-size: " + fontSize + "px");
        textField.setStyle("-fx-font-size: " + (fontSize - 2) + "px ; -fx-font-family: 'Arial'");

        Button[] buttons = {addButton, editButton, deleteButton, checkInButton, viewButton, billingButton, addImageButton, patientBackgroundButton};
        for (Button button : buttons) {
            button.setPrefWidth(misc.getScreenWidth() * .05);
            button.setStyle("-fx-font-size: " + (fontSize - 2) + "px");
        }
    }

    public void toggleButtons(boolean showButtons) {
        showButtons(new Button[] {usableEditButton, usableDeleteButton}, showButtons);

        if (Pages.getPage().equals(Pages.APPOINTMENTS)) {
            hideButtons(new Button[] {usableBillingButton});
            showButtons(new Button[] {usableCheckInButton}, showButtons);
        }

        if (Pages.getPage().equals(Pages.REFERRALS)) {
            hideButtons(new Button[] {usableCheckInButton});
            showButtons(new Button[] {usableViewButton, usableBillingButton, usablePatientBackgroundButton}, showButtons);
        }

        if (Pages.getPage().equals(Pages.ORDERS)) {
            hideButtons(new Button[] {usableCheckInButton, usableViewButton, usableBillingButton});
            showButtons(new Button[] {usableAddImageButton}, showButtons);
        }
    }

    private void hideButtons(Button[] buttons) {
        for (Button button : buttons) {
            button.setManaged(false);
            button.setVisible(false);
        }
    }

    private void showButtons(Button[] buttons, boolean bool) {
        for (Button button : buttons) {
            button.setManaged(!bool);
            button.setVisible(!bool);
        }
    }

    public TextField getTextField() {return usableTextField;}

    public ComboBox<String> getComboBox() {return usableComboBox;}

    public Label getErrorLabel() {return usableErrorLabel;}

    public Button getAddButton() {return usableAddButton;}

    public Button getEditButton() {return usableEditButton;}

    public Button getCheckInButton() {return  usableCheckInButton;}

    public Button getDeleteButton() {return usableDeleteButton;}

    public Button getViewButton() {
        return usableViewButton;
    }

    public Button getBillingButton() { return usableBillingButton;}

    public Button getAddImageButton() {return usableAddImageButton;}

    public Button getPatientBackgroundButton() {return usablePatientBackgroundButton;}

}

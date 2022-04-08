package com.risjavafx.pages.referrals;

import com.risjavafx.Miscellaneous;
import com.risjavafx.popups.PopupManager;
import com.risjavafx.popups.Popups;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewReferralsPopup implements Initializable {

    public VBox popupContainer;
    public Button returnButton;
    public Button submitButton;
    public Button viewImagesButton;
    public ComboBox<String> appointmentsComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resizeElements();
    }

    private void resizeElements() {
        Miscellaneous misc = new Miscellaneous();

        popupContainer.setPrefHeight(Popups.getLargeMenuDimensions()[0]);
        popupContainer.setPrefWidth(Popups.getLargeMenuDimensions()[1]);
        popupContainer.setMaxHeight(Popups.getLargeMenuDimensions()[0]);
        popupContainer.setMaxWidth(Popups.getLargeMenuDimensions()[1]);

        for (Control control : new Control[] {returnButton, submitButton, viewImagesButton, appointmentsComboBox}) {
            control.setPrefHeight(40);
            control.setPrefWidth(misc.getScreenWidth() * .15);
            control.setStyle("-fx-font-size: 14px");
        }
    }

    public void returnToPage() {
        try {
            PopupManager.removePopup("MENU");
        }
        catch (Exception ignore) {}
    }

    public void submitChanges() {
        try {
            PopupManager.removePopup("MENU");
        }
        catch (Exception ignore) {}
    }
}

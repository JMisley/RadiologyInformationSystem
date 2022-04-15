package com.risjavafx.popups.models;

import com.risjavafx.popups.PopupBlueprint;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class PopupLoading extends PopupBlueprint implements Initializable {
    public StackPane popupContainer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resizePopupContainer(popupContainer);
    }
}

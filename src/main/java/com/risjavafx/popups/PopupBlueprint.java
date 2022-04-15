package com.risjavafx.popups;

import com.risjavafx.Miscellaneous;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class PopupBlueprint {
    Miscellaneous misc = new Miscellaneous();

     public void resizeElements(Pane popupBox, ImageView image, Button[] buttons, Label headerLabel, Label contentLabel) {
        resizePopupContainer(popupBox);

        image.setFitHeight(misc.getScreenHeight() * .075);

        for (Button button : buttons) {
            double height, width, font;
            if (buttons.length == 1) {
                height = .05; width = .1; font = 18;
            } else {
                height = .04; width = .12; font = 16;
            }
            button.setPrefHeight(misc.getScreenHeight() * height);
            button.setPrefWidth(misc.getScreenHeight() * width);
            button.setStyle("-fx-font-size: " + font + "px");
        }

        double headerFontSize;
        double contentFontSize;
        if ((misc.getScreenWidth()/80) <= 20) {
            headerFontSize = misc.getScreenWidth()/80;
            contentFontSize = misc.getScreenWidth()/100;
        } else {
            headerFontSize = 20;
            contentFontSize = 15;
        }
        headerLabel.setStyle("-fx-font-size: " + headerFontSize);
        contentLabel.setStyle("-fx-font-size: " + contentFontSize);
    }

    public void resizePopupContainer(Pane popupBox) {
        popupBox.setPrefHeight(Popups.getAlertDimensions()[0]);
        popupBox.setPrefWidth(Popups.getAlertDimensions()[1]);
        popupBox.setMaxHeight(Popups.getAlertDimensions()[0]);
        popupBox.setMaxWidth(Popups.getAlertDimensions()[1]);
    }
}

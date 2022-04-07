package com.risjavafx.controller;

import com.risjavafx.model.Miscellaneous;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class AlertPopup implements Initializable {

    public VBox alertBox;
    public ImageView errorImage;
    public Label headerLabel;
    public static Label usableHeaderLabel;
    public Label contentLabel;
    public static Label usableContentLabel;
    public Button exitButton;
    public static Button usableExitButton;

    Miscellaneous misc = new Miscellaneous();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resizeElements();
        usableHeaderLabel = headerLabel;
        usableContentLabel = contentLabel;
        usableExitButton = exitButton;

        errorImage.setImage(new Image("file:C:/Users/johnn/IdeaProjects/RISJavaFX/src/main/resources/com/risjavafx/images/error.png"));

        Main.popupAlert.showingProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!Main.popupMenu.isShowing()) {
                PageManager.root.setDisable(!aBoolean);
            }
        });
    }

    public void resizeElements() {
        alertBox.setPrefHeight(misc.getScreenHeight() * .3);
        alertBox.setPrefWidth(misc.getScreenHeight() * .415);

        errorImage.setFitHeight(misc.getScreenHeight() * .075);

        exitButton.setPrefHeight(misc.getScreenHeight() * .05);
        exitButton.setPrefWidth(misc.getScreenHeight() * .1);
        exitButton.setStyle("-fx-font-size: 18px");

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

    public static void setExitButtonLabel(String text) {
        usableExitButton.setText(text);
    }

    public static void setHeaderLabel(String header) {
        usableHeaderLabel.setText(header);
    }

    public static void setContentLabel(String content) {
        usableContentLabel.setText(content);
    }

    // Exit button onclick
    public void exitPopup() {
        Main.popupAlert.hide();
    }
}

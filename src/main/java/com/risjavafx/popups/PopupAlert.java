package com.risjavafx.popups;

import com.risjavafx.pages.PageManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class PopupAlert extends PopupBlueprint implements Initializable {
    
    @FXML private VBox popupContainer;
    @FXML private ImageView alertImage;
    @FXML private Label headerLabel;
    @FXML private Label contentLabel;
    @FXML private Button exitButton;

    private static ImageView usableAlertImage;
    private static Label usableHeaderLabel;
    private static Label usableContentLabel;
    private static Button usableExitButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Button[] buttons = {exitButton};
        resizeElements(popupContainer, alertImage, buttons, headerLabel, contentLabel);
        initializeUsables();

        Popups.getMenuPopupEnum().getPopup().showingProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!Popups.getMenuPopupEnum().getPopup().isShowing()) {
                PageManager.getRoot().setDisable(!aBoolean);
            }
        });
    }

    public void initializeUsables() {
        usableHeaderLabel = headerLabel;
        usableContentLabel = contentLabel;
        usableExitButton = exitButton;
        usableAlertImage = alertImage;
    }

    public void setExitButtonLabel(String text) {usableExitButton.setText(text);}

    public void setHeaderLabel(String header) {usableHeaderLabel.setText(header);}

    public void setContentLabel(String content) {usableContentLabel.setText(content);}

    public void setAlertImage(Image image) {usableAlertImage.setImage(image);}

    public void exitPopup() {Popups.getAlertPopupEnum().getPopup().hide();}
}

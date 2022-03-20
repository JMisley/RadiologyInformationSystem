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

public class PopupConfirmation extends PopupBlueprint implements Initializable {

    @FXML private VBox popupContainer;
    @FXML private ImageView confirmationImage;
    @FXML private Label headerLabel;
    @FXML private Label contentLabel;
    @FXML private Button confirmButton;
    @FXML private Button cancelButton;

    private static ImageView usableConfirmationImage;
    private static Label usableHeaderLabel;
    private static Label usableContentLabel;
    private static Button usableConfirmButton;
    private static Button usableCancelButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Button[] buttons = {confirmButton, cancelButton};
        resizeElements(popupContainer, confirmationImage, buttons, headerLabel, contentLabel);
        initializeUsables();

        try {
            Popups.getMenuPopupEnum().getPopup().showingProperty().addListener((observableValue, aBoolean, t1) -> {
                if (!Popups.getMenuPopupEnum().getPopup().isShowing()) {
                    PageManager.root.setDisable(!aBoolean);
                }
            });
        } catch (Exception ignored) {}
    }

    public void initializeUsables() {
        usableConfirmationImage = confirmationImage;
        usableHeaderLabel = headerLabel;
        usableContentLabel = contentLabel;
        usableConfirmButton = confirmButton;
        usableCancelButton = cancelButton;
    }

    public void setConfirmButtonLabel(String text) {usableConfirmButton.setText(text);}

    public void setExitButtonLabel(String text) {usableCancelButton.setText(text);}

    public void setHeaderLabel(String header) {usableHeaderLabel.setText(header);}

    public void setContentLabel(String content) {usableContentLabel.setText(content);}

    public void setConfirmationImage(Image image) {usableConfirmationImage.setImage(image);}

    public Button getConfirmationButton() {return usableConfirmButton;}

    public Button getCancelButton() {return usableCancelButton;}
}

package com.risjavafx.components;

import com.risjavafx.Miscellaneous;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class TitleBar implements javafx.fxml.Initializable {

    public Button titleBarCloseButton;
    public Button titleBarMaxButton;
    public Button titleBarMinButton;
    public ImageView closeImage;
    public ImageView maxImage;
    public ImageView minImage;
    private static double x = 0, y = 0;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        closeImage.setId("closeImage");
        maxImage.setId("maximizeImage");
        minImage.setId("minimizeImage");
    }

    public static void createTitleBar(Pane mainContainer, HBox titleBar) {
        Miscellaneous misc = new Miscellaneous();
        mainContainer.setMinHeight(misc.getScreenHeight());
        mainContainer.setMinWidth(misc.getScreenWidth());
        ComponentsManager.createComponent(Components.TITLE_BAR, titleBar);
    }

    public void closeApp() {
        Main.usableStage.close();
    }

    public void maxApp() {
        resetStagePosition();
    }

    public void minApp() {
        Main.usableStage.setIconified(true);
    }

    private static void resetStagePosition() {
        Main.usableStage.setX(0);
        Main.usableStage.setY(0);
    }

    private static void setDraggable(HBox titleBar) {
        titleBar.setOnMousePressed(mouseEvent -> {
            x = (mouseEvent.getSceneX());
            y = (mouseEvent.getSceneY());
        });

        titleBar.setOnMouseDragged(mouseEvent -> {
            Main.usableStage.setX(mouseEvent.getScreenX() - x);
            Main.usableStage.setY(mouseEvent.getScreenY() - y);
        });
    }
}

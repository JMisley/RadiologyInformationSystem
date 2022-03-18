package com.risjavafx.components;

import com.risjavafx.Miscellaneous;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

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
    static Miscellaneous misc = new Miscellaneous();

    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image close = new Image("file:C:/Users/johnn/IdeaProjects/RISJavaFX/src/main/resources/com/risjavafx/images/close.png");
        Image max = new Image("file:C:/Users/johnn/IdeaProjects/RISJavaFX/src/main/resources/com/risjavafx/images/circle.png");
        Image min = new Image("file:C:/Users/johnn/IdeaProjects/RISJavaFX/src/main/resources/com/risjavafx/images/minus.png");

        closeImage.setImage(close);
        maxImage.setImage(max);
        minImage.setImage(min);
    }

    public static void createTitleBar(BorderPane mainContainer, HBox titleBar) {
        mainContainer.setMinHeight(misc.getScreenHeight());
        mainContainer.setMinWidth(misc.getScreenWidth());
        setDraggable(titleBar);
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

package com.risjavafx.controller;

import com.risjavafx.model.Miscellaneous;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
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

    public static <E> void createTitleBar(BorderPane mainContainer, HBox titleBar, Class<E> thisClass) {
        mainContainer.setMinHeight(misc.getScreenHeight());
        mainContainer.setMinWidth(misc.getScreenWidth());
        setDraggable(titleBar);
        try {
            URL navigationBarComponent = thisClass.getResource("components/TitleBar.fxml");
            titleBar.getChildren().setAll((Node) FXMLLoader.load(Objects.requireNonNull(navigationBarComponent)));
        } catch (IOException e) {
            e.printStackTrace();
        }
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

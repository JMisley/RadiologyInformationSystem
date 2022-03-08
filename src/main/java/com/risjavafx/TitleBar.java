package com.risjavafx;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class TitleBar implements javafx.fxml.Initializable {

    public Button titleBarCloseButton;
    public Button titleBarMaxButton;
    public Button titleBarMinButton;
    public ImageView closeImage;
    public ImageView maxImage;
    public ImageView minImage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image close = new Image("file:C:/Users/johnn/IdeaProjects/RISJavaFX/src/main/resources/com/risjavafx/images/close.png");
        Image max = new Image("file:C:/Users/johnn/IdeaProjects/RISJavaFX/src/main/resources/com/risjavafx/images/circle.png");
        Image min = new Image("file:C:/Users/johnn/IdeaProjects/RISJavaFX/src/main/resources/com/risjavafx/images/minus.png");

        closeImage.setImage(close);
        maxImage.setImage(max);
        minImage.setImage(min);
    }
}

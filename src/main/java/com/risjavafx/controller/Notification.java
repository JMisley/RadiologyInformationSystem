package com.risjavafx.controller;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class Notification {
    public static void createNotification() {
        ImageView imageView = new ImageView(new Image("file:C:/Users/johnn/IdeaProjects/RISJavaFX/src/main/resources/com/risjavafx/images/success.png"));
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        Notifications
                .create()
                .owner(PageManager.root)
                .title("Submission Complete")
                .text("You successfully added a new user")
                .hideAfter(Duration.seconds(5))
                .position(Pos.BOTTOM_RIGHT)
                .graphic(imageView)
                .show();
    }
}

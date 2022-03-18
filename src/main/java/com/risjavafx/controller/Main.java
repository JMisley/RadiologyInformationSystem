package com.risjavafx.controller;

import com.risjavafx.model.Miscellaneous;
import com.risjavafx.model.Pages;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    public static Stage usableStage;
    Miscellaneous misc = new Miscellaneous();

    @Override
    public void start(Stage primaryStage) {
        usableStage = primaryStage;

        Scene scene = new Scene(new BorderPane());
        PageManager.setScene(scene);
        PageManager.switchPage(Pages.ADMIN);

        usableStage.setScene(scene);
        usableStage.setMinWidth(misc.getScreenWidth());
        usableStage.setMinHeight(misc.getScreenHeight());
        usableStage.setMaximized(false);
        usableStage.initStyle(StageStyle.UNDECORATED);
        usableStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
package com.risjavafx.controller;

import com.risjavafx.model.Miscellaneous;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {

    public static Stage stage;
    private static final ScreenManager screenController = new ScreenManager();

    @Override
    public void start(Stage primaryStage) throws IOException {
        Miscellaneous misc = new Miscellaneous();
        stage = primaryStage;
        Parent root = FXMLLoader.load(((Objects.requireNonNull(getClass().getResource("pages/admin-page.fxml")))));
        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());
        primaryStage.setScene(new Scene(root));
        primaryStage.setMinWidth(misc.getScreenWidth());
        primaryStage.setMinHeight(misc.getScreenHeight());
        primaryStage.setMaximized(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();

        screenController.initializeHashMap();
    }

    // Method to change scene
    public void changeScene(String pageUrl) {
        stage.getScene().setRoot(screenController.getPage(pageUrl));
    }

    public static void main(String[] args) {
        launch();
    }
}
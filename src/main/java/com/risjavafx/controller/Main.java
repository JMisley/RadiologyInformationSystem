package com.risjavafx.controller;

import com.risjavafx.model.Miscellaneous;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {

    public static Stage stage;
    public static Popup popup;
    private static final ScreenManager screenController = new ScreenManager();
    Miscellaneous misc = new Miscellaneous();

    @Override
    public void start(Stage primaryStage) throws IOException {
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

    // Method to create a popup menu. Input a decimals to represent a percentage of the screen height and width
    public void createPopup(String pageUrl) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(pageUrl)));
        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());

        popup = new Popup();
        popup.setAutoHide(true);
        popup.centerOnScreen();
        popup.getContent().add(root);
        popup.show(stage);
    }

    // Method to change scene
    public void changeScene(String pageUrl) {
        stage.getScene().setRoot(screenController.getPage(pageUrl));
    }

    public static void main(String[] args) {
        launch();
    }
}
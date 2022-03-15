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

    public static Stage usableStage;
    public static Popup popupMenu = new Popup();
    public static Popup popupAlert = new Popup();
    public static Parent mainRoot;
    private static final ScreenManager screenController = new ScreenManager();
    Miscellaneous misc = new Miscellaneous();

    @Override
    public void start(Stage primaryStage) throws IOException {
        usableStage = primaryStage;
        mainRoot = FXMLLoader.load(((Objects.requireNonNull(getClass().getResource("pages/login-page.fxml")))));
        mainRoot.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());
        primaryStage.setScene(new Scene(mainRoot));
        primaryStage.setMinWidth(misc.getScreenWidth());
        primaryStage.setMinHeight(misc.getScreenHeight());
        primaryStage.setMaximized(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();

        screenController.initializeHashMap();
    }

    // Method to create a popup menu. Input a decimals to represent a percentage of the screen height and width
    public void createPopup(String pageUrl, Popup popup) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(pageUrl)));
        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());

        popup.getContent().add(root);
        popup.setAutoHide(false);
        popup.show(usableStage);

        usableStage.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (aBoolean) popup.setOpacity(0f);
            else popup.setOpacity(1f);
        });
    }

    // Method to change scene
    public void changeScene(String pageUrl) throws IOException {
        mainRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(pageUrl)));
        mainRoot.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());
        usableStage.getScene().setRoot(mainRoot);
    }

    public static void main(String[] args) {
        launch();
    }
}
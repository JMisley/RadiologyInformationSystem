package com.risjavafx.controller;

import com.risjavafx.model.Miscellaneous;
import com.risjavafx.model.Pages;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {

    public static Stage usableStage;
    public static Popup popupMenu = new Popup();
    public static Popup popupAlert = new Popup();
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

    public static void main(String[] args) {
        launch();
    }
}
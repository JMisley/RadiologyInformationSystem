package com.risjavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class Main extends Application {

    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        stage = primaryStage;
        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("login-page.fxml"))));
        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());
        primaryStage.setTitle("Radiology System");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMinWidth(primaryScreenBounds.getWidth());
        primaryStage.setMinHeight(primaryScreenBounds.getHeight());
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    // Method to change scene
    public void changeScene(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml)));
        pane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());
        stage.getScene().setRoot(pane);
    }

    public static void main(String[] args) {
        launch();
    }
}
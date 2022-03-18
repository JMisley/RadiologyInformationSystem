package com.risjavafx.components;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class ComponentsManager {
    public static void createComponent(Components component, Pane contentContainer) {
        try {
            URL navigationBarComponent = ComponentsManager.class.getResource(component.getFilename());
            contentContainer.getChildren().setAll((Node) FXMLLoader.load(Objects.requireNonNull(navigationBarComponent)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.risjavafx;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Admin implements Initializable {

    public HBox topContent;

    // Load NavigationBar component into home-page.fxml
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            URL navigationBarComponent = getClass().getResource("fxml components/NavigationBar.fxml");
            topContent.getChildren().setAll((Node) FXMLLoader.load(Objects.requireNonNull(navigationBarComponent)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

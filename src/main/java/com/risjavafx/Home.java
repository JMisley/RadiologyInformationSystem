package com.risjavafx;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Home implements Initializable {

    public BorderPane mainContainer;
    public HBox titleBar;
    public StackPane centerContent;
    public HBox topContent;
    Miscellaneous misc = new Miscellaneous();

    // Load NavigationBar component into home-page.fxml
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TitleBar.createTitleBar(mainContainer, titleBar, this.getClass());
        createNavBar();
    }

    public void createNavBar() {
        try {
            URL navigationBarComponent = getClass().getResource("fxml components/NavigationBar.fxml");
            topContent.getChildren().setAll((Node) FXMLLoader.load(Objects.requireNonNull(navigationBarComponent)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

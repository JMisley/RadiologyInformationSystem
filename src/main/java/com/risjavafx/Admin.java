package com.risjavafx;

import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import java.net.URL;
import java.util.ResourceBundle;

public class Admin implements Initializable {

    public BorderPane mainContainer;
    public HBox titleBar;
    public HBox topContent;

    // Load NavigationBar component into home-page.fxml
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TitleBar.createTitleBar(mainContainer, titleBar, this.getClass());
        NavigationMenu.createNavBar(topContent, this.getClass());
    }
}

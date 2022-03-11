package com.risjavafx.controller;

import com.risjavafx.model.Miscellaneous;
import com.risjavafx.model.Pages;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.net.URL;
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
        Pages.setPage(Pages.HOME);
        TitleBar.createTitleBar(mainContainer, titleBar, this.getClass());
        NavigationBar.createNavBar(topContent, this.getClass());
    }
}

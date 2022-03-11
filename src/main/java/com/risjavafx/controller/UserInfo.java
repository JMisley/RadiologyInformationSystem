package com.risjavafx.controller;

import com.risjavafx.model.Pages;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import java.net.URL;
import java.util.ResourceBundle;

public class UserInfo implements Initializable {

    public BorderPane mainContainer;
    public HBox titleBar;
    public HBox topContent;

    // Load NavigationBar component into home-page.fxml
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Pages.setPage(Pages.USERINFO);
        TitleBar.createTitleBar(mainContainer, titleBar, this.getClass());
        NavigationBar.createNavBar(topContent, this.getClass());
    }
}

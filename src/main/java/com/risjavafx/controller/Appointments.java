package com.risjavafx.controller;

import com.risjavafx.model.Pages;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class Appointments implements Initializable {

    public HBox topContent;
    public BorderPane mainContainer;
    public HBox titleBar;

    // Load NavigationBar component into home-page.fxml
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Pages.setPage(Pages.APPOINTMENTS);
        TitleBar.createTitleBar(mainContainer, titleBar, this.getClass());
        NavigationBar.createNavBar(topContent, this.getClass());
    }
}

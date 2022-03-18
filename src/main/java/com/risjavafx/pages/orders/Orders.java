package com.risjavafx.pages.orders;

import com.risjavafx.components.TitleBar;
import com.risjavafx.components.NavigationBar;
import com.risjavafx.pages.Pages;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class Orders implements Initializable {

    public HBox topContent;
    public BorderPane mainContainer;
    public HBox titleBar;

    // Load NavigationBar component into home-page.fxml
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Pages.setPage(Pages.ORDERS);
        TitleBar.createTitleBar(mainContainer, titleBar);
        NavigationBar.createNavBar(topContent);
    }
}

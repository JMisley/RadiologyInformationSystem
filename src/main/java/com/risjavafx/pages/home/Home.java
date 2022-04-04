package com.risjavafx.pages.home;

import com.risjavafx.components.TitleBar;
import com.risjavafx.Miscellaneous;
import com.risjavafx.components.NavigationBar;
import com.risjavafx.pages.Pages;
import com.risjavafx.pages.admin.Admin;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class Home implements Initializable {

    public BorderPane mainContainer;
    public HBox titleBar;
    public StackPane centerContent;
    public HBox topContent;
    public ScrollPane scrollPane;

    // Load NavigationBar component into home-page.fxml
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Pages.setPage(Pages.HOME);
        TitleBar.createTitleBar(mainContainer, titleBar);
        NavigationBar.createNavBar(topContent);
    }
}

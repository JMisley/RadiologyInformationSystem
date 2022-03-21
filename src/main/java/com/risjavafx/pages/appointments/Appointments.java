package com.risjavafx.pages.appointments;

import com.risjavafx.components.TableSearchBar;
import com.risjavafx.components.TitleBar;
import com.risjavafx.components.NavigationBar;
import com.risjavafx.pages.PageManager;
import com.risjavafx.pages.Pages;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class Appointments implements Initializable {

    public HBox topContent;
    public BorderPane mainContainer;
    public HBox titleBar;
    public SplitPane centerContentContainer;
    public HBox tableSearchBarContainer;

    TableSearchBar tableSearchBar = new TableSearchBar();

    // Load NavigationBar component into home-page.fxml
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TitleBar.createTitleBar(mainContainer, titleBar);
        tableSearchBar.createSearchBar(tableSearchBarContainer);
        NavigationBar.createNavBar(topContent);

        PageManager.getScene().rootProperty().addListener(observable -> {
            if (Pages.getPage() == Pages.APPOINTMENTS) {
                System.out.println("aPpOiNtMeNtS");
                tableSearchBar.createSearchBar(tableSearchBarContainer);
            }
        });
    }
}

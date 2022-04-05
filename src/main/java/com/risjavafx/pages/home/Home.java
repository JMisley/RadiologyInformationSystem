package com.risjavafx.pages.home;

import com.risjavafx.Miscellaneous;
import com.risjavafx.components.TitleBar;
import com.risjavafx.components.NavigationBar;
import com.risjavafx.pages.PageManager;
import com.risjavafx.pages.Pages;
import com.risjavafx.pages.TableManager;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class Home implements Initializable {

    public BorderPane mainContainer;
    public HBox titleBar;
    public HBox topContent;
    public ScrollPane scrollPane;
    public VBox tableViewList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Pages.setPage(Pages.HOME);
        TitleBar.createTitleBar(mainContainer, titleBar);
        NavigationBar.createNavBar(topContent);

        StackPane[] stackPanes = {
                new StackPane(TableManager.getAdminTable()),
                new StackPane(TableManager.getAppointmentsTable())};
        createScrollView(tableViewList, stackPanes);

        PageManager.getScene().rootProperty().addListener(observable -> {
            if (Pages.getPage() == Pages.HOME) {
                refreshTables();
            }
        });
    }

    private void createScrollView(VBox tableViewList, StackPane[] stackPanes) {
        for (StackPane stackPane : stackPanes) {
            addToScrollView(stackPane);
            resizeElements();
            scrollPane.setContent(tableViewList);
        }
    }

    private void addToScrollView(StackPane stackPane) {
        Miscellaneous misc = new Miscellaneous();
        tableViewList.getChildren().add(stackPane);
        stackPane.setMaxWidth(misc.getScreenWidth() * .75);
        stackPane.setMaxHeight(misc.getScreenHeight() * .85);
    }

    private void refreshTables() {
        tableViewList.getChildren().clear();
        addToScrollView(new StackPane(TableManager.getAdminTable()));
        addToScrollView(new StackPane(TableManager.getAppointmentsTable()));
        System.out.println(Arrays.toString(tableViewList.getChildren().toArray()));
    }

    private void resizeElements() {
        Miscellaneous misc = new Miscellaneous();
        tableViewList.setMaxWidth(misc.getScreenWidth());
        tableViewList.setMaxHeight(misc.getScreenHeight());
        tableViewList.setMinWidth(misc.getScreenWidth());
        tableViewList.setMinHeight(misc.getScreenHeight());
        tableViewList.setId("root");
    }
}

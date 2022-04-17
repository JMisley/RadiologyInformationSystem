package com.risjavafx.components.main;

import com.risjavafx.Miscellaneous;
import com.risjavafx.pages.PageManager;
import com.risjavafx.pages.Pages;
import com.risjavafx.popups.PopupManager;
import com.risjavafx.popups.Popups;
import com.risjavafx.pages.admin.AdminEditPopup;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    public static Stage usableStage;

    @Override
    public void start(Stage primaryStage) {
        Miscellaneous misc = new Miscellaneous();
        usableStage = primaryStage;

        Scene scene = new Scene(new BorderPane());

        popupAndPageSetup(scene);

        usableStage.setScene(scene);
        usableStage.setMinWidth(misc.getScreenWidth());
        usableStage.setMinHeight(misc.getScreenHeight());
        usableStage.setMaximized(false);
        usableStage.initStyle(StageStyle.UNDECORATED);
        usableStage.show();
    }

    private void popupAndPageSetup(Scene scene) {
        PopupManager.loadPopupsToCache(new Popups[] {Popups.LOADING});
        PageManager.loadPageToCache(Pages.PROGRESS);
        PageManager.setScene(scene);
        PageManager.switchPage(Pages.LOGIN);
    }

    public static void createNewWindow(Pages page) {
        Miscellaneous misc = new Miscellaneous();
        try {
            Stage newStage = new Stage();
            newStage.setScene(new Scene(PageManager.getRootFromUnloadedPage(page)));
            newStage.setHeight(misc.getScreenHeight() *.8);
            newStage.setWidth(misc.getScreenWidth() * .6);
            newStage.setResizable(false);
            newStage.show();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
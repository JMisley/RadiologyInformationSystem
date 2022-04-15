package com.risjavafx.popups;

import com.risjavafx.components.main.Main;
import com.risjavafx.Miscellaneous;
import com.risjavafx.pages.PageManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Popup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PopupManager {
    private static final Map<Popups, Parent> cache = new HashMap<>();

    public static Popup popupMenu = new Popup();
    public static Popup largePopupMenu = new Popup();
    public static Popup popupAlert = new Popup();
    public static Popup loadingPopup = new Popup();
    private static final ArrayList<Popups> currentPopups = new ArrayList<>();
    static Miscellaneous misc = new Miscellaneous();

    // Method to create a popup menu
    public static void createPopup(Popups popups) {
        Parent popupRoot = cache.get(popups);
        System.out.println("Popup from cache");
        popups.getPopup().getContent().add(popupRoot);
        PageManager.getRoot().setDisable(true);
        popups.getPopup().show(Main.usableStage);
        currentPopups.add(popups);

//        Main.usableStage.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
//            if (aBoolean) popups.getPopup().setOpacity(0f);
//            else popups.getPopup().setOpacity(1f);
//        });
    }

    public static void loadPopupsToCache(Popups[] popups) {
        try {
            for (Popups popup : popups) {
                Parent root = FXMLLoader.load(Objects.requireNonNull(PopupManager.class.getResource(popup.getFilename())));
                root.getStylesheets().add(Objects.requireNonNull(PageManager.class.getResource("stylesheet/styles.css")).toExternalForm());

                switch (popup.getType()) {
                    case "MENU" -> {
                        popup.getPopup().setY(misc.getScreenHeight() / 2 - Popups.getMenuDimensions()[0] / 2);
                        popup.getPopup().setX(misc.getScreenWidth() / 2 - Popups.getMenuDimensions()[1] / 2);
                        Popups.setMenuPopupEnum(popup);
                    }
                    case "LARGE_MENU" -> {
                        popup.getPopup().setY(misc.getScreenHeight() / 2 - Popups.getLargeMenuDimensions()[0] / 2);
                        popup.getPopup().setX(misc.getScreenWidth() / 2 - Popups.getLargeMenuDimensions()[1] / 2);
                        Popups.setLargeMenuPopupEnum(popup);
                    }
                    case "ALERT" -> {
                        popup.getPopup().setY(misc.getScreenHeight() / 2 - Popups.getAlertDimensions()[0] / 2);
                        popup.getPopup().setX(misc.getScreenWidth() / 2 - Popups.getAlertDimensions()[1] / 2);
                        Popups.setAlertPopupEnum(popup);
                    }
                    case "LOADING" -> {
                        popup.getPopup().setY(misc.getScreenHeight() / 2 - Popups.getLoadingDimensions()[0] / 2);
                        popup.getPopup().setX(misc.getScreenWidth() / 2 - Popups.getLoadingDimensions()[1] / 2);
                        Popups.setLoadingPopupEnum(popup);
                    }
                }

                cache.put(popup, root);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removePopup() {
        if (currentPopups.get(currentPopups.size() - 1).getType().equals("ALERT")) {
            if (currentPopups.size() <= 1)
                PageManager.getRoot().setDisable(false);
            currentPopups.get(currentPopups.size() - 1).getPopup().getContent().clear();
            currentPopups.get(currentPopups.size() - 1).getPopup().hide();
            currentPopups.remove(currentPopups.size() - 1);
        } else {
            PageManager.getRoot().setDisable(false);
            for (Popups popups : currentPopups) {
                popups.getPopup().getContent().clear();
                popups.getPopup().hide();
                currentPopups.clear();
            }
        }
    }
}
package com.risjavafx.popups;

import com.risjavafx.components.Main;
import com.risjavafx.Miscellaneous;
import com.risjavafx.pages.PageManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Popup;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PopupManager {
    private static final Map<Popups, Parent> cache = new HashMap<>();
    private static Popups currentPopup;

    public static Popup popupMenu = new Popup();
    public static Popup popupAlert = new Popup();
    static Miscellaneous misc = new Miscellaneous();

    // Method to create a popup menu. Input a decimals to represent a percentage of the screen height and width
    public static void createPopup(Popups popups) {
        try {
            currentPopup = popups;
            Parent popupRoot = cache.get(currentPopup);

            currentPopup.getPopup().getContent().add(popupRoot);
            currentPopup.getPopup().show(Main.usableStage);

            Main.usableStage.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
                if (aBoolean) currentPopup.getPopup().setOpacity(0f);
                else currentPopup.getPopup().setOpacity(1f);
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void loadPopupsToCache() {
        try {
            for (Popups popup : Popups.values()) {
                Parent root = FXMLLoader.load(Objects.requireNonNull(PopupManager.class.getResource(popup.getFilename())));
                root.getStylesheets().add(Objects.requireNonNull(PageManager.class.getResource("stylesheet/styles.css")).toExternalForm());

                if (popup.getType().equals("MENU")) {
                    popup.getPopup().setY(misc.getScreenHeight() / 2 - Popups.getMenuDimensions()[0] / 2);
                    popup.getPopup().setX(misc.getScreenWidth() / 2 - Popups.getMenuDimensions()[1] / 2);
                    Popups.setMenuPopupEnum(popup);
                } else if (popup.getType().equals("ALERT")) {
                    popup.getPopup().setY(misc.getScreenHeight() / 2 - Popups.getAlertDimensions()[0] / 2);
                    popup.getPopup().setX(misc.getScreenWidth() / 2 - Popups.getAlertDimensions()[1] / 2);
                    Popups.setAlertPopupEnum(popup);
                }
                cache.put(popup, root);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removePopup(String type) {
        if (type.equals("MENU")) {
            for (Popups popups : Popups.values()) {
                popups.getPopup().hide();
                popups.getPopup().getContent().clear();
            }
        } else if (type.equals("ALERT")) {
            for (Popups popups : Popups.getAlertPopupsArray()) {
                popups.getPopup().hide();
                popups.getPopup().getContent().clear();
            }
        }
    }

    public static void clearCache() {
        cache.clear();
    }
}

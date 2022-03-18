package com.risjavafx.model;

import com.risjavafx.controller.PopupManager;
import javafx.stage.Popup;

public enum Popups {
    ADMIN("popups/admin-popup.fxml", PopupManager.popupMenu, "MENU"),
    ALERT("popups/alert-popup.fxml", PopupManager.popupAlert, "ALERT");

    private static Popups popups;
    private final String filename;
    private final Popup popup;
    private final String type;

    static final Miscellaneous misc = new Miscellaneous();

    Popups(String fileName, Popup popup, String type) {
        this.filename = fileName;
        this.popup = popup;
        this.type = type;
    }

    public String getFilename() {
        return filename;
    }

    public Popup getPopup() {
        return popup;
    }

    public String getType() {
        return type;
    }

    public static void setPopupEnum(Popups popups) {
        Popups.popups = popups;
    }

    public static Popups getPopupEnum() {
        return popups;
    }

    public static double[] getMenuDimensions() {
        return new double[]{misc.getScreenWidth() * .315, misc.getScreenWidth() * .285};
    }

    public static double[] getAlertDimensions() {
        return new double[]{misc.getScreenHeight() * .3, misc.getScreenHeight() * .415};
    }
}

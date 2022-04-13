package com.risjavafx.popups;

import com.risjavafx.Miscellaneous;
import javafx.stage.Popup;

public enum Popups {
    ADMIN("admin-popup.fxml", PopupManager.popupMenu, "MENU", true),
    APPOINTMENT("appointment-popup.fxml", PopupManager.popupMenu, "MENU", true),
    ORDERS("orders-popup.fxml", PopupManager.popupMenu, "MENU", true),
    BILLING("billing-popup.fxml", PopupManager.popupMenu, "MENU", true),
    CONFIRMATION("popup-confirmation.fxml", PopupManager.popupAlert, "ALERT", false),
    ALERT("popup-alert.fxml", PopupManager.popupAlert, "ALERT", true),
    REFERRALS("referrals/referral-popup.fxml", PopupManager.popupMenu, "MENU", true),
    VIEW_REFERRALS("referrals/view-referral-popup.fxml", PopupManager.largePopupMenu, "LARGE_MENU", true);
    private static Popups menuPopups;
    private static Popups largeMenuPopups;
    private static Popups alertPopups;
    private final String filename;
    private final Popup popup;
    private final String type;
    private final boolean isCachable;

    static final Miscellaneous misc = new Miscellaneous();

    Popups(String fileName, Popup popup, String type, boolean isCachable) {
        this.filename = fileName;
        this.popup = popup;
        this.type = type;
        this.isCachable = isCachable;
    }

    public static Popups[] getAlertPopupsArray() {
        return new Popups[]{CONFIRMATION, ALERT};
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

    public boolean isCachable() {
        return isCachable;
    }

    public static void setMenuPopupEnum(Popups popups) {
        Popups.menuPopups = popups;
    }

    public static Popups getMenuPopupEnum() {
        return menuPopups;
    }

    public static void setAlertPopupEnum(Popups popups) {
        Popups.alertPopups = popups;
    }

    public static Popups getAlertPopupEnum() {
        return alertPopups;
    }

    public static void setLargeMenuPopupEnum(Popups popups) {
        Popups.largeMenuPopups = popups;
    }

    public static Popups getLargeMenuPopupEnum() {
        return largeMenuPopups;
    }

    public static double[] getMenuDimensions() {
        return new double[]{misc.getScreenWidth() * .315, misc.getScreenWidth() * .285};
    }

    public static double[] getLargeMenuDimensions() {
        return new double[]{misc.getScreenHeight() * .8, misc.getScreenWidth() * .4};
    }

    public static double[] getAlertDimensions() {
        return new double[]{misc.getScreenHeight() * .3, misc.getScreenHeight() * .415};
    }
}
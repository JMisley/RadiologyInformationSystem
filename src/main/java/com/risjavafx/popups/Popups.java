package com.risjavafx.popups;

import com.risjavafx.Miscellaneous;
import javafx.stage.Popup;

public enum Popups {
    ADMIN("admin-popup.fxml", PopupManager.popupMenu, "MENU"),
    APPOINTMENT("appointment-popup.fxml", PopupManager.popupMenu, "MENU"),
    ORDERS("orders-popup.fxml", PopupManager.popupMenu, "MENU"),
    CONFIRMATION("popup-confirmation.fxml", PopupManager.popupAlert, "ALERT"),
    ALERT("popup-alert.fxml", PopupManager.popupAlert, "ALERT"),
   // BILLING("referrals/billing-popup.fxml", PopupManager.popupMenu, "MENU"),
    REFERRALS("referrals/referral-popup.fxml", PopupManager.popupMenu, "MENU"),
    VIEW_REFERRALS("referrals/view-referral-popup.fxml", PopupManager.popupMenu, "LARGE_MENU");

    private static Popups menuPopups;
    private static Popups alertPopups;
    private final String filename;
    private final Popup popup;
    private final String type;

    static final Miscellaneous misc = new Miscellaneous();

    Popups(String fileName, Popup popup, String type) {
        this.filename = fileName;
        this.popup = popup;
        this.type = type;
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

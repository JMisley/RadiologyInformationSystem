package com.risjavafx.popups;

import com.risjavafx.Miscellaneous;
import javafx.stage.Popup;

public enum Popups {
    ADMIN("admin-popup.fxml", PopupManager.popupMenu, "MENU", true),
    APPOINTMENT("appointment-popup.fxml", PopupManager.popupMenu, "MENU", true),
    ORDERS("orders-popup.fxml", PopupManager.popupMenu, "MENU", true),
    BILLING("billing-popup.fxml", PopupManager.largePopupMenu, "LARGE_MENU", true),
    CONFIRMATION("popup-confirmation.fxml", PopupManager.popupAlert, "ALERT", false),
    ALERT("popup-alert.fxml", PopupManager.popupAlert, "ALERT", true),
    LOADING("popup-loading.fxml", PopupManager.popupAlert, "ALERT", true),
    REFERRALS("referrals/referral-popup.fxml", PopupManager.popupMenu, "MENU", true),
    PATIENT_BACKGROUND("referrals/patient-background.fxml", PopupManager.popupMenu, "MENU", true),
    ORDER_ALERT("order-alert.fxml", PopupManager.customPopup, "CUSTOM", true),
    VIEW_REFERRALS("referrals/view-referral-popup.fxml", PopupManager.largePopupMenu, "LARGE_MENU", true);

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

    public static double[] getMenuDimensions() {
        return new double[]{misc.getScreenWidth() * .315, misc.getScreenWidth() * .285};
    }

    public static double[] getLargeMenuDimensions() {
        return new double[]{misc.getScreenHeight() * .8, misc.getScreenWidth() * .4};
    }

    public static double[] getAlertDimensions() {
        return new double[]{misc.getScreenHeight() * .3, misc.getScreenHeight() * .415};
    }

    public static double[] getCustomDimensions() {
        return new double[]{misc.getScreenHeight() * .6, misc.getScreenWidth() * .33};
    }
}
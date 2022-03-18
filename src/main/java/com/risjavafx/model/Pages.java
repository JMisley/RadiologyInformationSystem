package com.risjavafx.model;

import com.risjavafx.controller.*;

public enum Pages {
    HOME("pages/home-page.fxml", Home.class, true),
    USERINFO("pages/userinfo-page.fxml", UserInfo.class, true),
    ADMIN("pages/admin-page.fxml", Admin.class, true),
    REFERRALS("pages/referrals-page.fxml", Referrals.class, true),
    APPOINTMENTS("pages/appointments-page.fxml", Appointments.class, true),
    ORDERS("pages/orders-page.fxml", Orders.class, true),
    LOGIN("pages/login-page.fxml", Login.class, false);

    private static Pages page;
    private final String filename;
    private final Class<?> controller;
    private final boolean isCachable;

    Pages(String fileName, Class<?> thisClass, boolean isCachable) {
        this.filename = fileName;
        this.controller = thisClass;
        this.isCachable = isCachable;
    }

    public String getFilename() {
        return filename;
    }

    public Class<?> getController() {
        return controller;
    }

    public boolean isCachable() {
        return isCachable;
    }

    public static Pages getPage() {
        return page;
    }

    public static void setPage(Pages page) {
        Pages.page = page;
    }
}

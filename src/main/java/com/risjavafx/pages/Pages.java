package com.risjavafx.pages;

import com.risjavafx.pages.admin.Admin;
import com.risjavafx.pages.appointments.Appointments;
import com.risjavafx.pages.home.Home;
import com.risjavafx.pages.images.ImagesPage;
import com.risjavafx.pages.login.Login;
import com.risjavafx.pages.orders.Orders;
import com.risjavafx.pages.loadingPage.LoadingPage;
import com.risjavafx.pages.referrals.Referrals;
import com.risjavafx.pages.userInfo.UserInfo;

public enum Pages {
    ADMIN("admin-page.fxml", Admin.class, true),
    REFERRALS("referrals-page.fxml", Referrals.class, true),
    APPOINTMENTS("appointments-page.fxml", Appointments.class, true),
    ORDERS("orders-page.fxml", Orders.class, true),
    USERINFO("userinfo-page.fxml", UserInfo.class, true),
    HOME("home-page.fxml", Home.class, true),
    PROGRESS("loadingPage/loading-page.fxml", LoadingPage.class, true),
    LOGIN("login-page.fxml", Login.class, false),
    IMAGE("images-page.fxml", ImagesPage.class, false);

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

    public static Pages getPage() {return page;}

    public static void setPage(Pages page) {
        Pages.page = page;
    }
}
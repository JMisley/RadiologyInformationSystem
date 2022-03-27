package com.risjavafx.model;

public enum Pages {
    HOME("pages/home-page.fxml", true),
    USERINFO("pages/userinfo-page.fxml", true),
    ADMIN("pages/admin-page.fxml", true),
    REFERRALS("pages/referrals-page.fxml", true),
    APPOINTMENTS("pages/appointments-page.fxml", true),
    ORDERS("pages/orders-page.fxml", true),
    //LOGIN("pages/login-page.fxml", false);
    IMAGE("pages/image-page.fxml", true);


    private static Pages page;
    private final String filename;
    private final boolean isCachable;

    Pages (String fileName, boolean isCachable) {
        this.filename = fileName;
        this.isCachable = isCachable;
    }

    public String getFilename() {
        return filename;
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

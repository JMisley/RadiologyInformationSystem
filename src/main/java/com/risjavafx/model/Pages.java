package com.risjavafx.model;

public enum Pages {
    HOME, USERINFO, ADMIN, REFERRALS, APPOINTMENTS, ORDERS;

    private static Pages page;

    public static Pages getPage() {
        return page;
    }

    public static void setPage(Pages page) {
        Pages.page = page;
    }
}

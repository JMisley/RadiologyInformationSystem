package com.risjavafx.components;

public enum Components {
    NAVIGATION_BAR("NavigationBar.fxml"),
    TABLE_SEARCH_BAR("TableSearchBar.fxml"),
    TITLE_BAR("TitleBar.fxml");

    private static Components component;
    private final String filename;

    Components(String fileName) {
        this.filename = fileName;
    }

    public static Components getComponent() {
        return component;
    }

    public String getFilename() {
        return filename;
    }
}

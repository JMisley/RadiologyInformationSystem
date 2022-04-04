package com.risjavafx.pages;

import javafx.scene.layout.StackPane;

public class TableManager {
    private static StackPane adminTable;

    public static StackPane getAdminTable() {
        return adminTable;
    }

    public static void setAdminTable(StackPane adminTable) {
        TableManager.adminTable = adminTable;
    }
}

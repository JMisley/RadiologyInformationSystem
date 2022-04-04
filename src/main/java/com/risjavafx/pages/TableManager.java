package com.risjavafx.pages;

import com.risjavafx.pages.admin.AdminData;
import com.risjavafx.pages.admin.LoginData;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;

public class TableManager {
    private static StackPane adminTable;

    public static StackPane getAdminTable() {
        return adminTable;
    }

    public static void setAdminTable(TableView<LoginData> adminTable) {
        TableManager.adminTable = new StackPane(adminTable);
    }
}

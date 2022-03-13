package com.risjavafx.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class ScreenManager {
    public HashMap<String, Parent> screensMap = new HashMap<>();
    public String[] pageUrls = new String[] {
            "pages/home-page.fxml",
            "pages/userinfo-page.fxml",
            "pages/admin-page.fxml",
            "pages/referrals-page.fxml",
            "pages/appointments-page.fxml",
            "pages/orders-page.fxml",
            "pages/login-page.fxml"
    };

    public Parent getPage(String pageUrl) {
        return screensMap.get(pageUrl);
    }

    public void initializeHashMap() throws IOException {
        for (String url : pageUrls) {
            Parent pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(url)));
            pane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());
            screensMap.put(url, pane);
        }
    }
}

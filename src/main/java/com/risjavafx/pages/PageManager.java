package com.risjavafx.pages;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PageManager {
    private static final Map<Pages, Parent> cache = new HashMap<>();
    public static Scene scene;
    public static Parent root;

    public static void setScene (Scene scene) {
        PageManager.scene = scene;
    }

    public static void switchPage(Pages page) {
        if (scene == null) {
            System.out.println("No scene was set");
            return;
        }
        try {
            Parent root;
            if (cache.containsKey(page) && page.isCachable()) {
                root = cache.get(page);
                System.out.println("From cache");
            } else {
                root = FXMLLoader.load(Objects.requireNonNull(PageManager.class.getResource(page.getFilename())));
                root.getStylesheets().add(Objects.requireNonNull(PageManager.class.getResource("stylesheet/styles.css")).toExternalForm());
                cache.put(page, root);
                System.out.println("From file system");
            }
            scene.setRoot(root);
            PageManager.root = root;
            Pages.setPage(page);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

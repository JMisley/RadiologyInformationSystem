package com.risjavafx.pages;

import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.util.Duration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PageManager {
    private static final Map<Pages, Parent> cache = new HashMap<>();
    private static Scene scene;
    private static Parent root;

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
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void switchPageWithFade(Pages page) {
        Pages rootPage = null;
        for (Pages pages : cache.keySet()) {
            if (cache.get(pages).equals(PageManager.root))
                rootPage = pages;
        }
        FadeTransition transition1 = fadeTransition(rootPage, 1, .3);
        transition1.setOnFinished(event -> fadeTransition(page, .5, 1));
    }

    private static FadeTransition fadeTransition(Pages page, double opacityStart, double opacityEnd) {
        FadeTransition fadeTransition = new FadeTransition();
        if (PageManager.root != cache.get(page)) {
            PageManager.root = cache.get(page);
            scene.setRoot(PageManager.root);
        }
        fadeTransition.setDuration(Duration.millis(1000));
        fadeTransition.setNode(PageManager.root);
        fadeTransition.setFromValue(opacityStart);
        fadeTransition.setToValue(opacityEnd);
        fadeTransition.play();
        return fadeTransition;
    }

    public static void clearCache() {
        cache.clear();
    }

    // Use this to override caching functionality for specific methods in an isCachable class
    public static Scene getScene() {
        return scene;
    }

    public static void setScene(Scene scene) {
        PageManager.scene = scene;
    }

    public static Parent getRoot() {
        return root;
    }

    // Loads all pages to cache
    public static void loadPagesToCache() {
        try {
            for (Pages page : Pages.values()) {
                if (page.isCachable()) {
                    Parent root = FXMLLoader.load(Objects.requireNonNull(PageManager.class.getResource(page.getFilename())));
                    root.getStylesheets().add(Objects.requireNonNull(PageManager.class.getResource("stylesheet/styles.css")).toExternalForm());
                    cache.put(page, root);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    // Loads a single desired page to cache
    public static void loadPageToCache(Pages page) {
        try {
            if (page.isCachable()) {
                Parent root = FXMLLoader.load(Objects.requireNonNull(PageManager.class.getResource(page.getFilename())));
                root.getStylesheets().add(Objects.requireNonNull(PageManager.class.getResource("stylesheet/styles.css")).toExternalForm());
                cache.put(page, root);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Returns root of a page that is not loaded into cache
    public static Parent getRootFromUnloadedPage(Pages page) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(PageManager.class.getResource(page.getFilename())));
        root.getStylesheets().add(Objects.requireNonNull(PageManager.class.getResource("stylesheet/styles.css")).toExternalForm());
        return root;
    }
}
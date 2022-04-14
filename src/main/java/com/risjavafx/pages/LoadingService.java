package com.risjavafx.pages;

import com.risjavafx.UserStates;
import com.risjavafx.components.main.Main;
import com.risjavafx.popups.PopupManager;
import com.risjavafx.popups.Popups;
import com.risjavafx.popups.models.Notification;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;

import java.net.URL;
import java.util.ResourceBundle;

public class LoadingService implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    // Reloads entire application and opens to a specific page (uses Progress Page as loading UI)
    public static class GlobalResetPageSwitch extends Service<Void> {
        private final Pages page;

        public GlobalResetPageSwitch(Pages page) {
            this.page = page;
        }

        public Task<Void> createTask() {
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() {
                    PageManager.switchPage(Pages.PROGRESS);
                    UserStates.setUserState();
                    PageManager.loadPagesToCache();
                    PopupManager.loadPopupsToCache();
                    return null;
                }
            };
            task.setOnSucceeded(event -> PageManager.switchPage(page));
            return task;
        }
    }

    // Reloads entire application without switching page (uses progress spinner as loading UI)
    public static class GlobalResetDefault extends Service<Void> {
        String notificationHeader, notificationText;

        public GlobalResetDefault() {
        }

        public GlobalResetDefault(String notificationHeader, String notificationText) {
            this.notificationHeader = notificationHeader;
            this.notificationText = notificationText;
        }

        public Task<Void> createTask() {
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() {
                    Main.usableStage.getScene().setCursor(Cursor.WAIT);
                    Main.usableStage.getScene().getRoot().setDisable(true);
                    PageManager.loadPagesToCache();
                    PopupManager.loadPopupsToCache();
                    return null;
                }
            };
            task.setOnSucceeded(event -> {
                Main.usableStage.getScene().getRoot().setDisable(false);
                Main.usableStage.getScene().setCursor(Cursor.DEFAULT);
                if (notificationHeader != null)
                    Notification.createNotification(notificationHeader, notificationText);
            });
            return task;
        }
    }

    // Reloads entire application without switching page (uses progress spinner as loading UI)
    public static class Reset extends Service<Void> {
        public Task<Void> createTask() {
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() {
                    PopupManager.createPopup(Popups.ADMIN);
                    return null;
                }
            };
            task.setOnSucceeded(event -> {});
            return task;
        }
    }
}
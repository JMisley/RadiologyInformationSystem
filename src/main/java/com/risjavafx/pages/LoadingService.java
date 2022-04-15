package com.risjavafx.pages;

import com.risjavafx.UserStates;
import com.risjavafx.popups.PopupManager;
import com.risjavafx.popups.Popups;
import com.risjavafx.popups.models.Notification;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class LoadingService {

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
                    PopupManager.loadPopupsToCache(Popups.values());
                    return null;
                }
            };
            task.setOnSucceeded(event -> PageManager.switchPage(page));
            return task;
        }
    }

    // Reloads entire application and stays on current page (uses Progress Spinner as loading UI)
    public static class DefaultReset extends Service<Void> {
        String notiHeader;
        String notiText;

        public DefaultReset() {}

        public DefaultReset(String notiHeader, String notiText) {
            this.notiHeader = notiHeader;
            this.notiText = notiText;
        }

        public Task<Void> createTask() {
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() {
                    Platform.runLater(() -> PopupManager.createPopup(Popups.LOADING));
                    PageManager.loadPagesToCache();
                    PopupManager.loadPopupsToCache(Popups.values());
                    return null;
                }
            };
            task.setOnSucceeded(event -> {
                if (notiHeader != null)
                    Notification.createNotification(notiHeader, notiText);
                PopupManager.removePopup();
            });
            return task;
    }
}
}
package com.risjavafx.pages;

import com.risjavafx.UserStates;
import com.risjavafx.popups.PopupManager;
import com.risjavafx.popups.Popups;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class LoadingService  {

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
        public Task<Void> createTask() {
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() {
                    PageManager.loadPagesToCache();
                    PopupManager.loadPopupsToCache();
                    return null;
                }
            };
            task.setOnSucceeded(event -> succeeded());
            return task;
        }
    }
}
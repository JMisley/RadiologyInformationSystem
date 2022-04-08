package com.risjavafx.pages;

import com.risjavafx.UserStates;
import com.risjavafx.popups.PopupManager;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class LoadingService  {

    // Reloads entire application and opens to a specific page (uses Progress Page as progress spinner)
    public static class GlobalReset extends Service<Void> {
        private final Pages page;

        public GlobalReset(Pages page) {
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
}
package com.risjavafx.pages;

import com.risjavafx.popups.PopupManager;
import com.risjavafx.popups.Popups;

public interface Loadable {
    default void loadMethods() {
        PopupManager.loadPopupsToCache(Popups.values());
        PageManager.loadPagesToCache();
    }

    default void performAction() {}
}

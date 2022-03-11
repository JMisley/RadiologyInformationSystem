package com.risjavafx.model;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

public class Miscellaneous {
    Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

    public double getScreenWidth() {
        return primaryScreenBounds.getWidth();
    }

    public double getScreenHeight() {
       return primaryScreenBounds.getHeight();
    }
}

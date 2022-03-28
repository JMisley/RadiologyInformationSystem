package com.risjavafx.pages.loadingPage;

import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoadingPage implements Initializable {
    public ImageView progressSpinner;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        progressSpinner.setImage(new Image(Objects.requireNonNull(getClass().getResource("spinner.gif")).toExternalForm()));
    }
}

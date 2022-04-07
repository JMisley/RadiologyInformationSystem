package com.risjavafx.pages.loadingPage;

import com.risjavafx.components.TitleBar;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoadingPage implements Initializable {
    public ImageView progressSpinner;
    public BorderPane mainContainer;
    public HBox titleBar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TitleBar.createTitleBar(mainContainer, titleBar);
        progressSpinner.setImage(new Image(Objects.requireNonNull(getClass().getResource("spinner.gif")).toExternalForm()));
    }
}

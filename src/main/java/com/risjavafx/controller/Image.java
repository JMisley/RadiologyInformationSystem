package com.risjavafx.controller;

import com.risjavafx.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;


import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
public class Image implements Initializable {

    public HBox imgVBox;
    public HBox topContent, titleBar;
    public BorderPane mainContainer;
    public Button ifileButton;
    public StackPane centerContent;



    public void initialize(URL url, ResourceBundle resourceBundle) {
        Pages.setPage(Pages.IMAGE);
        TitleBar.createTitleBar(mainContainer, titleBar, this.getClass());
        uploadImages();

    }

    public void uploadImages() {
        //primaryStage.setTitle("upload new image");
        FileChooser fileChooser = new FileChooser();
        HBox imgBox = new HBox();
        Button fileButton = new Button("Select File");
        imgBox.getChildren().add(fileButton);
        imgBox.setAlignment(Pos.CENTER);
        fileButton.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(new Stage());
        });

        imgVBox.getChildren().add(imgBox);





        //VBox imgVBox = new VBox(fileButton);
        //Scene imgscene = new Scene(imgBox, 250, 250);
        //imgBox.setAlignment(Pos.CENTER);

        //primaryStage.setScene(imgscene);
        //primaryStage.show();


    }
}


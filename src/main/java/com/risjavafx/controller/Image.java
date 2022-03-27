package com.risjavafx.controller;

import com.risjavafx.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import com.risjavafx.model.Driver;
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
import java.io.IOException;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.io.*;
import java.io.File;
import java.sql.PreparedStatement;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Image implements Initializable {

    public HBox imgVBox;
    public HBox topContent, titleBar;
    public BorderPane mainContainer;
    public Button ifileButton;
    public StackPane centerContent;
   // public Button add = new Button("Add File");
    public FileInputStream file;
    public Label imageIDLabel;


    Driver driver = new Driver();
    Main main = new Main();
    Miscellaneous misc = new Miscellaneous();

    public Image() throws SQLException {
    }


    public void initialize(URL url, ResourceBundle resourceBundle) {
        Pages.setPage(Pages.IMAGE);
        TitleBar.createTitleBar(mainContainer, titleBar, this.getClass());
        setImagingIDLabel();
        uploadImages();


    }

    public void setImagingIDLabel() {
        try {
            String sql = """
                    select MAX(imaging_id)
                    from imaging_info;
                    """;
            ResultSet resultSet = driver.connection.createStatement().executeQuery(sql);
            while (resultSet.next()) {
                imageIDLabel.setText(String.valueOf(resultSet.getInt("MAX(imaging_id)") + 1));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    public void uploadImages() {
        //primaryStage.setTitle("upload new image");
        FileChooser fileChooser = new FileChooser();
        HBox imgBox = new HBox();
        Button fileButton = new Button("Select File");
        Button add = new Button("Add Image");
        imgBox.getChildren().add(fileButton);
        imgBox.getChildren().add(add);
        imgBox.setAlignment(Pos.CENTER);
        imgVBox.getChildren().add(imgBox);
        fileButton.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(new Stage());
            add.setOnAction(f ->{
                String sql = """
                        insert into imaging_info
                        values (?, ?);
                        """;
                try {
                    PreparedStatement preparedStatement = driver.connection.prepareStatement(sql);
                    preparedStatement.setInt(1, Integer.parseInt(imageIDLabel.getText()));
                    preparedStatement.setString(2, selectedFile.getAbsolutePath());
                    preparedStatement.execute();

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });

        });

        //imgVBox.getChildren().add(imgBox);






        //VBox imgVBox = new VBox(fileButton);
        //Scene imgscene = new Scene(imgBox, 250, 250);
        //imgBox.setAlignment(Pos.CENTER);

        //primaryStage.setScene(imgscene);
        //primaryStage.show();


    }






}



package com.risjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import javax.xml.transform.Result;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class Appointments implements Initializable {
  Miscellaneous misc = new Miscellaneous();
    public HBox topContent;
    public StackPane centerContent;
    public TableColumn<AppointmentData, String>
            patient = new TableColumn<>("Patient"),
            modality = new TableColumn<>("Modality"),
            price = new TableColumn<>("Price"),
            dateTime = new TableColumn<>("Date"),
            radiologist = new TableColumn<>("Radiologist"),
            technician = new TableColumn<>("Technician"),
            closedFlag = new TableColumn<>("Closed");

    public ArrayList<TableColumn<AppointmentData, String>> tableColumnsList = new ArrayList<>() {{
        add(patient);
        add(modality);
        add(price);
        add(dateTime);
        add(radiologist);
        add(technician);
        add(closedFlag);
    }};
    public HBox titleBar;
    public BorderPane mainContainer;


    // Load NavigationBar component into home-page.fxml
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TitleBar.createTitleBar(mainContainer, titleBar, this.getClass());
        NavigationMenu.createNavBar(topContent, this.getClass());

        try {
            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void createTable() throws SQLException {
        queryData();
        setCellFactoryValues();
        InfoTable<AppointmentData, String> infoTable = new InfoTable<>() {{
            setColumns(tableColumnsList);
            addColumnsToTable();
            centerContent.setMaxWidth(misc.getScreenWidth() * .85);
            centerContent.setMaxHeight(misc.getScreenHeight() * .75);
            tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            tableView.setStyle("styles.css");
        }};
        centerContent.getChildren().add(infoTable.tableView);
        infoTable.tableView.setItems(queryData());
    }

    public ObservableList<AppointmentData> queryData() throws SQLException {
        ObservableList<AppointmentData> observableList = FXCollections.observableArrayList();
        Driver driver = new Driver();
        ResultSet resultSet = driver.connection.createStatement().executeQuery("""
                SELECT *
                FROM appointments, modalities, patients, users
                WHERE modality_id = modality AND user_id = radiologist
                              
                """);


        while (resultSet.next()) {
            String name = resultSet.getString("first_name") + " " + resultSet.getString("last_name");
            String checkIn;
            if (resultSet.getByte("closed") == 1) {
                checkIn = "concluded";
            } else {
                checkIn = "In Progress";
            }
            observableList.add(new AppointmentData(
                    name,
                    resultSet.getString("name"),
                    resultSet.getString("price"),
                    resultSet.getDate("date_time").toString(),
                    resultSet.getString("radiologist"),
                    resultSet.getString("technician"),
                    checkIn
            ));
        }
        return observableList;
    }

    public void setCellFactoryValues() {
        patient.setCellValueFactory(new PropertyValueFactory<>("Patient"));
        modality.setCellValueFactory(new PropertyValueFactory<>("Modality"));
        price.setCellValueFactory(new PropertyValueFactory<>("Price"));
        dateTime.setCellValueFactory(new PropertyValueFactory<>("Date"));
        radiologist.setCellValueFactory(new PropertyValueFactory<>("Radiologist"));
        technician.setCellValueFactory(new PropertyValueFactory<>("Technician"));
        closedFlag.setCellValueFactory(new PropertyValueFactory<>("closed"));
    }
}
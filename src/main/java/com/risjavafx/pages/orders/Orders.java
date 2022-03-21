package com.risjavafx.pages.orders;

import java.io.IOException;
import java.net.URL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import com.risjavafx.Driver;
import com.risjavafx.Miscellaneous;
import com.risjavafx.components.InfoTable;
import com.risjavafx.components.NavigationBar;
import com.risjavafx.components.TitleBar;
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


public class Orders implements Initializable {
    public HBox titleBar;
    public BorderPane mainContainer;
    Miscellaneous misc = new Miscellaneous();

    public HBox topContent;
    public StackPane centerContent;
    public TableColumn<OrdersData, String> orderId = new TableColumn("Order ID");
    public TableColumn<OrdersData, String> patient = new TableColumn("Patient");
    public TableColumn<OrdersData, String> referralMd = new TableColumn("Referral MD");
    public TableColumn<OrdersData, String> modality = new TableColumn("Modality");
    public TableColumn<OrdersData, String> appointment = new TableColumn("Appointment");
    public TableColumn<OrdersData, String> notes = new TableColumn("Notes");
    public TableColumn<OrdersData, String> status = new TableColumn("Status");
    public TableColumn<OrdersData, String> report = new TableColumn("Report");
    public ArrayList<TableColumn<OrdersData, String>> tableColumnsList = new ArrayList<>(){{
        add(orderId); add(patient); add(referralMd); add(modality); add(appointment); add(notes); add(status); add(report);
    }};
    // Load NavigationBar component into home-page.fxml
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TitleBar.createTitleBar(mainContainer, titleBar);
        NavigationBar.createNavBar(topContent);


        try {
            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void createTable() throws SQLException {
        queryData();
        setCellFactoryValues();
        InfoTable<OrdersData, String> infoTable = new InfoTable<>(){{
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

    public ObservableList<OrdersData> queryData() throws SQLException {ObservableList<OrdersData> observableList = FXCollections.observableArrayList();
        Driver driver = new Driver();
        ResultSet resultSet = driver.connection.createStatement().executeQuery("""
                SELECT * FROM db_ris.orders
                """);

        while (resultSet.next()) {
            observableList.add(new OrdersData(
                    resultSet.getString("order_id"),
                    resultSet.getString("patient"),
                    resultSet.getString("referral_md"),
                    resultSet.getString("modality"),
                    resultSet.getString("appointment"),
                    resultSet.getString("notes"),
                    resultSet.getString("status"),
                    resultSet.getString("report")
            ));
        }
        return observableList;
    }

    public void setCellFactoryValues() {
        orderId.setCellValueFactory(new PropertyValueFactory<>("orderIdData"));
        patient.setCellValueFactory(new PropertyValueFactory<>("patientData"));
        referralMd.setCellValueFactory(new PropertyValueFactory<>("referralMdData"));
        modality.setCellValueFactory(new PropertyValueFactory<>("modality"));
        appointment.setCellValueFactory(new PropertyValueFactory<>("appointment"));
        notes.setCellValueFactory(new PropertyValueFactory<>("notes"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        report.setCellValueFactory(new PropertyValueFactory<>("report"));
    }
}

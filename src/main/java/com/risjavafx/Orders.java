package com.risjavafx;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;


public class Orders implements Initializable {
    public HBox topContent;
    public StackPane centerContent;
    public TableColumn<OrdersData, Integer> orderId = new TableColumn("Order ID");
    public TableColumn<OrdersData, Integer> patient = new TableColumn("Patient");
    public TableColumn<OrdersData, Integer> referralMd = new TableColumn("Referral MD");
//    public TableColumn<OrdersData, String> modality = new TableColumn("Modality");
//    public TableColumn<OrdersData, String> appointment = new TableColumn("Appointment");
//    public TableColumn<OrdersData, String> notes = new TableColumn("Notes");
//    public TableColumn<OrdersData, String> report = new TableColumn("Report");
public ArrayList<TableColumn<OrdersData, Integer
        >> tableColumnsList = new ArrayList<>(){{
    add(orderId); add(patient); add(referralMd);
}};
    // Load NavigationBar component into home-page.fxml
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createNavBar();

        try {
            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createNavBar() {
        try {
            URL navigationBarComponent = getClass().getResource("fxml components/NavigationBar.fxml");
            topContent.getChildren().setAll((Node) FXMLLoader.load(Objects.requireNonNull(navigationBarComponent)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createTable() throws SQLException {
        queryData();
        setCellFactoryValues();
        InfoTable<OrdersData> infoTable = new InfoTable<>(){{
            setColumns(tableColumnsList);
            addColumnsToTable();
            tableView.setMaxWidth(1150);
            tableView.setMaxHeight(650);
            tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            tableView.setStyle("styles.css");
        }};
        centerContent.getChildren().add(infoTable.tableView);
        infoTable.tableView.setItems(queryData());
    }

    public ObservableList<OrdersData> queryData() throws SQLException {ObservableList<OrdersData> observableList = FXCollections.observableArrayList();
        Driver driver = new Driver();
        ResultSet resultSet = driver.connection.createStatement().executeQuery("""
                SELECT order_id, patient, referral_md FROM db_ris.orders
                """);

        while (resultSet.next()) {
            observableList.add(new OrdersData(
                    resultSet.getString("order_id"),
                    resultSet.getString("patient"),
                    resultSet.getString("referral_md")
//                    resultSet.getString("email"),
//                    resultSet.getString("name")
            ));
        }
        return observableList;
    }

    public void setCellFactoryValues() {
        orderId.setCellValueFactory(new PropertyValueFactory<>("orderIdData"));
        patient.setCellValueFactory(new PropertyValueFactory<>("patientData"));
        referralMd.setCellValueFactory(new PropertyValueFactory<>("referralMdData"));
//        emailAdr.setCellValueFactory(new PropertyValueFactory<>("emailAddressData"));
//        systemRole.setCellValueFactory(new PropertyValueFactory<>("systemRoleData"));
    }
}

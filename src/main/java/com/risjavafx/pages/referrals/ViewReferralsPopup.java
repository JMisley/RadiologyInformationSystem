package com.risjavafx.pages.referrals;

import com.risjavafx.Driver;
import com.risjavafx.Miscellaneous;
import com.risjavafx.pages.PageManager;
import com.risjavafx.popups.PopupManager;
import com.risjavafx.popups.Popups;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.sql.SQLException;

public class ViewReferralsPopup implements Initializable  {

    public VBox popupContainer;
    public Button returnButton;
    public Button submitButton;
    public Button viewImagesButton;
    public ComboBox<String> appointmentsComboBox;
    public ComboBox<Integer> ordersComboBox;
    public static int  clickedPatientId;
    public TextArea notesTextArea;
    public TextArea reportTextArea;
    public ImageView view;
    public Image image;

    public File file;
    public BorderPane layout;
    public ImageView imageView1;


    Driver driver = new Driver();
    Miscellaneous misc = new Miscellaneous();

    public ViewReferralsPopup() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resizeElements();
    setAppointmentsComboBoxListener();
        Popups.VIEW_REFERRALS.getPopup().showingProperty().addListener((observableValue, aBoolean, t1) -> {
            PageManager.getRoot().setDisable(!aBoolean);
            populateComboBoxAppointment();
            populateComboBoxOrder();
            populateNotesTextArea();
            populateReportTextArea();
            setViewImagesButton();


        });
    }

    private void resizeElements() {
        Miscellaneous misc = new Miscellaneous();

        popupContainer.setPrefHeight(Popups.getLargeMenuDimensions()[0]);
        popupContainer.setPrefWidth(Popups.getLargeMenuDimensions()[1]);
        popupContainer.setMaxHeight(Popups.getLargeMenuDimensions()[0]);
        popupContainer.setMaxWidth(Popups.getLargeMenuDimensions()[1]);

        for (Control control : new Control[] {returnButton, submitButton, viewImagesButton, appointmentsComboBox}) {
            control.setPrefHeight(40);
            control.setPrefWidth(misc.getScreenWidth() * .15);
            control.setStyle("-fx-font-size: 14px");
        }
    }
 public  static void setPatientClickedId(int clickedPatientId){
        ViewReferralsPopup.clickedPatientId = clickedPatientId;
 }
 public static int getPatientClickedId() {
 return ViewReferralsPopup.clickedPatientId;
 }

    public void populateComboBoxAppointment(){
        try {
            String sql = """
                    SELECT date_time
                    FROM  appointments
                    WHERE  appointments.patient = ?
                     """;
            ObservableList<String> oblist = FXCollections.observableArrayList();
            PreparedStatement preparedStatement = driver.connection.prepareStatement(sql);
            preparedStatement.setInt(1, getPatientClickedId());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                oblist.add(resultSet.getString("date_time"));
            }
            appointmentsComboBox.setItems(oblist);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    public void populateComboBoxOrder(){
        appointmentsComboBox.valueProperty().addListener(observable -> {
            try {
                String sql = """
                    SELECT orders.order_id
                    FROM orders, appointments
                    WHERE orders.appointment = appointment_id AND appointments.date_time = ?
                     """;
                ObservableList<Integer> oblist = FXCollections.observableArrayList();
                PreparedStatement preparedStatement = driver.connection.prepareStatement(sql);
                preparedStatement.setString(1, returnAppointmentComboBoxDate());
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    oblist.add(resultSet.getInt("order_id"));
                }
                ordersComboBox.setItems(oblist);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

    }



    public void setViewImagesButton() {
        viewImagesButton.setOnAction(actionEvent -> {
            try {
                ResultSet rs;
                String query = "SELECT * FROM imaging_info, orders WHERE orders = order_id;";
                PreparedStatement preparedStatement = driver.connection.prepareStatement(query);
                rs = preparedStatement.executeQuery();

                while(rs.next()){
                InputStream is = rs.getBinaryStream("Image");
                OutputStream os = new FileOutputStream(new File("photo.jpg"));
                byte[] content = new byte[2024];
                int size = 0;
                while ((size = is.read(content)) != -1) {
                    os.write(content, 0, size);
                }

                os.close();
                is.close();

                image = new Image("file:photo.jpg", 100, 150, true, true);
                imageView1 = new ImageView(image);
                imageView1.setFitWidth(100);
                imageView1.setFitHeight(150);
                imageView1.setPreserveRatio(true);

                layout = new BorderPane();
                layout.setCenter(imageView1);
                BorderPane.setAlignment(imageView1, Pos.CENTER);



            }
                preparedStatement.close();
                rs.close();

        } catch (SQLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

    });
    }

    public void populateNotesTextArea(){
        ordersComboBox.valueProperty().addListener(observable -> {


        try {
            String sql = """
                    SELECT notes
                    FROM orders
                    WHERE orders.order_id = ?
                    """;
            PreparedStatement preparedStatement = driver.connection.prepareStatement(sql);
            preparedStatement.setInt(1, ordersComboBox.getValue());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
            notesTextArea.setText(resultSet.getString("notes"));
            }
        }

        catch (Exception exception){

       }
        });
    }

    public void populateReportTextArea(){
        ordersComboBox.valueProperty().addListener(observable -> {


            try {
                String sql = """
                    SELECT report
                    FROM orders
                    WHERE orders.order_id = ?
                    """;
                PreparedStatement preparedStatement = driver.connection.prepareStatement(sql);
                preparedStatement.setInt(1, ordersComboBox.getValue());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    reportTextArea.setText(resultSet.getString("report"));
                }
            }

            catch (Exception exception){

            }
        });
    }

    public String[] getDataToInsert() throws SQLException {



        String sql = """
                        SELECT patients.first_name, patients.last_name, modalities.name
                    FROM appointments,
                         patients,
                         modalities
                    WHERE appointments.date_time =  ?
                      AND appointments.patient = patients.patient_id
                      AND appointments.modality = modalities.modality_id;
                    """;
        PreparedStatement preparedStatement = this.driver.connection.prepareStatement(sql);
        preparedStatement.setString(1, returnAppointmentComboBoxDate());
        ResultSet resultSet = preparedStatement.executeQuery();


        if (resultSet.next()) {
            return new String[]{resultSet.getString("first_name") + " " + resultSet.getString("last_name"), resultSet.getString("name")};

        }

        return null;
    }

    public String returnAppointmentComboBoxDate(){

        String str = appointmentsComboBox.getValue();

        return str;
    }



    public void setAppointmentsComboBoxListener() {
        appointmentsComboBox.valueProperty().addListener(observable -> {

            String str = appointmentsComboBox.getValue();

            System.out.println(str);
        });
    }

    public void returnToPage() {
        try {
            PopupManager.removePopup("MENU");
        }
        catch (Exception ignore) {}
    }

    public void submitChanges() {
        try {
            PopupManager.removePopup("MENU");
        }
        catch (Exception ignore) {}
    }
}

package com.risjavafx.pages.images;

import com.risjavafx.Driver;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.*;
import java.net.URL;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ImagesPage implements Initializable {

    private static int orderId;
    public ImageView imageView;
    public StackPane stackPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayImage();
    }

    private void displayImage() {
        try {
            Driver driver = new Driver();
            String sql = """
                SELECT Image
                FROM imaging_info
                WHERE order_id = ?
                """;
            PreparedStatement preparedStatement = driver.connection.prepareStatement(sql);
            preparedStatement.setInt(1, orderId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
                retrieveImage(resultSet);

            imageView.setImage(new Image("file:photo.jpg"));
            imageView.fitHeightProperty().bind(stackPane.heightProperty());
            imageView.fitWidthProperty().bind(stackPane.widthProperty());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void retrieveImage(ResultSet resultSet) throws SQLException, IOException {
        Blob blob = resultSet.getBlob("Image");
        InputStream inputStream = blob.getBinaryStream();
        OutputStream outputStream = new FileOutputStream("photo.jpg");
        byte[] content = new byte[10000];
        int size;
        while ((size = inputStream.read(content)) != -1) {
            outputStream.write(content, 0, size);
        }
        outputStream.close();
        inputStream.close();
    }

    public static void setOrderId(int orderId) {
        ImagesPage.orderId = orderId;
    }
}

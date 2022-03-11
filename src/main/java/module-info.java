module com.example.risjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    exports com.risjavafx.model;
    opens com.risjavafx.model to javafx.fxml;
    exports com.risjavafx.controller;
    opens com.risjavafx.controller to javafx.fxml;
}
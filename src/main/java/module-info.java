module com.example.risjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.controlsfx.controls;

    exports com.risjavafx.model;
    opens com.risjavafx.model to javafx.fxml;
    exports com.risjavafx.controller;
    opens com.risjavafx.controller to javafx.fxml;
}
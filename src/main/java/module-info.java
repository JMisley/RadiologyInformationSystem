module com.example.risjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires MaterialFX;

    opens com.risjavafx to javafx.fxml;
    exports com.risjavafx;
}
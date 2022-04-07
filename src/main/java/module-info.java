module com.example.risjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.controlsfx.controls;

    exports com.risjavafx.pages.admin;
    opens com.risjavafx.pages.admin to javafx.fxml;
    exports com.risjavafx.components;
    opens com.risjavafx.components to javafx.fxml;
    exports com.risjavafx.pages;
    opens com.risjavafx.pages to javafx.fxml;
    exports com.risjavafx.pages.userInfo;
    opens com.risjavafx.pages.userInfo to javafx.fxml;
    exports com.risjavafx.pages.referrals;
    opens com.risjavafx.pages.referrals to javafx.fxml;
    exports com.risjavafx.pages.orders;
    opens com.risjavafx.pages.orders to javafx.fxml;
    exports com.risjavafx.pages.home;
    opens com.risjavafx.pages.home to javafx.fxml;
    exports com.risjavafx.pages.appointments;
    opens com.risjavafx.pages.appointments to javafx.fxml;
    exports com.risjavafx.pages.login;
    opens com.risjavafx.pages.login to javafx.fxml;
    exports com.risjavafx.pages.loadingPage;
    opens com.risjavafx.pages.loadingPage to javafx.fxml;
    exports com.risjavafx.popups;
    opens com.risjavafx.popups to javafx.fxml;
    exports com.risjavafx;
    opens com.risjavafx to javafx.fxml;
    exports com.risjavafx.popups.models;
    opens com.risjavafx.popups.models to javafx.fxml;
}
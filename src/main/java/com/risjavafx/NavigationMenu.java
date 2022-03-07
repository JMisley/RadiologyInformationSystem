package com.risjavafx;

import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NavigationMenu implements Initializable {
    public HBox menuBar;
    public Button homeButton, userInfoButton, adminButton, referralsButton, appointmentsButton, ordersButton, logoutButton;
    public Button[] buttonArray;
    public Main main;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        menuBar.setPrefWidth(primaryScreenBounds.getWidth());
        buttonArray = new Button[] {homeButton, userInfoButton, adminButton, referralsButton, appointmentsButton, ordersButton};
        main = new Main();

        getPageButton().setId("menuBarButtonClicked");
    }

    public void openHome() throws IOException {
        Pages.setPage(Pages.HOME);
        main.changeScene("navigation pages/home-page.fxml");
    }

    public void openUserInfo() throws IOException {
        Pages.setPage(Pages.USERINFO);
        main.changeScene("navigation pages/userinfo-page.fxml");
    }

    public void openAdmin() throws IOException {
        Pages.setPage(Pages.ADMIN);
        main.changeScene("navigation pages/admin-page.fxml");
    }

    public void openReferrals() throws IOException {
        Pages.setPage(Pages.REFERRALS);
        main.changeScene("navigation pages/referrals-page.fxml");
    }

    public void openAppointments() throws IOException {
        Pages.setPage(Pages.APPOINTMENTS);
        main.changeScene("navigation pages/appointments-page.fxml");
    }

    public void openOrders() throws IOException {
        Pages.setPage(Pages.ORDERS);
        main.changeScene("navigation pages/orders-page.fxml");
    }

    public void userLogout() throws IOException {
        main.changeScene("login-page.fxml");
    }

    public Button getPageButton() {
        switch (Pages.getPage()) {
            case HOME -> {return homeButton;}
            case USERINFO -> {return userInfoButton;}
            case ADMIN -> {return adminButton;}
            case REFERRALS -> {return referralsButton;}
            case APPOINTMENTS -> {return appointmentsButton;}
            case ORDERS -> {return ordersButton;}
        }
        return null;
    }
}

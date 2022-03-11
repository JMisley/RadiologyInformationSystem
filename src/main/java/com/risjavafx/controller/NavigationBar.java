package com.risjavafx.controller;

import com.risjavafx.model.Miscellaneous;
import com.risjavafx.model.Pages;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class NavigationBar implements Initializable {
    public HBox menuBar;
    public Button homeButton, userInfoButton, adminButton, referralsButton, appointmentsButton, ordersButton, logoutButton;
    public Button[] buttonArray;
    Miscellaneous misc = new Miscellaneous();
    Main main = new Main();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttonArray = new Button[] {homeButton, userInfoButton, adminButton, referralsButton, appointmentsButton,
                ordersButton, logoutButton};
        getPageButton().setId("menuBarButtonClicked");
        setButtonWidth();
    }

    public static <E> void createNavBar(HBox topContent, Class<E> thisClass) {
        try {
            URL navigationBarComponent = thisClass.getResource("components/NavigationBar.fxml");
            topContent.getChildren().setAll((Node) FXMLLoader.load(Objects.requireNonNull(navigationBarComponent)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openHome() throws IOException {
        Pages.setPage(Pages.HOME);
        main.changeScene("pages/home-page.fxml");
    }

    public void openUserInfo() throws IOException {
        Pages.setPage(Pages.USERINFO);
        main.changeScene("pages/userinfo-page.fxml");
    }

    public void openAdmin() throws IOException {
        Pages.setPage(Pages.ADMIN);
        main.changeScene("pages/admin-page.fxml");
    }

    public void openReferrals() throws IOException {
        Pages.setPage(Pages.REFERRALS);
        main.changeScene("pages/referrals-page.fxml");
    }

    public void openAppointments() throws IOException {
        Pages.setPage(Pages.APPOINTMENTS);
        main.changeScene("pages/appointments-page.fxml");
    }

    public void openOrders() throws IOException {
        Pages.setPage(Pages.ORDERS);
        main.changeScene("pages/orders-page.fxml");
    }

    public void userLogout() throws IOException {
        main.changeScene("pages/login-page.fxml");
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

    public void setButtonWidth() {
        for (Button button: buttonArray) {
            button.setPrefWidth((misc.getScreenWidth()/7) * .8);
            button.setMaxWidth(225);

            double fontSize;
            if ((misc.getScreenWidth()/80) < 20) {
                fontSize = misc.getScreenWidth()/80;
            } else {
                fontSize = 20;
            }
            button.setStyle("-fx-font-size: " + fontSize);
        }
    }
}

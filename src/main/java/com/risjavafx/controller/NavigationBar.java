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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttonArray = new Button[]{homeButton, userInfoButton, adminButton, referralsButton, appointmentsButton,
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

    public void openHome() {
        switchPage(Pages.HOME);
    }

    public void openUserInfo() {
        switchPage(Pages.USERINFO);
    }

    public void openAdmin() {
        switchPage(Pages.ADMIN);
    }

    public void openReferrals() {
        switchPage(Pages.REFERRALS);
    }

    public void openAppointments() {
        switchPage(Pages.APPOINTMENTS);
    }

    public void openOrders() {
        switchPage(Pages.ORDERS);
    }

    public void userLogout() {
        switchPage(Pages.LOGIN);
    }

    public Button getPageButton() {
        switch (Pages.getPage()) {
            case HOME -> {
                return homeButton;
            }
            case USERINFO -> {
                return userInfoButton;
            }
            case ADMIN -> {
                return adminButton;
            }
            case REFERRALS -> {
                return referralsButton;
            }
            case APPOINTMENTS -> {
                return appointmentsButton;
            }
            case ORDERS -> {
                return ordersButton;
            }
        }
        return null;
    }

    public void setButtonWidth() {
        for (Button button : buttonArray) {
            button.setPrefWidth((misc.getScreenWidth() / 7) * .8);
            button.setMaxWidth(225);

            double fontSize;
            if ((misc.getScreenWidth() / 80) < 20) {
                fontSize = misc.getScreenWidth() / 80;
            } else {
                fontSize = 20;
            }
            button.setStyle("-fx-font-size: " + fontSize);
        }
    }

    public void switchPage(Pages page) {
        if (Pages.getPage() != page) {
            PageManager.switchPage(page);
        }
    }
}

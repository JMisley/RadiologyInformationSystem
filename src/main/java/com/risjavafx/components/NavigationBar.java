package com.risjavafx.components;

import com.risjavafx.Miscellaneous;
import com.risjavafx.UserStates;
import com.risjavafx.pages.PageManager;
import com.risjavafx.pages.Pages;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.net.URL;
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
        adjustButtonsToUserRole();
    }

    public static void createNavBar(HBox topContent) {
        ComponentsManager.createComponent(Components.NAVIGATION_BAR, topContent);
    }

    public void openHome() {
        switchPage(Pages.HOME);
    }

    public void openUserInfo() {
        switchPage(Pages.USERINFO);
    }

    public void openAdmin() {switchPage(Pages.ADMIN);}

    public void openReferrals() {
        switchPage(Pages.REFERRALS);
    }

    public void openAppointments() {switchPage(Pages.APPOINTMENTS);}

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
            Pages.setPage(page);
            PageManager.switchPage(page);
        }
    }

    // Adjust buttons to  display or to be hidden based on the user role
    public void adjustButtonsToUserRole() {
        switch (UserStates.getUserState()) {
            case ADMIN -> {}
            case REFERRAL_MD, RADIOLOGIST -> disableSelectedButtons(new Button[]{adminButton});
            case RECEPTIONIST -> disableSelectedButtons(new Button[]{adminButton, ordersButton});
            case TECHNICIAN -> disableSelectedButtons(new Button[]{adminButton, referralsButton});
        }
    }

    public void disableSelectedButtons(Button[] buttons) {
        for (Button button : buttons) {
            menuBar.getChildren().remove(button);
        }
    }
}

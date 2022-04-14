package com.risjavafx.pages.home;

import com.risjavafx.Miscellaneous;
import com.risjavafx.components.TitleBar;
import com.risjavafx.components.NavigationBar;
import com.risjavafx.pages.PageManager;
import com.risjavafx.pages.Pages;
import com.risjavafx.pages.TableManager;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class Home implements Initializable {

    public BorderPane mainContainer;
    public HBox titleBar;
    public HBox topContent;
    public ScrollPane scrollPane;
    public VBox tableViewList;

    ArrayList<StackPane> stackPanes = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Pages.setPage(Pages.HOME);
        TitleBar.createTitleBar(mainContainer, titleBar);
        NavigationBar.createNavBar(topContent);

        createStackPanesArray();
        createScrollView(tableViewList, stackPanes);

        // SOME PROBLEMS
        PageManager.getScene().rootProperty().addListener(observable -> {
            if (Pages.getPage() == Pages.HOME) {
                createStackPanesArray();
                createScrollView(tableViewList, stackPanes);
                refreshTables();
            }
        });
    }

    private void createScrollView(VBox tableViewList, ArrayList<StackPane> stackPanes) {
        for (StackPane stackPane : stackPanes) {
            addToScrollView(stackPane);
            resizeElements();
            scrollPane.setContent(tableViewList);
        }
    }

    private void addToScrollView(StackPane stackPane) {
        Miscellaneous misc = new Miscellaneous();
        tableViewList.getChildren().add(stackPane);
        stackPane.setMaxWidth(misc.getScreenWidth() * .75);
        stackPane.setMaxHeight(misc.getScreenHeight() * .50);
        stackPane.setMinWidth(misc.getScreenWidth() * .75);
        stackPane.setMinHeight(misc.getScreenHeight() * .50);
        stackPane.setId("tableContainer");
    }

    private void createStackPanesArray() {
        StackPane adminPane = new StackPane(TableManager.getAdminTable());
        StackPane referralsPane = new StackPane(TableManager.getReferralsTable());
        StackPane appointmentsPane = new StackPane(TableManager.getAppointmentsTable());
        StackPane ordersPane = new StackPane(TableManager.getOrdersTable());

        switch (UserStates.getUserState()) {
            case ADMIN -> stackPanes.addAll(Arrays.asList(adminPane, referralsPane, appointmentsPane, ordersPane));
            case REFERRAL_MD, RADIOLOGIST -> stackPanes.addAll(Arrays.asList(referralsPane, appointmentsPane, ordersPane));
            case RECEPTIONIST -> stackPanes.addAll(Arrays.asList(referralsPane, appointmentsPane));
            case TECHNICIAN -> stackPanes.addAll(Arrays.asList(appointmentsPane, ordersPane));
        }
    }

    private void refreshTables() {
        tableViewList.getChildren().clear();
        for (StackPane stackPane : stackPanes)
            addToScrollView(stackPane);
    }

    private void resizeElements() {
        Miscellaneous misc = new Miscellaneous();
        tableViewList.setMaxWidth(misc.getScreenWidth());
        tableViewList.setMinWidth(misc.getScreenWidth());
        tableViewList.setMinHeight(misc.getScreenHeight());
        tableViewList.setSpacing(100);
        tableViewList.setPadding(new Insets(100, 0, 100, 0));
        tableViewList.setId("root");
    }
}
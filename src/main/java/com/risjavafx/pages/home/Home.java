package com.risjavafx.pages.home;

import com.risjavafx.Miscellaneous;
import com.risjavafx.components.InfoTable;
import com.risjavafx.components.TitleBar;
import com.risjavafx.components.NavigationBar;
import com.risjavafx.pages.Pages;
import com.risjavafx.pages.TableManager;
import com.risjavafx.pages.admin.Admin;
import com.risjavafx.pages.admin.AdminData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class Home implements Initializable {

    public BorderPane mainContainer;
    public HBox titleBar;
    public HBox topContent;
    public ScrollPane scrollPane;
    public VBox tableViewList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Pages.setPage(Pages.HOME);
        TitleBar.createTitleBar(mainContainer, titleBar);
        NavigationBar.createNavBar(topContent);

        StackPane[] stackPanes = {TableManager.getAdminTable()};
        createScrollView(tableViewList, stackPanes);
    }

    // BIG PROBLEMS!!!!!
    private void createScrollView(VBox tableViewList, StackPane[] stackPanes) {
        Miscellaneous misc = new Miscellaneous();

        for (StackPane stackPane : stackPanes) {
            tableViewList.getChildren().add(stackPane);
            stackPane.setMaxWidth(misc.getScreenWidth() * .75);
            stackPane.setMaxHeight(misc.getScreenHeight() * .85);
        }
        resizeElements();
        scrollPane.setContent(tableViewList);
    }

    private void resizeElements() {
        Miscellaneous misc = new Miscellaneous();
        tableViewList.setMaxWidth(misc.getScreenWidth());
        tableViewList.setMaxHeight(misc.getScreenHeight());
        tableViewList.setMinWidth(misc.getScreenWidth());
        tableViewList.setMinHeight(misc.getScreenHeight());
        tableViewList.setId("root");
    }

    private TableView<AdminData> createAdminTable() {
        InfoTable<AdminData, String> infoTable = new InfoTable<>();
        Admin admin = new Admin();

        infoTable.setColumns(admin.getTableColumnsList());
        infoTable.addColumnsToTable();

        infoTable.setCustomColumnWidth(admin.getTableColumnsList().get(0), .12);
        infoTable.setCustomColumnWidth(admin.getTableColumnsList().get(1), .24);
        infoTable.setCustomColumnWidth(admin.getTableColumnsList().get(2), .17);
        infoTable.setCustomColumnWidth(admin.getTableColumnsList().get(3), .27);
        infoTable.setCustomColumnWidth(admin.getTableColumnsList().get(4), .2);

        ObservableList<AdminData> oblist = FXCollections.observableArrayList(admin.createObservableList());
        infoTable.tableView.setItems(oblist);
        oblist.remove(0);

        return infoTable.tableView;
    }
}

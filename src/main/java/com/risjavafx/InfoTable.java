package com.risjavafx;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.ArrayList;

public class InfoTable<T> {

    public TableView<T> tableView;
    public ArrayList<TableColumn<T, String>> columnsArrayList = new ArrayList<>();


    public InfoTable() {
        tableView = new TableView<>();
        tableView.setColumnResizePolicy(resizeFeatures -> false);
    }

    public void setColumns(ArrayList<TableColumn<T, String>> columnsArrayList) {
        this.columnsArrayList = columnsArrayList;
    }

    public void addColumnsToTable() {
        for (TableColumn<T, String> tableColumn : columnsArrayList) {
            tableView.getColumns().add(tableColumn);
            tableColumn.setReorderable(false);
            tableColumn.setSortable(true);
        }
    }

    // Allows you to specify what column width percent you want for each column
    public void setCustomColumnWidth(TableColumn<T, String> column, double width) {
        column.prefWidthProperty().bind(tableView.widthProperty().multiply(width));
    }
}

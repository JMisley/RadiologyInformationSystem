package com.risjavafx;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.ArrayList;

public class InfoTable <E> {

    public TableView<E> tableView;
    public ArrayList<TableColumn<E, String>> columnsArrayList = new ArrayList<>();


    public InfoTable() {
        tableView = new TableView<>();
        tableView.setColumnResizePolicy(resizeFeatures -> false);
    }

    public void setColumns(ArrayList<TableColumn<E, String>> columnsArrayList) {
        this.columnsArrayList = columnsArrayList;
    }

    public void addColumnsToTable() {
        for (TableColumn<E, String> tableColumn : columnsArrayList) {
            tableView.getColumns().add(tableColumn);
        }
    }

    // Allows you to specify what column width percent you want for each column
    public void setCustomColumnWidth(TableColumn<E, String> column, double width) {
        column.prefWidthProperty().bind(tableView.widthProperty().multiply(width));
    }

    // Sets all columns to equal width
    public void setEvenColumnWidth(ArrayList<TableColumn<E, String>> columnsArrayList) {
        for (TableColumn<E, String> tableColumn : columnsArrayList) {
            tableColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(1/(double)columnsArrayList.size()));
        }
    }
}

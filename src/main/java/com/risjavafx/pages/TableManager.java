package com.risjavafx.pages;

import com.risjavafx.pages.admin.AdminData;
import com.risjavafx.pages.appointments.AppointmentData;
import com.risjavafx.pages.orders.OrdersData;
import javafx.scene.control.TableView;

public class TableManager {
    private static TableView<AdminData> adminTable;
    private static TableView<AppointmentData> appointmentsTable;
    private static TableView<OrdersData> ordersTable;

    public static TableView<AdminData> getAdminTable() {
        return adminTable;
    }

    public static void setAdminTable(TableView<AdminData> adminTable) {
        TableManager.adminTable = adminTable;
    }

    public static TableView<AppointmentData> getAppointmentsTable() {
        return appointmentsTable;
    }

    public static void setAppointmentsTable(TableView<AppointmentData> appointmentsTable) {
        TableManager.appointmentsTable = appointmentsTable;
    }

    public static TableView<OrdersData> getOrdersTable() {
        return ordersTable;
    }

    public static void setOrdersTable(TableView<OrdersData> ordersTable) {
        TableManager.ordersTable = ordersTable;
    }
}

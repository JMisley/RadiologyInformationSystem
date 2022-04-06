package com.risjavafx.pages;

import com.risjavafx.pages.admin.AdminData;
import com.risjavafx.pages.appointments.AppointmentData;
import javafx.scene.control.TableView;

public class TableManager {
    private static TableView<AdminData> adminTable;
    private static TableView<AppointmentData> appointmentsTable;

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
}

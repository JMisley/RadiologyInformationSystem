package com.risjavafx.pages.admin;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

@SuppressWarnings("unused")
public class AdminData {

    SimpleIntegerProperty userIdData;
    SimpleStringProperty displayNameData;
    SimpleStringProperty usernameData;
    SimpleStringProperty emailAddressData;
    SimpleStringProperty systemRoleData;

    public AdminData() {

    }

    public AdminData(int userId, String displayName, String username, String emailAdr, String systemRole) {
        this.userIdData = new SimpleIntegerProperty(userId);
        this.displayNameData = new SimpleStringProperty(displayName);
        this.usernameData = new SimpleStringProperty(username);
        this.emailAddressData = new SimpleStringProperty(emailAdr);
        this.systemRoleData = new SimpleStringProperty(systemRole);
    }

    public int getUserIdData() {
        return userIdData.get();
    }

    public void setUserIdData(int userId) {
        this.userIdData.set(userId);
    }

    public String getDisplayNameData() {
        return displayNameData.get();
    }

    public void setDisplayNameData(String displayNameData) {
        this.displayNameData.set(displayNameData);
    }

    public String getUsernameData() {
        return usernameData.get();
    }

    public void setUsernameData(String usernameData) {
        this.usernameData.set(usernameData);
    }

    public String getEmailAddressData() {
        return emailAddressData.get();
    }

    public void setEmailAddressData(String emailAddressData) {
        this.emailAddressData.set(emailAddressData);
    }

    public String getSystemRoleData() {
        return systemRoleData.get();
    }

    public void setSystemRoleData(String systemRoleData) {
        this.systemRoleData.set(systemRoleData);
    }
}

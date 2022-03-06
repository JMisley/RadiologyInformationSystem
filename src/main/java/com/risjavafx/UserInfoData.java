package com.risjavafx;

import javafx.beans.property.SimpleStringProperty;

public class UserInfoData {
    SimpleStringProperty userIdData;
    SimpleStringProperty usernameData;
    SimpleStringProperty displayNameData;
    SimpleStringProperty emailAddressData;
    SimpleStringProperty systemRoleData;

    public UserInfoData(String userId, String username, String displayName, String emailAdr, String systemRole) {
        this.userIdData = new SimpleStringProperty(userId);
        this.usernameData = new SimpleStringProperty(username);
        this.displayNameData = new SimpleStringProperty(displayName);
        this.emailAddressData = new SimpleStringProperty(emailAdr);
        this.systemRoleData = new SimpleStringProperty(systemRole);
    }

    public String getUserIdData() {
        return userIdData.get();
    }

    public void setUserIdData(String USERID) {
        this.userIdData.set(USERID);
    }

    public String getUsernameData() {
        return usernameData.get();
    }

    public void setUsernameData(String usernameData) {
        this.usernameData.set(usernameData);
    }

    public String getDisplayNameData() {
        return displayNameData.get();
    }

    public void setDisplayNameData(String displayNameData) {
        this.displayNameData.set(displayNameData);
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

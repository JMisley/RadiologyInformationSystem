package com.risjavafx;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public enum UserStates {
    USER,
    ADMIN,
    REFERRAL_MD,
    RECEPTIONIST,
    TECHNICIAN,
    RADIOLOGIST;

    private static UserStates userState;
    private static int userId;

    public static void setUserState() {
        try {
            Driver driver = new Driver();
            PreparedStatement preparedStatement;
            final String sql = """
                        SELECT roles.name
                        FROM roles, users_roles
                        WHERE users_roles.user_id = ? AND roles.role_id = users_roles.role_id
                    """;
            preparedStatement = driver.connection.prepareStatement(sql);
            preparedStatement.setInt(1, UserStates.userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                for (UserStates state : UserStates.values()) {
                    if (state.toString().equals(resultSet.getString("name"))) {
                        UserStates.userState = state;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static UserStates getUserState() {
        return userState;
    }

    public static void setUserId(int userId) {
        UserStates.userId = userId;
    }
}

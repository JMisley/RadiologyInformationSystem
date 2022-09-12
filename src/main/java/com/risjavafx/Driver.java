package com.risjavafx;

import java.sql.*;

public class Driver {

    // Set up connection to database
    public static Connection getConnection() throws SQLException {
        String MYSQL_URL = "jdbc string";
        return DriverManager.getConnection(MYSQL_URL, "username", "password");
    }
}
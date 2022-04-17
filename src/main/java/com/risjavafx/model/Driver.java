package com.risjavafx.model;

import java.sql.*;

public class Driver {

    public Connection connection;

    // Set up connection to database
    public Driver() throws SQLException {
        String MYSQL_URL = "jdbc:mysql://localhost:3306/db_ris";
        connection = DriverManager.getConnection(MYSQL_URL, "root", "1357980Jg*");
    }
}

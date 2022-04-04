package com.risjavafx;

import java.sql.*;

public class Driver {

    public Connection connection;

    // Set up connection to database
    public Driver() throws SQLException {
        String MYSQL_URL = "jdbc:mysql://aws-ris-db.cs15pqp4fpnm.us-east-1.rds.amazonaws.com/db_ris";
        connection = DriverManager.getConnection(MYSQL_URL, "pleasedonthackme", "itsreallyinconvenient");
    }
}
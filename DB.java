package com.ems;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    private static final String URL =
        "jdbc:mysql://localhost:3307p /employee_management_db?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Lalita@04";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Connector/J JAR is missing from WEB-INF/lib.", e);
        }
        return DriverManager.getConnection(URL, DB_USER, DB_PASSWORD);
    }
}

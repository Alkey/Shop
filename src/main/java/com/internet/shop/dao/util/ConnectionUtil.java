package com.internet.shop.dao.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/shop?serverTimezone=UTC";
    private static final String USER = "user";
    private static final String PASSWORD = "user";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }
}

package com.space;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/cosmoport?serverTimezone=UTC";
        String username = "root";
        String password = "root";
        System.out.println("Connecting...");

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connection successful!");
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
    }
}

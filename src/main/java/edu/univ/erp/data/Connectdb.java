package edu.univ.erp.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Connectdb {

    private static final String urlUniversitydb = "jdbc:mysql://127.0.0.1:3306/universitydb";
    private static final String urlAuthdb = "jdbc:mysql://127.0.0.1:3306/authenticationdb";
    private static final String username = "####";
    private static final String password = "@@@@@@@";


    public static Connection getUniversitydbConnection() throws SQLException {
        return DriverManager.getConnection(urlUniversitydb, username, password);
    }


    public static Connection getAuthenticationdbConnection() throws SQLException {
        return DriverManager.getConnection(urlAuthdb, username, password);
    }
}
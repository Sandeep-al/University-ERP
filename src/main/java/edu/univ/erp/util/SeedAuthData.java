package edu.univ.erp.util; 
import edu.univ.erp.data.Connectdb; 
import org.mindrot.jbcrypt.BCrypt;  

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.sql.ResultSet;

public class SeedAuthData {

        
        private static final String ADMIN_USERNAME = "admin";
        private static final String ADMIN_PASSWORD = "adminpass";
        private static final String ADMIN_ROLE = "admin";

    public static void main(String[] args) {
        System.out.println("Starting auth data seeder...");

        String insertAuthSql = "INSERT INTO users_auth (username, role, password_hash) VALUES (?, ?, ?)";
        String insertUniSql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        String hashedPassword = BCrypt.hashpw(ADMIN_PASSWORD, BCrypt.gensalt());

       
        try (Connection connAuth = Connectdb.getAuthenticationdbConnection()) {
            String checkSql = "SELECT COUNT(*) FROM users_auth WHERE username = ?";
            try (PreparedStatement checkStmt = connAuth.prepareStatement(checkSql)) {
                checkStmt.setString(1, ADMIN_USERNAME);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    try (PreparedStatement insertStmt = connAuth.prepareStatement(insertAuthSql)) {
                        insertStmt.setString(1, ADMIN_USERNAME);
                        insertStmt.setString(2, ADMIN_ROLE);
                        insertStmt.setString(3, hashedPassword);
                        int rowsAffected = insertStmt.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Successfully inserted admin into users_auth.");
                        }
                    }
                } else {
                    System.out.println("Admin already exists in users_auth.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error during seeding users_auth: " + e.getMessage());
            e.printStackTrace();
        }

        
        try (Connection connUni = Connectdb.getUniversitydbConnection()) {
            String checkSql = "SELECT COUNT(*) FROM users WHERE username = ?";
            try (PreparedStatement checkStmt = connUni.prepareStatement(checkSql)) {
                checkStmt.setString(1, ADMIN_USERNAME);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    try (PreparedStatement insertStmt = connUni.prepareStatement(insertUniSql)) {
                        insertStmt.setString(1, ADMIN_USERNAME);
                        insertStmt.setString(2, hashedPassword);
                        insertStmt.setString(3, ADMIN_ROLE);
                        int rowsAffected = insertStmt.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Successfully inserted admin into users.");
                        }
                    }
                } else {
                    System.out.println("Admin already exists in users.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error during seeding users: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
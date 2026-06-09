package edu.univ.erp.auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import edu.univ.erp.data.Connectdb;

public class AuthService {

    public String[] checkUserLogin(String username, String plainPassword) {

        String query = "SELECT password_hash, role, status, user_id FROM users_auth WHERE username = ?";

        try (Connection conn = Connectdb.getAuthenticationdbConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    String storedHash = rs.getString("password_hash");
                    String role = rs.getString("role");

                    String userId = rs.getString("user_id");

                    if (BCrypt.checkpw(plainPassword, storedHash)) {

                        System.out.println("Login successful. Role: " + role);
                        return new String[] { role, userId };

                    } else {
                        System.out.println("Incorrect password.");
                        return null;
                    }
                } else {
                    System.out.println("User not found.");
                    return null;
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return null;
        }
    }

}
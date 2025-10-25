package com.lopez.payroll.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Handles all database operations related to user authentication and password management.
 * Requires a valid DatabaseConnection singleton instance.
 */
public class DatabaseConnection {

    /**
     * Helper method to obtain a reusable database connection.
     */
    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Checks if an email exists in the 'users' table.
     * 
     * @param email The email to check
     * @return true if the email exists, false otherwise
     */
    public boolean emailExists(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error checking email existence: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Saves a verification code for a specific user email.
     * 
     * @param email The user's email
     * @param code  The generated verification code
     */
    public void saveVerificationCode(String email, String code) {
        String query = "UPDATE users SET verification_code = ? WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, code);
            stmt.setString(2, email);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ Error saving verification code: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Updates a user's password and clears the verification code.
     * 
     * @param email        The user's email
     * @param newPassword  The new password (already validated)
     * @return true if the update succeeded, false otherwise
     */
    public boolean updatePassword(String email, String newPassword) {
        String query = "UPDATE users SET password = ?, verification_code = NULL WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, newPassword);
            stmt.setString(2, email);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error updating password: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Verifies if a given code matches the stored verification code for an email.
     * 
     * @param email The user's email
     * @param code  The verification code to check
     * @return true if the code matches, false otherwise
     */
    public boolean verifyCode(String email, String code) {
        String query = "SELECT verification_code FROM users WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedCode = rs.getString("verification_code");
                    return storedCode != null && storedCode.equals(code);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error verifying code: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}

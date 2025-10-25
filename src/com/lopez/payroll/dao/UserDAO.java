package com.lopez.payroll.dao;

import com.lopez.payroll.database.DatabaseConnection;
import com.lopez.payroll.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    /**
     * Get user by ID and password (for login)
     * Method name changed to match UserService call
     */
    public User getUserByCredentials(String employeeId, String password) {
        String sql = "SELECT * FROM users WHERE id = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, employeeId);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setFirstName(rs.getString("first_name"));
                user.setMiddleName(rs.getString("middle_name"));
                user.setLastName(rs.getString("last_name"));
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getString("status"));
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Check if email exists
     */
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Add a new user (for ADMIN_Add_emp)
     */
    public boolean addUser(User user) {
        String sql = "INSERT INTO users (email, password, first_name, middle_name, last_name, role, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFirstName());
            ps.setString(4, user.getMiddleName());
            ps.setString(5, user.getLastName());
            ps.setString(6, user.getRole());
            ps.setString(7, user.getStatus());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update existing user (for ADMIN_manage_emp)
     */
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET email = ?, first_name = ?, middle_name = ?, " +
                     "last_name = ?, role = ?, status = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getFirstName());
            ps.setString(3, user.getMiddleName());
            ps.setString(4, user.getLastName());
            ps.setString(5, user.getRole());
            ps.setString(6, user.getStatus());
            ps.setInt(7, user.getId());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete user
     */
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get all users (for ADMIN_manage_emp table display)
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setFirstName(rs.getString("first_name"));
                user.setMiddleName(rs.getString("middle_name"));
                user.setLastName(rs.getString("last_name"));
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getString("status"));
                
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return users;
    }

    /**
     * Get user by ID only
     */
    public User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setFirstName(rs.getString("first_name"));
                user.setMiddleName(rs.getString("middle_name"));
                user.setLastName(rs.getString("last_name"));
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getString("status"));
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Check if user ID exists
     */
    public boolean userIdExists(int userId) {
        String sql = "SELECT COUNT(*) FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}



/*package com.lopez.payroll.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.lopez.payroll.database.DatabaseConnection;
import com.lopez.payroll.model.User;

public class UserDAO {
	
public int create(User user) {
	
		
		String sql = """
				INSERT INTO users 
					(username, first_name, last_name, middle_name, password, email, 
					role, birth_date, gender, nationality, house_no, barangay, city, province,
					zip_code, contact_no, emergency_no1, emergency_no2, position, 
					joining_date, status, basic_salary, photo, created_at, updated_at ) 
				VALUES 
					(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
			""";
		
		int generatedId = -1; // Default if insertion fails
		
		 try (Connection conn = DatabaseConnection.getInstance().getConnection();
	             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			

            ps.setString(1, user.getUsername());
            ps.setString(4, user.getFirstName());
            ps.setString(5, user.getLastName());
            ps.setString(5, user.getMiddleName());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.setString(1, user.getRole());
            ps.setString(1, user.getGender());




            
            
            int rowsInserted = ps.executeUpdate();

            if (rowsInserted > 0) {
                // Retrieve the auto-generated key
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                        user.setId(generatedId);
                    }
                }
            }

            	
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
		 return generatedId;
	}
	
	// NEW METHOD - Login using Employee ID
	public User getUserById(int userId) {
		String sql = """
				SELECT 
					e.id, e.email, e.full_name, e.role, m.status
                FROM employees e
                LEFT JOIN 
					manage_employees m ON e.id = m.id
                WHERE 
					e.id = ?
				""";
		
		User user = null;
		
		try (Connection conn = DatabaseConnection.getInstance().getConnection();
	            PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            
            pstmt.setInt(1, userId);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
            	user = new User();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getString("status"));   
            }
            
            return user;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return user;
        }
		
	}

}

*/



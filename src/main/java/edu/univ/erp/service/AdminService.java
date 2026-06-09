
package edu.univ.erp.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import edu.univ.erp.data.Connectdb;

public class AdminService {

	private boolean ismaintainence = false;

	public boolean createUser(String username, String password, String role) {

		try (Connection connUni = Connectdb.getUniversitydbConnection()) {
			String checkSql = "SELECT COUNT(*) FROM users WHERE username = ?";
			try (PreparedStatement checkStmt = connUni.prepareStatement(checkSql)) {
				checkStmt.setString(1, username);
				ResultSet rs = checkStmt.executeQuery();
				if (rs.next() && rs.getInt(1) > 0) {
					System.err.println("Username already exists in universitydb.users!");
					return false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		try (Connection connAuth = Connectdb.getAuthenticationdbConnection()) {
			String checkSql = "SELECT COUNT(*) FROM users_auth WHERE username = ?";
			try (PreparedStatement checkStmt = connAuth.prepareStatement(checkSql)) {
				checkStmt.setString(1, username);
				ResultSet rs = checkStmt.executeQuery();
				if (rs.next() && rs.getInt(1) > 0) {
					System.err.println("Username already exists in authenticationdb.users_auth!");
					return false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		boolean s = false;

		String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
		try (Connection connUni = Connectdb.getUniversitydbConnection();
				PreparedStatement stmtUni = connUni.prepareStatement(sql)) {
			stmtUni.setString(1, username);
			stmtUni.setString(2, hashedPassword);
			stmtUni.setString(3, role);
			stmtUni.executeUpdate();
			s = true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		String insertSql = "INSERT INTO users_auth (username, role, password_hash) VALUES (?, ?, ?)";
		try (Connection authConn = Connectdb.getAuthenticationdbConnection();
				PreparedStatement pstmt = authConn.prepareStatement(insertSql)) {
			pstmt.setString(1, username);
			pstmt.setString(2, role);
			pstmt.setString(3, hashedPassword);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return s;
	}

	public boolean createCourse(String code, String name, int credits, int professorId) {

		try (Connection conn = Connectdb.getUniversitydbConnection()) {
			String checkSql = "SELECT COUNT(*) FROM courses WHERE code = ?";
			try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
				checkStmt.setString(1, code);
				ResultSet rs = checkStmt.executeQuery();
				if (rs.next() && rs.getInt(1) > 0) {
					System.err.println("Course code already exists!");
					return false;
				}
			}

			String sql = "INSERT INTO courses (code, name, credits, professor_id) VALUES (?, ?, ?, ?)";
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, code);
				stmt.setString(2, name);
				stmt.setInt(3, credits);
				stmt.setInt(4, professorId);
				stmt.executeUpdate();
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<String> getAllUsers() {
		List<String> users = new ArrayList<>();
		String sql = "SELECT id, username, role FROM users";
		try (Connection conn = Connectdb.getUniversitydbConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				users.add(rs.getInt("id") + ": " + rs.getString("username") + " (" + rs.getString("role") + ")");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}

	public List<String> getAllCourses() {
		List<String> courses = new ArrayList<>();
		String sql = "SELECT c.id, c.code, c.name, c.credits, u.username as professor FROM courses c LEFT JOIN users u ON c.professor_id = u.id";
		try (Connection conn = Connectdb.getUniversitydbConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				courses.add(rs.getInt("id") + ": " + rs.getString("code") + " - " + rs.getString("name") + " ("
						+ rs.getInt("credits") + " credits), Prof: " + rs.getString("professor"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return courses;
	}

	public void setMaintenanceMode(boolean mode) {
		this.ismaintainence = mode;
	}

	public boolean isMaintenanceMode() {
		return ismaintainence;
	}

	public int getUserIdByUsername(String username) {
		String sql = "SELECT id FROM users WHERE username = ?";
		try (Connection conn = Connectdb.getUniversitydbConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

}

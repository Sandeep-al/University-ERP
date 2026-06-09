package edu.univ.erp.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.univ.erp.data.Connectdb;

public class StudentService {

    public List<String> getRegisteredCourses(int studentId) {
        List<String> courses = new ArrayList<>();
        String sql = "SELECT c.id, c.code, c.name, c.credits FROM registrations r JOIN courses c ON r.course_id = c.id WHERE r.student_id = ?";
        try (Connection conn = Connectdb.getUniversitydbConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                courses.add(rs.getInt("id") + ": " + rs.getString("code") + " - " + rs.getString("name") + " ("
                        + rs.getInt("credits") + " credits)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    public List<String> getAvailableCourses(int studentId) {
        List<String> courses = new ArrayList<>();
        String sql = "SELECT id, code, name, credits FROM courses WHERE id NOT IN (SELECT course_id FROM registrations WHERE student_id = ?)";
        try (Connection conn = Connectdb.getUniversitydbConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                courses.add(rs.getInt("id") + ": " + rs.getString("code") + " - " + rs.getString("name") + " ("
                        + rs.getInt("credits") + " credits)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    public boolean registerCourse(String username, int courseId) {
        try (Connection conn = Connectdb.getUniversitydbConnection()) {

            int studentId = getStudentIdByUsername(conn, username);

            String creditSql = "SELECT SUM(c.credits) as total FROM registrations r JOIN courses c ON r.course_id = c.id WHERE r.student_id = ?";
            int totalCredits = 0;
            try (PreparedStatement stmt = conn.prepareStatement(creditSql)) {
                stmt.setInt(1, studentId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next())
                    totalCredits = rs.getInt("total");
            }

            String courseSql = "SELECT credits FROM courses WHERE id = ?";
            int courseCredits = 0;
            try (PreparedStatement stmt = conn.prepareStatement(courseSql)) {
                stmt.setInt(1, courseId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next())
                    courseCredits = rs.getInt("credits");
            }
            if (totalCredits + courseCredits > 20)
                return false;

            String regSql = "INSERT INTO registrations (student_id, course_id) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(regSql)) {
                stmt.setInt(1, studentId);
                stmt.setInt(2, courseId);
                stmt.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private int getStudentIdByUsername(Connection conn, String username) throws SQLException {
        String sql = "SELECT id FROM users WHERE username = ? AND role = 'student'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return -1;
    }

    public boolean dropCourse(int studentId, int courseId) {
        try (Connection conn = Connectdb.getUniversitydbConnection()) {

            String regIdSql = "SELECT id FROM registrations WHERE student_id = ? AND course_id = ?";
            int regId = -1;
            try (PreparedStatement regStmt = conn.prepareStatement(regIdSql)) {
                regStmt.setInt(1, studentId);
                regStmt.setInt(2, courseId);
                ResultSet rs = regStmt.executeQuery();
                if (rs.next()) {
                    regId = rs.getInt("id");
                }
            }
            if (regId != -1) {

                String delGradesSql = "DELETE FROM grades WHERE registration_id = ?";
                try (PreparedStatement delGradesStmt = conn.prepareStatement(delGradesSql)) {
                    delGradesStmt.setInt(1, regId);
                    delGradesStmt.executeUpdate();
                }
            }

            String delRegSql = "DELETE FROM registrations WHERE student_id = ? AND course_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(delRegSql)) {
                stmt.setInt(1, studentId);
                stmt.setInt(2, courseId);
                stmt.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getGrades(int studentId) {
        List<String> grades = new ArrayList<>();
        String sql = "SELECT c.code, c.name, g.marks, g.letter_grade FROM registrations r JOIN courses c ON r.course_id = c.id LEFT JOIN grades g ON r.id = g.registration_id WHERE r.student_id = ?";
        try (Connection conn = Connectdb.getUniversitydbConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                grades.add(rs.getString("code") + " - " + rs.getString("name") + ": Marks=" + rs.getString("marks")
                        + ", Grade=" + rs.getString("letter_grade"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return grades;
    }
}
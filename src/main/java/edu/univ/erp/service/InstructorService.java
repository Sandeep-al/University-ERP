package edu.univ.erp.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.univ.erp.data.Connectdb;

public class InstructorService {

    public List<String> getAssignedCourses(int professorId) {
        List<String> courses = new ArrayList<>();
        String sql = "SELECT id, code, name FROM courses WHERE professor_id = ?";
        try (Connection conn = Connectdb.getUniversitydbConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, professorId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                courses.add(rs.getInt("id") + ": " + rs.getString("code") + " - " + rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    public List<String> getStudentsInCourse(int courseId) {
        List<String> students = new ArrayList<>();
        String sql = "SELECT u.id, u.username FROM registrations r JOIN users u ON r.student_id = u.id WHERE r.course_id = ?";
        try (Connection conn = Connectdb.getUniversitydbConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, courseId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                students.add(rs.getInt("id") + ": " + rs.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public boolean assignMarks(int studentId, int courseId, int marks) {
        try (Connection conn = Connectdb.getUniversitydbConnection()) {

            String regSql = "SELECT id FROM registrations WHERE student_id = ? AND course_id = ?";
            int regId = -1;
            try (PreparedStatement stmt = conn.prepareStatement(regSql)) {
                stmt.setInt(1, studentId);
                stmt.setInt(2, courseId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next())
                    regId = rs.getInt("id");
            }
            if (regId == -1)
                return false;

            String sql = "INSERT INTO grades (registration_id, marks) VALUES (?, ?) " +
                    "ON DUPLICATE KEY UPDATE marks = VALUES(marks)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, regId);
                stmt.setInt(2, marks);
                stmt.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean assignLetterGrade(int studentId, int courseId, String letterGrade) {
        try (Connection conn = Connectdb.getUniversitydbConnection()) {

            String regSql = "SELECT id FROM registrations WHERE student_id = ? AND course_id = ?";
            int regId = -1;
            try (PreparedStatement stmt = conn.prepareStatement(regSql)) {
                stmt.setInt(1, studentId);
                stmt.setInt(2, courseId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next())
                    regId = rs.getInt("id");
            }
            if (regId == -1)
                return false;

            String sql = "INSERT INTO grades (registration_id, letter_grade) VALUES (?, ?) " +
                    "ON DUPLICATE KEY UPDATE letter_grade = VALUES(letter_grade)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, regId);
                stmt.setString(2, letterGrade);
                stmt.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
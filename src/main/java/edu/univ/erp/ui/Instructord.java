package edu.univ.erp.ui;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.util.*;

import edu.univ.erp.service.InstructorService;

public class Instructord extends JFrame {
	private final InstructorService instructorService;
	private final int professorId;

	public Instructord(int professorId, String username) {
		this.instructorService = new InstructorService();
		this.professorId = professorId;

		setTitle("Instructor Dashboard");
		setSize(400, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4, 1, 10, 10));

		JButton viewCoursesBtn = new JButton("View Assigned Courses");
		JButton gradeStudentsBtn = new JButton("Grade Students");
		JButton assignLetterBtn = new JButton("Assign Letter Grades");
		JButton logoutBtn = new JButton("Logout");

		panel.add(viewCoursesBtn);
		panel.add(gradeStudentsBtn);
		panel.add(assignLetterBtn);
		panel.add(logoutBtn);

		add(panel);

		viewCoursesBtn.addActionListener(e -> showCourses());
		gradeStudentsBtn.addActionListener(e -> openGradeStudentsWindow());
		assignLetterBtn.addActionListener(e -> openAssignLetterWindow());
		logoutBtn.addActionListener(e -> logout());
	}

	private void showCourses() {
		List<String> courses = instructorService.getAssignedCourses(professorId);
		JOptionPane.showMessageDialog(this, String.join("\n", courses), "Assigned Courses",
				JOptionPane.INFORMATION_MESSAGE);
	}

	private void openGradeStudentsWindow() {
		List<String> courses = instructorService.getAssignedCourses(professorId);
		String course = (String) JOptionPane.showInputDialog(this, "Select Course:", "Courses",
				JOptionPane.PLAIN_MESSAGE, null, courses.toArray(), null);
		if (course == null)
			return;
		int courseId = Integer.parseInt(course.split(":")[0].trim());
		List<String> students = instructorService.getStudentsInCourse(courseId);
		for (String student : students) {
			int studentId = Integer.parseInt(student.split(":")[0].trim());
			String marks = JOptionPane.showInputDialog(this, "Enter marks (0-100) for " + student + ":");
			if (marks == null)
				continue;
			try {
				int mark = Integer.parseInt(marks);
				boolean s = instructorService.assignMarks(studentId, courseId, mark);
				if (!s)
					JOptionPane.showMessageDialog(this, "Failed to assign marks for " + student);
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Invalid marks for " + student);
			}
		}
		JOptionPane.showMessageDialog(this, "Marks entry complete.");
	}

	private void openAssignLetterWindow() {
		List<String> courses = instructorService.getAssignedCourses(professorId);
		String course = (String) JOptionPane.showInputDialog(this, "Select Course:", "Courses",
				JOptionPane.PLAIN_MESSAGE, null, courses.toArray(), null);
		if (course == null)
			return;
		int courseId = Integer.parseInt(course.split(":")[0].trim());
		List<String> students = instructorService.getStudentsInCourse(courseId);
		String[] grades = { "A", "B", "C", "D", "F" };
		for (String student : students) {
			int studentId = Integer.parseInt(student.split(":")[0].trim());
			String grade = (String) JOptionPane.showInputDialog(this, "Assign letter grade for " + student + ":",
					"Letter Grade", JOptionPane.PLAIN_MESSAGE, null, grades, null);
			if (grade == null)
				continue;
			boolean s = instructorService.assignLetterGrade(studentId, courseId, grade);
			if (!s)
				JOptionPane.showMessageDialog(this, "Failed to assign letter grade for " + student);
		}
		JOptionPane.showMessageDialog(this, "Letter grade entry complete.");
	}

	private void logout() {
		dispose();

	}
}

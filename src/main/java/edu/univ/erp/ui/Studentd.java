package edu.univ.erp.ui;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.util.*;

import edu.univ.erp.service.StudentService;

public class Studentd extends JFrame {
	private final StudentService studentService;
	private final int studentId;
	private final String username;

	public Studentd(int studentId, String username) {
		this.studentService = new StudentService();
		this.studentId = studentId;
		this.username = username;
		setTitle("Student Dashboard");
		setSize(400, 300);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4, 1, 10, 10));

		JButton viewCoursesBtn = new JButton("View Registered Courses");
		JButton registerCourseBtn = new JButton("Register for Course");
		JButton dropCourseBtn = new JButton("Drop Course");
		JButton viewGradesBtn = new JButton("View Grades");

		panel.add(viewCoursesBtn);
		panel.add(registerCourseBtn);
		panel.add(dropCourseBtn);
		panel.add(viewGradesBtn);

		add(panel);

		viewCoursesBtn.addActionListener(e -> showRegisteredCourses());
		registerCourseBtn.addActionListener(e -> openRegisterCourseWindow());
		dropCourseBtn.addActionListener(e -> openDropCourseWindow());
		viewGradesBtn.addActionListener(e -> showGrades());
	}

	private void showRegisteredCourses() {
		List<String> courses = studentService.getRegisteredCourses(studentId);
		JOptionPane.showMessageDialog(this, String.join("\n", courses), "Registered Courses",
				JOptionPane.INFORMATION_MESSAGE);
	}

	private void openRegisterCourseWindow() {
		List<String> available = studentService.getAvailableCourses(studentId);
		if (available.isEmpty()) {
			JOptionPane.showMessageDialog(this, "No available courses.");
			return;
		}
		String course = (String) JOptionPane.showInputDialog(this, "Select Course:", "Available Courses",
				JOptionPane.PLAIN_MESSAGE, null, available.toArray(), null);
		if (course == null)
			return;
		int courseId = Integer.parseInt(course.split(":")[0].trim());
		boolean s = studentService.registerCourse(username, courseId);
		JOptionPane.showMessageDialog(this, s ? "Registered!" : "Failed ( credit limit or already registered)");
	}

	private void openDropCourseWindow() {
		List<String> registered = studentService.getRegisteredCourses(studentId);
		if (registered.isEmpty()) {
			JOptionPane.showMessageDialog(this, "No registered courses.");
			return;
		}
		String course = (String) JOptionPane.showInputDialog(this, "Select Course to Drop:", "Registered Courses",
				JOptionPane.PLAIN_MESSAGE, null, registered.toArray(), null);
		if (course == null)
			return;
		int courseId = Integer.parseInt(course.split(":")[0].trim());
		boolean s = studentService.dropCourse(studentId, courseId);
		JOptionPane.showMessageDialog(this, s ? "Dropped!" : "Failed to drop course.");
	}

	private void showGrades() {
		List<String> grades = studentService.getGrades(studentId);
		JOptionPane.showMessageDialog(this, String.join("\n", grades), "Grades", JOptionPane.INFORMATION_MESSAGE);
	}
}

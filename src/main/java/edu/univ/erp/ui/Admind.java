package edu.univ.erp.ui;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.util.*;

import edu.univ.erp.service.AdminService;

public class Admind extends JFrame {
	private AdminService adminService;

	public Admind() {
		this.adminService = new AdminService();
		setTitle("Admin Dashboard");
		setSize(400, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(6, 1, 10, 10));

		JButton createUserBtn = new JButton("Create User");
		JButton createCourseBtn = new JButton("Create Course");
		JButton viewUsersBtn = new JButton("View All Users");
		JButton viewCoursesBtn = new JButton("View All Courses");
		JButton maintenanceBtn = new JButton("Toggle Maintenance Mode");
		JButton logoutBtn = new JButton("Logout");

		panel.add(createUserBtn);
		panel.add(createCourseBtn);
		panel.add(viewUsersBtn);
		panel.add(viewCoursesBtn);
		panel.add(maintenanceBtn);
		panel.add(logoutBtn);

		add(panel);

		createUserBtn.addActionListener(e -> openCreateUserWindow());
		createCourseBtn.addActionListener(e -> openCreateCourseWindow());
		viewUsersBtn.addActionListener(e -> showUsers());
		viewCoursesBtn.addActionListener(e -> showCourses());
		maintenanceBtn.addActionListener(e -> toggleMaintenance());
		logoutBtn.addActionListener(e -> logout());
	}

	private void openCreateUserWindow() {
		JFrame frame = new JFrame("Create User");
		frame.setSize(300, 250);
		frame.setLocationRelativeTo(this);
		JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
		JTextField usernameField = new JTextField();
		JPasswordField passwordField = new JPasswordField();
		String[] roles = { "admin", "student", "professor" };
		JComboBox<String> roleBox = new JComboBox<>(roles);
		JButton submitBtn = new JButton("Create");
		panel.add(new JLabel("Username:"));
		panel.add(usernameField);
		panel.add(new JLabel("Password:"));
		panel.add(passwordField);
		panel.add(new JLabel("Role:"));
		panel.add(roleBox);
		panel.add(new JLabel());
		panel.add(submitBtn);
		frame.add(panel);
		submitBtn.addActionListener(e -> {
			String username = usernameField.getText();
			String password = new String(passwordField.getPassword());
			String role = (String) roleBox.getSelectedItem();
			boolean success = adminService.createUser(username, password, role);
			JOptionPane.showMessageDialog(frame, success ? "User created!" : "Failed to create user.");
			if (success)
				frame.dispose();
		});
		frame.setVisible(true);
	}

	private void openCreateCourseWindow() {
		JFrame frame = new JFrame("Create Course");
		frame.setSize(350, 300);
		frame.setLocationRelativeTo(this);
		JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
		JTextField codeField = new JTextField();
		JTextField nameField = new JTextField();
		JTextField creditsField = new JTextField();

		List<String> users = adminService.getAllUsers();
		List<String> profUsernames = new ArrayList<>();
		for (String user : users) {
			if (user.contains("professor")) {
				String[] parts = user.split(":");
				profUsernames.add(parts[1].trim());
			}
		}
		JComboBox<String> profBox = new JComboBox<>(profUsernames.toArray(new String[0]));
		JButton submitBtn = new JButton("Create");

		panel.add(new JLabel("Course Code:"));
		panel.add(codeField);
		panel.add(new JLabel("Course Name:"));
		panel.add(nameField);
		panel.add(new JLabel("Credits:"));
		panel.add(creditsField);
		panel.add(new JLabel("Professor:"));
		panel.add(profBox);
		panel.add(new JLabel());
		panel.add(submitBtn);
		frame.add(panel);

		submitBtn.addActionListener(e -> {
			String code = codeField.getText();
			String name = nameField.getText();
			int credits = Integer.parseInt(creditsField.getText());
			String selected = (String) profBox.getSelectedItem();
			String selectedUsername = selected.split(" ")[0];
			int profId = adminService.getUserIdByUsername(selectedUsername);
			boolean success = adminService.createCourse(code, name, credits, profId);
			JOptionPane.showMessageDialog(frame, success ? "Course created!" : "Failed to create course.");
			if (success)
				frame.dispose();
		});

		frame.setVisible(true);
	}

	private void showUsers() {
		java.util.List<String> users = adminService.getAllUsers();
		JOptionPane.showMessageDialog(this, String.join("\n", users), "All Users", JOptionPane.INFORMATION_MESSAGE);
	}

	private void showCourses() {
		java.util.List<String> courses = adminService.getAllCourses();
		JOptionPane.showMessageDialog(this, String.join("\n", courses), "All Courses", JOptionPane.INFORMATION_MESSAGE);
	}

	private void toggleMaintenance() {
		boolean current = adminService.isMaintenanceMode();
		adminService.setMaintenanceMode(!current);
		JOptionPane.showMessageDialog(this, "Maintenance mode is now " + (!current ? "ON" : "OFF"));
	}

	private void logout() {
		dispose();

	}
}

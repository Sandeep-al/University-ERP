package edu.univ.erp.ui;
import edu.univ.erp.auth.AuthService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class LoginWindow extends JFrame {

    private JTextField userField;
    private JPasswordField passField;
    private JButton loginButton;

    private AuthService authService;

    public LoginWindow() {
        this.authService = new AuthService();

        setTitle("IIITD ERP Login");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        userField = new JTextField();
        passField = new JPasswordField();
        loginButton = new JButton("Login");

        panel.add(new JLabel("Username:"));
        panel.add(userField);
        panel.add(new JLabel("Password:"));
        panel.add(passField);
        panel.add(new JLabel()); 
        panel.add(loginButton);

        add(panel);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
    }

    private void handleLogin() {
        String username = userField.getText();
        String password = new String(passField.getPassword());

        String[] loginResult = authService.checkUserLogin(username, password);

        if (loginResult == null) {
            JOptionPane.showMessageDialog(this, "User does not exist or password is incorrect.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Login Successful! Role: " + loginResult[0], "Success",
                JOptionPane.INFORMATION_MESSAGE);

        switch (loginResult[0].toLowerCase()) {
            case "admin":
                new Admind().setVisible(true);
                break;
            case "professor":
                new Instructord(Integer.parseInt(loginResult[1]), username).setVisible(true);
                break;
            case "student":
                new Studentd(Integer.parseInt(loginResult[1]), username).setVisible(true);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Unknown role: " + loginResult[1]);
        }
        userField.setText("");
        passField.setText("");
    }

}
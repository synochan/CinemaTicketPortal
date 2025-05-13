package view.customer;

import controller.UserController;
import view.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel for new user registration
 */
public class UserRegistrationPanel extends JPanel {
    private MainFrame mainFrame;
    private UserController userController;
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField emailField;
    private JTextField fullNameField;
    private JLabel statusLabel;

    public UserRegistrationPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.userController = mainFrame.getUserController();
        
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Register New Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JButton backButton = new JButton("← Back to Login");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showPanel("UserLoginPanel");
            }
        });
        headerPanel.add(backButton, BorderLayout.EAST);
        
        // Create main content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(30, 100, 30, 100));
        
        // Create registration form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Add logo panel
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel logoLabel = new JLabel("Join CineBook CDO");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(new Color(51, 51, 153));
        logoPanel.add(logoLabel);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(logoPanel, gbc);
        
        // Full Name Field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel fullNameLabel = new JLabel("Full Name:");
        formPanel.add(fullNameLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        fullNameField = new JTextField(20);
        formPanel.add(fullNameField, gbc);
        
        // Email Field
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel emailLabel = new JLabel("Email:");
        formPanel.add(emailLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        emailField = new JTextField(20);
        formPanel.add(emailField, gbc);
        
        // Username Field
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel usernameLabel = new JLabel("Username:");
        formPanel.add(usernameLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        usernameField = new JTextField(20);
        formPanel.add(usernameField, gbc);
        
        // Password Field
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel passwordLabel = new JLabel("Password:");
        formPanel.add(passwordLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        passwordField = new JPasswordField(20);
        formPanel.add(passwordField, gbc);
        
        // Confirm Password Field
        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        formPanel.add(confirmPasswordLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 5;
        confirmPasswordField = new JPasswordField(20);
        formPanel.add(confirmPasswordField, gbc);
        
        // Register Button
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        JButton registerButton = new JButton("Register");
        registerButton.setBackground(new Color(51, 153, 255));
        registerButton.setForeground(Color.WHITE);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performRegistration();
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(registerButton);
        formPanel.add(buttonPanel, gbc);
        
        // Status Label
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.RED);
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusPanel.add(statusLabel);
        formPanel.add(statusPanel, gbc);
        
        // Add form to content panel
        contentPanel.add(formPanel, BorderLayout.CENTER);
        
        // Add to main panel
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
    
    /**
     * Performs the registration operation
     */
    private void performRegistration() {
        String fullName = fullNameField.getText();
        String email = emailField.getText();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        // Validate input fields
        if (fullName.isEmpty() || email.isEmpty() || username.isEmpty() || 
            password.isEmpty() || confirmPassword.isEmpty()) {
            statusLabel.setText("Please fill in all fields");
            return;
        }
        
        // Check if username is available
        if (!userController.isUsernameAvailable(username)) {
            statusLabel.setText("Username already exists. Please choose another one.");
            return;
        }
        
        // Validate email format
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            statusLabel.setText("Please enter a valid email address");
            return;
        }
        
        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            statusLabel.setText("Passwords do not match");
            passwordField.setText("");
            confirmPasswordField.setText("");
            return;
        }
        
        // Register the user
        if (userController.registerUser(username, password, email, fullName) != null) {
            statusLabel.setText("");
            JOptionPane.showMessageDialog(this,
                "Registration successful! You can now login with your credentials.",
                "Registration Complete",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Clear the fields
            fullNameField.setText("");
            emailField.setText("");
            usernameField.setText("");
            passwordField.setText("");
            confirmPasswordField.setText("");
            
            // Go back to login panel
            mainFrame.showPanel("UserLoginPanel");
        } else {
            statusLabel.setText("Registration failed. Please try again.");
        }
    }
}
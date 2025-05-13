import controller.AdminController;
import controller.UserController;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Simple demonstration of the authentication system
 */
public class SimpleAuthDemo {
    private JFrame frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JTextField fullNameField;
    
    private AdminController adminController;
    private UserController userController;
    
    // Panel names
    private static final String LOGIN_PANEL = "login";
    private static final String REGISTER_PANEL = "register";
    private static final String WELCOME_PANEL = "welcome";
    
    public SimpleAuthDemo() {
        // Initialize controllers
        adminController = new AdminController();
        userController = new UserController(adminController);
        
        // Create UI
        createUI();
    }
    
    private void createUI() {
        // Create main frame
        frame = new JFrame("CineBook CDO - Authentication Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);
        
        // Create main panel with card layout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Create and add panels
        mainPanel.add(createLoginPanel(), LOGIN_PANEL);
        mainPanel.add(createRegisterPanel(), REGISTER_PANEL);
        mainPanel.add(createWelcomePanel(), WELCOME_PANEL);
        
        // Set initial panel
        cardLayout.show(mainPanel, LOGIN_PANEL);
        
        // Add to frame
        frame.getContentPane().add(mainPanel);
        frame.setVisible(true);
    }
    
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Login to CineBook CDO", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        
        // Username
        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField(20);
        formPanel.add(usernameField);
        
        // Password
        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField(20);
        formPanel.add(passwordField);
        
        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        
        // Register button
        JButton registerButton = new JButton("Register New Account");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, REGISTER_PANEL);
            }
        });
        
        // Add buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        
        // Add panels
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Register New Account", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        
        // Username
        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField(20);
        formPanel.add(usernameField);
        
        // Password
        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField(20);
        formPanel.add(passwordField);
        
        // Email
        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField(20);
        formPanel.add(emailField);
        
        // Full Name
        formPanel.add(new JLabel("Full Name:"));
        fullNameField = new JTextField(20);
        formPanel.add(fullNameField);
        
        // Register button
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register();
            }
        });
        
        // Back button
        JButton backButton = new JButton("Back to Login");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, LOGIN_PANEL);
            }
        });
        
        // Add buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);
        
        // Add panels
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Welcome to CineBook CDO", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Content
        JPanel contentPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        
        JLabel welcomeLabel = new JLabel("You have successfully logged in!");
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        contentPanel.add(welcomeLabel);
        
        JLabel userLabel = new JLabel("User: ");
        contentPanel.add(userLabel);
        
        JLabel emailLabel = new JLabel("Email: ");
        contentPanel.add(emailLabel);
        
        JLabel nameLabel = new JLabel("Name: ");
        contentPanel.add(nameLabel);
        
        // Logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        
        // Add components
        panel.add(contentPanel, BorderLayout.CENTER);
        panel.add(logoutButton, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void updateWelcomePanel() {
        User currentUser = userController.getCurrentUser();
        if (currentUser != null) {
            JPanel welcomePanel = (JPanel) mainPanel.getComponent(2);
            JPanel contentPanel = (JPanel) welcomePanel.getComponent(1);
            
            JLabel userLabel = (JLabel) contentPanel.getComponent(1);
            userLabel.setText("User: " + currentUser.getUsername());
            
            JLabel emailLabel = (JLabel) contentPanel.getComponent(2);
            emailLabel.setText("Email: " + (currentUser.getEmail() != null ? currentUser.getEmail() : "N/A"));
            
            JLabel nameLabel = (JLabel) contentPanel.getComponent(3);
            nameLabel.setText("Name: " + (currentUser.getFullName() != null ? currentUser.getFullName() : "N/A"));
        }
    }
    
    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter both username and password", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (userController.login(username, password)) {
            // Update welcome panel with user info
            updateWelcomePanel();
            
            // Show welcome panel
            cardLayout.show(mainPanel, WELCOME_PANEL);
            
            // Clear fields
            usernameField.setText("");
            passwordField.setText("");
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void register() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String email = emailField.getText();
        String fullName = fullNameField.getText();
        
        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || fullName.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill in all fields", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check if username is available
        if (!userController.isUsernameAvailable(username)) {
            JOptionPane.showMessageDialog(frame, "Username already exists. Please choose another.", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Register the user
        User newUser = userController.registerUser(username, password, email, fullName);
        
        if (newUser != null) {
            JOptionPane.showMessageDialog(frame, "Registration successful! You can now login.", "Registration Success", JOptionPane.INFORMATION_MESSAGE);
            
            // Clear fields
            usernameField.setText("");
            passwordField.setText("");
            emailField.setText("");
            fullNameField.setText("");
            
            // Go back to login panel
            cardLayout.show(mainPanel, LOGIN_PANEL);
        } else {
            JOptionPane.showMessageDialog(frame, "Registration failed. Please try again.", "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void logout() {
        userController.logout();
        cardLayout.show(mainPanel, LOGIN_PANEL);
    }
    
    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Start the application
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SimpleAuthDemo();
            }
        });
    }
}
package view.customer;

import controller.UserController;
import model.User;
import view.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel for user login
 */
public class UserLoginPanel extends JPanel {
    private MainFrame mainFrame;
    private UserController userController;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel statusLabel;

    public UserLoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.userController = mainFrame.getUserController();
        
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("User Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JButton backButton = new JButton("← Back to Home");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showPanel(MainFrame.MOVIE_LIST_PANEL);
            }
        });
        headerPanel.add(backButton, BorderLayout.EAST);
        
        // Create main content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(50, 100, 50, 100));
        
        // Create login form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Add logo panel
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel logoLabel = new JLabel("CineBook CDO");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(new Color(51, 51, 153));
        logoPanel.add(logoLabel);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(logoPanel, gbc);
        
        // Add username field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel usernameLabel = new JLabel("Username:");
        formPanel.add(usernameLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        usernameField = new JTextField(20);
        formPanel.add(usernameField, gbc);
        
        // Add password field
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel passwordLabel = new JLabel("Password:");
        formPanel.add(passwordLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        passwordField = new JPasswordField(20);
        formPanel.add(passwordField, gbc);
        
        // Add login button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(51, 153, 255));
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
        buttonPanel.add(loginButton);
        
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showPanel("UserRegistrationPanel");
            }
        });
        buttonPanel.add(registerButton);
        
        formPanel.add(buttonPanel, gbc);
        
        // Add status label
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.RED);
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusPanel.add(statusLabel);
        formPanel.add(statusPanel, gbc);
        
        // Add sample login note
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        JLabel noteLabel = new JLabel("Example users: john/pass123, maria/pass123, alex/pass123");
        noteLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        noteLabel.setForeground(Color.GRAY);
        JPanel notePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        notePanel.add(noteLabel);
        formPanel.add(notePanel, gbc);
        
        // Add form to content panel
        contentPanel.add(formPanel, BorderLayout.CENTER);
        
        // Add to main panel
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
    
    /**
     * Performs the login operation
     */
    private void performLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter both username and password");
            return;
        }
        
        if (userController.login(username, password)) {
            statusLabel.setText(" "); // Clear error
            
            // Go back to movie list panel with user logged in
            mainFrame.showPanel(MainFrame.MOVIE_LIST_PANEL);
            
            // Clear the fields
            usernameField.setText("");
            passwordField.setText("");
            
            JOptionPane.showMessageDialog(this, 
                "Welcome back, " + userController.getCurrentUser().getFullName() + "!",
                "Login Successful", 
                JOptionPane.INFORMATION_MESSAGE);
            
        } else {
            statusLabel.setText("Invalid username or password");
            passwordField.setText("");
        }
    }
}
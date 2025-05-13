package view;

import controller.AdminController;
import controller.BookingController;
import controller.MovieController;
import controller.ReportController;
import controller.UserController;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Main frame of the application
 */
public class MainFrame extends JFrame {
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private MovieController movieController;
    private BookingController bookingController;
    private AdminController adminController;
    private UserController userController;
    private ReportController reportController;

    // User interface elements
    private JButton userButton;
    private JPopupMenu userMenu;

    // Panel names for card layout
    public static final String MOVIE_LIST_PANEL = "MovieListPanel";
    public static final String ADMIN_LOGIN_PANEL = "AdminLoginPanel";
    public static final String USER_LOGIN_PANEL = "UserLoginPanel";
    public static final String USER_REGISTRATION_PANEL = "UserRegistrationPanel";

    public MainFrame() {
        setTitle("CineBook CDO - Cinema Booking System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setMinimumSize(new Dimension(800, 500));
        setLocationRelativeTo(null);
        
        // Initialize controllers
        movieController = new MovieController();
        bookingController = new BookingController(movieController);
        adminController = new AdminController();
        userController = new UserController(adminController);
        reportController = new ReportController(bookingController, movieController);
        
        // Initialize main panel with card layout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Create toolbar
        JToolBar toolbar = createToolbar();
        
        // Add components to frame
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(toolbar, BorderLayout.NORTH);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        
        // Create initial panels - we'll initialize these in a separate method
        initializePanels();
        
        // Set initial panel
        cardLayout.show(mainPanel, MOVIE_LIST_PANEL);
        
        setVisible(true);
    }
    
    /**
     * Initializes all the panels used in the application
     */
    private void initializePanels() {
        // Create main panels (will be implemented later)
        JPanel movieListPanel = new JPanel();
        movieListPanel.add(new JLabel("Movie List Panel - To be implemented"));
        
        JPanel adminLoginPanel = new JPanel();
        adminLoginPanel.add(new JLabel("Admin Login Panel - To be implemented"));
        
        JPanel userLoginPanel = new JPanel();
        userLoginPanel.add(new JLabel("User Login Panel - To be implemented"));
        
        JPanel userRegistrationPanel = new JPanel();
        userRegistrationPanel.add(new JLabel("User Registration Panel - To be implemented"));
        
        // Add panels to card layout
        mainPanel.add(movieListPanel, MOVIE_LIST_PANEL);
        mainPanel.add(adminLoginPanel, ADMIN_LOGIN_PANEL);
        mainPanel.add(userLoginPanel, USER_LOGIN_PANEL);
        mainPanel.add(userRegistrationPanel, USER_REGISTRATION_PANEL);
    }

    /**
     * Creates the toolbar
     */
    private JToolBar createToolbar() {
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        
        // Create logo label
        JLabel logoLabel = new JLabel("CineBook CDO");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 20));
        
        // Create home button
        JButton homeButton = new JButton("Home");
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPanel(MOVIE_LIST_PANEL);
            }
        });
        
        // Create user button with dropdown menu
        userButton = new JButton("👤 Guest");
        userMenu = new JPopupMenu();
        
        JMenuItem loginItem = new JMenuItem("Login");
        loginItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPanel(USER_LOGIN_PANEL);
            }
        });
        userMenu.add(loginItem);
        
        JMenuItem registerItem = new JMenuItem("Register");
        registerItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPanel(USER_REGISTRATION_PANEL);
            }
        });
        userMenu.add(registerItem);
        
        // Add logout option (initially disabled)
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.setEnabled(false);
        logoutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userController.logout();
                updateUserInterface();
                JOptionPane.showMessageDialog(MainFrame.this, 
                        "You have been logged out.", 
                        "Logout Successful", 
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        userMenu.add(logoutItem);
        
        userButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userMenu.show(userButton, 0, userButton.getHeight());
                
                // Update the menu based on login status
                loginItem.setEnabled(!userController.isUserLoggedIn());
                registerItem.setEnabled(!userController.isUserLoggedIn());
                logoutItem.setEnabled(userController.isUserLoggedIn());
            }
        });
        
        // Create admin button
        JButton adminButton = new JButton("Admin");
        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPanel(ADMIN_LOGIN_PANEL);
            }
        });
        
        // Add components to toolbar
        toolbar.add(logoLabel);
        toolbar.add(Box.createHorizontalStrut(10));
        toolbar.add(homeButton);
        toolbar.add(Box.createHorizontalGlue()); // Push remaining components to the right
        toolbar.add(userButton);
        toolbar.add(Box.createHorizontalStrut(10));
        toolbar.add(adminButton);
        toolbar.add(Box.createHorizontalStrut(10));
        
        return toolbar;
    }
    
    /**
     * Updates the user interface based on logged in status
     */
    public void updateUserInterface() {
        if (userController.isUserLoggedIn()) {
            User currentUser = userController.getCurrentUser();
            userButton.setText("👤 " + currentUser.getUsername());
        } else {
            userButton.setText("👤 Guest");
        }
    }

    /**
     * Shows a specific panel
     */
    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

    /**
     * Adds a panel to the card layout
     */
    public void addPanel(JPanel panel, String panelName) {
        mainPanel.add(panel, panelName);
    }

    /**
     * Shows a panel and removes a specified previous panel
     */
    public void showPanelAndRemove(String panelNameToShow, String panelNameToRemove) {
        cardLayout.show(mainPanel, panelNameToShow);
        
        // In this simplified implementation, we don't actually remove the panel
        // as it causes complications with CardLayout. Instead, we just switch
        // panels and let the layout handle it.
        // For a production app, we'd implement a more robust panel management system.
    }

    // Getters for controllers
    public MovieController getMovieController() {
        return movieController;
    }

    public BookingController getBookingController() {
        return bookingController;
    }

    public AdminController getAdminController() {
        return adminController;
    }
    
    public UserController getUserController() {
        return userController;
    }

    public ReportController getReportController() {
        return reportController;
    }
}

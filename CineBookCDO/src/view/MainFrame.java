package view;

import controller.AdminController;
import controller.BookingController;
import controller.MovieController;
import controller.ReportController;
import view.admin.AdminLoginPanel;
import view.customer.MovieListPanel;

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
    private ReportController reportController;
    private String currentUsername;

    // Panel names for card layout
    public static final String MOVIE_LIST_PANEL = "MovieListPanel";
    public static final String ADMIN_LOGIN_PANEL = "AdminLoginPanel";

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
        reportController = new ReportController(bookingController, movieController);
        
        // Set default username
        currentUsername = "Guest";
        
        // Initialize main panel with card layout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Create toolbar
        JToolBar toolbar = createToolbar();
        
        // Create and add panels to card layout
        mainPanel.add(new MovieListPanel(this), MOVIE_LIST_PANEL);
        mainPanel.add(new AdminLoginPanel(this), ADMIN_LOGIN_PANEL);
        
        // Set initial panel
        cardLayout.show(mainPanel, MOVIE_LIST_PANEL);
        
        // Add components to frame
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(toolbar, BorderLayout.NORTH);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        
        setVisible(true);
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
        
        // Create user dropdown
        JButton userButton = new JButton("👤 " + currentUsername);
        
        // Create admin button
        JButton adminButton = new JButton("Admin");
        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPanel(ADMIN_LOGIN_PANEL);
            }
        });
        
        // Create username input dialog
        JButton setUsernameButton = new JButton("Set Username");
        setUsernameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = JOptionPane.showInputDialog(MainFrame.this, 
                        "Enter your username:", 
                        "Set Username", 
                        JOptionPane.PLAIN_MESSAGE);
                
                if (username != null && !username.trim().isEmpty()) {
                    currentUsername = username.trim();
                    userButton.setText("👤 " + currentUsername);
                    JOptionPane.showMessageDialog(MainFrame.this, 
                            "Username set to: " + currentUsername, 
                            "Username Updated", 
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        
        // Add components to toolbar
        toolbar.add(logoLabel);
        toolbar.add(Box.createHorizontalStrut(10));
        toolbar.add(homeButton);
        toolbar.add(Box.createHorizontalStrut(10));
        toolbar.add(setUsernameButton);
        toolbar.add(Box.createHorizontalGlue()); // Push remaining components to the right
        toolbar.add(userButton);
        toolbar.add(Box.createHorizontalStrut(10));
        toolbar.add(adminButton);
        toolbar.add(Box.createHorizontalStrut(10));
        
        return toolbar;
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
        
        // Remove the panel from card layout
        if (panelNameToRemove != null) {
            Component component = null;
            for (Component comp : mainPanel.getComponents()) {
                if (mainPanel.getComponentZOrder(comp) == mainPanel.getComponentZOrder(panelNameToRemove)) {
                    component = comp;
                    break;
                }
            }
            
            if (component != null) {
                mainPanel.remove(component);
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        }
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

    public ReportController getReportController() {
        return reportController;
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }
}

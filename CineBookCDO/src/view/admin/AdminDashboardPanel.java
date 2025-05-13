package view.admin;

import controller.AdminController;
import controller.BookingController;
import controller.MovieController;
import controller.ReportController;
import model.Booking;
import view.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Main dashboard panel for admin functions
 */
public class AdminDashboardPanel extends JPanel {
    private MainFrame mainFrame;
    private AdminController adminController;
    private BookingController bookingController;
    private MovieController movieController;
    private ReportController reportController;
    private JPanel mainContentPanel;
    private CardLayout cardLayout;

    // Panel identifiers
    private static final String DASHBOARD_HOME = "DashboardHome";
    private static final String MOVIE_MANAGEMENT = "MovieManagement";
    private static final String SEAT_MANAGEMENT = "SeatManagement";
    private static final String REPORTS = "Reports";

    public AdminDashboardPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.adminController = mainFrame.getAdminController();
        this.bookingController = mainFrame.getBookingController();
        this.movieController = mainFrame.getMovieController();
        this.reportController = mainFrame.getReportController();
        
        setLayout(new BorderLayout());
        
        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(51, 51, 102));
        headerPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        JLabel titleLabel = new JLabel("CineBook CDO Admin Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(mainFrame,
                        "Are you sure you want to logout?",
                        "Confirm Logout",
                        JOptionPane.YES_NO_OPTION);
                
                if (choice == JOptionPane.YES_OPTION) {
                    mainFrame.showPanel(MainFrame.ADMIN_LOGIN_PANEL);
                }
            }
        });
        headerPanel.add(logoutButton, BorderLayout.EAST);
        
        // Create sidebar navigation panel
        JPanel sidebarPanel = createSidebarPanel();
        
        // Create main content panel with card layout
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        
        // Add dashboard home panel
        mainContentPanel.add(createDashboardHomePanel(), DASHBOARD_HOME);
        
        // Add movie management panel
        MovieManagementPanel movieManagementPanel = new MovieManagementPanel(mainFrame);
        mainContentPanel.add(movieManagementPanel, MOVIE_MANAGEMENT);
        
        // Add seat management panel
        SeatManagementPanel seatManagementPanel = new SeatManagementPanel(mainFrame);
        mainContentPanel.add(seatManagementPanel, SEAT_MANAGEMENT);
        
        // Add reports panel
        ReportPanel reportPanel = new ReportPanel(mainFrame);
        mainContentPanel.add(reportPanel, REPORTS);
        
        // Show dashboard home initially
        cardLayout.show(mainContentPanel, DASHBOARD_HOME);
        
        // Create split pane for sidebar and content
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
                sidebarPanel, mainContentPanel);
        splitPane.setDividerLocation(200);
        splitPane.setDividerSize(1);
        splitPane.setContinuousLayout(true);
        splitPane.setEnabled(false); // Prevent user from adjusting divider
        
        // Add components to main panel
        add(headerPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    }
    
    /**
     * Creates the sidebar navigation panel
     */
    private JPanel createSidebarPanel() {
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(240, 240, 245));
        sidebarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));
        
        // Dashboard button
        JButton dashboardButton = createSidebarButton("Dashboard", DASHBOARD_HOME);
        
        // Movie Management button
        JButton movieButton = createSidebarButton("Movie Management", MOVIE_MANAGEMENT);
        
        // Seat Management button
        JButton seatButton = createSidebarButton("Seat Management", SEAT_MANAGEMENT);
        
        // Reports button
        JButton reportButton = createSidebarButton("Reports", REPORTS);
        
        // Return to Main System button
        JButton returnButton = new JButton("Return to Main System");
        returnButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        returnButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, returnButton.getPreferredSize().height));
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showPanel(MainFrame.MOVIE_LIST_PANEL);
            }
        });
        
        // Add buttons to sidebar
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        sidebarPanel.add(dashboardButton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(movieButton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(seatButton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(reportButton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        sidebarPanel.add(new JSeparator());
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(returnButton);
        sidebarPanel.add(Box.createVerticalGlue());
        
        return sidebarPanel;
    }
    
    /**
     * Creates a sidebar navigation button
     */
    private JButton createSidebarButton(String text, final String targetPanel) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainContentPanel, targetPanel);
            }
        });
        
        return button;
    }
    
    /**
     * Creates the dashboard home panel with summary information
     */
    private JPanel createDashboardHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Create welcome panel
        JPanel welcomePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel welcomeLabel = new JLabel("Welcome to Admin Dashboard");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomePanel.add(welcomeLabel);
        
        // Create statistics panel
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        statsPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        // Recent bookings stat
        JPanel bookingsPanel = createStatPanel("Recent Bookings", getRecentBookingsCount());
        
        // Total movies stat
        JPanel moviesPanel = createStatPanel("Total Movies", movieController.getAllMovies().size());
        
        // Total revenue stat
        JPanel revenuePanel = createStatPanel("Total Revenue", 
                "₱" + String.format("%.2f", calculateTotalRevenue()));
        
        // Seat occupancy stat
        JPanel occupancyPanel = createStatPanel("Average Occupancy", 
                String.format("%.1f", calculateAverageOccupancy()) + "%");
        
        statsPanel.add(bookingsPanel);
        statsPanel.add(moviesPanel);
        statsPanel.add(revenuePanel);
        statsPanel.add(occupancyPanel);
        
        // Create recent activity panel
        JPanel activityPanel = new JPanel(new BorderLayout());
        activityPanel.setBorder(BorderFactory.createTitledBorder("Recent Bookings"));
        
        // Table for recent bookings
        String[] columnNames = {"Booking Code", "Movie", "Customer", "Seats", "Amount"};
        Object[][] data = getRecentBookingsData();
        
        JTable bookingsTable = new JTable(data, columnNames);
        bookingsTable.setFillsViewportHeight(true);
        
        activityPanel.add(new JScrollPane(bookingsTable), BorderLayout.CENTER);
        
        // Add all panels
        panel.add(welcomePanel, BorderLayout.NORTH);
        panel.add(statsPanel, BorderLayout.CENTER);
        panel.add(activityPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Creates a panel for displaying a statistic
     */
    private JPanel createStatPanel(String title, Object value) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        panel.setBackground(new Color(250, 250, 255));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel valueLabel = new JLabel(value.toString());
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(valueLabel);
        
        return panel;
    }
    
    /**
     * Gets the count of recent bookings
     */
    private int getRecentBookingsCount() {
        return bookingController.getAllBookings().size();
    }
    
    /**
     * Calculates the total revenue from all bookings
     */
    private double calculateTotalRevenue() {
        double total = 0.0;
        List<Booking> allBookings = bookingController.getAllBookings();
        
        for (Booking booking : allBookings) {
            total += booking.getTotalAmount();
        }
        
        return total;
    }
    
    /**
     * Calculates the average seat occupancy across all movies
     */
    private double calculateAverageOccupancy() {
        return reportController.generateSeatOccupancyReport().values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }
    
    /**
     * Gets data for the recent bookings table
     */
    private Object[][] getRecentBookingsData() {
        List<Booking> bookings = bookingController.getAllBookings();
        int count = Math.min(bookings.size(), 10); // Show at most 10 recent bookings
        
        Object[][] data = new Object[count][5];
        
        for (int i = 0; i < count; i++) {
            Booking booking = bookings.get(bookings.size() - 1 - i); // Show most recent first
            
            // Format seats as a comma-separated list
            StringBuilder seats = new StringBuilder();
            for (int j = 0; j < booking.getBookedSeats().size(); j++) {
                seats.append(booking.getBookedSeats().get(j).getSeatId());
                if (j < booking.getBookedSeats().size() - 1) {
                    seats.append(", ");
                }
            }
            
            data[i][0] = booking.getBookingCode();
            data[i][1] = booking.getMovie().getTitle();
            data[i][2] = booking.getUsername();
            data[i][3] = seats.toString();
            data[i][4] = "₱" + String.format("%.2f", booking.getTotalAmount());
        }
        
        return data;
    }
}

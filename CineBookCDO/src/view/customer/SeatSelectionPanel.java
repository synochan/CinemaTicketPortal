package view.customer;

import controller.BookingController;
import controller.MovieController;
import model.Booking;
import model.Cinema;
import model.Movie;
import model.Seat;
import view.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel for seat selection
 */
public class SeatSelectionPanel extends JPanel {
    private MainFrame mainFrame;
    private MovieController movieController;
    private BookingController bookingController;
    private Movie movie;
    private String cinema;
    private String showtime;
    private List<Seat> selectedSeats;
    private JPanel seatsPanel;
    private JButton[][] seatButtons;
    private JLabel totalLabel;
    private double totalAmount;

    public SeatSelectionPanel(MainFrame mainFrame, Movie movie, String cinema, String showtime) {
        this.mainFrame = mainFrame;
        this.movieController = mainFrame.getMovieController();
        this.bookingController = mainFrame.getBookingController();
        this.movie = movie;
        this.cinema = cinema;
        this.showtime = showtime;
        this.selectedSeats = new ArrayList<>();
        this.totalAmount = 0.0;
        
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Select Your Seats");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JButton backButton = new JButton("← Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Go back to movie detail panel
                mainFrame.showPanel("MovieDetailPanel");
            }
        });
        headerPanel.add(backButton, BorderLayout.EAST);
        
        // Create content panel
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        
        // Create info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Booking Information"));
        
        JLabel movieLabel = new JLabel("Movie: " + movie.getTitle());
        JLabel cinemaLabel = new JLabel("Cinema: " + cinema);
        JLabel showtimeLabel = new JLabel("Showtime: " + showtime);
        
        totalLabel = new JLabel("Total: ₱0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        infoPanel.add(movieLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(cinemaLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(showtimeLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        infoPanel.add(totalLabel);
        
        // Create legend panel
        JPanel legendPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        legendPanel.setBorder(BorderFactory.createTitledBorder("Legend"));
        
        JPanel availablePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel selectedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel standardPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel deluxePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton availableButton = new JButton();
        availableButton.setEnabled(false);
        availableButton.setPreferredSize(new Dimension(20, 20));
        availableButton.setBackground(Color.WHITE);
        
        JButton selectedButton = new JButton();
        selectedButton.setEnabled(false);
        selectedButton.setPreferredSize(new Dimension(20, 20));
        selectedButton.setBackground(Color.GREEN);
        
        JButton standardButton = new JButton();
        standardButton.setEnabled(false);
        standardButton.setPreferredSize(new Dimension(20, 20));
        standardButton.setBackground(Color.LIGHT_GRAY);
        
        JButton deluxeButton = new JButton();
        deluxeButton.setEnabled(false);
        deluxeButton.setPreferredSize(new Dimension(20, 20));
        deluxeButton.setBackground(new Color(255, 200, 200)); // Light red
        
        availablePanel.add(availableButton);
        availablePanel.add(new JLabel("Available"));
        
        selectedPanel.add(selectedButton);
        selectedPanel.add(new JLabel("Selected"));
        
        standardPanel.add(standardButton);
        standardPanel.add(new JLabel("Standard (₱180)"));
        
        deluxePanel.add(deluxeButton);
        deluxePanel.add(new JLabel("Deluxe (₱250)"));
        
        legendPanel.add(availablePanel);
        legendPanel.add(selectedPanel);
        legendPanel.add(standardPanel);
        legendPanel.add(deluxePanel);
        
        // Create seating chart panel
        JPanel seatingChartPanel = new JPanel(new BorderLayout());
        seatingChartPanel.setBorder(BorderFactory.createTitledBorder("Seating Chart"));
        
        // Create screen panel (at the top of seating chart)
        JPanel screenPanel = new JPanel();
        screenPanel.setBackground(Color.DARK_GRAY);
        screenPanel.setPreferredSize(new Dimension(100, 30));
        JLabel screenLabel = new JLabel("SCREEN");
        screenLabel.setForeground(Color.WHITE);
        screenPanel.add(screenLabel);
        
        // Get cinema information
        Cinema cinemaObj = movieController.getCinemaByName(cinema);
        if (cinemaObj != null) {
            // Create seats panel
            seatsPanel = new JPanel();
            int rows = cinemaObj.getRows();
            int cols = cinemaObj.getColumns();
            seatsPanel.setLayout(new GridLayout(rows, cols, 5, 5));
            
            // Create seat buttons
            seatButtons = new JButton[rows][cols];
            
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    final int r = row;
                    final int c = col;
                    
                    // Get seat information
                    String seatId = (char) ('A' + row) + String.valueOf(col + 1);
                    Seat seat = cinemaObj.getSeatById(seatId);
                    
                    // Create seat button
                    JButton seatButton = new JButton(seatId);
                    seatButton.setMargin(new Insets(2, 2, 2, 2));
                    seatButton.setPreferredSize(new Dimension(50, 40));
                    
                    // Set button color based on seat type
                    if (seat != null && seat.getType().equals("Deluxe")) {
                        seatButton.setBackground(new Color(255, 200, 200)); // Light red for deluxe
                    } else {
                        seatButton.setBackground(Color.LIGHT_GRAY); // Gray for standard
                    }
                    
                    // Check if seat is available
                    boolean isAvailable = movieController.isSeatAvailable(cinema, movie.getTitle(), showtime, seatId);
                    seatButton.setEnabled(isAvailable);
                    
                    if (!isAvailable) {
                        seatButton.setBackground(Color.RED);
                    }
                    
                    // Add action listener
                    seatButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            toggleSeatSelection(r, c, seatId);
                        }
                    });
                    
                    // Add to array and panel
                    seatButtons[row][col] = seatButton;
                    seatsPanel.add(seatButton);
                }
            }
        }
        
        seatingChartPanel.add(screenPanel, BorderLayout.NORTH);
        seatingChartPanel.add(seatsPanel, BorderLayout.CENTER);
        
        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton clearButton = new JButton("Clear Selection");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearSelectedSeats();
            }
        });
        
        JButton continueButton = new JButton("Continue to Snacks");
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedSeats.isEmpty()) {
                    JOptionPane.showMessageDialog(mainFrame, 
                            "Please select at least one seat.", 
                            "No Seats Selected", 
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Create booking object
                Booking booking = bookingController.createBooking(
                        mainFrame.getCurrentUsername(), movie, cinema, showtime);
                
                // Add selected seats to booking
                bookingController.addSeatsToBooking(booking, selectedSeats);
                
                // Go to snack selection
                SnackSelectionPanel snackPanel = new SnackSelectionPanel(mainFrame, booking);
                mainFrame.addPanel(snackPanel, "SnackSelectionPanel");
                mainFrame.showPanel("SnackSelectionPanel");
            }
        });
        
        buttonsPanel.add(clearButton);
        buttonsPanel.add(continueButton);
        
        // Arrange panels
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(infoPanel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(legendPanel);
        
        contentPanel.add(leftPanel, BorderLayout.WEST);
        contentPanel.add(seatingChartPanel, BorderLayout.CENTER);
        
        // Add to main panel
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);
    }

    /**
     * Toggles seat selection on/off
     */
    private void toggleSeatSelection(int row, int col, String seatId) {
        Cinema cinemaObj = movieController.getCinemaByName(cinema);
        if (cinemaObj == null) return;
        
        Seat seat = cinemaObj.getSeatById(seatId);
        if (seat == null) return;
        
        JButton seatButton = seatButtons[row][col];
        
        // Check if seat is already selected
        boolean isSelected = false;
        for (Seat selectedSeat : selectedSeats) {
            if (selectedSeat.getSeatId().equals(seatId)) {
                isSelected = true;
                break;
            }
        }
        
        if (isSelected) {
            // Deselect seat
            selectedSeats.removeIf(s -> s.getSeatId().equals(seatId));
            totalAmount -= seat.getPrice();
            
            // Reset button color based on seat type
            if (seat.getType().equals("Deluxe")) {
                seatButton.setBackground(new Color(255, 200, 200)); // Light red for deluxe
            } else {
                seatButton.setBackground(Color.LIGHT_GRAY); // Gray for standard
            }
        } else {
            // Select seat
            selectedSeats.add(seat);
            totalAmount += seat.getPrice();
            seatButton.setBackground(Color.GREEN);
        }
        
        // Update total label
        totalLabel.setText("Total: ₱" + String.format("%.2f", totalAmount));
    }

    /**
     * Clears all selected seats
     */
    private void clearSelectedSeats() {
        Cinema cinemaObj = movieController.getCinemaByName(cinema);
        if (cinemaObj == null) return;
        
        for (Seat seat : selectedSeats) {
            // Find and reset button
            for (int row = 0; row < seatButtons.length; row++) {
                for (int col = 0; col < seatButtons[row].length; col++) {
                    if (seatButtons[row][col].getText().equals(seat.getSeatId())) {
                        // Reset button color based on seat type
                        if (seat.getType().equals("Deluxe")) {
                            seatButtons[row][col].setBackground(new Color(255, 200, 200)); // Light red for deluxe
                        } else {
                            seatButtons[row][col].setBackground(Color.LIGHT_GRAY); // Gray for standard
                        }
                    }
                }
            }
        }
        
        // Clear selection and reset total
        selectedSeats.clear();
        totalAmount = 0.0;
        totalLabel.setText("Total: ₱0.00");
    }
}

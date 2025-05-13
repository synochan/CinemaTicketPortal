package view.admin;

import controller.MovieController;
import model.Cinema;
import model.Movie;
import model.Seat;
import view.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Panel for managing cinema seats
 */
public class SeatManagementPanel extends JPanel {
    private MainFrame mainFrame;
    private MovieController movieController;
    private JComboBox<Movie> movieComboBox;
    private JComboBox<String> cinemaComboBox;
    private JComboBox<String> showtimeComboBox;
    private JPanel seatingChartPanel;
    private JButton[][] seatButtons;
    private JLabel statusLabel;

    public SeatManagementPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.movieController = mainFrame.getMovieController();
        
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Create panel title
        JLabel titleLabel = new JLabel("Seat Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        // Create controls panel
        JPanel controlsPanel = createControlsPanel();
        
        // Create seating chart panel (initially empty)
        seatingChartPanel = new JPanel(new BorderLayout());
        seatingChartPanel.setBorder(BorderFactory.createTitledBorder("Seating Chart"));
        
        // Create screen panel (at the top of seating chart)
        JPanel screenPanel = new JPanel();
        screenPanel.setBackground(Color.DARK_GRAY);
        screenPanel.setPreferredSize(new Dimension(100, 30));
        JLabel screenLabel = new JLabel("SCREEN");
        screenLabel.setForeground(Color.WHITE);
        screenPanel.add(screenLabel);
        
        seatingChartPanel.add(screenPanel, BorderLayout.NORTH);
        
        // Empty message initially
        JLabel emptyLabel = new JLabel("Select a movie, cinema, and showtime to manage seats", JLabel.CENTER);
        seatingChartPanel.add(emptyLabel, BorderLayout.CENTER);
        
        // Create status panel
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusLabel = new JLabel(" ");
        statusPanel.add(statusLabel, BorderLayout.WEST);
        
        // Create legend panel
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        legendPanel.add(createLegendItem("Available", Color.WHITE));
        legendPanel.add(createLegendItem("Booked", Color.RED));
        legendPanel.add(createLegendItem("Standard", Color.LIGHT_GRAY));
        legendPanel.add(createLegendItem("Deluxe", new Color(255, 200, 200)));
        
        statusPanel.add(legendPanel, BorderLayout.EAST);
        
        // Create actions panel
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton resetAllButton = new JButton("Reset All Seats");
        resetAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetAllSeats();
            }
        });
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSeatingChart();
            }
        });
        
        actionsPanel.add(resetAllButton);
        actionsPanel.add(refreshButton);
        
        // Add components to main panel
        add(titleLabel, BorderLayout.NORTH);
        add(controlsPanel, BorderLayout.WEST);
        add(seatingChartPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);
        
        // Load initial data
        loadMovies();
    }
    
    /**
     * Creates the panel containing the controls
     */
    private JPanel createControlsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(0, 0, 0, 20));
        panel.setPreferredSize(new Dimension(250, 0));
        
        // Create selection panel
        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.Y_AXIS));
        selectionPanel.setBorder(BorderFactory.createTitledBorder("Select Show"));
        selectionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Movie selection
        JLabel movieLabel = new JLabel("Movie:");
        movieLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        movieComboBox = new JComboBox<>();
        movieComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        movieComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, movieComboBox.getPreferredSize().height));
        movieComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCinemas();
            }
        });
        
        // Cinema selection
        JLabel cinemaLabel = new JLabel("Cinema:");
        cinemaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        cinemaComboBox = new JComboBox<>();
        cinemaComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        cinemaComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, cinemaComboBox.getPreferredSize().height));
        cinemaComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateShowtimes();
            }
        });
        
        // Showtime selection
        JLabel showtimeLabel = new JLabel("Showtime:");
        showtimeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        showtimeComboBox = new JComboBox<>();
        showtimeComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        showtimeComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, showtimeComboBox.getPreferredSize().height));
        showtimeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSeatingChart();
            }
        });
        
        // Add components to selection panel
        selectionPanel.add(movieLabel);
        selectionPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        selectionPanel.add(movieComboBox);
        selectionPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        selectionPanel.add(cinemaLabel);
        selectionPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        selectionPanel.add(cinemaComboBox);
        selectionPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        selectionPanel.add(showtimeLabel);
        selectionPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        selectionPanel.add(showtimeComboBox);
        
        // Create instructions panel
        JPanel instructionsPanel = new JPanel(new BorderLayout());
        instructionsPanel.setBorder(BorderFactory.createTitledBorder("Instructions"));
        instructionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea instructionsText = new JTextArea(
                "• Select a movie, cinema, and showtime\n" +
                "• Click on seats to toggle availability\n" +
                "• Red seats are currently booked\n" +
                "• 'Reset All Seats' will make all seats available\n" +
                "• Click 'Refresh' to update the display"
        );
        instructionsText.setEditable(false);
        instructionsText.setLineWrap(true);
        instructionsText.setWrapStyleWord(true);
        instructionsText.setBackground(new Color(240, 240, 240));
        
        instructionsPanel.add(instructionsText, BorderLayout.CENTER);
        
        // Add panels to controls panel
        panel.add(selectionPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(instructionsPanel);
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    /**
     * Creates a legend item with a color box and label
     */
    private JPanel createLegendItem(String text, Color color) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        
        JPanel colorBox = new JPanel();
        colorBox.setPreferredSize(new Dimension(15, 15));
        colorBox.setBackground(color);
        colorBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        JLabel label = new JLabel(text);
        
        panel.add(colorBox);
        panel.add(label);
        
        return panel;
    }
    
    /**
     * Loads the movies from the controller
     */
    private void loadMovies() {
        movieComboBox.removeAllItems();
        
        List<Movie> movies = movieController.getAllMovies();
        for (Movie movie : movies) {
            movieComboBox.addItem(movie);
        }
        
        if (movies.size() > 0) {
            movieComboBox.setSelectedIndex(0);
        }
    }
    
    /**
     * Updates the cinemas combo box based on the selected movie
     */
    private void updateCinemas() {
        cinemaComboBox.removeAllItems();
        showtimeComboBox.removeAllItems();
        
        Movie selectedMovie = (Movie) movieComboBox.getSelectedItem();
        if (selectedMovie != null) {
            List<String> cinemas = selectedMovie.getCinemas();
            for (String cinema : cinemas) {
                cinemaComboBox.addItem(cinema);
            }
            
            if (cinemas.size() > 0) {
                cinemaComboBox.setSelectedIndex(0);
            }
        }
    }
    
    /**
     * Updates the showtimes combo box based on the selected movie
     */
    private void updateShowtimes() {
        showtimeComboBox.removeAllItems();
        
        Movie selectedMovie = (Movie) movieComboBox.getSelectedItem();
        if (selectedMovie != null) {
            List<String> showtimes = selectedMovie.getShowtimes();
            for (String showtime : showtimes) {
                showtimeComboBox.addItem(showtime);
            }
            
            if (showtimes.size() > 0) {
                showtimeComboBox.setSelectedIndex(0);
            }
        }
    }
    
    /**
     * Updates the seating chart based on the selected movie, cinema, and showtime
     */
    private void updateSeatingChart() {
        Movie selectedMovie = (Movie) movieComboBox.getSelectedItem();
        String selectedCinema = (String) cinemaComboBox.getSelectedItem();
        String selectedShowtime = (String) showtimeComboBox.getSelectedItem();
        
        if (selectedMovie == null || selectedCinema == null || selectedShowtime == null) {
            return;
        }
        
        // Remove current seating chart
        seatingChartPanel.removeAll();
        
        // Get cinema information
        Cinema cinema = movieController.getCinemaByName(selectedCinema);
        if (cinema == null) {
            return;
        }
        
        // Re-add screen panel
        JPanel screenPanel = new JPanel();
        screenPanel.setBackground(Color.DARK_GRAY);
        screenPanel.setPreferredSize(new Dimension(100, 30));
        JLabel screenLabel = new JLabel("SCREEN");
        screenLabel.setForeground(Color.WHITE);
        screenPanel.add(screenLabel);
        
        seatingChartPanel.add(screenPanel, BorderLayout.NORTH);
        
        // Create seats panel
        JPanel seatsPanel = new JPanel();
        int rows = cinema.getRows();
        int cols = cinema.getColumns();
        seatsPanel.setLayout(new GridLayout(rows, cols, 5, 5));
        
        // Create seat buttons
        seatButtons = new JButton[rows][cols];
        
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                final int r = row;
                final int c = col;
                
                // Get seat information
                String seatId = (char) ('A' + row) + String.valueOf(col + 1);
                Seat seat = cinema.getSeatById(seatId);
                
                // Create seat button
                JButton seatButton = new JButton(seatId);
                seatButton.setMargin(new Insets(2, 2, 2, 2));
                seatButton.setPreferredSize(new Dimension(50, 40));
                
                // Set button color based on seat type and availability
                if (seat != null) {
                    boolean isAvailable = movieController.isSeatAvailable(
                            selectedCinema, selectedMovie.getTitle(), selectedShowtime, seatId);
                    
                    if (!isAvailable) {
                        seatButton.setBackground(Color.RED);
                    } else if (seat.getType().equals("Deluxe")) {
                        seatButton.setBackground(new Color(255, 200, 200)); // Light red for deluxe
                    } else {
                        seatButton.setBackground(Color.LIGHT_GRAY); // Gray for standard
                    }
                    
                    // Add action listener
                    final Seat finalSeat = seat;
                    seatButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            toggleSeatAvailability(r, c, seatId, finalSeat);
                        }
                    });
                }
                
                // Add to array and panel
                seatButtons[row][col] = seatButton;
                seatsPanel.add(seatButton);
            }
        }
        
        // Add seats panel to seating chart
        seatingChartPanel.add(seatsPanel, BorderLayout.CENTER);
        
        // Update status
        statusLabel.setText("Showing seats for " + selectedMovie.getTitle() + 
                " at " + selectedCinema + ", " + selectedShowtime);
        
        // Refresh layout
        seatingChartPanel.revalidate();
        seatingChartPanel.repaint();
    }
    
    /**
     * Toggles the availability of a seat
     */
    private void toggleSeatAvailability(int row, int col, String seatId, Seat seat) {
        Movie selectedMovie = (Movie) movieComboBox.getSelectedItem();
        String selectedCinema = (String) cinemaComboBox.getSelectedItem();
        String selectedShowtime = (String) showtimeComboBox.getSelectedItem();
        
        if (selectedMovie == null || selectedCinema == null || selectedShowtime == null) {
            return;
        }
        
        boolean isAvailable = movieController.isSeatAvailable(
                selectedCinema, selectedMovie.getTitle(), selectedShowtime, seatId);
        
        JButton seatButton = seatButtons[row][col];
        
        if (isAvailable) {
            // Book the seat
            movieController.bookSeat(selectedCinema, selectedMovie.getTitle(), selectedShowtime, seatId);
            seatButton.setBackground(Color.RED);
        } else {
            // Release the seat
            movieController.releaseSeat(selectedCinema, selectedMovie.getTitle(), selectedShowtime, seatId);
            
            // Reset button color based on seat type
            if (seat.getType().equals("Deluxe")) {
                seatButton.setBackground(new Color(255, 200, 200)); // Light red for deluxe
            } else {
                seatButton.setBackground(Color.LIGHT_GRAY); // Gray for standard
            }
        }
    }
    
    /**
     * Resets all seats to available for the selected show
     */
    private void resetAllSeats() {
        Movie selectedMovie = (Movie) movieComboBox.getSelectedItem();
        String selectedCinema = (String) cinemaComboBox.getSelectedItem();
        String selectedShowtime = (String) showtimeComboBox.getSelectedItem();
        
        if (selectedMovie == null || selectedCinema == null || selectedShowtime == null) {
            JOptionPane.showMessageDialog(mainFrame,
                    "Please select a movie, cinema, and showtime first",
                    "Selection Required",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int choice = JOptionPane.showConfirmDialog(mainFrame,
                "Are you sure you want to reset all seats to available?\n" +
                "This will remove all existing bookings for this show.",
                "Confirm Reset",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
                
        if (choice == JOptionPane.YES_OPTION) {
            // Get cinema information
            Cinema cinema = movieController.getCinemaByName(selectedCinema);
            if (cinema == null) {
                return;
            }
            
            // Reset all seats
            for (Seat seat : cinema.getSeats()) {
                movieController.releaseSeat(
                        selectedCinema, selectedMovie.getTitle(), selectedShowtime, seat.getSeatId());
            }
            
            // Update seating chart
            updateSeatingChart();
            
            JOptionPane.showMessageDialog(mainFrame,
                    "All seats have been reset to available",
                    "Seats Reset",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}

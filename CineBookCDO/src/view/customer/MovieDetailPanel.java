package view.customer;

import model.Movie;
import view.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel to display movie details and allow proceeding to seat selection
 */
public class MovieDetailPanel extends JPanel {
    private MainFrame mainFrame;
    private Movie movie;
    private String cinema;
    private String showtime;

    public MovieDetailPanel(MainFrame mainFrame, Movie movie, String cinema, String showtime) {
        this.mainFrame = mainFrame;
        this.movie = movie;
        this.cinema = cinema;
        this.showtime = showtime;
        
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Movie Details");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JButton backButton = new JButton("← Back to Movies");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showPanel(MainFrame.MOVIE_LIST_PANEL);
            }
        });
        headerPanel.add(backButton, BorderLayout.EAST);
        
        // Create movie details panel
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        
        // Movie title
        JLabel movieTitleLabel = new JLabel(movie.getTitle());
        movieTitleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        movieTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Movie genre and duration
        JLabel genreDurationLabel = new JLabel(movie.getGenre() + " | " + movie.getDuration() + " mins");
        genreDurationLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        genreDurationLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Selected cinema and showtime
        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        selectionPanel.setBorder(BorderFactory.createTitledBorder("Selected Show"));
        selectionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel cinemaLabel = new JLabel("Cinema: " + cinema);
        JLabel showtimeLabel = new JLabel("Time: " + showtime);
        selectionPanel.add(cinemaLabel);
        selectionPanel.add(Box.createHorizontalStrut(20));
        selectionPanel.add(showtimeLabel);
        
        // Movie description
        JTextArea descriptionArea = new JTextArea(5, 40);
        descriptionArea.setText(movie.getDescription());
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setOpaque(false);
        descriptionArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Pricing information
        JPanel pricingPanel = new JPanel();
        pricingPanel.setLayout(new GridLayout(2, 1));
        pricingPanel.setBorder(BorderFactory.createTitledBorder("Pricing Information"));
        pricingPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        pricingPanel.add(new JLabel("Standard Seat: ₱180.00"));
        pricingPanel.add(new JLabel("Deluxe Seat: ₱250.00"));
        
        // Continue button
        JButton continueButton = new JButton("Continue to Seat Selection");
        continueButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SeatSelectionPanel seatSelectionPanel = new SeatSelectionPanel(
                        mainFrame, movie, cinema, showtime);
                mainFrame.addPanel(seatSelectionPanel, "SeatSelectionPanel");
                mainFrame.showPanel("SeatSelectionPanel");
            }
        });
        
        // Add components to details panel
        detailsPanel.add(movieTitleLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        detailsPanel.add(genreDurationLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        detailsPanel.add(selectionPanel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        detailsPanel.add(descriptionArea);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        detailsPanel.add(pricingPanel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        detailsPanel.add(continueButton);
        
        // Add components to main panel
        add(headerPanel, BorderLayout.NORTH);
        add(new JScrollPane(detailsPanel), BorderLayout.CENTER);
    }
}

package view.customer;

import controller.MovieController;
import model.Movie;
import view.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Panel to display the list of available movies
 */
public class MovieListPanel extends JPanel {
    private MainFrame mainFrame;
    private MovieController movieController;
    private JList<Movie> movieList;
    private DefaultListModel<Movie> movieListModel;
    private JPanel movieDetailsPanel;
    private JLabel movieTitleLabel;
    private JLabel movieGenreLabel;
    private JTextArea movieDescriptionArea;
    private JList<String> showtimesList;
    private DefaultListModel<String> showtimesListModel;
    private JList<String> cinemasList;
    private DefaultListModel<String> cinemasListModel;
    private JButton bookButton;

    public MovieListPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.movieController = mainFrame.getMovieController();
        
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Now Showing in CDO Cinemas");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Create split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(300);
        
        // Create movie list panel
        JPanel leftPanel = new JPanel(new BorderLayout());
        movieListModel = new DefaultListModel<>();
        movieList = new JList<>(movieListModel);
        movieList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        movieList.setCellRenderer(new MovieListCellRenderer());
        
        // Load movies into list
        loadMovies();
        
        // Add selection listener
        movieList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && movieList.getSelectedValue() != null) {
                    displayMovieDetails(movieList.getSelectedValue());
                }
            }
        });
        
        JScrollPane movieScrollPane = new JScrollPane(movieList);
        leftPanel.add(new JLabel("Select a Movie:"), BorderLayout.NORTH);
        leftPanel.add(movieScrollPane, BorderLayout.CENTER);
        
        // Create movie details panel
        movieDetailsPanel = new JPanel();
        movieDetailsPanel.setLayout(new BoxLayout(movieDetailsPanel, BoxLayout.Y_AXIS));
        movieDetailsPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
        
        // Movie title label
        movieTitleLabel = new JLabel("Select a movie to see details");
        movieTitleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        movieTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Movie genre label
        movieGenreLabel = new JLabel("");
        movieGenreLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        movieGenreLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Movie description
        movieDescriptionArea = new JTextArea(5, 20);
        movieDescriptionArea.setLineWrap(true);
        movieDescriptionArea.setWrapStyleWord(true);
        movieDescriptionArea.setEditable(false);
        movieDescriptionArea.setBackground(new Color(240, 240, 240));
        movieDescriptionArea.setBorder(BorderFactory.createEtchedBorder());
        movieDescriptionArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        JScrollPane descScrollPane = new JScrollPane(movieDescriptionArea);
        descScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Showtimes list
        JPanel showtimesPanel = new JPanel(new BorderLayout());
        showtimesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        showtimesPanel.setBorder(BorderFactory.createTitledBorder("Showtimes"));
        showtimesListModel = new DefaultListModel<>();
        showtimesList = new JList<>(showtimesListModel);
        JScrollPane showtimesScrollPane = new JScrollPane(showtimesList);
        showtimesPanel.add(showtimesScrollPane, BorderLayout.CENTER);
        
        // Cinemas list
        JPanel cinemasPanel = new JPanel(new BorderLayout());
        cinemasPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        cinemasPanel.setBorder(BorderFactory.createTitledBorder("Available at"));
        cinemasListModel = new DefaultListModel<>();
        cinemasList = new JList<>(cinemasListModel);
        JScrollPane cinemasScrollPane = new JScrollPane(cinemasList);
        cinemasPanel.add(cinemasScrollPane, BorderLayout.CENTER);
        
        // Book button
        bookButton = new JButton("Book Tickets");
        bookButton.setEnabled(false);
        bookButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Movie selectedMovie = movieList.getSelectedValue();
                String selectedCinema = cinemasList.getSelectedValue();
                String selectedShowtime = showtimesList.getSelectedValue();
                
                if (selectedMovie != null && selectedCinema != null && selectedShowtime != null) {
                    // Create movie detail panel and show it
                    MovieDetailPanel movieDetailPanel = new MovieDetailPanel(
                            mainFrame, selectedMovie, selectedCinema, selectedShowtime);
                    mainFrame.addPanel(movieDetailPanel, "MovieDetailPanel");
                    mainFrame.showPanel("MovieDetailPanel");
                } else {
                    JOptionPane.showMessageDialog(mainFrame, 
                            "Please select a movie, cinema, and showtime.", 
                            "Selection Required", 
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        
        // Set selection listener for enabling/disabling book button
        ListSelectionListener selectionListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    bookButton.setEnabled(
                            movieList.getSelectedValue() != null && 
                            cinemasList.getSelectedValue() != null && 
                            showtimesList.getSelectedValue() != null);
                }
            }
        };
        
        showtimesList.addListSelectionListener(selectionListener);
        cinemasList.addListSelectionListener(selectionListener);
        
        // Add everything to details panel
        movieDetailsPanel.add(movieTitleLabel);
        movieDetailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        movieDetailsPanel.add(movieGenreLabel);
        movieDetailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        movieDetailsPanel.add(descScrollPane);
        movieDetailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        movieDetailsPanel.add(showtimesPanel);
        movieDetailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        movieDetailsPanel.add(cinemasPanel);
        movieDetailsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        movieDetailsPanel.add(bookButton);
        
        // Add panels to split pane
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(new JScrollPane(movieDetailsPanel));
        
        // Add components to main panel
        add(headerPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    }

    /**
     * Loads movies from controller into the list
     */
    private void loadMovies() {
        movieListModel.clear();
        List<Movie> movies = movieController.getAllMovies();
        
        for (Movie movie : movies) {
            movieListModel.addElement(movie);
        }
        
        if (movies.size() > 0) {
            movieList.setSelectedIndex(0);
        }
    }

    /**
     * Displays details of a selected movie
     */
    private void displayMovieDetails(Movie movie) {
        movieTitleLabel.setText(movie.getTitle());
        movieGenreLabel.setText(movie.getGenre() + " | " + movie.getDuration() + " mins");
        movieDescriptionArea.setText(movie.getDescription());
        
        // Update showtimes list
        showtimesListModel.clear();
        for (String showtime : movie.getShowtimes()) {
            showtimesListModel.addElement(showtime);
        }
        
        // Update cinemas list
        cinemasListModel.clear();
        for (String cinema : movie.getCinemas()) {
            cinemasListModel.addElement(cinema);
        }
        
        // Select first items if available
        if (showtimesListModel.size() > 0) {
            showtimesList.setSelectedIndex(0);
        }
        
        if (cinemasListModel.size() > 0) {
            cinemasList.setSelectedIndex(0);
        }
    }

    /**
     * Custom renderer for movie list items
     */
    private class MovieListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(
                JList<?> list, Object value, int index, 
                boolean isSelected, boolean cellHasFocus) {
            
            JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);
            
            if (value instanceof Movie) {
                Movie movie = (Movie) value;
                label.setText(movie.getTitle());
                label.setToolTipText(movie.getDescription());
            }
            
            return label;
        }
    }
}

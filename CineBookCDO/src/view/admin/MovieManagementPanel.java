package view.admin;

import controller.MovieController;
import model.Movie;
import view.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Panel for managing movies in the admin dashboard
 */
public class MovieManagementPanel extends JPanel {
    private MainFrame mainFrame;
    private MovieController movieController;
    private JTable moviesTable;
    private DefaultTableModel tableModel;
    private JTextField titleField;
    private JTextField genreField;
    private JTextArea descriptionField;
    private JTextField durationField;
    private JTextField posterPathField;
    private JList<String> showtimesList;
    private DefaultListModel<String> showtimesModel;
    private JList<String> cinemasList;
    private DefaultListModel<String> cinemasModel;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton resetButton;
    private int selectedMovieId = -1;

    public MovieManagementPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.movieController = mainFrame.getMovieController();
        
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Create panel title
        JLabel titleLabel = new JLabel("Movie Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        // Create split pane for table and form
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(450);
        
        // Create movies table panel
        JPanel tablePanel = createMoviesTablePanel();
        
        // Create form panel
        JPanel formPanel = createMovieFormPanel();
        
        // Add panels to split pane
        splitPane.setLeftComponent(tablePanel);
        splitPane.setRightComponent(formPanel);
        
        // Add components to main panel
        add(titleLabel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        
        // Load initial data
        loadMoviesData();
    }
    
    /**
     * Creates the panel containing the movies table
     */
    private JPanel createMoviesTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(0, 0, 0, 10));
        
        // Create table model with non-editable cells
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
        
        // Add columns
        tableModel.addColumn("ID");
        tableModel.addColumn("Title");
        tableModel.addColumn("Genre");
        tableModel.addColumn("Duration (mins)");
        tableModel.addColumn("Cinemas");
        
        // Create table
        moviesTable = new JTable(tableModel);
        moviesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        moviesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = moviesTable.getSelectedRow();
                if (selectedRow >= 0) {
                    loadMovieDetails(selectedRow);
                }
            }
        });
        
        // Set column widths
        moviesTable.getColumnModel().getColumn(0).setPreferredWidth(30);
        moviesTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        moviesTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        moviesTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        moviesTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        
        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(moviesTable);
        
        // Create refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadMoviesData();
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(refreshButton);
        
        // Add to panel
        panel.add(new JLabel("Movies List"), BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Creates the panel containing the movie form
     */
    private JPanel createMovieFormPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 1, 0, 0, Color.GRAY),
                new EmptyBorder(0, 10, 0, 0)));
        
        // Create form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Title:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        titleField = new JTextField(20);
        formPanel.add(titleField, gbc);
        
        // Genre
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Genre:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        genreField = new JTextField(20);
        formPanel.add(genreField, gbc);
        
        // Duration
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Duration (mins):"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        durationField = new JTextField(20);
        formPanel.add(durationField, gbc);
        
        // Poster Path
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Poster Path:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        posterPathField = new JTextField(20);
        formPanel.add(posterPathField, gbc);
        
        // Description
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Description:"), gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        descriptionField = new JTextArea(5, 20);
        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);
        JScrollPane descScrollPane = new JScrollPane(descriptionField);
        formPanel.add(descScrollPane, gbc);
        
        // Showtimes
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(new JLabel("Showtimes:"), gbc);
        
        showtimesModel = new DefaultListModel<>();
        showtimesList = new JList<>(showtimesModel);
        JScrollPane showtimesScrollPane = new JScrollPane(showtimesList);
        showtimesScrollPane.setPreferredSize(new Dimension(0, 80));
        
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(showtimesScrollPane, gbc);
        
        // Showtime actions
        gbc.gridx = 2;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel showtimeButtonsPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        
        JButton addShowtimeButton = new JButton("Add");
        addShowtimeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String showtime = JOptionPane.showInputDialog(mainFrame,
                        "Enter showtime (e.g., '7:30 PM'):",
                        "Add Showtime",
                        JOptionPane.PLAIN_MESSAGE);
                
                if (showtime != null && !showtime.trim().isEmpty()) {
                    showtimesModel.addElement(showtime.trim());
                }
            }
        });
        
        JButton removeShowtimeButton = new JButton("Remove");
        removeShowtimeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = showtimesList.getSelectedIndex();
                if (selectedIndex >= 0) {
                    showtimesModel.remove(selectedIndex);
                } else {
                    JOptionPane.showMessageDialog(mainFrame,
                            "Please select a showtime to remove",
                            "No Selection",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        
        showtimeButtonsPanel.add(addShowtimeButton);
        showtimeButtonsPanel.add(removeShowtimeButton);
        formPanel.add(showtimeButtonsPanel, gbc);
        
        // Cinemas
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(new JLabel("Cinemas:"), gbc);
        
        cinemasModel = new DefaultListModel<>();
        cinemasList = new JList<>(cinemasModel);
        JScrollPane cinemasScrollPane = new JScrollPane(cinemasList);
        cinemasScrollPane.setPreferredSize(new Dimension(0, 80));
        
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(cinemasScrollPane, gbc);
        
        // Cinema actions
        gbc.gridx = 2;
        gbc.gridy = 9;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel cinemaButtonsPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        
        JButton addCinemaButton = new JButton("Add");
        addCinemaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] options = {"Cinema 1", "Cinema 2", "Cinema 3"};
                String cinema = (String) JOptionPane.showInputDialog(mainFrame,
                        "Select cinema:",
                        "Add Cinema",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);
                
                if (cinema != null && !cinemasModel.contains(cinema)) {
                    cinemasModel.addElement(cinema);
                }
            }
        });
        
        JButton removeCinemaButton = new JButton("Remove");
        removeCinemaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = cinemasList.getSelectedIndex();
                if (selectedIndex >= 0) {
                    cinemasModel.remove(selectedIndex);
                } else {
                    JOptionPane.showMessageDialog(mainFrame,
                            "Please select a cinema to remove",
                            "No Selection",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        
        cinemaButtonsPanel.add(addCinemaButton);
        cinemaButtonsPanel.add(removeCinemaButton);
        formPanel.add(cinemaButtonsPanel, gbc);
        
        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        addButton = new JButton("Add Movie");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addMovie();
            }
        });
        
        updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMovie();
            }
        });
        updateButton.setEnabled(false);
        
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteMovie();
            }
        });
        deleteButton.setEnabled(false);
        
        resetButton = new JButton("Reset Form");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetForm();
            }
        });
        
        buttonsPanel.add(addButton);
        buttonsPanel.add(updateButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(resetButton);
        
        // Add to panel
        panel.add(new JLabel("Movie Details"), BorderLayout.NORTH);
        panel.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Loads the movie data from the controller into the table
     */
    private void loadMoviesData() {
        // Clear table
        tableModel.setRowCount(0);
        
        // Get all movies
        List<Movie> movies = movieController.getAllMovies();
        
        // Add rows to table
        for (Movie movie : movies) {
            // Format cinemas as comma-separated list
            StringBuilder cinemas = new StringBuilder();
            for (int i = 0; i < movie.getCinemas().size(); i++) {
                cinemas.append(movie.getCinemas().get(i));
                if (i < movie.getCinemas().size() - 1) {
                    cinemas.append(", ");
                }
            }
            
            Object[] row = {
                movie.getId(),
                movie.getTitle(),
                movie.getGenre(),
                movie.getDuration(),
                cinemas.toString()
            };
            
            tableModel.addRow(row);
        }
        
        // Reset form
        resetForm();
    }
    
    /**
     * Loads the details of a selected movie into the form
     */
    private void loadMovieDetails(int selectedRow) {
        // Get selected movie ID
        selectedMovieId = (int) tableModel.getValueAt(selectedRow, 0);
        
        // Find movie object
        Movie movie = movieController.getMovieById(selectedMovieId);
        
        if (movie != null) {
            // Load data into form
            titleField.setText(movie.getTitle());
            genreField.setText(movie.getGenre());
            durationField.setText(String.valueOf(movie.getDuration()));
            posterPathField.setText(movie.getPosterPath());
            descriptionField.setText(movie.getDescription());
            
            // Load showtimes
            showtimesModel.clear();
            for (String showtime : movie.getShowtimes()) {
                showtimesModel.addElement(showtime);
            }
            
            // Load cinemas
            cinemasModel.clear();
            for (String cinema : movie.getCinemas()) {
                cinemasModel.addElement(cinema);
            }
            
            // Enable update and delete buttons
            updateButton.setEnabled(true);
            deleteButton.setEnabled(true);
            addButton.setEnabled(false);
        }
    }
    
    /**
     * Adds a new movie
     */
    private void addMovie() {
        // Validate inputs
        if (!validateInputs()) {
            return;
        }
        
        try {
            // Create new movie
            int nextId = tableModel.getRowCount() > 0 ? 
                    (int) tableModel.getValueAt(tableModel.getRowCount() - 1, 0) + 1 : 1;
                    
            String title = titleField.getText();
            String description = descriptionField.getText();
            String genre = genreField.getText();
            int duration = Integer.parseInt(durationField.getText());
            String posterPath = posterPathField.getText();
            
            Movie movie = new Movie(nextId, title, description, genre, duration, posterPath);
            
            // Add showtimes
            for (int i = 0; i < showtimesModel.size(); i++) {
                movie.addShowtime(showtimesModel.getElementAt(i));
            }
            
            // Add cinemas
            for (int i = 0; i < cinemasModel.size(); i++) {
                movie.addCinema(cinemasModel.getElementAt(i));
            }
            
            // Add movie
            movieController.addMovie(movie);
            
            // Reload data
            loadMoviesData();
            
            JOptionPane.showMessageDialog(mainFrame,
                    "Movie added successfully!",
                    "Movie Added",
                    JOptionPane.INFORMATION_MESSAGE);
                    
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(mainFrame,
                    "Please enter a valid number for duration.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Updates an existing movie
     */
    private void updateMovie() {
        // Validate inputs
        if (!validateInputs()) {
            return;
        }
        
        try {
            // Find movie
            Movie movie = movieController.getMovieById(selectedMovieId);
            
            if (movie != null) {
                // Update movie data
                movie.setTitle(titleField.getText());
                movie.setDescription(descriptionField.getText());
                movie.setGenre(genreField.getText());
                movie.setDuration(Integer.parseInt(durationField.getText()));
                movie.setPosterPath(posterPathField.getText());
                
                // Update showtimes
                movie.getShowtimes().clear();
                for (int i = 0; i < showtimesModel.size(); i++) {
                    movie.addShowtime(showtimesModel.getElementAt(i));
                }
                
                // Update cinemas
                movie.getCinemas().clear();
                for (int i = 0; i < cinemasModel.size(); i++) {
                    movie.addCinema(cinemasModel.getElementAt(i));
                }
                
                // Update movie
                movieController.updateMovie(movie);
                
                // Reload data
                loadMoviesData();
                
                JOptionPane.showMessageDialog(mainFrame,
                        "Movie updated successfully!",
                        "Movie Updated",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(mainFrame,
                    "Please enter a valid number for duration.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Deletes a movie
     */
    private void deleteMovie() {
        int choice = JOptionPane.showConfirmDialog(mainFrame,
                "Are you sure you want to delete this movie?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
                
        if (choice == JOptionPane.YES_OPTION) {
            // Delete movie
            movieController.removeMovie(selectedMovieId);
            
            // Reload data
            loadMoviesData();
            
            JOptionPane.showMessageDialog(mainFrame,
                    "Movie deleted successfully!",
                    "Movie Deleted",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Resets the form
     */
    private void resetForm() {
        // Clear form fields
        titleField.setText("");
        genreField.setText("");
        durationField.setText("");
        posterPathField.setText("");
        descriptionField.setText("");
        showtimesModel.clear();
        cinemasModel.clear();
        
        // Reset buttons
        addButton.setEnabled(true);
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        
        // Clear selection
        moviesTable.clearSelection();
        selectedMovieId = -1;
    }
    
    /**
     * Validates the form inputs
     */
    private boolean validateInputs() {
        // Check title
        if (titleField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame,
                    "Please enter a movie title.",
                    "Missing Title",
                    JOptionPane.WARNING_MESSAGE);
            titleField.requestFocus();
            return false;
        }
        
        // Check genre
        if (genreField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame,
                    "Please enter a movie genre.",
                    "Missing Genre",
                    JOptionPane.WARNING_MESSAGE);
            genreField.requestFocus();
            return false;
        }
        
        // Check duration
        try {
            int duration = Integer.parseInt(durationField.getText().trim());
            if (duration <= 0) {
                JOptionPane.showMessageDialog(mainFrame,
                        "Duration must be a positive number.",
                        "Invalid Duration",
                        JOptionPane.WARNING_MESSAGE);
                durationField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(mainFrame,
                    "Please enter a valid number for duration.",
                    "Invalid Duration",
                    JOptionPane.WARNING_MESSAGE);
            durationField.requestFocus();
            return false;
        }
        
        // Check description
        if (descriptionField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame,
                    "Please enter a movie description.",
                    "Missing Description",
                    JOptionPane.WARNING_MESSAGE);
            descriptionField.requestFocus();
            return false;
        }
        
        // Check showtimes
        if (showtimesModel.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame,
                    "Please add at least one showtime.",
                    "No Showtimes",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Check cinemas
        if (cinemasModel.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame,
                    "Please add at least one cinema.",
                    "No Cinemas",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }
}

package controller;

import model.Movie;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for movie-related operations
 */
public class MovieController {
    private List<Movie> movies;
    
    public MovieController() {
        this.movies = new ArrayList<>();
        initializeMovies();
    }
    
    /**
     * Initializes sample movies
     */
    private void initializeMovies() {
        // Add some sample movies
        Movie movie1 = new Movie(1, "Inception", 
                "A thief who steals corporate secrets through dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.",
                "Sci-Fi/Action", 148, 
                "Christopher Nolan", 
                "Leonardo DiCaprio, Joseph Gordon-Levitt, Ellen Page", 
                "inception.jpg", 
                "https://www.youtube.com/watch?v=8hP9D6kZseM", 
                "PG-13");
        
        Movie movie2 = new Movie(2, "The Dark Knight", 
                "When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests of his ability to fight injustice.",
                "Action/Crime", 152, 
                "Christopher Nolan", 
                "Christian Bale, Heath Ledger, Aaron Eckhart", 
                "dark_knight.jpg", 
                "https://www.youtube.com/watch?v=EXeTwQWrcwY", 
                "PG-13");
        
        Movie movie3 = new Movie(3, "Parasite", 
                "Greed and class discrimination threaten the newly formed symbiotic relationship between the wealthy Park family and the destitute Kim clan.",
                "Thriller/Drama", 132, 
                "Bong Joon-ho", 
                "Song Kang-ho, Lee Sun-kyun, Cho Yeo-jeong", 
                "parasite.jpg", 
                "https://www.youtube.com/watch?v=5xH0HfJHsaY", 
                "R");
        
        // Add showtimes
        LocalDateTime now = LocalDateTime.now();
        
        movie1.addShowtime(now.plusDays(1).withHour(10).withMinute(0));
        movie1.addShowtime(now.plusDays(1).withHour(13).withMinute(0));
        movie1.addShowtime(now.plusDays(1).withHour(16).withMinute(0));
        movie1.addShowtime(now.plusDays(1).withHour(19).withMinute(0));
        
        movie2.addShowtime(now.plusDays(1).withHour(11).withMinute(0));
        movie2.addShowtime(now.plusDays(1).withHour(14).withMinute(0));
        movie2.addShowtime(now.plusDays(1).withHour(17).withMinute(0));
        movie2.addShowtime(now.plusDays(1).withHour(20).withMinute(0));
        
        movie3.addShowtime(now.plusDays(1).withHour(12).withMinute(0));
        movie3.addShowtime(now.plusDays(1).withHour(15).withMinute(0));
        movie3.addShowtime(now.plusDays(1).withHour(18).withMinute(0));
        movie3.addShowtime(now.plusDays(1).withHour(21).withMinute(0));
        
        // Add movies to list
        movies.add(movie1);
        movies.add(movie2);
        movies.add(movie3);
    }
    
    /**
     * Gets all movies
     */
    public List<Movie> getAllMovies() {
        return new ArrayList<>(movies);
    }
    
    /**
     * Gets a movie by ID
     */
    public Movie getMovieById(int id) {
        for (Movie movie : movies) {
            if (movie.getId() == id) {
                return movie;
            }
        }
        return null;
    }
    
    /**
     * Adds a new movie
     */
    public void addMovie(Movie movie) {
        movies.add(movie);
    }
    
    /**
     * Updates a movie
     */
    public boolean updateMovie(Movie updatedMovie) {
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getId() == updatedMovie.getId()) {
                movies.set(i, updatedMovie);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Removes a movie
     */
    public boolean removeMovie(int id) {
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getId() == id) {
                movies.remove(i);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Gets movies by genre
     */
    public List<Movie> getMoviesByGenre(String genre) {
        List<Movie> filteredMovies = new ArrayList<>();
        for (Movie movie : movies) {
            if (movie.getGenre().contains(genre)) {
                filteredMovies.add(movie);
            }
        }
        return filteredMovies;
    }
    
    /**
     * Searches movies by title or description
     */
    public List<Movie> searchMovies(String query) {
        List<Movie> searchResults = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        
        for (Movie movie : movies) {
            if (movie.getTitle().toLowerCase().contains(lowerQuery) || 
                movie.getDescription().toLowerCase().contains(lowerQuery)) {
                searchResults.add(movie);
            }
        }
        
        return searchResults;
    }
}
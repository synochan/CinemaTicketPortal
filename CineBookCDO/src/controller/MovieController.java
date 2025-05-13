package controller;

import model.Cinema;
import model.Movie;
import model.Seat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class for movie-related operations
 */
public class MovieController {
    private List<Movie> movies;
    private List<Cinema> cinemas;
    private Map<String, Map<String, Map<String, Boolean>>> seatAvailability; // cinema -> movie -> showtime -> seats

    public MovieController() {
        this.movies = new ArrayList<>();
        this.cinemas = new ArrayList<>();
        this.seatAvailability = new HashMap<>();
        initializeData();
    }

    /**
     * Initializes sample data for the application
     */
    private void initializeData() {
        // Initialize cinemas
        Cinema cinema1 = new Cinema("Cinema 1", 8, 10);
        Cinema cinema2 = new Cinema("Cinema 2", 8, 10);
        Cinema cinema3 = new Cinema("Cinema 3", 8, 10);
        
        cinemas.add(cinema1);
        cinemas.add(cinema2);
        cinemas.add(cinema3);

        // Initialize movies with sample data
        Movie movie1 = new Movie(
                1, 
                "Avengers: Endgame", 
                "After the devastating events of Avengers: Infinity War, the universe is in ruins. With the help of remaining allies, the Avengers assemble once more in order to reverse Thanos' actions and restore balance to the universe.", 
                "Action, Adventure, Drama", 
                181, 
                "avengers_endgame.jpg"
        );
        movie1.addShowtime("5:00 PM");
        movie1.addShowtime("8:30 PM");
        movie1.addCinema("Cinema 1");
        
        Movie movie2 = new Movie(
                2, 
                "Frozen II", 
                "Anna, Elsa, Kristoff, Olaf and Sven leave Arendelle to travel to an ancient, autumn-bound forest of an enchanted land. They set out to find the origin of Elsa's powers in order to save their kingdom.", 
                "Animation, Adventure, Comedy", 
                103, 
                "frozen_2.jpg"
        );
        movie2.addShowtime("7:30 PM");
        movie2.addShowtime("3:00 PM");
        movie2.addCinema("Cinema 2");
        
        Movie movie3 = new Movie(
                3, 
                "Joker", 
                "In Gotham City, mentally troubled comedian Arthur Fleck is disregarded and mistreated by society. He then embarks on a downward spiral of revolution and bloody crime. This path brings him face-to-face with his alter-ego: the Joker.", 
                "Crime, Drama, Thriller", 
                122, 
                "joker.jpg"
        );
        movie3.addShowtime("9:00 PM");
        movie3.addShowtime("6:30 PM");
        movie3.addCinema("Cinema 3");
        
        movies.add(movie1);
        movies.add(movie2);
        movies.add(movie3);

        // Initialize seat availability
        for (Cinema cinema : cinemas) {
            Map<String, Map<String, Boolean>> movieMap = new HashMap<>();
            seatAvailability.put(cinema.getName(), movieMap);
            
            for (Movie movie : movies) {
                if (movie.getCinemas().contains(cinema.getName())) {
                    Map<String, Boolean> showtimeMap = new HashMap<>();
                    movieMap.put(movie.getTitle(), showtimeMap);
                    
                    for (String showtime : movie.getShowtimes()) {
                        for (Seat seat : cinema.getSeats()) {
                            showtimeMap.put(showtime + "_" + seat.getSeatId(), true);
                        }
                    }
                }
            }
        }
    }

    /**
     * Gets all movies
     */
    public List<Movie> getAllMovies() {
        return new ArrayList<>(movies);
    }

    /**
     * Gets all cinemas
     */
    public List<Cinema> getAllCinemas() {
        return new ArrayList<>(cinemas);
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
     * Gets a cinema by name
     */
    public Cinema getCinemaByName(String name) {
        for (Cinema cinema : cinemas) {
            if (cinema.getName().equals(name)) {
                return cinema;
            }
        }
        return null;
    }

    /**
     * Adds a new movie
     */
    public void addMovie(Movie movie) {
        movies.add(movie);
        
        // Initialize seat availability for this movie
        for (String cinemaName : movie.getCinemas()) {
            Map<String, Map<String, Boolean>> movieMap = seatAvailability.get(cinemaName);
            if (movieMap == null) {
                movieMap = new HashMap<>();
                seatAvailability.put(cinemaName, movieMap);
            }
            
            Map<String, Boolean> showtimeMap = new HashMap<>();
            movieMap.put(movie.getTitle(), showtimeMap);
            
            Cinema cinema = getCinemaByName(cinemaName);
            if (cinema != null) {
                for (String showtime : movie.getShowtimes()) {
                    for (Seat seat : cinema.getSeats()) {
                        showtimeMap.put(showtime + "_" + seat.getSeatId(), true);
                    }
                }
            }
        }
    }

    /**
     * Updates an existing movie
     */
    public void updateMovie(Movie updatedMovie) {
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getId() == updatedMovie.getId()) {
                movies.set(i, updatedMovie);
                break;
            }
        }
    }

    /**
     * Removes a movie
     */
    public void removeMovie(int movieId) {
        Movie movieToRemove = null;
        for (Movie movie : movies) {
            if (movie.getId() == movieId) {
                movieToRemove = movie;
                break;
            }
        }
        
        if (movieToRemove != null) {
            movies.remove(movieToRemove);
            
            // Remove seat availability data for this movie
            for (String cinemaName : movieToRemove.getCinemas()) {
                Map<String, Map<String, Boolean>> movieMap = seatAvailability.get(cinemaName);
                if (movieMap != null) {
                    movieMap.remove(movieToRemove.getTitle());
                }
            }
        }
    }

    /**
     * Checks if a seat is available
     */
    public boolean isSeatAvailable(String cinemaName, String movieTitle, String showtime, String seatId) {
        Map<String, Map<String, Boolean>> movieMap = seatAvailability.get(cinemaName);
        if (movieMap != null) {
            Map<String, Boolean> showtimeMap = movieMap.get(movieTitle);
            if (showtimeMap != null) {
                Boolean isAvailable = showtimeMap.get(showtime + "_" + seatId);
                return isAvailable != null && isAvailable;
            }
        }
        return false;
    }

    /**
     * Books a seat
     */
    public void bookSeat(String cinemaName, String movieTitle, String showtime, String seatId) {
        Map<String, Map<String, Boolean>> movieMap = seatAvailability.get(cinemaName);
        if (movieMap != null) {
            Map<String, Boolean> showtimeMap = movieMap.get(movieTitle);
            if (showtimeMap != null) {
                showtimeMap.put(showtime + "_" + seatId, false);
            }
        }
    }

    /**
     * Releases a seat (marks it as available)
     */
    public void releaseSeat(String cinemaName, String movieTitle, String showtime, String seatId) {
        Map<String, Map<String, Boolean>> movieMap = seatAvailability.get(cinemaName);
        if (movieMap != null) {
            Map<String, Boolean> showtimeMap = movieMap.get(movieTitle);
            if (showtimeMap != null) {
                showtimeMap.put(showtime + "_" + seatId, true);
            }
        }
    }

    /**
     * Gets all available seats for a cinema, movie, and showtime
     */
    public List<Seat> getAvailableSeats(String cinemaName, String movieTitle, String showtime) {
        List<Seat> availableSeats = new ArrayList<>();
        Cinema cinema = getCinemaByName(cinemaName);
        
        if (cinema != null) {
            for (Seat seat : cinema.getSeats()) {
                if (isSeatAvailable(cinemaName, movieTitle, showtime, seat.getSeatId())) {
                    availableSeats.add(seat);
                }
            }
        }
        
        return availableSeats;
    }

    /**
     * Gets all booked seats for a cinema, movie, and showtime
     */
    public List<Seat> getBookedSeats(String cinemaName, String movieTitle, String showtime) {
        List<Seat> bookedSeats = new ArrayList<>();
        Cinema cinema = getCinemaByName(cinemaName);
        
        if (cinema != null) {
            for (Seat seat : cinema.getSeats()) {
                if (!isSeatAvailable(cinemaName, movieTitle, showtime, seat.getSeatId())) {
                    bookedSeats.add(seat);
                }
            }
        }
        
        return bookedSeats;
    }

    /**
     * Adds a showtime to a movie
     */
    public void addShowtime(int movieId, String showtime) {
        Movie movie = getMovieById(movieId);
        if (movie != null) {
            movie.addShowtime(showtime);
            
            // Initialize seat availability for this showtime
            for (String cinemaName : movie.getCinemas()) {
                Map<String, Map<String, Boolean>> movieMap = seatAvailability.get(cinemaName);
                if (movieMap != null) {
                    Map<String, Boolean> showtimeMap = movieMap.get(movie.getTitle());
                    if (showtimeMap != null) {
                        Cinema cinema = getCinemaByName(cinemaName);
                        if (cinema != null) {
                            for (Seat seat : cinema.getSeats()) {
                                showtimeMap.put(showtime + "_" + seat.getSeatId(), true);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Removes a showtime from a movie
     */
    public void removeShowtime(int movieId, String showtime) {
        Movie movie = getMovieById(movieId);
        if (movie != null) {
            movie.removeShowtime(showtime);
            
            // Remove seat availability data for this showtime
            for (String cinemaName : movie.getCinemas()) {
                Map<String, Map<String, Boolean>> movieMap = seatAvailability.get(cinemaName);
                if (movieMap != null) {
                    Map<String, Boolean> showtimeMap = movieMap.get(movie.getTitle());
                    if (showtimeMap != null) {
                        Cinema cinema = getCinemaByName(cinemaName);
                        if (cinema != null) {
                            for (Seat seat : cinema.getSeats()) {
                                showtimeMap.remove(showtime + "_" + seat.getSeatId());
                            }
                        }
                    }
                }
            }
        }
    }
}

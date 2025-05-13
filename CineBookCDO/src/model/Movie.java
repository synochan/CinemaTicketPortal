package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a movie in the system
 */
public class Movie {
    private int id;
    private String title;
    private String description;
    private String genre;
    private int durationMinutes;
    private String director;
    private String cast;
    private String poster;  // URL to poster image
    private String trailer; // URL to trailer
    private String rating;  // PG, PG-13, R, etc.
    private List<LocalDateTime> showtimes;
    
    public Movie(int id, String title, String description, String genre, int durationMinutes) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.durationMinutes = durationMinutes;
        this.showtimes = new ArrayList<>();
    }
    
    // Constructor with all fields
    public Movie(int id, String title, String description, String genre, int durationMinutes,
                String director, String cast, String poster, String trailer, String rating) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.durationMinutes = durationMinutes;
        this.director = director;
        this.cast = cast;
        this.poster = poster;
        this.trailer = trailer;
        this.rating = rating;
        this.showtimes = new ArrayList<>();
    }
    
    // Getters and setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getGenre() {
        return genre;
    }
    
    public void setGenre(String genre) {
        this.genre = genre;
    }
    
    public int getDurationMinutes() {
        return durationMinutes;
    }
    
    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
    
    public String getDirector() {
        return director;
    }
    
    public void setDirector(String director) {
        this.director = director;
    }
    
    public String getCast() {
        return cast;
    }
    
    public void setCast(String cast) {
        this.cast = cast;
    }
    
    public String getPoster() {
        return poster;
    }
    
    public void setPoster(String poster) {
        this.poster = poster;
    }
    
    public String getTrailer() {
        return trailer;
    }
    
    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }
    
    public String getRating() {
        return rating;
    }
    
    public void setRating(String rating) {
        this.rating = rating;
    }
    
    public List<LocalDateTime> getShowtimes() {
        return showtimes;
    }
    
    public void setShowtimes(List<LocalDateTime> showtimes) {
        this.showtimes = showtimes;
    }
    
    public void addShowtime(LocalDateTime showtime) {
        this.showtimes.add(showtime);
    }
    
    public void removeShowtime(LocalDateTime showtime) {
        this.showtimes.remove(showtime);
    }
    
    /**
     * Formats duration as hours and minutes
     */
    public String getFormattedDuration() {
        int hours = durationMinutes / 60;
        int minutes = durationMinutes % 60;
        
        if (hours > 0) {
            return hours + "h " + minutes + "m";
        } else {
            return minutes + "m";
        }
    }
    
    @Override
    public String toString() {
        return title;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return id == movie.id;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
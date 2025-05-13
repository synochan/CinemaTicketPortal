package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a movie in the cinema booking system
 */
public class Movie {
    private int id;
    private String title;
    private String description;
    private String genre;
    private int duration; // in minutes
    private String posterPath;
    private List<String> showtimes;
    private List<String> cinemas;
    private static int nextId = 1;

    public Movie(String title, String cinema, String showtime) {
        this.id = nextId++;
        this.title = title;
        this.description = "Description for " + title;
        this.genre = "General";
        this.duration = 120;
        this.posterPath = "default_poster.png";
        this.showtimes = new ArrayList<>();
        this.showtimes.add(showtime);
        this.cinemas = new ArrayList<>();
        this.cinemas.add(cinema);
    }

    public Movie(int id, String title, String description, String genre, int duration, String posterPath) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.duration = duration;
        this.posterPath = posterPath;
        this.showtimes = new ArrayList<>();
        this.cinemas = new ArrayList<>();
    }

    // Getters and setters
    public int getId() {
        return id;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public List<String> getShowtimes() {
        return showtimes;
    }

    public void addShowtime(String showtime) {
        if (!showtimes.contains(showtime)) {
            showtimes.add(showtime);
        }
    }

    public void removeShowtime(String showtime) {
        showtimes.remove(showtime);
    }

    public List<String> getCinemas() {
        return cinemas;
    }

    public void addCinema(String cinema) {
        if (!cinemas.contains(cinema)) {
            cinemas.add(cinema);
        }
    }

    public void removeCinema(String cinema) {
        cinemas.remove(cinema);
    }

    @Override
    public String toString() {
        return title;
    }
}

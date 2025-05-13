package controller;

import model.Movie;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class for booking-related operations
 */
public class BookingController {
    private MovieController movieController;
    private Map<Integer, List<String>> bookedSeats; // Map<showingId, List<seatId>>
    
    public BookingController(MovieController movieController) {
        this.movieController = movieController;
        this.bookedSeats = new HashMap<>();
    }
    
    /**
     * Gets available seats for a specific movie showing
     */
    public List<String> getAvailableSeats(int movieId, LocalDateTime showtime) {
        int showingId = generateShowingId(movieId, showtime);
        List<String> allSeats = generateAllSeats();
        List<String> booked = bookedSeats.getOrDefault(showingId, new ArrayList<>());
        
        List<String> available = new ArrayList<>(allSeats);
        available.removeAll(booked);
        
        return available;
    }
    
    /**
     * Books seats for a movie showing
     */
    public boolean bookSeats(int movieId, LocalDateTime showtime, List<String> seats) {
        int showingId = generateShowingId(movieId, showtime);
        
        // Get currently booked seats
        List<String> booked = bookedSeats.getOrDefault(showingId, new ArrayList<>());
        
        // Check if any requested seats are already booked
        for (String seat : seats) {
            if (booked.contains(seat)) {
                return false; // Seat already booked
            }
        }
        
        // Book the seats
        booked.addAll(seats);
        bookedSeats.put(showingId, booked);
        
        return true;
    }
    
    /**
     * Cancels a booking
     */
    public boolean cancelBooking(int movieId, LocalDateTime showtime, List<String> seats) {
        int showingId = generateShowingId(movieId, showtime);
        
        if (!bookedSeats.containsKey(showingId)) {
            return false;
        }
        
        List<String> booked = bookedSeats.get(showingId);
        return booked.removeAll(seats);
    }
    
    /**
     * Checks if seats are available
     */
    public boolean areSeatsAvailable(int movieId, LocalDateTime showtime, List<String> seats) {
        int showingId = generateShowingId(movieId, showtime);
        List<String> booked = bookedSeats.getOrDefault(showingId, new ArrayList<>());
        
        for (String seat : seats) {
            if (booked.contains(seat)) {
                return false; // At least one seat is already booked
            }
        }
        
        return true;
    }
    
    /**
     * Gets booked seats count for a movie showing
     */
    public int getBookedSeatsCount(int movieId, LocalDateTime showtime) {
        int showingId = generateShowingId(movieId, showtime);
        return bookedSeats.getOrDefault(showingId, new ArrayList<>()).size();
    }
    
    /**
     * Gets total seats count
     */
    public int getTotalSeatsCount() {
        return generateAllSeats().size();
    }
    
    /**
     * Gets occupancy percentage for a movie showing
     */
    public double getOccupancyPercentage(int movieId, LocalDateTime showtime) {
        int booked = getBookedSeatsCount(movieId, showtime);
        int total = getTotalSeatsCount();
        
        return (double) booked / total * 100;
    }
    
    /**
     * Generates a unique identifier for a movie showing
     */
    private int generateShowingId(int movieId, LocalDateTime showtime) {
        return movieId * 1000000 + 
               showtime.getYear() * 10000 + 
               showtime.getMonthValue() * 100 + 
               showtime.getDayOfMonth();
    }
    
    /**
     * Generates all possible seats (e.g., A1, A2, B1, B2, etc.)
     */
    private List<String> generateAllSeats() {
        List<String> seats = new ArrayList<>();
        
        // Generate standard seats (rows A-F, columns 1-10)
        for (char row = 'A'; row <= 'F'; row++) {
            for (int col = 1; col <= 10; col++) {
                seats.add(row + String.valueOf(col));
            }
        }
        
        // Generate deluxe seats (rows G-J, columns 1-10)
        for (char row = 'G'; row <= 'J'; row++) {
            for (int col = 1; col <= 10; col++) {
                seats.add(row + String.valueOf(col));
            }
        }
        
        return seats;
    }
}
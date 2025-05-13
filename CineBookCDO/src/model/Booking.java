package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a booking in the system
 */
public class Booking {
    private String id;
    private User user;
    private Movie movie;
    private LocalDateTime showtime;
    private List<String> seats;
    private double totalAmount;
    private LocalDateTime bookingTime;
    private boolean isPaid;
    private String paymentMethod;
    
    public Booking(User user, Movie movie, LocalDateTime showtime, List<String> seats) {
        this.id = UUID.randomUUID().toString();
        this.user = user;
        this.movie = movie;
        this.showtime = showtime;
        this.seats = new ArrayList<>(seats);
        this.bookingTime = LocalDateTime.now();
        this.isPaid = false;
        calculateTotalAmount();
    }
    
    /**
     * Calculates the total amount for the booking
     */
    private void calculateTotalAmount() {
        double standardSeatPrice = 200.0; // PHP 200
        double deluxeSeatPrice = 350.0;   // PHP 350
        
        double amount = 0;
        
        // Calculate price based on seat types
        for (String seat : seats) {
            char row = seat.charAt(0);
            
            // Rows A-F are standard seats, G-J are deluxe
            if (row >= 'A' && row <= 'F') {
                amount += standardSeatPrice;
            } else if (row >= 'G' && row <= 'J') {
                amount += deluxeSeatPrice;
            }
        }
        
        this.totalAmount = amount;
    }
    
    /**
     * Processes payment for this booking
     */
    public boolean processPayment(String paymentMethod) {
        this.paymentMethod = paymentMethod;
        this.isPaid = true;
        return true;
    }
    
    // Getters and setters
    public String getId() {
        return id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Movie getMovie() {
        return movie;
    }
    
    public void setMovie(Movie movie) {
        this.movie = movie;
    }
    
    public LocalDateTime getShowtime() {
        return showtime;
    }
    
    public void setShowtime(LocalDateTime showtime) {
        this.showtime = showtime;
    }
    
    public List<String> getSeats() {
        return seats;
    }
    
    public void setSeats(List<String> seats) {
        this.seats = new ArrayList<>(seats);
        calculateTotalAmount();
    }
    
    public double getTotalAmount() {
        return totalAmount;
    }
    
    public LocalDateTime getBookingTime() {
        return bookingTime;
    }
    
    public boolean isPaid() {
        return isPaid;
    }
    
    public void setPaid(boolean paid) {
        isPaid = paid;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    /**
     * Gets formatted seats as a string (e.g., "A1, A2, B3")
     */
    public String getFormattedSeats() {
        if (seats.isEmpty()) {
            return "";
        }
        
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < seats.size(); i++) {
            builder.append(seats.get(i));
            if (i < seats.size() - 1) {
                builder.append(", ");
            }
        }
        
        return builder.toString();
    }
}
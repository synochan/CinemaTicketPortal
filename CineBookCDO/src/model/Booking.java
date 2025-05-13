package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a booking in the cinema system
 */
public class Booking {
    private int id;
    private String username;
    private Movie movie;
    private String cinema;
    private String showtime;
    private List<Seat> bookedSeats;
    private List<Snack> snacks;
    private double totalAmount;
    private String paymentMethod;
    private LocalDateTime bookingTime;
    private String bookingCode;
    private static int nextId = 1;

    public Booking(String username, Movie movie, String cinema, String showtime) {
        this.id = nextId++;
        this.username = username;
        this.movie = movie;
        this.cinema = cinema;
        this.showtime = showtime;
        this.bookedSeats = new ArrayList<>();
        this.snacks = new ArrayList<>();
        this.totalAmount = 0.0;
        this.bookingTime = LocalDateTime.now();
        this.bookingCode = generateBookingCode();
    }

    /**
     * Generates a unique booking code
     */
    private String generateBookingCode() {
        // Format: CDO-YYYYMMDD-XXXX where XXXX is the booking ID
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateStr = bookingTime.format(formatter);
        return "CDO-" + dateStr + "-" + String.format("%04d", id);
    }

    /**
     * Adds a seat to the booking
     */
    public void addSeat(Seat seat) {
        bookedSeats.add(seat);
        totalAmount += seat.getPrice();
    }

    /**
     * Removes a seat from the booking
     */
    public void removeSeat(Seat seat) {
        if (bookedSeats.remove(seat)) {
            totalAmount -= seat.getPrice();
        }
    }

    /**
     * Adds a snack to the booking
     */
    public void addSnack(Snack snack) {
        snacks.add(snack);
        totalAmount += snack.getPrice();
    }

    /**
     * Removes a snack from the booking
     */
    public void removeSnack(Snack snack) {
        if (snacks.remove(snack)) {
            totalAmount -= snack.getPrice();
        }
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Movie getMovie() {
        return movie;
    }

    public String getCinema() {
        return cinema;
    }

    public String getShowtime() {
        return showtime;
    }

    public List<Seat> getBookedSeats() {
        return bookedSeats;
    }

    public List<Snack> getSnacks() {
        return snacks;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    /**
     * Gets a formatted summary of the booking
     */
    public String getBookingSummary() {
        StringBuilder summary = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        summary.append("===== CINEBOOK CDO TICKET =====\n");
        summary.append("Booking Code: ").append(bookingCode).append("\n");
        summary.append("Movie: ").append(movie.getTitle()).append("\n");
        summary.append("Cinema: ").append(cinema).append("\n");
        summary.append("Showtime: ").append(showtime).append("\n");
        summary.append("Customer: ").append(username).append("\n");
        summary.append("Seats: ");
        
        for (int i = 0; i < bookedSeats.size(); i++) {
            summary.append(bookedSeats.get(i).getSeatId());
            if (i < bookedSeats.size() - 1) {
                summary.append(", ");
            }
        }
        summary.append("\n");
        
        if (!snacks.isEmpty()) {
            summary.append("Snacks:\n");
            for (Snack snack : snacks) {
                summary.append("- ").append(snack.getName())
                      .append(" (₱").append(String.format("%.2f", snack.getPrice())).append(")\n");
            }
        }
        
        summary.append("Total Amount: ₱").append(String.format("%.2f", totalAmount)).append("\n");
        summary.append("Payment Method: ").append(paymentMethod).append("\n");
        summary.append("Booking Time: ").append(bookingTime.format(formatter)).append("\n");
        summary.append("==============================\n");
        summary.append("QR Code: ").append(bookingCode).append(" [Simulated QR Code]\n");
        
        return summary.toString();
    }
}

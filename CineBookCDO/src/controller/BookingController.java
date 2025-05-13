package controller;

import model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for booking-related operations
 */
public class BookingController {
    private List<Booking> bookings;
    private List<Snack> availableSnacks;
    private MovieController movieController;

    public BookingController(MovieController movieController) {
        this.bookings = new ArrayList<>();
        this.availableSnacks = new ArrayList<>();
        this.movieController = movieController;
        initializeSnacks();
    }

    /**
     * Initializes available snacks
     */
    private void initializeSnacks() {
        // Add popcorn options
        availableSnacks.add(new Snack("Small Popcorn", "Popcorn", 70.0, "Small buttered popcorn"));
        availableSnacks.add(new Snack("Medium Popcorn", "Popcorn", 120.0, "Medium buttered popcorn"));
        availableSnacks.add(new Snack("Large Popcorn", "Popcorn", 160.0, "Large buttered popcorn"));
        availableSnacks.add(new Snack("Caramel Popcorn", "Popcorn", 140.0, "Sweet caramel coated popcorn"));
        
        // Add drink options
        availableSnacks.add(new Snack("Small Soda", "Drink", 50.0, "Small fountain soda"));
        availableSnacks.add(new Snack("Medium Soda", "Drink", 80.0, "Medium fountain soda"));
        availableSnacks.add(new Snack("Large Soda", "Drink", 100.0, "Large fountain soda"));
        availableSnacks.add(new Snack("Bottled Water", "Drink", 40.0, "500ml bottled water"));
        
        // Add combo options
        availableSnacks.add(new Snack("Popcorn & Soda Combo", "Combo", 180.0, "Medium popcorn with medium soda"));
        availableSnacks.add(new Snack("Family Combo", "Combo", 350.0, "Large popcorn with 2 large sodas"));
        availableSnacks.add(new Snack("Snack Platter", "Combo", 250.0, "Medium popcorn, nachos and medium soda"));
    }

    /**
     * Creates a new booking
     */
    public Booking createBooking(String username, Movie movie, String cinema, String showtime) {
        Booking booking = new Booking(username, movie, cinema, showtime);
        return booking;
    }

    /**
     * Adds seats to a booking
     */
    public void addSeatsToBooking(Booking booking, List<Seat> seats) {
        for (Seat seat : seats) {
            booking.addSeat(seat);
            movieController.bookSeat(booking.getCinema(), booking.getMovie().getTitle(), booking.getShowtime(), seat.getSeatId());
        }
    }

    /**
     * Adds snacks to a booking
     */
    public void addSnacksToBooking(Booking booking, List<Snack> snacks) {
        for (Snack snack : snacks) {
            booking.addSnack(snack);
        }
    }

    /**
     * Completes a booking with payment
     */
    public void completeBooking(Booking booking, String paymentMethod) {
        booking.setPaymentMethod(paymentMethod);
        bookings.add(booking);
    }

    /**
     * Cancels a booking and releases seats
     */
    public void cancelBooking(Booking booking) {
        for (Seat seat : booking.getBookedSeats()) {
            movieController.releaseSeat(booking.getCinema(), booking.getMovie().getTitle(), booking.getShowtime(), seat.getSeatId());
        }
        bookings.remove(booking);
    }

    /**
     * Gets all completed bookings
     */
    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings);
    }

    /**
     * Gets bookings for a specific movie
     */
    public List<Booking> getBookingsByMovie(String movieTitle) {
        List<Booking> movieBookings = new ArrayList<>();
        for (Booking booking : bookings) {
            if (booking.getMovie().getTitle().equals(movieTitle)) {
                movieBookings.add(booking);
            }
        }
        return movieBookings;
    }

    /**
     * Gets bookings for a specific cinema
     */
    public List<Booking> getBookingsByCinema(String cinemaName) {
        List<Booking> cinemaBookings = new ArrayList<>();
        for (Booking booking : bookings) {
            if (booking.getCinema().equals(cinemaName)) {
                cinemaBookings.add(booking);
            }
        }
        return cinemaBookings;
    }

    /**
     * Gets bookings for a specific user
     */
    public List<Booking> getBookingsByUser(String username) {
        List<Booking> userBookings = new ArrayList<>();
        for (Booking booking : bookings) {
            if (booking.getUsername().equals(username)) {
                userBookings.add(booking);
            }
        }
        return userBookings;
    }

    /**
     * Gets all available snacks
     */
    public List<Snack> getAvailableSnacks() {
        return new ArrayList<>(availableSnacks);
    }

    /**
     * Gets snacks by category
     */
    public List<Snack> getSnacksByCategory(String category) {
        List<Snack> categorySnacks = new ArrayList<>();
        for (Snack snack : availableSnacks) {
            if (snack.getCategory().equals(category)) {
                categorySnacks.add(snack);
            }
        }
        return categorySnacks;
    }

    /**
     * Adds a new snack to available snacks
     */
    public void addSnack(Snack snack) {
        availableSnacks.add(snack);
    }

    /**
     * Removes a snack from available snacks
     */
    public void removeSnack(int snackId) {
        Snack snackToRemove = null;
        for (Snack snack : availableSnacks) {
            if (snack.getId() == snackId) {
                snackToRemove = snack;
                break;
            }
        }
        
        if (snackToRemove != null) {
            availableSnacks.remove(snackToRemove);
        }
    }

    /**
     * Gets a snack by ID
     */
    public Snack getSnackById(int id) {
        for (Snack snack : availableSnacks) {
            if (snack.getId() == id) {
                return snack;
            }
        }
        return null;
    }
}

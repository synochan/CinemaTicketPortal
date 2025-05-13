package controller;

import model.Booking;
import model.Movie;
import model.Seat;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class for generating reports
 */
public class ReportController {
    private BookingController bookingController;
    private MovieController movieController;

    public ReportController(BookingController bookingController, MovieController movieController) {
        this.bookingController = bookingController;
        this.movieController = movieController;
    }

    /**
     * Generates a report of tickets sold per movie
     */
    public Map<String, Integer> generateTicketsPerMovieReport() {
        List<Booking> allBookings = bookingController.getAllBookings();
        Map<String, Integer> ticketsPerMovie = new HashMap<>();
        
        for (Booking booking : allBookings) {
            String movieTitle = booking.getMovie().getTitle();
            int numTickets = booking.getBookedSeats().size();
            
            ticketsPerMovie.put(movieTitle, ticketsPerMovie.getOrDefault(movieTitle, 0) + numTickets);
        }
        
        return ticketsPerMovie;
    }

    /**
     * Generates a report of tickets sold per cinema
     */
    public Map<String, Integer> generateTicketsPerCinemaReport() {
        List<Booking> allBookings = bookingController.getAllBookings();
        Map<String, Integer> ticketsPerCinema = new HashMap<>();
        
        for (Booking booking : allBookings) {
            String cinemaName = booking.getCinema();
            int numTickets = booking.getBookedSeats().size();
            
            ticketsPerCinema.put(cinemaName, ticketsPerCinema.getOrDefault(cinemaName, 0) + numTickets);
        }
        
        return ticketsPerCinema;
    }

    /**
     * Generates a report of revenue per movie
     */
    public Map<String, Double> generateRevenuePerMovieReport() {
        List<Booking> allBookings = bookingController.getAllBookings();
        Map<String, Double> revenuePerMovie = new HashMap<>();
        
        for (Booking booking : allBookings) {
            String movieTitle = booking.getMovie().getTitle();
            double movieRevenue = 0.0;
            
            // Add seat prices
            for (Seat seat : booking.getBookedSeats()) {
                movieRevenue += seat.getPrice();
            }
            
            revenuePerMovie.put(movieTitle, revenuePerMovie.getOrDefault(movieTitle, 0.0) + movieRevenue);
        }
        
        return revenuePerMovie;
    }

    /**
     * Generates a report of revenue per cinema
     */
    public Map<String, Double> generateRevenuePerCinemaReport() {
        List<Booking> allBookings = bookingController.getAllBookings();
        Map<String, Double> revenuePerCinema = new HashMap<>();
        
        for (Booking booking : allBookings) {
            String cinemaName = booking.getCinema();
            
            revenuePerCinema.put(cinemaName, revenuePerCinema.getOrDefault(cinemaName, 0.0) + booking.getTotalAmount());
        }
        
        return revenuePerCinema;
    }

    /**
     * Generates a report of seat occupancy per movie
     */
    public Map<String, Double> generateSeatOccupancyReport() {
        List<Movie> allMovies = movieController.getAllMovies();
        Map<String, Double> occupancyPerMovie = new HashMap<>();
        
        for (Movie movie : allMovies) {
            int totalSeats = 0;
            int bookedSeats = 0;
            
            for (String cinemaName : movie.getCinemas()) {
                for (String showtime : movie.getShowtimes()) {
                    List<Seat> availableSeats = movieController.getAvailableSeats(cinemaName, movie.getTitle(), showtime);
                    List<Seat> booked = movieController.getBookedSeats(cinemaName, movie.getTitle(), showtime);
                    
                    totalSeats += availableSeats.size() + booked.size();
                    bookedSeats += booked.size();
                }
            }
            
            if (totalSeats > 0) {
                double occupancyRate = (double) bookedSeats / totalSeats * 100.0;
                occupancyPerMovie.put(movie.getTitle(), occupancyRate);
            } else {
                occupancyPerMovie.put(movie.getTitle(), 0.0);
            }
        }
        
        return occupancyPerMovie;
    }

    /**
     * Exports a report to a text file
     */
    public boolean exportReportToFile(String reportType, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = LocalDateTime.now().format(formatter);
            
            writer.write("CineBook CDO - " + reportType + " Report");
            writer.newLine();
            writer.write("Generated on: " + timestamp);
            writer.newLine();
            writer.write("---------------------------------------------------------");
            writer.newLine();
            
            switch (reportType) {
                case "Tickets Per Movie":
                    Map<String, Integer> ticketsPerMovie = generateTicketsPerMovieReport();
                    writer.write("TICKETS SOLD PER MOVIE:");
                    writer.newLine();
                    
                    for (Map.Entry<String, Integer> entry : ticketsPerMovie.entrySet()) {
                        writer.write(entry.getKey() + ": " + entry.getValue() + " tickets");
                        writer.newLine();
                    }
                    break;
                    
                case "Tickets Per Cinema":
                    Map<String, Integer> ticketsPerCinema = generateTicketsPerCinemaReport();
                    writer.write("TICKETS SOLD PER CINEMA:");
                    writer.newLine();
                    
                    for (Map.Entry<String, Integer> entry : ticketsPerCinema.entrySet()) {
                        writer.write(entry.getKey() + ": " + entry.getValue() + " tickets");
                        writer.newLine();
                    }
                    break;
                    
                case "Revenue Per Movie":
                    Map<String, Double> revenuePerMovie = generateRevenuePerMovieReport();
                    writer.write("REVENUE PER MOVIE:");
                    writer.newLine();
                    
                    for (Map.Entry<String, Double> entry : revenuePerMovie.entrySet()) {
                        writer.write(entry.getKey() + ": ₱" + String.format("%.2f", entry.getValue()));
                        writer.newLine();
                    }
                    break;
                    
                case "Revenue Per Cinema":
                    Map<String, Double> revenuePerCinema = generateRevenuePerCinemaReport();
                    writer.write("REVENUE PER CINEMA:");
                    writer.newLine();
                    
                    for (Map.Entry<String, Double> entry : revenuePerCinema.entrySet()) {
                        writer.write(entry.getKey() + ": ₱" + String.format("%.2f", entry.getValue()));
                        writer.newLine();
                    }
                    break;
                    
                case "Seat Occupancy":
                    Map<String, Double> occupancyPerMovie = generateSeatOccupancyReport();
                    writer.write("SEAT OCCUPANCY PER MOVIE:");
                    writer.newLine();
                    
                    for (Map.Entry<String, Double> entry : occupancyPerMovie.entrySet()) {
                        writer.write(entry.getKey() + ": " + String.format("%.2f", entry.getValue()) + "%");
                        writer.newLine();
                    }
                    break;
                    
                default:
                    writer.write("Unknown report type.");
                    break;
            }
            
            writer.write("---------------------------------------------------------");
            writer.newLine();
            writer.write("End of Report");
            
            return true;
        } catch (IOException e) {
            System.err.println("Error exporting report: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets a string representation of a report
     */
    public String getReportAsString(String reportType) {
        StringBuilder report = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = LocalDateTime.now().format(formatter);
        
        report.append("CineBook CDO - ").append(reportType).append(" Report\n");
        report.append("Generated on: ").append(timestamp).append("\n");
        report.append("---------------------------------------------------------\n");
        
        switch (reportType) {
            case "Tickets Per Movie":
                Map<String, Integer> ticketsPerMovie = generateTicketsPerMovieReport();
                report.append("TICKETS SOLD PER MOVIE:\n");
                
                for (Map.Entry<String, Integer> entry : ticketsPerMovie.entrySet()) {
                    report.append(entry.getKey()).append(": ").append(entry.getValue()).append(" tickets\n");
                }
                break;
                
            case "Tickets Per Cinema":
                Map<String, Integer> ticketsPerCinema = generateTicketsPerCinemaReport();
                report.append("TICKETS SOLD PER CINEMA:\n");
                
                for (Map.Entry<String, Integer> entry : ticketsPerCinema.entrySet()) {
                    report.append(entry.getKey()).append(": ").append(entry.getValue()).append(" tickets\n");
                }
                break;
                
            case "Revenue Per Movie":
                Map<String, Double> revenuePerMovie = generateRevenuePerMovieReport();
                report.append("REVENUE PER MOVIE:\n");
                
                for (Map.Entry<String, Double> entry : revenuePerMovie.entrySet()) {
                    report.append(entry.getKey()).append(": ₱").append(String.format("%.2f", entry.getValue())).append("\n");
                }
                break;
                
            case "Revenue Per Cinema":
                Map<String, Double> revenuePerCinema = generateRevenuePerCinemaReport();
                report.append("REVENUE PER CINEMA:\n");
                
                for (Map.Entry<String, Double> entry : revenuePerCinema.entrySet()) {
                    report.append(entry.getKey()).append(": ₱").append(String.format("%.2f", entry.getValue())).append("\n");
                }
                break;
                
            case "Seat Occupancy":
                Map<String, Double> occupancyPerMovie = generateSeatOccupancyReport();
                report.append("SEAT OCCUPANCY PER MOVIE:\n");
                
                for (Map.Entry<String, Double> entry : occupancyPerMovie.entrySet()) {
                    report.append(entry.getKey()).append(": ").append(String.format("%.2f", entry.getValue())).append("%\n");
                }
                break;
                
            default:
                report.append("Unknown report type.");
                break;
        }
        
        report.append("---------------------------------------------------------\n");
        report.append("End of Report");
        
        return report.toString();
    }
}

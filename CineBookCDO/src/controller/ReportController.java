package controller;

import model.Movie;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class for report-related operations
 */
public class ReportController {
    private BookingController bookingController;
    private MovieController movieController;
    
    public ReportController(BookingController bookingController, MovieController movieController) {
        this.bookingController = bookingController;
        this.movieController = movieController;
    }
    
    /**
     * Gets ticket sales report
     */
    public Map<Movie, Integer> getTicketSalesReport() {
        Map<Movie, Integer> salesReport = new HashMap<>();
        List<Movie> movies = movieController.getAllMovies();
        
        for (Movie movie : movies) {
            int totalSales = 0;
            
            for (LocalDateTime showtime : movie.getShowtimes()) {
                totalSales += bookingController.getBookedSeatsCount(movie.getId(), showtime);
            }
            
            salesReport.put(movie, totalSales);
        }
        
        return salesReport;
    }
    
    /**
     * Gets revenue report
     */
    public Map<Movie, Double> getRevenueReport() {
        Map<Movie, Double> revenueReport = new HashMap<>();
        Map<Movie, Integer> salesReport = getTicketSalesReport();
        
        // Assume standard ticket price is 200 pesos
        final double TICKET_PRICE = 200.0;
        
        for (Map.Entry<Movie, Integer> entry : salesReport.entrySet()) {
            Movie movie = entry.getKey();
            int ticketsSold = entry.getValue();
            double revenue = ticketsSold * TICKET_PRICE;
            
            revenueReport.put(movie, revenue);
        }
        
        return revenueReport;
    }
    
    /**
     * Gets occupancy report
     */
    public Map<Movie, Double> getOccupancyReport() {
        Map<Movie, Double> occupancyReport = new HashMap<>();
        List<Movie> movies = movieController.getAllMovies();
        
        for (Movie movie : movies) {
            double totalOccupancy = 0;
            int showtimeCount = movie.getShowtimes().size();
            
            if (showtimeCount > 0) {
                for (LocalDateTime showtime : movie.getShowtimes()) {
                    totalOccupancy += bookingController.getOccupancyPercentage(movie.getId(), showtime);
                }
                
                double averageOccupancy = totalOccupancy / showtimeCount;
                occupancyReport.put(movie, averageOccupancy);
            } else {
                occupancyReport.put(movie, 0.0);
            }
        }
        
        return occupancyReport;
    }
    
    /**
     * Gets total revenue
     */
    public double getTotalRevenue() {
        Map<Movie, Double> revenueReport = getRevenueReport();
        double totalRevenue = 0;
        
        for (double revenue : revenueReport.values()) {
            totalRevenue += revenue;
        }
        
        return totalRevenue;
    }
    
    /**
     * Gets total tickets sold
     */
    public int getTotalTicketsSold() {
        Map<Movie, Integer> salesReport = getTicketSalesReport();
        int totalTickets = 0;
        
        for (int tickets : salesReport.values()) {
            totalTickets += tickets;
        }
        
        return totalTickets;
    }
    
    /**
     * Gets average occupancy percentage
     */
    public double getAverageOccupancy() {
        Map<Movie, Double> occupancyReport = getOccupancyReport();
        double totalOccupancy = 0;
        
        for (double occupancy : occupancyReport.values()) {
            totalOccupancy += occupancy;
        }
        
        return totalOccupancy / occupancyReport.size();
    }
}
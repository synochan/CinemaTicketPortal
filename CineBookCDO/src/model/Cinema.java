package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a cinema in the booking system
 */
public class Cinema {
    private int id;
    private String name;
    private int rows;
    private int columns;
    private List<Seat> seats;
    private static int nextId = 1;

    public Cinema(String name, int rows, int columns) {
        this.id = nextId++;
        this.name = name;
        this.rows = rows;
        this.columns = columns;
        this.seats = generateSeats(rows, columns);
    }

    /**
     * Generates a grid of seats for the cinema
     */
    private List<Seat> generateSeats(int rows, int columns) {
        List<Seat> generatedSeats = new ArrayList<>();
        
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                // Last two rows are deluxe seats
                String seatType = (row >= rows - 2) ? "Deluxe" : "Standard";
                String seatId = (char) ('A' + row) + String.valueOf(col + 1);
                double price = (seatType.equals("Deluxe")) ? 250.0 : 180.0;
                
                Seat seat = new Seat(seatId, seatType, row, col, price);
                generatedSeats.add(seat);
            }
        }
        
        return generatedSeats;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    /**
     * Resets all seats to available
     */
    public void resetSeats() {
        for (Seat seat : seats) {
            seat.setAvailable(true);
        }
    }

    /**
     * Gets a specific seat by row and column
     */
    public Seat getSeat(int row, int col) {
        for (Seat seat : seats) {
            if (seat.getRow() == row && seat.getColumn() == col) {
                return seat;
            }
        }
        return null;
    }

    /**
     * Gets a specific seat by seat ID (e.g., "A1", "B3")
     */
    public Seat getSeatById(String seatId) {
        for (Seat seat : seats) {
            if (seat.getSeatId().equals(seatId)) {
                return seat;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return name;
    }
}

package model;

/**
 * Represents a seat in a cinema
 */
public class Seat {
    private String seatId; // Example: "A1", "B5", etc.
    private String type; // "Standard" or "Deluxe"
    private int row;
    private int column;
    private boolean isAvailable;
    private double price;

    public Seat(String seatId, String type, int row, int column, double price) {
        this.seatId = seatId;
        this.type = type;
        this.row = row;
        this.column = column;
        this.isAvailable = true;
        this.price = price;
    }

    // Getters and setters
    public String getSeatId() {
        return seatId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return seatId + " (" + type + ")";
    }
}

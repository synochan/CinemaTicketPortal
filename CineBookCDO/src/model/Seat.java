package model;

/**
 * Represents a cinema seat
 */
public class Seat {
    private String id;      // e.g., "A1", "B2", etc.
    private char row;
    private int column;
    private String type;    // "Standard" or "Deluxe"
    private double price;
    private boolean isAvailable;
    
    public Seat(String id, char row, int column) {
        this.id = id;
        this.row = row;
        this.column = column;
        
        // Default values
        this.isAvailable = true;
        
        // Determine seat type and price based on row
        if (row >= 'A' && row <= 'F') {
            this.type = "Standard";
            this.price = 200.0; // PHP 200
        } else {
            this.type = "Deluxe";
            this.price = 350.0; // PHP 350
        }
    }
    
    public Seat(String id, char row, int column, String type, double price) {
        this.id = id;
        this.row = row;
        this.column = column;
        this.type = type;
        this.price = price;
        this.isAvailable = true;
    }
    
    // Getters and setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public char getRow() {
        return row;
    }
    
    public void setRow(char row) {
        this.row = row;
    }
    
    public int getColumn() {
        return column;
    }
    
    public void setColumn(int column) {
        this.column = column;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public boolean isAvailable() {
        return isAvailable;
    }
    
    public void setAvailable(boolean available) {
        isAvailable = available;
    }
    
    @Override
    public String toString() {
        return id;
    }
}
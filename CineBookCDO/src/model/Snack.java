package model;

/**
 * Represents a snack item available for purchase
 */
public class Snack {
    private int id;
    private String name;
    private String category; // "Popcorn", "Drink", "Combo"
    private double price;
    private String description;
    private static int nextId = 1;

    public Snack(String name, String category, double price, String description) {
        this.id = nextId++;
        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name + " - ₱" + String.format("%.2f", price);
    }
}

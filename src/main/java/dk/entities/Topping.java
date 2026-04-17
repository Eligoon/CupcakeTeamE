package dk.entities;

public class Topping {
    private int toppingId;
    private String name;
    private double price;

    public Topping(int toppingId, String name, double price) {
        this.toppingId = toppingId;
        this.name = name;
        this.price = price;
    }

    public int getToppingId() {
        return toppingId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}
package dk.entities;

public class Cupcake {
    private int cupcakeId;
    private int bottomId;
    private int toppingId;
    private String bottomName;
    private double bottomPrice;
    private String toppingName;
    private double toppingPrice;

    public Cupcake(int cupcakeId, int bottomId, int toppingId, String bottomName, double bottomPrice, String toppingName, double toppingPrice) {
        this.cupcakeId = cupcakeId;
        this.bottomId = bottomId;
        this.toppingId = toppingId;
        this.bottomName = bottomName;
        this.bottomPrice = bottomPrice;
        this.toppingName = toppingName;
        this.toppingPrice = toppingPrice;
    }

    public int getCupcakeId() {
        return cupcakeId;
    }

    public int getBottomId() {
        return bottomId;
    }

    public int getToppingId() {
        return toppingId;
    }

    public String getBottomName() {
        return bottomName;
    }

    public double getBottomPrice() {
        return bottomPrice;
    }

    public String getToppingName() {
        return toppingName;
    }

    public double getToppingPrice() {
        return toppingPrice;
    }

    public String getName() {
        return bottomName + " • " + toppingName;
    }

    public double getPrice() {
        return bottomPrice + toppingPrice;
    }
}
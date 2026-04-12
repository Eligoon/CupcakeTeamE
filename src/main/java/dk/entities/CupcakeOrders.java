package dk.entities;

public class CupcakeOrders {

    private int id;
    private int cupcakeId;
    private int orderId;
    private int quantity;
    private double unitPrice;

    private String bottomName;
    private String toppingName;

    public CupcakeOrders(int id, int cupcakeId, int orderId, int quantity, double unitPrice,
                         String bottomName, String toppingName) {
        this.id = id;
        this.cupcakeId = cupcakeId;
        this.orderId = orderId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.bottomName = bottomName;
        this.toppingName = toppingName;
    }

    public int getId() {
        return id;
    }

    public int getCupcakeId() {
        return cupcakeId;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public String getBottomName() {
        return bottomName;
    }

    public String getToppingName() {
        return toppingName;
    }
}
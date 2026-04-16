package dk.entities;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private String customerName;
    private double total;
    private int orderId;
    private int userId;
    private LocalDateTime createdAt;
    private String status;
    private List<CupcakeOrders> lines;

    public Order(String customerName, double total, int orderId, int userId, LocalDateTime createdAt, String status, List<CupcakeOrders> lines) {
        this.customerName = customerName;
        this.total = total;
        this.orderId = orderId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.status = status;
        this.lines = lines;
    }

    public String getCustomerName() {
        return customerName;
    }

    public double getTotal() {
        return total;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getUserId() {
        return userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getStatus() {
        return status;
    }

    public List<CupcakeOrders> getLines() {
        return lines;
    }

    public void setLines(List<CupcakeOrders> lines) {
        this.lines = lines;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}

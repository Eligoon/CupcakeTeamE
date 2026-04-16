package dk.entities;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private int orderId;
    private int userId;
    private LocalDateTime createdAt;
    private String status;

    public Order(int orderId, int userId, LocalDateTime createdAt, String status) {
        this.orderId = orderId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.status = status;
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
}

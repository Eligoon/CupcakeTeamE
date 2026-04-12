package dk.services;

import dk.persistence.*;
import dk.exceptions.DatabaseException;

public class OrderService {

    public static int createOrder(int userId, ConnectionPool cp)
            throws DatabaseException {
        return OrderMapper.createOrder(userId, cp);
    }

    public static void addCupcakeToOrder(int orderId, int cupcakeId, int quantity, double unitPrice, ConnectionPool cp)
            throws DatabaseException {
        OrderMapper.addCupcakeToOrder(orderId, cupcakeId, quantity, unitPrice, cp);
    }

    public static double calculateTotal(int orderId, ConnectionPool cp)
            throws DatabaseException {
        return OrderMapper.calculateTotal(orderId, cp);
    }

    public static void payOrder(int orderId, int userId, double total, ConnectionPool cp)
            throws DatabaseException {

        if (OrderMapper.isPaid(orderId, cp)) {
            throw new RuntimeException("Order already paid");
        }

        double balance = AccountMapper.getBalance(userId, cp);

        if (balance < total) {
            throw new RuntimeException("Not enough money");
        }

        AccountMapper.subtractMoney(userId, total, cp);
        OrderMapper.markAsPaid(orderId, cp);
    }
}
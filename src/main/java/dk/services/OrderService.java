package dk.services;

import dk.persistence.*;
import dk.exceptions.DatabaseException;

public class OrderService {

    public static void payOrder(int orderId, int userId, double total, ConnectionPool cp)
            throws DatabaseException {

        double balance = AccountMapper.getBalance(userId, cp);

        if (balance < total) {
            throw new RuntimeException("Not enough money");
        }

        AccountMapper.subtractMoney(userId, total, cp);

        OrderMapper.markAsPaid(orderId, cp);
    }
}

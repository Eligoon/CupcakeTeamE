package dk.services;

import dk.entities.CupcakeOrders;
import dk.exceptions.DatabaseException;
import dk.persistence.ConnectionPool;
import dk.persistence.CupcakeOrdersMapper;

import java.util.List;

public class CartService {

    public static List<CupcakeOrders> getCart(int orderId, ConnectionPool cp)
            throws DatabaseException {

        return CupcakeOrdersMapper.getOrdersByOrderId(orderId, cp);
    }

    public static double calculateTotal(int orderId, ConnectionPool cp)
            throws DatabaseException {

        return CupcakeOrdersMapper.calculateTotal(orderId, cp);
    }
}
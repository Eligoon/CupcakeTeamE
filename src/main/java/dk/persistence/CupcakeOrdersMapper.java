package dk.persistence;

import dk.entities.CupcakeOrders;
import dk.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CupcakeOrdersMapper {

    public static List<CupcakeOrders> getOrdersByOrderId(int orderId, ConnectionPool connectionPool)
            throws DatabaseException {

        List<CupcakeOrders> items = new ArrayList<>();

        String sql = """
            SELECT id, cupcake_id, order_id, quantity, unit_price
            FROM public.cupcakes_orders
            WHERE order_id = ?
        """;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                items.add(new CupcakeOrders(
                        rs.getInt("id"),
                        rs.getInt("cupcake_id"),
                        rs.getInt("order_id"),
                        rs.getInt("quantity"),
                        rs.getDouble("unit_price")
                ));
            }

            return items;

        } catch (SQLException e) {
            throw new DatabaseException("Error fetching order items", e.getMessage());
        }
    }
}
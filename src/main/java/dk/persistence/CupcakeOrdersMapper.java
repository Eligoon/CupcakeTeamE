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
        SELECT 
            co.id,
            co.cupcake_id,
            co.order_id,
            co.quantity,
            co.unit_price,
            b.name AS bottom_name,
            t.name AS topping_name
        FROM public.cupcakes_orders co
        JOIN public.cupcakes c ON co.cupcake_id = c.cupcake_id
        JOIN public.bottoms b ON c.bottom_id = b.bottom_id
        JOIN public.toppings t ON c.topping_id = t.topping_id
        WHERE co.order_id = ?
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
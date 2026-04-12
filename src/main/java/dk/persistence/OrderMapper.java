package dk.persistence;

import dk.entities.Order;
import dk.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper {

    public static List<Order> getOrdersByUser(int userId, ConnectionPool connectionPool)
            throws DatabaseException {

        List<Order> orders = new ArrayList<>();

        String sql = """
            SELECT order_id, user_id, created_at, status
            FROM public.orders
            WHERE user_id = ?
        """;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                orders.add(new Order(
                        rs.getInt("order_id"),
                        rs.getInt("user_id"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getString("status")
                ));
            }

            return orders;

        } catch (SQLException e) {
            throw new DatabaseException("Error fetching orders", e.getMessage());
        }
    }

    public static void markAsPaid(int orderId, ConnectionPool connectionPool)
            throws DatabaseException {

        String sql = """
        UPDATE public.orders
        SET status = 'paid'
        WHERE order_id = ?
    """;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Error marking order as paid", e.getMessage());
        }
    }
}
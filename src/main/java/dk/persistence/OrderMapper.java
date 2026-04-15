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

    public static List<Order> getAllOrders(ConnectionPool connectionPool)
            throws DatabaseException {

        List<Order> orders = new ArrayList<>();

        String sql = """
            SELECT order_id, user_id, created_at, status
            FROM public.orders
        """;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

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
            throw new DatabaseException("Error fetching all orders", e.getMessage());
        }
    }

    public static int createOrder(int userId, ConnectionPool connectionPool)
            throws DatabaseException {

        String sql = """
            INSERT INTO public.orders (user_id, status)
            VALUES (?, 'cart')
            RETURNING order_id
        """;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("order_id");
            }

            throw new DatabaseException("Could not create order");

        } catch (SQLException e) {
            throw new DatabaseException("Error creating order", e.getMessage());
        }
    }


    public static void addCupcakeToOrder(int orderId, int cupcakeId, int quantity, ConnectionPool connectionPool)
            throws DatabaseException {

        String sql = """
            INSERT INTO public.cupcakes_orders (order_id, cupcake_id, quantity, unit_price)
            SELECT ?, ?, ?, (b.price + t.price)
            FROM public.cupcakes c
            JOIN public.bottoms b ON c.bottom_id = b.bottom_id
            JOIN public.toppings t ON c.topping_id = t.topping_id
            WHERE c.cupcake_id = ?
        """;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ps.setInt(2, cupcakeId);
            ps.setInt(3, quantity);
            ps.setInt(4, cupcakeId);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Error adding cupcake to order", e.getMessage());
        }
    }

    public static double calculateTotal(int orderId, ConnectionPool connectionPool)
            throws DatabaseException {

        String sql = """
            SELECT SUM(quantity * unit_price) AS total
            FROM public.cupcakes_orders
            WHERE order_id = ?
        """;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("total");
            }

            return 0;

        } catch (SQLException e) {
            throw new DatabaseException("Error calculating total", e.getMessage());
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

    public static boolean isPaid(int orderId, ConnectionPool connectionPool)
            throws DatabaseException {

        String sql = """
            SELECT status
            FROM public.orders
            WHERE order_id = ?
        """;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return "paid".equals(rs.getString("status"));
            }

            throw new DatabaseException("Order not found");

        } catch (SQLException e) {
            throw new DatabaseException("Error checking order status", e.getMessage());
        }
    }
}
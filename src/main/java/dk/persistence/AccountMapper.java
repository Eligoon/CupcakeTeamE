package dk.persistence;

import dk.exceptions.DatabaseException;

import java.sql.*;

public class AccountMapper {

    public static double getBalance(int userId, ConnectionPool connectionPool)
            throws DatabaseException {

        String sql = "SELECT balance FROM public.accounts WHERE user_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("balance");
            }

            return 0;

        } catch (SQLException e) {
            throw new DatabaseException("Error getting balance", e.getMessage());
        }
    }

    public static void addMoney(int userId, double amount, ConnectionPool connectionPool)
            throws DatabaseException {

        String sql = """
            UPDATE public.accounts
            SET balance = balance + ?
            WHERE user_id = ?
        """;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setDouble(1, amount);
            ps.setInt(2, userId);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Error adding money", e.getMessage());
        }
    }

    public static void subtractMoney(int userId, double amount, ConnectionPool connectionPool)
            throws DatabaseException {

        String sql = """
            UPDATE public.accounts
            SET balance = balance - ?
            WHERE user_id = ?
        """;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setDouble(1, amount);
            ps.setInt(2, userId);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Error subtracting money", e.getMessage());
        }
    }
}
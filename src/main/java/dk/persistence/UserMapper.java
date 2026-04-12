package dk.persistence;

import dk.entities.User;
import dk.exceptions.DatabaseException;

import java.sql.*;

public class UserMapper {

    public static User getUserByEmail(String email, ConnectionPool connectionPool)
            throws DatabaseException {

        String sql = """
            SELECT user_id, email, password_hash, role
            FROM public.users
            WHERE email = ?
        """;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("user_id"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getString("role")
                );
            }

            return null;

        } catch (SQLException e) {
            throw new DatabaseException("Error finding user", e.getMessage());
        }
    }

    public static void createUser(User user, ConnectionPool connectionPool)
            throws DatabaseException {

        String sql = """
            INSERT INTO public.users (email, password_hash, role)
            VALUES (?, ?, ?)
        """;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getRole());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Error creating user", e.getMessage());
        }
    }
}
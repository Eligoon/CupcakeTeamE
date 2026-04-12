package dk.persistence;

import dk.entities.Cupcake;
import dk.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CupcakeMapper {

    public static List<Cupcake> getAllCupcakes(ConnectionPool connectionPool)
            throws DatabaseException {

        List<Cupcake> cupcakes = new ArrayList<>();

        String sql = """
            SELECT cupcake_id, bottom_id, topping_id
            FROM public.cupcakes
        """;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                cupcakes.add(new Cupcake(
                        rs.getInt("cupcake_id"),
                        rs.getInt("bottom_id"),
                        rs.getInt("topping_id")
                ));
            }

            return cupcakes;

        } catch (SQLException e) {
            throw new DatabaseException("Error fetching cupcakes", e.getMessage());
        }
    }
}
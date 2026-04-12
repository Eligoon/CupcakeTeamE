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
        SELECT 
            c.cupcake_id,
            c.bottom_id,
            c.topping_id,
            b.name AS bottom_name,
            b.price AS bottom_price,
            t.name AS topping_name,
            t.price AS topping_price
        FROM public.cupcakes c
        JOIN public.bottoms b ON c.bottom_id = b.bottom_id
        JOIN public.toppings t ON c.topping_id = t.topping_id
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
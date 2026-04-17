package dk.persistence;

import dk.entities.Bottom;
import dk.entities.Topping;
import dk.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ToppingMapper {

    public static List<Topping> getAllToppings(ConnectionPool cp) throws DatabaseException {
        List<Topping> toppings = new ArrayList<>();

        String sql = "SELECT topping_id, name, price FROM public.toppings";

        try (Connection conn = cp.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                toppings.add(new Topping(
                        rs.getInt("topping_id"),
                        rs.getString("name"),
                        rs.getDouble("price")
                ));
            }

            return toppings;
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching toppings", e.getMessage());
        }
    }
}

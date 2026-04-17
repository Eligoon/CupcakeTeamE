package dk.persistence;

import dk.entities.Bottom;
import dk.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BottomMapper {

    public static List<Bottom> getAllBottoms(ConnectionPool cp) throws DatabaseException {
        List<Bottom> bottoms = new ArrayList<>();

        String sql = "SELECT bottom_id, name, price FROM public.bottoms";

        try (Connection conn = cp.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                bottoms.add(new Bottom(
                        rs.getInt("bottom_id"),
                        rs.getString("name"),
                        rs.getDouble("price")
                ));
            }

            return bottoms;
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching bottoms", e.getMessage());
        }
    }
}

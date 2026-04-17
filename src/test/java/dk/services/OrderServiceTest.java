package dk.services;

import dk.TestConfig;
import dk.persistence.ConnectionPool;
import dk.persistence.UserMapper;
import dk.entities.User;
import dk.utils.PasswordHasher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class OrderServiceTest {

    private ConnectionPool cp;

    @BeforeEach
    void setup() throws Exception {

        cp = TestConfig.getTestConnectionPool();

        try (Connection con = cp.getConnection();
             Statement st = con.createStatement()) {

            st.execute("DELETE FROM orders");
            st.execute("DELETE FROM users");
        }

        User user = new User(
                0,
                "user@test.dk",
                PasswordHasher.hash("1234"),
                "admin"
        );

        UserMapper.createUser(user, cp);
    }

    @Test
    void payOrder_notEnoughMoney() throws Exception {

        User created = UserMapper.getUserByEmail("user@test.dk", cp);
        int userId = created.getUserId();

        int orderId = OrderService.createOrder(userId, cp);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            OrderService.payOrder(orderId, userId, 99999, cp);
        });

        assertEquals("Not enough money", exception.getMessage());
    }
}
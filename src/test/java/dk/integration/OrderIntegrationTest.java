package dk.integration;

import dk.TestConfig;
import dk.entities.User;
import dk.persistence.ConnectionPool;
import dk.persistence.OrderMapper;
import dk.persistence.UserMapper;
import dk.services.OrderService;
import dk.services.UserService;
import dk.persistence.AccountMapper;
import dk.exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class OrderIntegrationTest {

    private ConnectionPool cp;
    private int userId;

    @BeforeEach
    void setup() throws Exception {

        cp = TestConfig.getTestConnectionPool();

        try (Connection con = cp.getConnection();
             Statement st = con.createStatement()) {

            st.execute("DELETE FROM cupcakes_orders");
            st.execute("DELETE FROM orders");
            st.execute("DELETE FROM users");
            st.execute("DELETE FROM accounts");
        }

        UserService.registerUser("user@test.dk", "1234", "admin", "first_name", "last_name",cp);

        User created = UserMapper.getUserByEmail("user@test.dk", cp);
        userId = created.getUserId();

        AccountMapper.addMoney(userId, 1000, cp);
    }

    @Test
    void fullOrderFlow() throws Exception {

        // 1. create order
        int orderId = OrderService.createOrder(userId, cp);
        assertTrue(orderId > 0);

        // 2. add cupcakes
        OrderService.addCupcakeToOrder(orderId, 1, 2, cp);

        // 3. calculate total
        double total = OrderService.calculateTotal(orderId, cp);
        assertTrue(total > 0);

        // 4. pay order
        OrderService.payOrder(orderId, userId, total, cp);

        // 5. verify paid
        assertTrue(OrderMapper.isPaid(orderId, cp));
    }
}
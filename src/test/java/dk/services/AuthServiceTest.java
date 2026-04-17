package dk.services;

import dk.TestConfig;
import dk.entities.User;
import dk.persistence.ConnectionPool;
import dk.persistence.UserMapper;
import dk.utils.PasswordHasher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    private ConnectionPool cp;

    @BeforeEach
    void setup() throws Exception {
        cp = TestConfig.getTestConnectionPool();

        // Clean DB so tests are independent
        try (Connection con = cp.getConnection();
             Statement st = con.createStatement()) {

            st.execute("DELETE FROM public.users");
        }

        // Insert test user for success + wrong password test
        String email = "user@cupcake.dk";
        String rawPassword = "1234";
        String hashedPassword = PasswordHasher.hash(rawPassword);

        User testUser = new User(
                0,
                email,
                hashedPassword,
                "admin"
        );

        UserMapper.createUser(testUser, cp);
    }

    @Test
    void login_success() throws Exception {

        User user = AuthService.login("user@cupcake.dk", "1234", cp);

        assertNotNull(user);
        assertEquals("user@cupcake.dk", user.getEmail());
    }

    @Test
    void login_wrongPassword() {

        Exception exception = assertThrows(Exception.class, () -> {
            AuthService.login("user@cupcake.dk", "wrong", cp);
        });

        assertEquals("Wrong password", exception.getMessage());
    }

    @Test
    void login_userNotFound() {

        Exception exception = assertThrows(Exception.class, () -> {
            AuthService.login("fake@mail.dk", "1234", cp);
        });

        assertEquals("User not found", exception.getMessage());
    }
}
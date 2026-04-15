package dk;

import dk.persistence.ConnectionPool;

public class TestConfig {

    public static ConnectionPool getTestConnectionPool() {
        return ConnectionPool.getInstance(
                "postgres",
                "postgres",
                "jdbc:postgresql://localhost:5432/%s?currentSchema=public",
                "cupcake"
        );
    }
}
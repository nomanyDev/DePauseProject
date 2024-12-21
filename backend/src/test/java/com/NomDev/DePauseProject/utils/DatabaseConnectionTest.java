package com.NomDev.DePauseProject.utils;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class DatabaseConnectionTest {

    @Test
    public void testDatabaseConnection() {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/depause_db", "root", "nc1Best*")) {
            assertNotNull(connection, "Connection should not be null");
        } catch (SQLException e) {
            fail("Connection failed: " + e.getMessage());
        }
    }
}
package lk.melodybeats.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {

    private static final String CONNECTION_URL =
            "jdbc:sqlserver://localhost:1433;"
            + "databaseName=MelodyBeat3;"
            + "integratedSecurity=true;"
            + "encrypt=true;"
            + "trustServerCertificate=true;"
            + "loginTimeout=10;";

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(CONNECTION_URL);
    }

    public static void main(String[] args) {

        try (Connection connection = getConnection()) {

            System.out.println("Database connected successfully!");
            System.out.println("Database: " + connection.getCatalog());

        } catch (SQLException exception) {

            System.err.println("Database connection failed!");
            System.err.println(exception.getMessage());
            exception.printStackTrace();
        }
    }
}
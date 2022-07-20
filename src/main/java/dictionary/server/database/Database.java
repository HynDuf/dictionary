package dictionary.server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String HOST_NAME = "localhost";
    private static final String DB_NAME = "en-vi-dictionary";
    private static final String USER_NAME = "en-vi-dictionary";
    private static final String PASSWORD = "n1-02-dictionary"; // Your MySQL password go here
    private static final String MYSQL_URL = "jdbc:mysql://" + HOST_NAME + ":3306/" + DB_NAME;

    public Database() {
        System.out.println("Connecting database...");
        try (Connection connection = DriverManager.getConnection(MYSQL_URL, USER_NAME, PASSWORD)) {
            System.out.println("Database connected!");
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }
}

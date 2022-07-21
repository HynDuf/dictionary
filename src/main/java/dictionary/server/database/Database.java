package dictionary.server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class Database {
    private static final String HOST_NAME = "localhost";
    private static final String DB_NAME = "en-vi-dictionary";
    private static final String USER_NAME = "en-vi-dictionary";
    private static final String PASSWORD = "n1-02-dictionary";
    private static final String PORT = "3306";
    private static final String MYSQL_URL =
            "jdbc:mysql://" + HOST_NAME + ":" + PORT + "/" + DB_NAME;

    private Connection connection = null;
    private String word;

    /**
     * Connect to MYSQL database.
     *
     * <p>Reference: https://stackoverflow.com/questions/2839321/connect-java-to-a-mysql-database
     */
    public void connectToDatabase() {
        System.out.println("Connecting database...");
        try {
            connection = DriverManager.getConnection(MYSQL_URL, USER_NAME, PASSWORD);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Database connected!");
    }

    public void closeDatabase() {
        close(connection);
        System.out.println("Database disconnected!");
    }

    /**
     * Close connection to MYSQL database.
     *
     * @param connection Connection variable
     */
    private static void close(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Close the PreparedStatement ps.
     *
     * @param ps PreparedStatement needed to close
     */
    private static void close(PreparedStatement ps) {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Close the ResultSet rs.
     *
     * @param rs ResultSet needed to be close
     */
    private static void close(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lookup an English `word` in database (look for the exact word `word`).
     *
     * @param word the searched word (full word)
     * @return the Vietnamese definition of `word`, if not found return "404" as a String.
     */
    public String lookUpWord(final String word) {
        final String SQL_QUERY = "SELECT definition FROM dictionary WHERE target = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(SQL_QUERY);
            ps.setString(1, word);
            try {
                ResultSet rs = ps.executeQuery();
                try {
                    if (rs.next()) {
                        return rs.getString("definition");
                    } else {
                        return "404";
                    }
                } finally {
                    close(rs);
                }
            } finally {
                close(ps);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "404";
    }

    /**
     * Insert new word to database.
     *
     * @param word English word
     * @param definition Vietnamese definition
     */
    public void insertWord(final String word, final String definition) {
        final String SQL_QUERY = "INSERT INTO dictionary (target, definition) VALUES (?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(SQL_QUERY);
            ps.setString(1, word);
            ps.setString(2, definition);
            try {
                ps.executeUpdate();
            } catch (SQLIntegrityConstraintViolationException e) {
                // `word` is already in database
                System.out.println(
                        "Cannot insert `"
                                + word
                                + "` to dictionary as `"
                                + word
                                + "` is already in the database!");
                return;
            } finally {
                close(ps);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete the word `word` from the database.
     *
     * <p>Nothing happens if `word` is not in the database for deletion.
     *
     * @param word the delete word
     */
    public void deleteWord(final String word) {
        final String SQL_QUERY = "DELETE FROM dictionary WHERE target = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(SQL_QUERY);
            ps.setString(1, word);
            try {
                ps.executeUpdate();
            } finally {
                close(ps);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update the word `word` to the according definition.
     *
     * <p>Nothing happens if `word` is not in the database for update.
     *
     * @param word the update word
     * @param definition the update definition
     */
    public void updateWordDefinition(final String word, final String definition) {
        this.word = word;
        final String SQL_QUERY = "UPDATE dictionary SET definition = ? WHERE target = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(SQL_QUERY);
            ps.setString(1, word);
            ps.setString(2, definition);
            try {
                ps.executeUpdate();
            } finally {
                close(ps);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

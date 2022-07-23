package dictionary.server.database;

import java.io.FileWriter;
import java.io.IOException;
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
     * Lookup an English word `target` in database (look for the exact word `target`).
     *
     * @param target the searched word (full word)
     * @return the Vietnamese definition of `target`, if not found return "404" as a String.
     */
    public String lookUpWord(final String target) {
        final String SQL_QUERY = "SELECT definition FROM dictionary WHERE target = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(SQL_QUERY);
            ps.setString(1, target);
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
     * @param target English word
     * @param definition Vietnamese definition
     */
    public void insertWord(final String target, final String definition) {
        final String SQL_QUERY = "INSERT INTO dictionary (target, definition) VALUES (?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(SQL_QUERY);
            ps.setString(1, target);
            ps.setString(2, definition);
            try {
                ps.executeUpdate();
            } catch (SQLIntegrityConstraintViolationException e) {
                // `word` is already in database
                System.out.println(
                        "Cannot insert `"
                                + target
                                + "` to dictionary as `"
                                + target
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
     * Delete the word `targe` from the database.
     *
     * <p>Nothing happens if `target` is not in the database for deletion.
     *
     * @param target the delete word
     */
    public void deleteWord(final String target) {
        final String SQL_QUERY = "DELETE FROM dictionary WHERE target = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(SQL_QUERY);
            ps.setString(1, target);
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
     * Update the word `target` to the according definition.
     *
     * <p>Nothing happens if `target` is not in the database for update.
     *
     * @param target the update word
     * @param definition the update definition
     */
    public void updateWordDefinition(final String target, final String definition) {
        final String SQL_QUERY = "UPDATE dictionary SET definition = ? WHERE target = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(SQL_QUERY);
            ps.setString(1, target);
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

    /**
     * Export all words to a csv file with `filename` name.
     *
     * @param filename the name of the file to export
     */
    public void exportToCsv(String filename) {
        final String SQL_QUERY = "SELECT * FROM dictionary";
        try {
            PreparedStatement ps = connection.prepareStatement(SQL_QUERY);
            try {
                ResultSet rs = ps.executeQuery();
                try {
                    FileWriter fw = new FileWriter(filename + ".csv");
                    int cols = rs.getMetaData().getColumnCount();
                    for (int i = 1; i <= cols; ++i) {
                        fw.append(rs.getMetaData().getColumnLabel(i));
                        if (i < cols) {
                            fw.append(',');
                        } else {
                            fw.append('\n');
                        }
                    }

                    while (rs.next()) {
                        for (int i = 1; i <= cols; ++i) {
                            fw.append(rs.getString(i));
                            if (i < cols) {
                                fw.append(',');
                            }
                        }
                        fw.append('\n');
                    }
                    fw.flush();
                    fw.close();
                } finally {
                    close(rs);
                }
            } finally {
                close(ps);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

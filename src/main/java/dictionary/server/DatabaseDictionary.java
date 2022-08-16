package dictionary.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

public class DatabaseDictionary extends Dictionary {
    private static final String HOST_NAME = "localhost";
    private static final String DB_NAME = "en-vi-dictionary";
    private static final String USER_NAME = "en-vi-dictionary";
    private static final String PASSWORD = "n1-02-dictionary";
    private static final String PORT = "3306";
    private static final String MYSQL_URL =
        "jdbc:mysql://" + HOST_NAME + ":" + PORT + "/" + DB_NAME;

    private static Connection connection = null;

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
     * Connect to MYSQL database.
     *
     * <p>Reference: https://stackoverflow.com/questions/2839321/connect-java-to-a-mysql-database
     */
    private void connectToDatabase() throws SQLException {
        System.out.println("Connecting to database...");
        connection = DriverManager.getConnection(MYSQL_URL, USER_NAME, PASSWORD);
        System.out.println("Database connected!\n");
    }

    /** Connect to MYSQL database. Add all words on the database into Trie data structure. */
    @Override
    public void initialize() throws SQLException {
        connectToDatabase();
        ArrayList<String> targets = getAllWordTargets();
        for (String word : targets) {
            Trie.insert(word);
        }
    }

    /** Close the Database connection. */
    @Override
    public void close() {
        close(connection);
        System.out.println("Database disconnected!");
    }

    /**
     * Lookup an English word `target` in database (look for the exact word `target`).
     *
     * @param target the searched word (full word)
     * @return the Vietnamese definition of `target`, if not found return "404" as a String.
     */
    @Override
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
     * @return true if `target` hasn't been added yet, false otherwise
     */
    @Override
    public boolean insertWord(final String target, final String definition) {
        final String SQL_QUERY = "INSERT INTO dictionary (target, definition) VALUES (?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(SQL_QUERY);
            ps.setString(1, target);
            ps.setString(2, definition);
            try {
                ps.executeUpdate();
            } catch (SQLIntegrityConstraintViolationException e) {
                // `word` is already in database
                return false;
            } finally {
                close(ps);
            }
            Trie.insert(target);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete the word `target` from the database.
     *
     * <p>Nothing happens if `target` is not in the database for deletion.
     *
     * @param target the deleted word
     * @return true if successfully delete, false otherwise
     */
    @Override
    public boolean deleteWord(final String target) {
        final String SQL_QUERY = "DELETE FROM dictionary WHERE target = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(SQL_QUERY);
            ps.setString(1, target);
            try {
                int deletedRows = ps.executeUpdate();
                if (deletedRows == 0) {
                    return false;
                }
            } finally {
                close(ps);
            }
            Trie.delete(target);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update the word `target` to the according definition.
     *
     * <p>Nothing happens if `target` is not in the database for update.
     *
     * @param target the update word
     * @param definition the update definition
     * @return true if successfully updated, false otherwise
     */
    @Override
    public boolean updateWordDefinition(final String target, final String definition) {
        final String SQL_QUERY = "UPDATE dictionary SET definition = ? WHERE target = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(SQL_QUERY);
            ps.setString(1, definition);
            ps.setString(2, target);
            try {
                int updatedRows = ps.executeUpdate();
                if (updatedRows == 0) {
                    return false;
                }
            } finally {
                close(ps);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Get all words from result set of the given SQL query.
     *
     * @param ps the SQL query included in PreparedStatement
     * @return ArrayList of Words
     * @throws SQLException exception
     */
    private ArrayList<Word> getWordsFromResultSet(PreparedStatement ps) throws SQLException {
        try {
            ResultSet rs = ps.executeQuery();
            try {
                ArrayList<Word> words = new ArrayList<>();
                while (rs.next()) {
                    words.add(new Word(rs.getString(2), rs.getString(3)));
                }
                return words;

            } finally {
                close(rs);
            }
        } finally {
            close(ps);
        }
    }

    /**
     * Get all words into an `ArrayList(Word)>`.
     *
     * @return an 'ArrayList(Word)' include all the words from the database
     */
    @Override
    public ArrayList<Word> getAllWords() {
        final String SQL_QUERY = "SELECT * FROM dictionary";
        try {
            PreparedStatement ps = connection.prepareStatement(SQL_QUERY);
            return getWordsFromResultSet(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Get all the words from the Database has `id` from `wordIndexFrom` to `wordIndexTo`.
     *
     * @param wordIndexFrom left bound
     * @param wordIndexTo right bound
     * @return an ArrayList of Word get from the database
     */
    public ArrayList<Word> getWordsPartial(int wordIndexFrom, int wordIndexTo) {
        final String SQL_QUERY = "SELECT * FROM dictionary WHERE id >= ? AND id <= ?";
        try {
            PreparedStatement ps = connection.prepareStatement(SQL_QUERY);
            ps.setInt(1, wordIndexFrom);
            ps.setInt(2, wordIndexTo);
            return getWordsFromResultSet(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Get all words target from the database (only target, non include the definition).
     *
     * @return ArrayList of string of words target
     */
    @Override
    public ArrayList<String> getAllWordTargets() {
        final String SQL_QUERY = "SELECT * FROM dictionary";
        try {
            PreparedStatement ps = connection.prepareStatement(SQL_QUERY);
            try {
                ResultSet rs = ps.executeQuery();
                try {
                    ArrayList<String> targets = new ArrayList<>();
                    while (rs.next()) {
                        targets.add(rs.getString(2));
                    }
                    return targets;

                } finally {
                    close(rs);
                }
            } finally {
                close(ps);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}

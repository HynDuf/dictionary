package dictionary.server;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class Dictionary {

    /**
     * Initialize the dictionary when starting the application. (Only overridden by
     * DatabaseDictionary for making MYSQL connection)
     */
    public void initialize() throws SQLException {}

    /**
     * Close the dictionary when exiting the application. (Only overridden by DatabaseDictionary for
     * close the MYSQL connection)
     */
    public void close() {}

    /**
     * Get all words in the dictionary.
     *
     * @return ArrayList of Word
     */
    public abstract ArrayList<Word> getAllWords();

    /**
     * Get all English words in the dictionary into an ArrayList of String.
     *
     * @return ArrayList of String of all words
     */
    public abstract ArrayList<String> getAllWordTargets();

    /**
     * Lookup the word `target` and return the corresponding definition.
     *
     * @param target the lookup word
     * @return the definition, if not found "404" is returned as a String.
     */
    public abstract String lookUpWord(final String target);

    /**
     * Insert a new word to dictionary.
     *
     * @param target the word
     * @param definition the definition
     * @return true if `target` hasn't been added yet, false otherwise
     */
    public abstract boolean insertWord(final String target, final String definition);

    /**
     * Delete the word `target`.
     *
     * @param target the deleted word
     * @return true if successfully delete, false otherwise
     */
    public abstract boolean deleteWord(final String target);

    /**
     * Update the Vietnamese definition of `target` to `definition`.
     *
     * @param target the word
     * @param definition the new definition
     * @return true if successfully updated, false otherwise
     */
    public abstract boolean updateWordDefinition(final String target, final String definition);

    /**
     * Export all words with every word and definition is on 1 line separated by a tab character.
     *
     * @return a string of exported words
     */
    public String exportAllWords() {
        ArrayList<Word> allWords = getAllWords();
        StringBuilder result = new StringBuilder();
        for (Word word : allWords) {
            result.append(word.getWordTarget())
                    .append('\t')
                    .append(word.getWordDefinition())
                    .append('\n');
        }
        return result.toString();
    }

    /**
     * Export all words and their definitions to the file `exportPath`.
     *
     * @param exportPath the path of the exported file
     * @throws IOException path not found
     */
    public void exportToFile(String exportPath) throws IOException {
        Writer out =
                new BufferedWriter(
                        new OutputStreamWriter(
                                new FileOutputStream(exportPath), StandardCharsets.UTF_8));
        String export = exportAllWords();
        out.write(export);
        out.close();
    }
}

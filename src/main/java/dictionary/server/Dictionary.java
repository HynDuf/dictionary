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
     * Get all the words in the dictionary into a string. Organized into a String table.
     *
     * @return the resulted string
     */
    public String displayAllWords() {
        ArrayList<Word> allWords = getAllWords();
        return printWordsTable(allWords, 1, allWords.size());
    }

    /**
     * Format words in `wordsList` into a visual table in String format.
     *
     * @param wordsList list of words
     * @return the visual table in String format
     */
    public String printWordsTable(ArrayList<Word> wordsList, int startIndex, int endIndex) {
        StringBuilder result =
            new StringBuilder(
                "No     | English                                     |" + " Vietnamese");
        result.append('\n').append(Helper.createLineSeparator(120));
        for (int i = startIndex - 1; i < endIndex; ++i) {
            Word w = wordsList.get(i);
            String first = String.valueOf(i + 1);
            first += Helper.createSpacesString(7 - first.length());
            String second = " " + w.getWordTarget();
            second += Helper.createSpacesString(45 - second.length());
            String[] definition = Helper.htmlToText(w.getWordDefinition()).split("\n");
            String third = " " + definition[0];
            result.append('\n').append(first).append('|').append(second).append('|').append(third);
            for (int j = 1; j < definition.length; ++j) {
                result.append('\n')
                    .append(Helper.createSpacesString(7))
                    .append('|')
                    .append(Helper.createSpacesString(45))
                    .append("| ")
                    .append(definition[j]);
            }
            result.append('\n').append(Helper.createLineSeparator(120));
        }
        result.append('\n');
        return result.toString();
    }

    /**
     * Fill with the definition of English words in `targets`.
     *
     * @param targets the words to find definition
     * @return ArrayList of Word
     */
    public ArrayList<Word> fillDefinition(ArrayList<String> targets) {
        ArrayList<Word> res = new ArrayList<>();
        for (String target : targets) {
            res.add(new Word(target, lookUpWord(target)));
        }
        return res;
    }

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

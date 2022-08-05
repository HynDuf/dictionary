package dictionary.server;

import dictionary.server.database.Database;

import java.util.ArrayList;

public class Dictionary {

    /** Constructor. Connect to Database. */
    public Dictionary() {

    }

    /**
     * Lookup the word `target` and return the corresponding definition.
     *
     * @param target the lookup word
     * @return the definition, if not found "404" is returned as a String.
     */
    public String lookUpWord(final String target) {
        return Database.lookUpWord(target);
    }

    /**
     * Insert a new word to dictionary.
     *
     * @param target the word
     * @param definition the definition
     * @return true if `target` hasn't been added yet, false otherwise
     */
    public boolean insertWord(final String target, final String definition) {
        return Database.insertWord(target, definition);
    }

    /**
     * Delete the word `target`.
     *
     * @param target the delete word
     * @return true if successfully delete, false otherwise
     */
    public boolean deleteWord(final String target) {
        return Database.deleteWord(target);
    }

    /**
     * Update the Vietnamese definition of `target` to `definition`.
     *
     * @param target the word
     * @param definition the new definition
     * @return true if successfully updated, false otherwise
     */
    public boolean updateWordDefinition(final String target, final String definition) {
        return Database.updateWordDefinition(target, definition);
    }

    /**
     * Get all the words in the dictionary into a string.
     *
     * @return the resulted string
     */
    public String getAllWords() {
        StringBuilder result = new StringBuilder(
            "No      | English                                               | Vietnamese");
        ArrayList<Word> words = Database.getAllWords();
        for (int i = 0; i < words.size(); ++i) {
            Word w = words.get(i);
            String first = String.valueOf(i + 1);
            first += Helper.createSpacesString(8 - first.length());
            String second = " " + w.getWordTarget();
            second += Helper.createSpacesString(55 - second.length());
            String third = " " + w.getWordExplain();
            result.append('\n').append(first).append('|').append(second).append('|').append(third);
        }
        result.append('\n');
        return result.toString();
    }
}

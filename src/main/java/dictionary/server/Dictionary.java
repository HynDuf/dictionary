package dictionary.server;

import java.util.ArrayList;

public class Dictionary {
    public static final int WordSize = 1000000;
    private static final ArrayList<Word> words = new ArrayList<>();

    public Dictionary() {}

    /**
     * Lookup the word `target` and return the corresponding definition.
     *
     * @param target the lookup word
     * @return the definition, if not found "404" is returned as a String.
     */
    public static String lookUpWord(final String target) {
        for (Word w : words) {
            if (w.getWordTarget().equals(target)) {
                return w.getWordExplain();
            }
        }
        return "404";
    }

    /**
     * Insert a new word to dictionary.
     *
     * @param target the word
     * @param definition the definition
     * @return true if `target` hasn't been added yet, false otherwise
     */
    public static boolean insertWord(final String target, final String definition) {
        for (Word w : words) {
            if (w.getWordTarget().equals(target)) {
                return false;
            }
        }
        Word w = new Word(target, definition);
        words.add(w);
        return true;
    }

    /**
     * Delete the word `target`.
     *
     * @param target the delete word
     * @return true if successfully delete, false otherwise
     */
    public static boolean deleteWord(final String target) {
        for (int i = 0; i < words.size(); ++i) {
            if (words.get(i).getWordTarget().equals(target)) {
                words.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Update the Vietnamese definition of `target` to `definition`.
     *
     * @param target the word
     * @param definition the new definition
     * @return true if successfully updated, false otherwise
     */
    public static boolean updateWordDefinition(final String target, final String definition) {
        for (Word w : words) {
            if (w.getWordTarget().equals(target)) {
                w.setWordExplain(definition);
                return true;
            }
        }
        return false;
    }

    /**
     * Get all the words in the dictionary into a string.
     *
     * @return the resulted string
     */
    public static String getAllWords() {
        String result = "No      | English                                     | Vietnamese";
        for (int i = 0; i < words.size(); ++i) {
            Word w = words.get(i);
            String first = String.valueOf(i + 1);
            first += Helper.createSpacesString(8 - first.length());
            String second = " " + w.getWordTarget();
            second += Helper.createSpacesString(45 - second.length());
            String third = " " + w.getWordExplain();
            result += '\n' + first + '|' + second + '|' + third;
        }
        result += '\n';
        return result;
    }

    public static ArrayList<String> exportAllWords() {
        ArrayList<String> result = new ArrayList<String>();

        for (Word w : words) {
            String target = w.getWordTarget();
            result.add(target);
        }

        return result;
    }
}

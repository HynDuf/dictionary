package dictionary.server;

import dictionary.server.database.Database;
import java.util.ArrayList;

public class Dictionary {
    private final ArrayList<Word> words = new ArrayList<>();
    private int numWords = 0;

    /**
     * Get all words in the dictionary.
     *
     * @return ArrayList of Word
     */
    public ArrayList<Word> getWords() {
        if (Database.isConnected()) {
            return Database.getAllWords();
        } else {
            return words;
        }
    }

    /**
     * Getter for numWords.
     *
     * @return number of words in the dictionary
     */
    public int getNumWords() {
        return numWords;
    }

    /**
     * Setter for numWords.
     *
     * @param numWords number of words
     */
    public void setNumWords(int numWords) {
        this.numWords = numWords;
    }

    /**
     * Lookup the word `target` and return the corresponding definition.
     *
     * @param target the lookup word
     * @return the definition, if not found "404" is returned as a String.
     */
    public String lookUpWord(final String target) {
        if (Database.isConnected()) {
            return Database.lookUpWord(target);
        } else {
            for (Word w : words) {
                if (w.getWordTarget().equals(target)) {
                    return w.getWordExplain();
                }
            }
            return "404";
        }
    }

    /**
     * Insert a new word to dictionary.
     *
     * @param target the word
     * @param definition the definition
     * @return true if `target` hasn't been added yet, false otherwise
     */
    public boolean insertWord(final String target, final String definition) {
        if (Database.isConnected()) {
            if (Database.insertWord(target, definition)) {
                numWords++;
                Trie.insert(target);
                return true;
            }
            return false;
        } else {
            for (Word w : words) {
                if (w.getWordTarget().equals(target)) {
                    return false;
                }
            }
            Word w = new Word(target, definition);
            words.add(w);
            Trie.insert(target);
            numWords++;
            return true;
        }
    }

    /**
     * Delete the word `target`.
     *
     * @param target the deleted word
     * @return true if successfully delete, false otherwise
     */
    public boolean deleteWord(final String target) {
        if (Database.isConnected()) {
            if (Database.deleteWord(target)) {
                numWords--;
                Trie.delete(target);
                return true;
            }
        } else {
            for (int i = 0; i < words.size(); ++i) {
                if (words.get(i).getWordTarget().equals(target)) {
                    words.remove(i);
                    numWords--;
                    Trie.delete(target);
                    return true;
                }
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
    public boolean updateWordDefinition(final String target, final String definition) {
        if (Database.isConnected()) {
            return Database.updateWordDefinition(target, definition);
        } else {
            for (Word w : words) {
                if (w.getWordTarget().equals(target)) {
                    w.setWordExplain(definition);
                    return true;
                }
            }
            return false;
        }
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
                        "No      | English                                               |"
                                + " Vietnamese");
        for (int i = startIndex - 1; i < endIndex; ++i) {
            Word w = wordsList.get(i);
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

    /**
     * Get all the words in the dictionary into a string. Organized into a String table.
     *
     * @return the resulted string
     */
    public String displayAllWords() {
        ArrayList<Word> allWords = getWords();
        return printWordsTable(allWords, 1, allWords.size());
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
     * Export all English words in the dictionary into an ArrayList of String.
     *
     * @return ArrayList of String of all words
     */
    public ArrayList<String> exportAllWords() {
        ArrayList<String> result = new ArrayList<>();

        for (Word w : words) {
            String target = w.getWordTarget();
            result.add(target);
        }

        return result;
    }
}

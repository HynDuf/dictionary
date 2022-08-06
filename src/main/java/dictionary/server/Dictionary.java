package dictionary.server;

import dictionary.server.database.Database;

import java.util.ArrayList;

public class Dictionary {
    private final ArrayList<Word> words = new ArrayList<>();
    private int numWords = 0;

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
            numWords++;
            return true;
        }
    }

    /**
     * Delete the word `target`.
     *
     * @param target the delete word
     * @return true if successfully delete, false otherwise
     */
    public boolean deleteWord(final String target) {
        if (Database.isConnected()) {
            if (Database.deleteWord(target)) {
                numWords--;
                return true;
            }
            return false;
        } else {
            for (int i = 0; i < words.size(); ++i) {
                if (words.get(i).getWordTarget().equals(target)) {
                    words.remove(i);
                    numWords--;
                    return true;
                }
            }
            return false;
        }
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
    public String printWordsTable(ArrayList<Word> wordsList, int startIndex) {
        StringBuilder result =
                new StringBuilder(
                        "No      | English                                               |"
                                + " Vietnamese");
        for (int i = 0; i < wordsList.size(); ++i) {
            Word w = wordsList.get(i);
            String first = String.valueOf(i + startIndex);
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
    public String getAllWords() {
        ArrayList<Word> allWords;
        if (Database.isConnected()) {
            allWords = Database.getAllWords();
        } else {
            allWords = words;
        }
        return printWordsTable(allWords, 1);
    }

    /**
     * Get words have index from `wordIndexFrom` to `wordIndexTo`. Organized into a String table.
     *
     * @param wordIndexFrom left bound
     * @param wordIndexTo right bound
     * @return the resulted String
     */
    public String getWordsPartial(int wordIndexFrom, int wordIndexTo) {
        ArrayList<Word> wordsList = new ArrayList<>();
        if (Database.isConnected()) {
            wordsList = Database.getWordsPartial(wordIndexFrom, wordIndexTo);
        } else {
            for (int i = wordIndexFrom - 1; i < wordIndexTo; i++) {
                wordsList.add(words.get(i));
            }
        }
        return printWordsTable(wordsList, wordIndexFrom);
    }
}

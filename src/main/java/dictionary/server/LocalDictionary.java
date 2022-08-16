package dictionary.server;

import java.util.ArrayList;

public class LocalDictionary extends Dictionary {

    private static final ArrayList<Word> words = new ArrayList<>();

    /**
     * Get all words in the dictionary.
     *
     * @return ArrayList of Word
     */
    @Override
    public ArrayList<Word> getAllWords() {
        return words;
    }

    /**
     * Get all English words in the dictionary into an ArrayList of String.
     *
     * @return ArrayList of String of all words
     */
    @Override
    public ArrayList<String> getAllWordTargets() {
        ArrayList<String> result = new ArrayList<>();
        for (Word w : words) {
            String target = w.getWordTarget();
            result.add(target);
        }
        return result;
    }

    /**
     * Lookup the word `target` and return the corresponding definition.
     *
     * @param target the lookup word
     * @return the definition, if not found "404" is returned as a String.
     */
    @Override
    public String lookUpWord(final String target) {

        for (Word w : words) {
            if (w.getWordTarget().equals(target)) {
                return w.getWordDefinition();
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
    @Override
    public boolean insertWord(final String target, final String definition) {
        for (Word w : words) {
            if (w.getWordTarget().equals(target)) {
                return false;
            }
        }
        Word w = new Word(target, definition);
        words.add(w);
        Trie.insert(target);
        return true;
    }

    /**
     * Delete the word `target`.
     *
     * @param target the deleted word
     * @return true if successfully delete, false otherwise
     */
    @Override
    public boolean deleteWord(final String target) {
        for (int i = 0; i < words.size(); ++i) {
            if (words.get(i).getWordTarget().equals(target)) {
                words.remove(i);
                Trie.delete(target);
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
    @Override
    public boolean updateWordDefinition(final String target, final String definition) {
        for (Word w : words) {
            if (w.getWordTarget().equals(target)) {
                w.setWordExplain(definition);
                return true;
            }
        }
        return false;
    }
}

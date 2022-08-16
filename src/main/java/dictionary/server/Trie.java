package dictionary.server;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Trie {

    private static final ArrayList<String> searchedWords = new ArrayList<>();
    private static final TrieNode root = new TrieNode();

    public static ArrayList<String> getSearchedWords() {
        return searchedWords;
    }

    /**
     * Insert word `target` into Trie DS.
     *
     * @param target the word to insert
     */
    public static void insert(String target) {
        int length = target.length();

        TrieNode pCrawl = root;

        for (int level = 0; level < length; level++) {
            char index = target.charAt(level);

            if (pCrawl.children.get(index) == null) {
                pCrawl.children.put(index, new TrieNode());
            }

            pCrawl = pCrawl.children.get(index);
        }

        // Set `target` word ends at pCrawl
        pCrawl.isEndOfWord = true;
    }

    /**
     * Get all words ended in the subtree of node `pCrawl`
     *
     * @param pCrawl the current node
     * @param target the current word that `pCrawl` represents
     */
    private static void dfsGetWordsSubtree(TrieNode pCrawl, String target) {
        if (pCrawl.isEndOfWord) {
            searchedWords.add(target);
        }
        for (char index : pCrawl.children.keySet()) {
            if (pCrawl.children.get(index) != null) {
                dfsGetWordsSubtree(pCrawl.children.get(index), target + index);
            }
        }
    }

    /**
     * Search all words start with `prefix` in the Trie.
     *
     * @param prefix the prefix to search
     * @return an ArrayList of String contains the words start with `prefix`
     */
    public static ArrayList<String> search(String prefix) {
        if (prefix.isEmpty()) {
            return new ArrayList<>();
        }
        searchedWords.clear();
        int length = prefix.length();
        TrieNode pCrawl = root;

        for (int level = 0; level < length; level++) {
            char index = prefix.charAt(level);

            if (pCrawl.children.get(index) == null) {
                return getSearchedWords();
            }

            pCrawl = pCrawl.children.get(index);
        }
        dfsGetWordsSubtree(pCrawl, prefix);
        return getSearchedWords();
    }

    /**
     * Delete the word `target` from Trie DS.
     *
     * @param target the word to delete
     */
    public static void delete(String target) {
        int length = target.length();

        TrieNode pCrawl = root;

        for (int level = 0; level < length; level++) {
            char index = target.charAt(level);
            if (pCrawl.children.get(index) == null) {
                System.out.println("This word has not been inserted");
                return;
            }
            pCrawl = pCrawl.children.get(index);
        }
        if (!pCrawl.isEndOfWord) {
            System.out.println("This word has not been inserted");
            return;
        }

        pCrawl.isEndOfWord = false;
    }

    /** a Node on the Trie DS. */
    public static class TrieNode {
        Map<Character, TrieNode> children = new TreeMap<>();
        /* isEndOfWord is true if the node represents the end of a word */
        boolean isEndOfWord;

        TrieNode() {
            isEndOfWord = false;
        }
    }
}

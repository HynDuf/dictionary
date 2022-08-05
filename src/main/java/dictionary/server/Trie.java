package dictionary.server;

import java.util.ArrayList;

public class Trie {
    // Alphabet size (# of symbols)
    private static final int ALPHABET_SIZE = 26;

    private static final ArrayList<String> searchedWords = new ArrayList<>();

    public static ArrayList<String> getSearchedWords() {
        return searchedWords;
    }


    // trie node
    public static class TrieNode {
        TrieNode[] children = new TrieNode[ALPHABET_SIZE];

        // isEndOfWord is true if the node represents
        // end of a word
        boolean isEndOfWord;

        TrieNode() {
            isEndOfWord = false;
            for (int i = 0; i < ALPHABET_SIZE; i++) children[i] = null;
        }
    }

    private static final TrieNode root = new TrieNode();

    // If not present, inserts key into trie
    // If the key is prefix of trie node,
    // just marks leaf node
    public static void insert(String key) {
        int level;
        int length = key.length();
        int index;

        TrieNode pCrawl = root;

        for (level = 0; level < length; level++) {
            index = key.charAt(level) - 'a';
            if (pCrawl.children[index] == null) pCrawl.children[index] = new TrieNode();

            pCrawl = pCrawl.children[index];
        }

        // mark last node as leaf
        pCrawl.isEndOfWord = true;
    }

    private static void dfs(TrieNode pCrawl, String key) {
        if (pCrawl.isEndOfWord) {
            searchedWords.add(key);
        }
        for (int i = 0; i < 26; ++i) {
            if (pCrawl.children[i] != null) {
                dfs(pCrawl.children[i], key + (char) (i + 'a'));
            }
        }
    }

    // Returns true if key presents in trie, else false
    public static ArrayList<String> search(String key) {
        searchedWords.clear();
        int length = key.length();
        TrieNode pCrawl = root;

        for (int level = 0; level < length; level++) {
            int index = key.charAt(level) - 'a';

            if (pCrawl.children[index] == null) {
                return getSearchedWords();
            }

            pCrawl = pCrawl.children[index];
        }
        dfs(pCrawl, key);
        return getSearchedWords();
    }
}

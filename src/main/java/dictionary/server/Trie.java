package dictionary.server;

import java.util.ArrayList;
import java.util.HashMap;

public class Trie {
    // Alphabet size (# of symbols)

    private static final ArrayList<String> searchedWords = new ArrayList<>();

    public static ArrayList<String> getSearchedWords() {
        return searchedWords;
    }


    // trie node
    public static class TrieNode {
        HashMap<Character, TrieNode> children = new HashMap<Character, TrieNode>();

        // isEndOfWord is true if the node represents
        // end of a word
        boolean isEndOfWord;

        TrieNode() {
            isEndOfWord = false;
            children.clear();
        }
    }

    private static final TrieNode root = new TrieNode();

    // If not present, inserts key into trie
    // If the key is prefix of trie node,
    // just marks leaf node
    public static void insert(String key) {
        int level;
        int length = key.length();
        char index;

        TrieNode pCrawl = root;

        for (level = 0; level < length; level++) {
            index = key.charAt(level);
            if (pCrawl.children.get(index) == null)
                pCrawl.children.put(index, new TrieNode());

            pCrawl = pCrawl.children.get(index);
        }

        // mark last node as leaf
        pCrawl.isEndOfWord = true;
    }

    private static void dfs(TrieNode pCrawl, String key) {
        if (pCrawl.isEndOfWord) {
            searchedWords.add(key);
        }
        for (char index : pCrawl.children.keySet()) {
            if (pCrawl.children.get(index) != null) {
                dfs(pCrawl.children.get(index), key + index);
            }
        }
    }

    // Returns true if key presents in trie, else false
    public static ArrayList<String> search(String key) {
        searchedWords.clear();
        int length = key.length();
        TrieNode pCrawl = root;

        for (int level = 0; level < length; level++) {
            char index = key.charAt(level);

            if (pCrawl.children.get(index) == null) {
                return getSearchedWords();
            }

            pCrawl = pCrawl.children.get(index);
        }
        dfs(pCrawl, key);
        return getSearchedWords();
    }
}

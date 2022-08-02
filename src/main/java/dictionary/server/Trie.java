package dictionary.server;

public class Trie {
    // Alphabet size (# of symbols)
    private static final int ALPHABET_SIZE = 26;

    public static String[] Searcher = new String[Dictionary.WordSize];

    public static int size = 0;

    // trie node
    public static class TrieNode {
        TrieNode[] children = new TrieNode[ALPHABET_SIZE];

        // isEndOfWord is true if the node represents
        // end of a word
        boolean isEndOfWord;

        TrieNode() {
            isEndOfWord = false;
            for (int i = 0; i < ALPHABET_SIZE; i++)
                children[i] = null;
        }
    }

    public static TrieNode root = new TrieNode();

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
            if (pCrawl.children[index] == null)
                pCrawl.children[index] = new TrieNode();

            pCrawl = pCrawl.children[index];
        }

        // mark last node as leaf
        pCrawl.isEndOfWord = true;
    }

    private static void dfs(TrieNode pCrawl, String key) {
        if (pCrawl.isEndOfWord == true) {
            Searcher[size] = key;
            ++size;
        }
        for (int i = 0; i < 26; ++i) {
            if (pCrawl.children[i] != null) {
                dfs(pCrawl.children[i], key + (char) (i + 97));
            }
        }

    }

    // Returns true if key presents in trie, else false
    public static boolean search(String key) {
        int level;
        int length = key.length();
        int index;
        TrieNode pCrawl = root;

        for (level = 0; level < length; level++) {
            index = key.charAt(level) - 'a';

            if (pCrawl.children[index] == null)
                return false;

            pCrawl = pCrawl.children[index];
        }

        dfs(pCrawl, key);

        return true;
    }

}

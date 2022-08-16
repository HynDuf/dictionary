package dictionary.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class History {
    private static final int MAX_WORDS_HISTORY = 30;
    private static final ArrayList<String> historySearch = new ArrayList<>();

    public static ArrayList<String> getHistorySearch() {
        return historySearch;
    }

    /**
     * Load history file everytime the application is open.
     *
     * <p>The file is in ./dictionary-user-data/words-search-history.txt where `./` is the relative
     * directory you run the application from.
     *
     * <p>If dictionary-user-data or words-search-history.txt haven't existed yet, created them.
     * Otherwise, load current words in words-search-history.txt to the ArrayList.
     */
    public static void loadHistory() {
        File dir = new File("dictionary-user-data/");
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                System.out.println("Couldn't create directory `dictionary-user-data`!");
            }
        }

        File file = new File(dir + "/words-search-history.txt");
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    System.out.println("Couldn't create file `words-search-history.txt`!");
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Couldn't create file `words-search-history.txt`!");
            }
            return;
        }

        try {
            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(
                                    new FileInputStream(
                                            "dictionary-user-data/words-search-history.txt"),
                                    StandardCharsets.UTF_8));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                historySearch.add(inputLine.strip());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Couldn't find " + "words-search-history.txt");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Couldn't read " + "words-search-history.txt");
        }
        refactorHistory();
    }

    /**
     * Add word to history. Maintain at max `MAX_WORDS_HISTORY` most recent word in the ArrayList.
     *
     * @param target the word to add
     */
    public static void addWordToHistory(String target) {
        historySearch.removeIf(e -> e.equals(target));
        historySearch.add(target);
        refactorHistory();
    }

    /** Write the words in the ArrayList to `dictionary-user-data/words-search-history.txt`. */
    public static void exportHistory() {
        try {
            Writer out =
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    new FileOutputStream(
                                            "dictionary-user-data/words-search-history.txt"),
                                    StandardCharsets.UTF_8));
            StringBuilder content = new StringBuilder();
            for (String target : historySearch) {
                content.append(target).append("\n");
            }
            out.write(content.toString());
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("An error occurred.`");
        }
    }

    /**
     * Refactor the history searches. Maintain at max `MAX_WORDS_HISTORY` most recent word in the
     * ArrayList.
     */
    public static void refactorHistory() {
        if (historySearch.size() <= MAX_WORDS_HISTORY) {
            return;
        }
        while (historySearch.size() > MAX_WORDS_HISTORY) {
            historySearch.remove(0);
        }
    }
}

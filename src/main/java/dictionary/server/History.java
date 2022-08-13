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

    public static void addWordToHistory(String target) {
        historySearch.add(target);
        refactorHistory();
    }

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

    public static void refactorHistory() {
        if (historySearch.size() <= MAX_WORDS_HISTORY) {
            return;
        }
        while (historySearch.size() > MAX_WORDS_HISTORY) {
            historySearch.remove(0);
        }
        exportHistory();
    }
}

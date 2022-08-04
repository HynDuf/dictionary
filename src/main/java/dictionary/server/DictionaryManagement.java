package dictionary.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class DictionaryManagement {

    private static Dictionary dictionary;

    public static void initialize() {
        dictionary = new Dictionary();
        dictionary.insertWord("hello", "xin chao");
        dictionary.insertWord("head", "xin chao");
        dictionary.insertWord("hi", "xin chao");
        dictionary.insertWord("highlight", "xin chao");
        dictionary.insertWord("happy", "xin chao");
        dictionary.insertWord("happiness", "xin chao");
        ArrayList<String> words = dictionary.exportAllWords();

        for (int i = 0; i < words.size(); i++) {
            Trie.insert(words.get(i));
        }
        ArrayList<String> tem = Trie.search("h");
        System.out.println("Start\n");
        for (String w : tem) {
            System.out.println(w);
        }
        System.out.println("done\n");
    }

    /** Utility class. Stop from create an instance of the class. */
    private DictionaryManagement() {}

    /** Option 1. Look up a word's definition. */
    public static void lookUpWord() {
        System.out.print("==> Enter the English word to look up: ");
        String target = Helper.readLine();
        String definition = dictionary.lookUpWord(target);
        if (definition.equals("404")) {
            System.out.println("The word you looked up isn't in the dictionary!\n");
        } else {
            System.out.println("Definition of `" + target + "`: " + definition + '\n');
        }
    }

    /** Option 2. Show all words currently in the dictionary. */
    public static void showAllWords() {
        System.out.println(dictionary.getAllWords());
    }

    /** Option 3. Add new words from the command line. */
    public static void insertFromCommandline() {
        System.out.print("==> Number of words to insert: ");
        int numWords = Helper.readInteger();
        Helper.readLine();
        for (int i = 0; i < numWords; ++i) {
            System.out.print("==> Word target: ");
            String target = Helper.readLine();
            System.out.print("==> Word definition: ");
            String definition = Helper.readLine();
            if (!dictionary.insertWord(target, definition)) {
                System.out.println("`" + target + "` already exists in the dictionary!");
            }
        }
    }

    /** Option 4. Delete a word. */
    public static void deleteWord() {
        System.out.print("==> Enter the English word to delete: ");
        String target = Helper.readLine();
        if (dictionary.deleteWord(target)) {
            System.out.println("`" + target + "` successfully deleted.\n");
        } else {
            System.out.println("The word you delete isn't in the dictionary!\n");
        }
    }

    /** Option 5. Update a word's definition. */
    public static void updateWord() {
        System.out.print("==> Enter the English word to update: ");
        String target = Helper.readLine();
        System.out.print("==> Enter the new definition for `" + target + "`: ");
        String definition = Helper.readLine();
        if (dictionary.updateWordDefinition(target, definition)) {
            System.out.println("`" + target + "` successfully updated.\n");
        } else {
            System.out.println("The word you update isn't in the dictionary!\n");
        }
    }

    /** Option 6. Insert word from file `inputFromFile.txt` */
    public static void insertFromFile() {

        /** pass the path to the file as a parameter */
        File file = new File("inputFromFile.txt");
        try {
            Scanner scFile = new Scanner(file);
            while (scFile.hasNextLine()) {
                String getLine = scFile.nextLine();
                int pos = getLine.indexOf("\t");
                if (pos == -1) {
                    continue;
                }
                String target = getLine.substring(0, pos).trim();
                String definition = getLine.substring(pos + 1).trim();
                dictionary.insertWord(target, definition);
                System.out.println("Inserted `" + target + "' successfully");
            }
            scFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Couldn't find `inputFromFile.txt`");
        }
    }

    /** Option 7. Export to file */
    public static void dictionaryExportToFile() {

        try {
            FileWriter out = new FileWriter("exportToFile.txt");
            String export = dictionary.getAllWords();
            // System.out.println(export);
            out.write(export);
            out.close();
            System.out.println("Exported successfully");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("An error occurred.`");
        }
    }

    /** Option 8. Searcher */
    public static void dictionarySearcher() {
        try {
            System.out.print("==> Enter the English word to search: ");
            String target = Helper.readLine();

            ArrayList<String> searchedWords = Trie.search(target);
            if (!searchedWords.isEmpty()) {
                String result =
                        "No      | English                                     | Vietnamese";
                for (int i = 0; i < searchedWords.size(); ++i) {
                    String first = String.valueOf(i + 1);
                    first += Helper.createSpacesString(8 - first.length());
                    String second = " " + searchedWords.get(i);
                    second += Helper.createSpacesString(45 - second.length());
                    String third = " " + dictionary.lookUpWord(searchedWords.get(i));
                    result += '\n' + first + '|' + second + '|' + third;
                }
                System.out.println(result);
            } else {
                System.out.println("No word starts with `" + target + "` found in the dictionary!");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("An error occurred.`");
        }
    }

    /** Option 9. Exit the application. */
    public static void exitApplication() {
        System.out.println("Exiting...");
        System.exit(0);
    }
}

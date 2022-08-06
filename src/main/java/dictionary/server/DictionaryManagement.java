package dictionary.server;

import dictionary.server.database.Database;
import dictionary.server.google_translate_api.TranslatorApi;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DictionaryManagement {

    private static final Dictionary dictionary = new Dictionary();

    /** Set up Database. */
    public static void setUpDatabase() {
        Database.connectToDatabase();
        dictionary.setNumWords(138481);
    }

    /** Option 1. Look up a word's definition. */
    public static void lookUpWord() {
        System.out.print("==> Enter the English word to look up: ");
        String target = Helper.readLine();
        String definition = dictionary.lookUpWord(target);
        definition = Helper.htmlToText(definition);
        if (definition.equals("404")) {
            System.out.println("The word you looked up isn't in the dictionary!\n");
        } else {
            System.out.println(
                    "Definition of `"
                            + target
                            + "` (Ensure your terminal can render Vietnamese characters): \n"
                            + definition
                            + '\n');
        }
        Helper.pressEnterToContinue();
    }

    /**
     * Option 2. Show words currently in the dictionary.
     *
     * <p>There are 2 types:
     *
     * <p>- Show all words.
     *
     * <p>- Show 10 words each.
     */
    public static void showWords() {
        while (true) {
            System.out.println("\nChoose an option to proceed:");
            System.out.println("1. Show all words");
            System.out.println(
                    "2. Show words partially (max of `WORDS_PER_PAGE` (Default: 10) words each)");
            System.out.print("==> Enter an option (1-2): ");
            int selection = Helper.readInteger();
            Helper.readLine();
            if (selection == 1) {
                System.out.println(dictionary.getAllWords());
                Helper.pressEnterToContinue();
                return;
            } else if (selection == 2) {
                showWordsPartial();
                return;
            } else {
                System.out.println("Invalid option!");
            }
        }
    }

    private static final int WORDS_PER_PAGE = 10;

    /** Show max of `WORDS_PER_PAGE` each time. Can change the active page interactively. */
    public static void showWordsPartial() {
        int numberOfPages = (dictionary.getNumWords() + WORDS_PER_PAGE - 1) / WORDS_PER_PAGE;
        int curWordIndex = 1;
        int curPageIndex = 1;
        while (true) {
            int toWordIndex = Math.min(curWordIndex + WORDS_PER_PAGE - 1, dictionary.getNumWords());
            System.out.println(dictionary.getWordsPartial(curWordIndex, toWordIndex));
            System.out.println("Page " + curPageIndex + " of " + numberOfPages + " pages in total");
            System.out.println(
                    "Words "
                            + curWordIndex
                            + "-"
                            + toWordIndex
                            + " of "
                            + dictionary.getNumWords()
                            + " words in total");
            System.out.print(
                    "==> Enter `l/r/q/<PAGE_NUMBER>` for `page before/page after/quit/jump to"
                        + " <PAGE_NUMBER>`: ");
            String option = Helper.readLine();
            if (option.equals("l")) {
                if (curPageIndex > 1) {
                    curPageIndex--;
                    curWordIndex -= WORDS_PER_PAGE;
                }
            } else if (option.equals("r")) {
                if (curPageIndex < numberOfPages) {
                    curPageIndex++;
                    curWordIndex += WORDS_PER_PAGE;
                }
            } else if (option.equals("q")) {
                return;
            } else {
                try {
                    int pageIndex = Integer.parseInt(option);
                    if (1 <= pageIndex && pageIndex <= numberOfPages) {
                        curPageIndex = pageIndex;
                        curWordIndex = (curPageIndex - 1) * WORDS_PER_PAGE + 1;
                    } else {
                        System.out.println("Invalid page number!");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid option!");
                }
            }
        }
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
                System.out.println("`" + target + "` already existed in the dictionary!\n");
            } else {
                System.out.println("`" + target + "` added successfully.\n");
            }
        }
        Helper.pressEnterToContinue();
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
        Helper.pressEnterToContinue();
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
        Helper.pressEnterToContinue();
    }

    /** Option 6. Insert word from file `inputFromFile.txt`. */
    public static void insertFromFile() {

        /* pass the path to the file as a parameter */
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
                if (dictionary.insertWord(target, definition)) {
                    System.out.println("Inserted `" + target + "' successfully");
                } else {
                    System.out.println("`" + target + "` already existed in the dictionary!");
                }
            }
            scFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Couldn't find `inputFromFile.txt`");
        }
        Helper.pressEnterToContinue();
    }

    /** Option 7. Translate English text to Vietnamese Text with GooogleTranslateAPI. */
    public static void translateEnToVi() {
        System.out.print("==> Enter the English text you want to translate: ");
        String text = Helper.readLine();
        System.out.println(
                "The Vietnamese translated text"
                        + " (Ensure that your terminal can render Vietnamese characters): ");
        System.out.println(TranslatorApi.translateEnToVi(text) + "\n");
        Helper.pressEnterToContinue();
    }

    /** Option 8. Text to Speech, speaking English text in English. */
    public static void textToSpeech() {
        System.out.print("==> Enter the English text you want to listen to: ");
        String text = Helper.readLine();
        TextToSpeech.playSound(text);
        Helper.pressEnterToContinue();
    }

    /** Option 9. Exit the application. */
    public static void exitApplication() {
        System.out.println("Exiting...");
    }
}

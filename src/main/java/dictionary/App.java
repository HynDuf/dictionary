package dictionary;

import dictionary.server.DictionaryManagement;
import dictionary.server.Helper;

public class App {

    private static final int LOOK_UP = 1;
    private static final int SHOW_ALL = 2;
    private static final int INSERT_CMD = 3;
    private static final int DELETE = 4;
    private static final int UPDATE = 5;
    private static final int INSERT_FILE = 6;
    private static final int EXPORT_FILE = 7;
    private static final int EXIT = 8;

    /**
     * Display options for the commandline application.
     *
     * @return the selected options (from 1 to 8)
     */
    public static int displayOptions() {
        while (true) {
            System.out.println("Choose an option to proceed:");
            System.out.println("1. Look up word");
            System.out.println("2. Show all words");
            System.out.println("3. Insert words");
            System.out.println("4. Delete a word");
            System.out.println("5. Update a word's definition");
            System.out.println("6. Insert from file");
            System.out.println("7. Export to file");
            System.out.println("8. Exit");
            System.out.print("==> Enter a selection (1-8): ");
            int selection = Helper.readInteger();
            Helper.readLine();
            if (1 <= selection && selection <= 8) {
                return selection;
            } else {
                System.out.println("Invalid option!");
            }
        }
    }

    /**
     * Execute the chosen selection.
     *
     * @param selection the chosen selection
     */
    public static void executeSelection(int selection) {
        switch (selection) {
            case LOOK_UP:
                DictionaryManagement.lookUpWord();
                break;
            case SHOW_ALL:
                DictionaryManagement.showAllWords();
                break;
            case INSERT_CMD:
                DictionaryManagement.insertFromCommandline();
                break;
            case DELETE:
                DictionaryManagement.deleteWord();
                break;
            case UPDATE:
                DictionaryManagement.updateWord();
                break;
            case INSERT_FILE:
                DictionaryManagement.insertFromFile();
                break;
            case EXPORT_FILE:
                DictionaryManagement.dictionaryExportToFile();
                break;
            case EXIT:
                DictionaryManagement.exitApplication();
                break;
            default:
                System.out.println("Invalid option!");
                break;
        }
    }

    /**
     * The main application flow.
     *
     * @param args cmd arguments
     */
    public static void main(String[] args) {
        while (true) {
            int selection = displayOptions();
            executeSelection(selection);
        }
    }
}

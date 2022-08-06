package dictionary;

import dictionary.server.DictionaryManagement;
import dictionary.server.Helper;
import dictionary.server.database.Database;

public class App {

    public static void databaseOption() {
        while (true) {
            System.out.print(
                    "Do you want to use SQL Database? (Requires Database already set up)\n"
                            + "==> Option [y(es)/n(o)]: ");
            String option = Helper.readLine();
            if (option.equals("y") || option.equals("yes")) {
                DictionaryManagement.setUpDatabase();
                return;
            } else if (option.equals("n") || option.equals("no")) {
                return;
            } else {
                System.out.println("Invalid option!\nPlease type `y` for yes and `n` for no.");
            }
        }
    }

    private static final int LOOK_UP = 1;
    private static final int DISPLAY_WORDS = 2;
    private static final int INSERT_CMD = 3;
    private static final int DELETE = 4;
    private static final int UPDATE = 5;
    private static final int INSERT_FILE = 6;
    private static final int TRANSLATE_E_V = 7;
    private static final int TEXT_TO_SPEECH = 8;
    private static final int EXIT = 9;

    /**
     * Display options for the commandline application.
     *
     * @return the selected options (from 1 to 7)
     */
    public static int displayOptions() {
        while (true) {
            System.out.println("Choose an option to proceed:");
            System.out.println("1. Look up word");
            System.out.println("2. Display words");
            System.out.println("3. Insert words");
            System.out.println("4. Delete a word");
            System.out.println("5. Update a word's definition");
            System.out.println("6. Insert from file");
            System.out.println("7. Translate English text to Vietnamese");
            System.out.println("8. Text to Speech an English text");
            System.out.println("9. Exit");
            System.out.print("==> Enter an option (1-9): ");
            int option = Helper.readInteger();
            Helper.readLine();
            if (1 <= option && option <= 9) {
                return option;
            } else {
                System.out.println("Invalid option!");
            }
        }
    }

    /**
     * Execute the chosen option.
     *
     * @param option the chosen option
     */
    public static void executeSelection(int option) {
        switch (option) {
            case LOOK_UP:
                DictionaryManagement.lookUpWord();
                break;
            case DISPLAY_WORDS:
                DictionaryManagement.showWords();
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
            case TRANSLATE_E_V:
                DictionaryManagement.translateEnToVi();
                break;
            case TEXT_TO_SPEECH:
                DictionaryManagement.textToSpeech();
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
        databaseOption();
        int option = 0;
        do {
            option = displayOptions();
            executeSelection(option);
        } while (option != EXIT);
        Database.closeDatabase();
    }
}

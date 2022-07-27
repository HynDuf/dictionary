package dictionary;

import dictionary.server.Dictionary;
import dictionary.server.UserInput;
public class App {
    public static Dictionary dictionary;
    public static UserInput userInput;

    private static final int LOOK_UP = 1;
    private static final int SHOW_ALL = 2;
    private static final int INSERT = 3;
    private static final int DELETE = 4;
    private static final int UPDATE = 5;

    private static final int ADVANCED = 6;
    private static final int EXIT = 7;

    /** Option 1. Look up a word's definition. */
    public static void lookUpWord() {
        System.out.print("==> Enter the English word to look up: ");
        String target = userInput.readLine();
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

    /** Add new words from the command line. */
    public static void insertFromCommandline() {
        System.out.print("==> Number of words to insert: ");
        int numWords = userInput.readInteger();
        userInput.readLine();
        for (int i = 0; i < numWords; ++i) {
            System.out.print("==> Word target: ");
            String target = userInput.readLine();
            System.out.print("==> Word definition: ");
            String definition = userInput.readLine();
            if (!dictionary.insertWord(target, definition)) {
                System.out.println("`" + target + "` already exists in the dictionary!");
            }
        }
    }
    /** Call insertFromFile(),showAllWords(), dictionaryLookup(). */
    public static void dictionaryAdvanced() throws  Exception {
        System.out.println("Choose an option to proceed:");
        System.out.println("1. Look up");
        System.out.println("2. Show all words");
        System.out.println("3. Insert from file");
        int selection = userInput.readInteger();
        userInput.readLine();
        switch (selection) {
            case LOOK_UP:
                lookUpWord();
                break;
            case SHOW_ALL:
                showAllWords();
                break;
            case INSERT:
                DictionaryManagement.insertFromFile();
        }
    }

    /** Delete a word. */
    public static void deleteWord() {
        System.out.print("==> Enter the English word to delete: ");
        String target = userInput.readLine();
        if (dictionary.deleteWord(target)) {
            System.out.println("`" + target + "` successfully deleted.\n");
        } else {
            System.out.println("The word you delete isn't in the dictionary!\n");
        }
    }

    /** Update a word's definition. */
    public static void updateWord() {
        System.out.print("==> Enter the English word to update: ");
        String target = userInput.readLine();
        System.out.print("==> Enter the new definition for `" + target + "`: ");
        String definition = userInput.readLine();
        if (dictionary.updateWordDefinition(target, definition)) {
            System.out.println("`" + target + "` successfully updated.\n");
        } else {
            System.out.println("The word you update isn't in the dictionary!\n");
        }
    }

    /** Exit the application. */
    public static void exitApplication() {
        System.out.println("Exiting...");
        System.exit(0);
    }

    /**
     * Display options for the commandline application.
     *
     * @return the selected options (from 1 to 6)
     */
    public static int displayOptions() {
        while (true) {
            System.out.println("Choose an option to proceed:");
            System.out.println("1. Look up word");
            System.out.println("2. Show all words");
            System.out.println("3. Insert words");
            System.out.println("4. Delete a word");
            System.out.println("5. Update a word's definition");
            System.out.println("6. Advanced");
            System.out.println("7. Exit");
            System.out.print("==> Enter a selection (1-7): ");
            int selection = userInput.readInteger();
            userInput.readLine();
            if (1 <= selection && selection <= 6) {
                return selection;
            }
        }
    }

    /**
     * Execute the chosen selection.
     *
     * @param selection the chosen selection
     */
    public static void executeSelection(int selection) throws Exception {
        switch (selection) {
            case LOOK_UP:
                lookUpWord();
                break;
            case SHOW_ALL:
                showAllWords();
                break;
            case INSERT:
                insertFromCommandline();
                break;
            case DELETE:
                deleteWord();
                break;
            case UPDATE:
                updateWord();
                break;
            case ADVANCED:
                dictionaryAdvanced();
                break;
            case EXIT:
                exitApplication();
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
    public static void main(String[] args) throws Exception {
        dictionary = new Dictionary();
        userInput = new UserInput(System.in);
        while (true) {
            int selection = displayOptions();
            executeSelection(selection);
        }
    }
}

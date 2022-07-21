package dictionary;

import dictionary.server.database.Database;

/** Hello world! */
public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        Database database = new Database();
        database.connectToDatabase();
        String word = "hello";
        String definition = database.lookUpWord(word);
        System.out.println(definition);
        database.insertWord("house", "(danh từ): ngôi nhà");
        database.closeDatabase();
    }
}

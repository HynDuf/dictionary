package dictionary.server.database;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DatabaseTest {

    /** Test all the methods in Database class. */
    @Test
    public void testWordLookUp() {
        Database db = new Database();
        db.connectToDatabase();
        String definition = db.lookUpWord("hello");
        assertTrue(!definition.equals("404"));

        definition = db.lookUpWord("xckj2l3k5jr");
        assertTrue(definition.equals("404"));

        // Duplicate insertion
        db.insertWord("hello", "Abc");

        db.insertWord("test1", "test");
        assertTrue(db.lookUpWord("test1").equals("test"));
        db.updateWordDefinition("test1", "test1");
        assertTrue(db.lookUpWord("test1").equals("test1"));
        db.deleteWord("test1");
        assertTrue(db.lookUpWord("test1").equals("404"));

        db.closeDatabase();
    }
}

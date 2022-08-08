package dictionary.server.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class DatabaseTest {

    /** Test all the methods in Database class. */
    @Test
    public void testWordLookUp() {
        Database.connectToDatabase();
        String definition = Database.lookUpWord("hello");
        assertNotEquals("404", definition);

        definition = Database.lookUpWord("xckj2l3k5jr");
        assertEquals("404", definition);

        // Duplicate insertion
        Database.insertWord("hello", "Abc");

        Database.insertWord("test1", "test");
        assertEquals("test", Database.lookUpWord("test1"));
        Database.updateWordDefinition("test1", "test1");
        assertEquals("test1", Database.lookUpWord("test1"));
        Database.deleteWord("test1");
        assertEquals("404", Database.lookUpWord("test1"));

        Database.closeDatabase();
    }
}

package dictionary.server;

import java.nio.CharBuffer;

public class Helper {
    /**
     * Creates a string of spaces that is 'spaces' spaces long.
     *
     * @param numSpaces The number of spaces to add to the string.
     * @return the string of `numSpaces` spaces
     */
    public static String createSpacesString(int numSpaces) {
        return CharBuffer.allocate(numSpaces).toString().replace('\0', ' ');
    }
}

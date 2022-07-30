package dictionary.server;

import java.nio.CharBuffer;
import java.util.Scanner;

public class Helper {
    private static Scanner sc = new Scanner(System.in);

    /**
     * Read the next integer.
     *
     * @return the integer
     */
    public static int readInteger() {
        return sc.nextInt();
    }

    /**
     * Read whole line of text, remove leading and trailing whitespaces from the text.
     *
     * @return read the whole line.
     */
    public static String readLine() {
        return sc.nextLine().trim();
    }

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

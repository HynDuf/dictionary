package dictionary.server;

import java.io.InputStream;
import java.util.Scanner;

public class UserInput {
    private Scanner sc;

    /**
     * Constructor, getting input from InputStream `inp`.
     *
     * @param inp InputStream to get input from
     */
    public UserInput(InputStream inp) {
        sc = new Scanner(inp);
    }

    /**
     * Read the next integer.
     *
     * @return the integer
     */
    public int readInteger() {
        return sc.nextInt();
    }

    /**
     * Read whole line of text, remove leading and trailing whitespaces from the text.
     *
     * @return read the whole line.
     */
    public String readLine() {
        return sc.nextLine().trim();
    }
}

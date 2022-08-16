package dictionary.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.CharBuffer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

public class Helper {

    /**
     * Creates a string of spaces that is 'spaces' spaces long.
     *
     * @param numSpaces The number of spaces to add to the string.
     * @return the string of `numSpaces` spaces
     */
    public static String createSpacesString(int numSpaces) {
        if (numSpaces < 0) {
            numSpaces = 0;
        }
        return CharBuffer.allocate(numSpaces).toString().replace('\0', ' ');
    }

    /**
     * Creates a string of `-`` that is 'length' characters long.
     *
     * @param length The number of `-` characters to add to the vertical separator line.
     * @return the string of `length` characters `-`
     */
    public static String createLineSeparator(int length) {
        if (length < 0) {
            length = 0;
        }
        return CharBuffer.allocate(length).toString().replace('\0', '-');
    }

    /**
     * Convert HTML to plain text keeping line breaks, paragraph...
     *
     * <p>Reference:
     * https://stackoverflow.com/questions/2513707/how-to-convert-html-to-text-keeping-linebreaks
     *
     * @param html HTML String
     * @return the plain text
     */
    public static String htmlToText(String html) {
        Document document = Jsoup.parse(html);
        Element body = document.body();

        return buildStringFromNode(body).toString();
    }

    /**
     * Build the plain text from Jsoup nodes.
     *
     * <p>Reference: <a
     * href="https://stackoverflow.com/questions/2513707/how-to-convert-html-to-text-keeping-linebreaks">...</a>
     *
     * @param node Jsoup nodes
     * @return StringBuffer
     */
    private static StringBuffer buildStringFromNode(Node node) {
        StringBuffer buffer = new StringBuffer();

        if (node instanceof TextNode textNode) {
            buffer.append(textNode.text().trim());
        }

        for (Node childNode : node.childNodes()) {
            buffer.append(buildStringFromNode(childNode));
        }

        if (node instanceof Element element) {
            String tagName = element.tagName();
            if ("p".equals(tagName) || "br".equals(tagName)) {
                buffer.append("\n");
            }
        }

        return buffer;
    }

    /**
     * Count number of lines in the file `file`.
     * @param file the path of the file
     * @return number of lines of the given file
     * @throws IOException file not found or couldn't read
     */
    public static int countNumLinesOfFile(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        int lines = 0;
        while (reader.readLine() != null) lines++;
        reader.close();
        return lines;
    }
}

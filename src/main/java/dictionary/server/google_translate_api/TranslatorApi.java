package dictionary.server.google_translate_api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class TranslatorApi {

    /**
     * Transalte English text `text` into Vietnamese.
     *
     * @param text the text to be translated
     * @return the Vietnamese translation, or "500" if got errors
     */
    public static String translateEnToVi(String text) {
        try {
            return translate("en", "vi", text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "500";
    }

    /**
     * Transalte Vietnamese text `text` into English.
     *
     * @param text the text to be translated
     * @return the English translation, or "500" if got errors
     */
    public static String translateViToEn(String text) {
        try {
            return translate("vi", "en", text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "500";
    }

    /**
     * Translate text from `langFrom` to `langTo`.
     *
     * <p><a
     * href="https://stackoverflow.com/questions/8147284/how-to-use-google-translate-api-in-my-java-application">Reference</a>
     *
     * @param langFrom the input language (2 letters (ex: 'en'))
     * @param langTo the output language (2 letters (ex: 'vi'))
     * @param text the text to be translated
     * @return the translation text in `langTo`
     */
    private static String translate(String langFrom, String langTo, String text)
            throws IOException {
        String urlStr =
                "https://script.google.com/macros/s/AKfycby3AOWmhe32TgV9nW-Q0TyGOEyCHQeFiIn7hRgy5m8jHPaXDl2GdToyW_3Ys5MTbK6wjg/exec"
                        + "?q="
                        + URLEncoder.encode(text, "UTF-8")
                        + "&target="
                        + langTo
                        + "&source="
                        + langFrom;
        URL url = new URL(urlStr);
        StringBuilder response = new StringBuilder();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}

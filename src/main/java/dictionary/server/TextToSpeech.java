package dictionary.server;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javazoom.jl.player.Player;

public class TextToSpeech {

    /**
     * Convert input {@code text} to voice and play it with freetts library.
     *
     * <p>Reference: https://youtu.be/_8XstaraP9E
     *
     * <p>Worked on Windows 10
     *
     * @param text The text to be converted to voice
     */
    public static void playSoundKevin16(String text) {
        System.setProperty(
                "freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        Voice voice = VoiceManager.getInstance().getVoice("kevin16");

        if (voice != null) {
            voice.allocate();
            voice.speak(text);
            voice.deallocate();
        } else {
            System.err.println("Error in getting voices");
        }
    }

    /**
     * Convert English input {@code text} to voice and play it with Google Translator TTS API
     *
     * @param text The text to be converted to voice in English
     */
    public static void playSoundGoogleTranslateEnToVi(String text) {
        try {
            String api =
                    "http://translate.google.com/translate_tts?ie=UTF-8&tl="
                            + "en"
                            + "&client=tw-ob&q="
                            + URLEncoder.encode(text, StandardCharsets.UTF_8);
            URL url = new URL(api);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            InputStream audio = con.getInputStream();
            new Player(audio).play();
            con.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error in getting voices");
        }
    }

    /**
     * Convert Vietnamese input {@code text} to voice and play it with Google Translator TTS API
     *
     * @param text The text to be converted to voice in Vietnamese
     */
    public static void playSoundGoogleTranslateViToEn(String text) {
        try {
            String api =
                    "http://translate.google.com/translate_tts?ie=UTF-8&tl="
                            + "vi"
                            + "&client=tw-ob&q="
                            + URLEncoder.encode(text, StandardCharsets.UTF_8);
            URL url = new URL(api);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            InputStream audio = con.getInputStream();
            new Player(audio).play();
            con.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error in getting voices");
        }
    }
}

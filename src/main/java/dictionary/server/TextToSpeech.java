package dictionary.server;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

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
    public static void playSound(String text) {
        System.setProperty(
                "freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        Voice voice = VoiceManager.getInstance().getVoice("kevin16");

        if (voice != null) {
            voice.allocate();
            System.out.print("Speaking...");
            voice.speak(text);
            System.out.print("Done...");
            voice.deallocate();
        } else {
            System.err.println("Error in getting voices");
        }
    }
}

package dictionary.server;

public class Word {
    private final String wordTarget;
    private String wordExplain;

    /**
     * Constructor new Word.
     *
     * @param wordTarget English word
     * @param wordExplain Vietnamese definition
     */
    public Word(String wordTarget, String wordExplain) {
        this.wordTarget = wordTarget;
        this.wordExplain = wordExplain;
    }

    /**
     * Get the English word.
     *
     * @return English word
     */
    public String getWordTarget() {
        return wordTarget;
    }

    /**
     * Get definition of word.
     *
     * @return Vietnamese definition of word
     */
    public String getWordDefinition() {
        return wordExplain;
    }

    /**
     * Set Vietnamese definition of the word.
     *
     * @param wordExplain Vietnamese definition
     */
    public void setWordExplain(String wordExplain) {
        this.wordExplain = wordExplain;
    }
}

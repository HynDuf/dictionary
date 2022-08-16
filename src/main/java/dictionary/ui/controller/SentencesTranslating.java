package dictionary.ui.controller;

import dictionary.server.TextToSpeech;
import dictionary.server.TranslatorApi;
import java.io.IOException;
import java.util.Objects;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SentencesTranslating {
    @FXML private TextArea sourceText;
    @FXML private TextArea sinkText;
    private boolean enToVi = true;
    @FXML private Label upButton;
    @FXML private Label downButton;
    @FXML private Button translateButton;
    @FXML private Button helpButton;
    @FXML private Button dictionaryButton;
    @FXML private Button voiceButton;
    @FXML private Button alterButton;

    public SentencesTranslating() {}

    @FXML
    private void initialize() {
        prepareButtonIcon(Application.isLightMode());
    }

    /**
     * Prepare the icons of all the buttons based on the given `mode` (dark mode is 0 and light mode
     * is 1).
     *
     * @param mode light mode or dark mode icons
     */
    public void prepareButtonIcon(boolean mode) {
        String suffix = (mode ? "light" : "dark");
        ImageView translateIcon = new ImageView("icon/translate-icon-" + suffix + ".png");
        translateIcon.setFitHeight(18);
        translateIcon.setFitWidth(18);
        ImageView helpIcon = new ImageView("icon/help-icon-" + suffix + ".png");
        helpIcon.setFitHeight(18);
        helpIcon.setFitWidth(18);
        ImageView dictionaryIcon = new ImageView("icon/dictionary-icon-" + suffix + ".png");
        dictionaryIcon.setFitHeight(18);
        dictionaryIcon.setFitWidth(18);
        ImageView voiceIcon = new ImageView("icon/voice-icon-" + suffix + ".png");
        voiceIcon.setFitHeight(28);
        voiceIcon.setFitWidth(25);
        ImageView alterIcon = new ImageView("icon/alter-icon-" + suffix + ".png");
        alterIcon.setFitHeight(18);
        alterIcon.setFitWidth(18);

        translateButton.setGraphic(translateIcon);
        helpButton.setGraphic(helpIcon);
        dictionaryButton.setGraphic(dictionaryIcon);
        voiceButton.setGraphic(voiceIcon);
        alterButton.setGraphic(alterIcon);
    }

    /**
     * Translate the text from English to Vietnamese (or reverse, depends on current state `enToVi`)
     * and output the content to the sinkText.
     */
    @FXML
    public void translateEnToVi() {
        String source = sourceText.getText();
        sinkText.setText(
                (enToVi
                        ? TranslatorApi.translateEnToVi(source)
                        : TranslatorApi.translateViToEn(source)));
    }

    /**
     * Change scene from Sentences Translator to the main Application.
     *
     * @param event action event
     */
    @FXML
    public void changeToApplication(ActionEvent event) {
        try {
            Parent root =
                    FXMLLoader.load(
                            Objects.requireNonNull(
                                    getClass()
                                            .getClassLoader()
                                            .getResource("fxml/Application.fxml")));
            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets()
                    .add(
                            Objects.requireNonNull(
                                            getClass()
                                                    .getResource(
                                                            (Application.isLightMode()
                                                                    ? "/css/Application-light.css"
                                                                    : "/css/Application-dark.css")))
                                    .toExternalForm());
            appStage.setTitle("Dictionary");
            appStage.setScene(scene);
            appStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Open (popup) a window showing instruction on how to use the sentence translator.
     *
     * @param event action event
     */
    @FXML
    public void showSentencesInstruction(ActionEvent event) {
        try {
            Parent root =
                    FXMLLoader.load(
                            Objects.requireNonNull(
                                    getClass()
                                            .getClassLoader()
                                            .getResource(
                                                    "fxml/SentencesTranslatingInstructionPopup.fxml")));
            Stage senInsStage = new Stage();
            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            senInsStage.initOwner(appStage);
            Scene scene = new Scene(root);
            if (!Application.isLightMode()) {
                scene.getStylesheets()
                        .add(
                                Objects.requireNonNull(
                                                getClass().getResource("/css/General-dark.css"))
                                        .toExternalForm());
            }
            senInsStage.setTitle("Hướng dẫn sử dụng");
            senInsStage.setResizable(false);
            senInsStage.setScene(scene);
            senInsStage.initModality(Modality.APPLICATION_MODAL);
            senInsStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Play sound TTS the source text (English to Vietnamese or reverse depends on current state
     * `enToVi`).
     */
    @FXML
    public void textToSpeech() {
        String source = sourceText.getText();
        if (enToVi) {
            TextToSpeech.playSoundGoogleTranslateEnToVi(source);
        } else {
            TextToSpeech.playSoundGoogleTranslateViToEn(source);
        }
    }

    /**
     * Change the current state `enToVi` to switch between English to Vietnamese or Vietnamese to
     * English.
     */
    @FXML
    public void swapLanguage() {
        enToVi = !enToVi;
        if (enToVi) {
            upButton.setText("English");
            downButton.setText("Vietnamese");
        } else {
            upButton.setText("Vietnamese");
            downButton.setText("English");
        }
    }
}

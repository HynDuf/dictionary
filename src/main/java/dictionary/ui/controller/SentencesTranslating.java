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
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SentencesTranslating {
    @FXML private TextArea sourceText;
    @FXML private TextArea sinkText;
    private boolean enToVi = true;
    @FXML private Label upButton;
    @FXML private Label downButton;

    public SentencesTranslating() {}

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
            appStage.setTitle("Dictionary");
            appStage.setResizable(false);
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
            upButton.setText("Tiếng Anh");
            downButton.setText("Tiếng Việt");
        } else {
            upButton.setText("Tiếng Việt");
            downButton.setText("Tiếng Anh");
        }
    }
}

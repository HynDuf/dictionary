package dictionary.ui;

import dictionary.server.google_translate_api.TranslatorApi;
import java.io.IOException;
import java.util.Objects;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SentencesTranslatingController {
    @FXML private TextField sourceText;
    @FXML private TextField sinkText;

    public SentencesTranslatingController() {}

    @FXML
    public void translateEnToVi() {
        String source = sourceText.getText();
        sinkText.setText(TranslatorApi.translateEnToVi(source));
    }

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
}

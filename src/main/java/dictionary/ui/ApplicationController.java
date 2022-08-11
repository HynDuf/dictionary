package dictionary.ui;

import dictionary.server.Dictionary;
import dictionary.server.TextToSpeech;
import dictionary.server.Trie;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ApplicationController {
    private String lastLookUpWord = "";
    @FXML private TextField inputText;
    @FXML private ListView<String> searchList;
    @FXML private WebView webView;

    public ApplicationController() {}

    @FXML
    public void searchWord() {
        searchList.getItems().clear();
        String target = inputText.getText();
        ArrayList<String> searchedWords = Trie.search(target);
        for (String w : searchedWords) {
            searchList.getItems().add(w);
        }
    }

    @FXML
    public void lookUpWord() {
        String target = inputText.getText();
        lastLookUpWord = target;
        String definition = Dictionary.lookUpWord(target);
        if (definition.equals("404")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setContentText("Từ này không tồn tại!");
            alert.show();
        } else {
            webView.getEngine().loadContent(definition, "text/html");
        }
    }

    @FXML
    public void selectWord(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            String target = searchList.getSelectionModel().getSelectedItem();
            inputText.setText(target);
            lookUpWord();
        }
    }

    @FXML
    public void changeToSentencesTranslatingApplication(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                Objects.requireNonNull(getClass().getClassLoader().getResource(
                    "fxml/SentencesTranslatingApplication.fxml")));
            Stage appStage =
                (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            appStage.setTitle("Sentences Translator");
            appStage.setResizable(false);
            appStage.setScene(scene);
            appStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void playSound() {
        if (!lastLookUpWord.isEmpty()) {
            TextToSpeech.playSound(lastLookUpWord);
        }
    }

    @FXML
    public void exportToFile(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                Objects.requireNonNull(getClass().getClassLoader().getResource(
                    "fxml/ExportToFile.fxml")));
            System.out.println("Some thingggggggggggg");
            Stage appStage =
                (Stage)((Node)event.getSource()).getScene().getWindow();
            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.initOwner(appStage);
            newStage.setTitle("Export to file");
            newStage.setResizable(false);
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

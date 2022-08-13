package dictionary.ui;

import dictionary.server.Dictionary;
import dictionary.server.Trie;

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
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ApplicationController {
    public ApplicationController() {}

    @FXML private TextField inputText;
    @FXML private ListView<String> searchList;

    @FXML
    public void searchWord() {
        searchList.getItems().clear();
        String target = inputText.getText();
        ArrayList<String> searchedWords = Trie.search(target);
        for (String w : searchedWords) {
            searchList.getItems().add(w);
        }
    }

    @FXML private WebView webView;

    @FXML
    public void lookUpWord() {
        String target = inputText.getText();
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
            Parent root =
                    FXMLLoader.load(
                            Objects.requireNonNull(
                                    getClass()
                                            .getClassLoader()
                                            .getResource(
                                                    "fxml/SentencesTranslatingApplication.fxml")));
            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
    public void showInformation(ActionEvent event){
        try {
            Parent root =
                    FXMLLoader.load(
                            Objects.requireNonNull(
                                    getClass()
                                            .getClassLoader()
                                            .getResource(
                                                    "fxml/InformationPopup.fxml")));
            Stage infStage = new Stage();
            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            infStage.initOwner(appStage);
            Scene scene = new Scene(root);
            infStage.setTitle("Về ứng dụng");
            infStage.setResizable(false);
            infStage.setScene(scene);
            infStage.initModality(Modality.APPLICATION_MODAL);
            infStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showInstruction(ActionEvent event){
        try {
            Parent root =
                    FXMLLoader.load(
                            Objects.requireNonNull(
                                    getClass()
                                            .getClassLoader()
                                            .getResource(
                                                    "fxml/InstructionPopup.fxml")));
            Stage insStage = new Stage();
            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            insStage.initOwner(appStage);
            Scene scene = new Scene(root);
            insStage.setTitle("Hướng dẫn sử dụng");
            insStage.setResizable(false);
            insStage.setScene(scene);
            insStage.initModality(Modality.APPLICATION_MODAL);
            insStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addingWord(ActionEvent event){
        try {
            Parent root =
                    FXMLLoader.load(
                            Objects.requireNonNull(
                                    getClass()
                                            .getClassLoader()
                                            .getResource(
                                                    "fxml/AddingWordPopup.fxml")));
            Stage addStage = new Stage();
            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            addStage.initOwner(appStage);
            Scene scene = new Scene(root);
            addStage.setTitle("Thêm từ");
            addStage.setResizable(false);
            addStage.setScene(scene);
            addStage.initModality(Modality.APPLICATION_MODAL);
            addStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



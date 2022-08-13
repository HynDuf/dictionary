package dictionary.ui;

import dictionary.server.Dictionary;
import dictionary.server.TextToSpeech;
import dictionary.server.Trie;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
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
            appStage.setScene(scene);
            appStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void playSound() {
        if (!lastLookUpWord.isEmpty()) {
            TextToSpeech.playSoundGoogleTranslate(lastLookUpWord);
        }
    }

    @FXML
    public void exportToFile(ActionEvent event) {
        try {
            Parent root =
                    FXMLLoader.load(
                            Objects.requireNonNull(
                                    getClass()
                                            .getClassLoader()
                                            .getResource("fxml/ExportToFile.fxml")));
            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.initOwner(appStage);
            newStage.setTitle("Xuất dữ liệu từ điển");
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showInformation(ActionEvent event) {
        try {
            Parent root =
                    FXMLLoader.load(
                            Objects.requireNonNull(
                                    getClass()
                                            .getClassLoader()
                                            .getResource("fxml/InformationPopup.fxml")));
            Stage infStage = new Stage();
            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            infStage.initOwner(appStage);
            Scene scene = new Scene(root);
            infStage.setTitle("Về ứng dụng");
            infStage.setScene(scene);
            infStage.initModality(Modality.APPLICATION_MODAL);
            infStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showInstruction(ActionEvent event) {
        try {
            Parent root =
                    FXMLLoader.load(
                            Objects.requireNonNull(
                                    getClass()
                                            .getClassLoader()
                                            .getResource("fxml/InstructionPopup.fxml")));
            Stage insStage = new Stage();
            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            insStage.initOwner(appStage);
            Scene scene = new Scene(root);
            insStage.setTitle("Hướng dẫn sử dụng");
            insStage.setScene(scene);
            insStage.initModality(Modality.APPLICATION_MODAL);
            insStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void editWordDefinition(ActionEvent event) {
        if (lastLookUpWord.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Thông báo");
            alert.setContentText("Chưa chọn từ để chỉnh sửa!");
            alert.show();
            return;
        }
        EditDefinitionController.setEditingWord(lastLookUpWord);
        try {
            Parent root =
                    FXMLLoader.load(
                            Objects.requireNonNull(
                                    getClass()
                                            .getClassLoader()
                                            .getResource("fxml/EditDefinition.fxml")));
            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.initOwner(appStage);
            newStage.setTitle("Chỉnh sửa giải nghĩa của từ");
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void deleteWord() {
        if (lastLookUpWord.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Thông báo");
            alert.setContentText("Chưa chọn từ để chỉnh sửa!");
            alert.show();
        } else {

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Xóa từ");
            alert.setHeaderText(
                    "Bạn có chắc chắn muốn xóa từ `"
                            + lastLookUpWord
                            + "` khỏi từ điển hay không?");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.isPresent()) {
                if (option.get() == ButtonType.OK) {
                    if (Dictionary.deleteWord(lastLookUpWord)) {
                        Alert alert1 = new Alert(AlertType.INFORMATION);
                        alert1.setTitle("Thông báo");
                        alert1.setContentText("Xóa từ `" + lastLookUpWord + "` thành công!");
                        alert1.show();
                    } else {
                        Alert alert1 = new Alert(AlertType.ERROR);
                        alert1.setTitle("Lỗi");
                        alert1.setContentText("Xóa từ `" + lastLookUpWord + "` không thành công!");
                        alert1.show();
                    }
                }
            }
        }
    }
}

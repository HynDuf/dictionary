package dictionary.ui;

import dictionary.server.Dictionary;
import dictionary.server.History;
import dictionary.server.TextToSpeech;
import dictionary.server.Trie;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ApplicationController {
    private String lastLookUpWord = "";
    @FXML private TextField inputText;
    @FXML private ListView<String> searchList;
    @FXML private WebView webView;
    private int lastIndex = 0;

    public ApplicationController() {}

    /** Focus on the inputText TextField when first open. Prepare the search list after that. */
    @FXML
    private void initialize() {
        Platform.runLater(() -> inputText.requestFocus());
        prepareSearchList();
    }

    /**
     * Move to the search list by pressing DOWN arrow key when at the `inputText` TextField.
     *
     * @param event action event
     */
    @FXML
    public void changeFocusDown(KeyEvent event) {
        if (event.getCode() == KeyCode.DOWN) {
            searchList.requestFocus();
            if (!searchList.getItems().isEmpty()) {
                searchList.getSelectionModel().select(0);
            }
        }
    }

    /**
     * Prepare the search lists having the text in `inputText` as prefix. Words in the history base
     * appears first in the list, and they begin with a "history" icon.
     */
    public void prepareSearchList() {
        searchList.getItems().clear();
        String target = inputText.getText();
        ArrayList<String> searchedWords = Trie.search(target);
        ArrayList<String> allHistory = History.getHistorySearch();
        for (int i = allHistory.size() - 1; i >= 0; i--) {
            if (target.isEmpty() || allHistory.get(i).startsWith(target)) {
                searchList.getItems().add("#" + allHistory.get(i));
            }
        }
        for (String w : searchedWords) {
            searchList.getItems().add(w);
        }
        searchList.setCellFactory(
                new Callback<>() {

                    @Override
                    public ListCell<String> call(ListView<String> list) {
                        return new ListCell<>() {
                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else if (item != null && !item.startsWith("#")) {
                                    setText(item);
                                    setFont(Font.font(14));
                                } else if (item != null) {
                                    try {
                                        Image fxImage =
                                                new Image(
                                                        new FileInputStream(
                                                                "src/main/resources/icon/history-icon.png"));
                                        ImageView imageView = new ImageView(fxImage);
                                        imageView.setFitHeight(14);
                                        imageView.setFitWidth(14);
                                        setGraphic(imageView);
                                        setText("  " + item.substring(1));
                                        setFont(Font.font(14));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        };
                    }
                });
    }

    /** Look up the word in the dictionary and show its definition in `webView`. */
    @FXML
    public void lookUpWord() {
        String target = inputText.getText();
        if (target.startsWith("#")) {
            target = target.substring(1);
        }
        if (!target.isEmpty()) {
            History.addWordToHistory(target);
        }

        String definition = Dictionary.lookUpWord(target);
        if (definition.equals("404")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setContentText("Từ này không tồn tại!");
            alert.show();
        } else {
            lastLookUpWord = target;
            webView.getEngine().loadContent(definition, "text/html");
        }
    }

    /**
     * Look up word when pressing Enter at the selected word from the search list
     *
     * @param e key event
     */
    @FXML
    public void selectWord(KeyEvent e) {
        if (searchList.getSelectionModel().getSelectedIndices().isEmpty()) {
            return;
        }
        if (e.getCode() == KeyCode.ENTER) {
            String target = searchList.getSelectionModel().getSelectedItem();
            if (target.startsWith("#")) {
                inputText.setText(target.substring(1));
            } else {
                inputText.setText(target);
            }
            lookUpWord();
        } else if (e.getCode() == KeyCode.UP) {
            if (searchList.getSelectionModel().getSelectedIndex() == 0 && lastIndex == 0) {
                inputText.requestFocus();
            }
        }
        lastIndex = searchList.getSelectionModel().getSelectedIndex();
    }

    /**
     * Double-click a word in the search list to look up its definition.
     *
     * <p>The double-clicked word will be added to the history though.
     *
     * @param mouseEvent mouse event
     */
    @FXML
    public void selectWordDoubleClick(MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {
            String target = searchList.getSelectionModel().getSelectedItem();
            if (target.startsWith("#")) {
                inputText.setText(target.substring(1));
            } else {
                inputText.setText(target);
            }
            lookUpWord();
        }
    }

    /**
     * Change scene to sentences translating (Google Translate).
     *
     * @param event action event
     */
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

    /** Pronounce the English word that is currently showed in the `webView`. */
    @FXML
    public void playSound() {
        if (!lastLookUpWord.isEmpty()) {
            TextToSpeech.playSoundGoogleTranslateEnToVi(lastLookUpWord);
        }
    }

    /**
     * Open (pop up) export to file window for words export to file utility.
     *
     * @param event action event
     */
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

    /**
     * Open (pop up) information details of the application.
     *
     * @param event action event
     */
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

    /**
     * Open (pop up) the application instruction.
     *
     * @param event action event
     */
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

    /**
     * Open (pop up) the edit word window for the currently looked up word (in the `webView`).
     *
     * @param event action event
     */
    @FXML
    public void editWordDefinition(ActionEvent event) {
        if (lastLookUpWord.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Thông báo");
            alert.setContentText("Chưa chọn từ để chỉnh sửa!");
            alert.show();
            return;
        }
        if (Dictionary.lookUpWord(lastLookUpWord).equals("404")) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setContentText(
                    "Không tồn tại từ `" + lastLookUpWord + "` trong từ điển để chỉnh sửa!");
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

    /** Open (pop up) delete confirmation for the last looked up word (in the `webView`). */
    @FXML
    public void deleteWord() {
        if (lastLookUpWord.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Thông báo");
            alert.setContentText("Chưa chọn từ để xóa!");
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
                        alert1.setContentText(
                                "Không tồn tại từ `" + lastLookUpWord + "` trong từ điển để xóa!");
                        alert1.show();
                    }
                }
            }
        }
    }

    /**
     * Open (pop up) the add word window for adding new words to the dictionary.
     *
     * @param event action event
     */
    @FXML
    public void addingWord(ActionEvent event) {
        try {
            Parent root =
                    FXMLLoader.load(
                            Objects.requireNonNull(
                                    getClass().getClassLoader().getResource("fxml/AddWord.fxml")));
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

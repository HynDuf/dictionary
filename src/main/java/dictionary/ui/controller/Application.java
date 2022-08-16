package dictionary.ui.controller;

import static dictionary.App.dictionary;

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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
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

public class Application {
    public static boolean lightMode = true;
    private String lastLookUpWord = "";
    @FXML private TextField inputText;
    @FXML private ListView<String> searchList;
    @FXML private WebView webView;
    private int lastIndex = 0;
    private Image historyIcon;
    @FXML private Button addWordButton;
    @FXML private Button showInformationButton;
    @FXML private Button showInstructionButton;
    @FXML private Button exportButton;
    @FXML private Button pronounceButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Button googleButton;
    @FXML private Button modeToggle;

    public Application() {}

    public static boolean isLightMode() {
        return lightMode;
    }

    public static void toggleMode() {
        lightMode = !lightMode;
    }

    /** Focus on the inputText TextField when first open. Prepare the search list after that. */
    @FXML
    private void initialize() {
        Platform.runLater(() -> inputText.requestFocus());
        prepareHistoryIcon(isLightMode());
        prepareButtonIcon(isLightMode());
        prepareSearchList();
    }

    @FXML
    public void toggleModeButton() {
        toggleMode();
        if (!isLightMode()) {
            modeToggle.getScene().getStylesheets().clear();
            modeToggle.getScene().getStylesheets().add("/css/Application-dark.css");
            inputText.requestFocus();
            prepareHistoryIcon(false);
            prepareButtonIcon(false);
            if (!lastLookUpWord.isEmpty()) {
                lookUpWord();
            } else {
                webView.getEngine()
                        .loadContent(
                                "<html><body bgcolor='#262837' style='color:#babccf'></body></html>",
                                "text/html");
            }
        } else {
            modeToggle.getScene().getStylesheets().clear();
            modeToggle.getScene().getStylesheets().add("/css/Application-light.css");
            inputText.requestFocus();
            prepareHistoryIcon(true);
            prepareButtonIcon(true);
            if (!lastLookUpWord.isEmpty()) {
                lookUpWord();
            } else {
                webView.getEngine().loadContent("", "text/html");
            }
        }
        prepareSearchList();
    }

    /**
     * Prepare the icons of all the buttons based on the given `mode` (dark mode is 0 and light mode
     * is 1).
     *
     * @param mode light mode or dark mode icons
     */
    public void prepareButtonIcon(boolean mode) {
        String suffix = (mode ? "light" : "dark");
        ImageView addIcon = new ImageView("icon/add-icon-" + suffix + ".png");
        addIcon.setFitHeight(18);
        addIcon.setFitWidth(18);
        ImageView infoIcon = new ImageView("icon/info-icon-" + suffix + ".png");
        infoIcon.setFitHeight(18);
        infoIcon.setFitWidth(18);
        ImageView helpIcon = new ImageView("icon/help-icon-" + suffix + ".png");
        helpIcon.setFitHeight(18);
        helpIcon.setFitWidth(18);
        ImageView exportIcon = new ImageView("icon/export-icon-" + suffix + ".png");
        exportIcon.setFitHeight(18);
        exportIcon.setFitWidth(18);
        ImageView pronounceIcon = new ImageView("icon/pronounce-icon-" + suffix + ".png");
        pronounceIcon.setFitHeight(18);
        pronounceIcon.setFitWidth(18);
        ImageView editIcon = new ImageView("icon/edit-icon-" + suffix + ".png");
        editIcon.setFitHeight(18);
        editIcon.setFitWidth(18);
        ImageView deleteIcon = new ImageView("icon/delete-icon-" + suffix + ".png");
        deleteIcon.setFitHeight(18);
        deleteIcon.setFitWidth(18);
        ImageView googleIcon = new ImageView("icon/google-icon-" + suffix + ".png");
        googleIcon.setFitHeight(18);
        googleIcon.setFitWidth(18);
        ImageView modeIcon = new ImageView("icon/mode-icon-" + suffix + ".png");
        modeIcon.setFitHeight(30);
        modeIcon.setFitWidth(30);

        addWordButton.setGraphic(addIcon);
        showInformationButton.setGraphic(infoIcon);
        showInstructionButton.setGraphic(helpIcon);
        exportButton.setGraphic(exportIcon);
        pronounceButton.setGraphic(pronounceIcon);
        editButton.setGraphic(editIcon);
        deleteButton.setGraphic(deleteIcon);
        googleButton.setGraphic(googleIcon);
        modeToggle.setGraphic(modeIcon);
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

    /** Load the history icon into its corresponding icon image. */
    private void prepareHistoryIcon(boolean mode) {
        try {
            historyIcon =
                    new Image(
                            new FileInputStream(
                                    "src/main/resources/icon/history-icon-"
                                            + (mode ? "light" : "dark")
                                            + ".png"));

        } catch (IOException e) {
            e.printStackTrace();
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
                                if (empty || item == null) {
                                    setGraphic(null);
                                    setText(null);
                                } else if (item.charAt(0) != '#') {
                                    setGraphic(null);
                                    setText(item);
                                    setFont(Font.font(15));
                                } else {
                                    ImageView imageView = new ImageView(historyIcon);
                                    imageView.setFitHeight(15);
                                    imageView.setFitWidth(15);
                                    setGraphic(imageView);
                                    setText("  " + item.substring(1));
                                    setFont(Font.font(15));
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

        String definition = dictionary.lookUpWord(target);
        if (definition.equals("404")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            setAlertCss(alert);
            alert.setTitle("Thông báo");
            alert.setContentText("Từ này không tồn tại!");
            alert.show();
            if (!Application.isLightMode()) {
                webView.getEngine()
                        .loadContent(
                                "<html><body bgcolor='#262837' style='color:#babccf'></body></html>",
                                "text/html");
            } else {
                webView.getEngine().loadContent("", "text/html");
            }
        } else {
            lastLookUpWord = target;
            if (!Application.isLightMode()) {
                definition =
                        "<html><body bgcolor='#262837' style='color:#babccf'>"
                                + definition
                                + "</body></html>";
            }
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
    public void changeToSentencesTranslating(ActionEvent event) {
        try {
            Parent root =
                    FXMLLoader.load(
                            Objects.requireNonNull(
                                    getClass()
                                            .getClassLoader()
                                            .getResource("fxml/SentencesTranslating.fxml")));
            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            if (!Application.isLightMode()) {
                scene.getStylesheets()
                        .add(
                                Objects.requireNonNull(
                                                getClass()
                                                        .getResource(
                                                                "/css/SentencesTranslating-dark.css"))
                                        .toExternalForm());
            }
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
            setGeneralCss(scene);
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
            setGeneralCss(scene);
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
            setGeneralCss(scene);
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
            setAlertCss(alert);
            alert.setTitle("Thông báo");
            alert.setContentText("Chưa chọn từ để chỉnh sửa!");
            alert.show();
            return;
        }
        if (dictionary.lookUpWord(lastLookUpWord).equals("404")) {
            Alert alert = new Alert(AlertType.ERROR);
            setAlertCss(alert);
            alert.setTitle("Lỗi");
            alert.setContentText(
                    "Không tồn tại từ `" + lastLookUpWord + "` trong từ điển để chỉnh sửa!");
            alert.show();
            return;
        }
        EditDefinition.setEditingWord(lastLookUpWord);
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
            setGeneralCss(scene);
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
            setAlertCss(alert);
            alert.setTitle("Thông báo");
            alert.setContentText("Chưa chọn từ để xóa!");
            alert.show();
        } else {

            Alert alert = new Alert(AlertType.CONFIRMATION);
            setAlertCss(alert);
            alert.setTitle("Xóa từ");
            alert.setHeaderText(
                    "Bạn có chắc chắn muốn xóa từ `"
                            + lastLookUpWord
                            + "` khỏi từ điển hay không?");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.isPresent()) {
                if (option.get() == ButtonType.OK) {
                    if (dictionary.deleteWord(lastLookUpWord)) {
                        Alert alert1 = new Alert(AlertType.INFORMATION);
                        setAlertCss(alert1);
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
            lastLookUpWord = "";
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
            FXMLLoader loader =
                    new FXMLLoader(getClass().getClassLoader().getResource("fxml/AddWord.fxml"));
            Parent root = loader.load();
            AddWord controller = loader.getController();
            Stage addStage = new Stage();
            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            addStage.initOwner(appStage);
            Scene scene = new Scene(root);
            setGeneralCss(scene);
            addStage.setTitle("Thêm từ");
            addStage.setResizable(false);
            addStage.setScene(scene);
            addStage.initModality(Modality.APPLICATION_MODAL);
            addStage.setOnCloseRequest(e -> controller.closeWhileImporting());
            addStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setGeneralCss(Scene scene) {
        scene.getStylesheets()
                .add(
                        Objects.requireNonNull(
                                        getClass()
                                                .getResource(
                                                        (Application.isLightMode()
                                                                ? "/css/General-light.css"
                                                                : "/css/General-dark.css")))
                                .toExternalForm());
    }

    /**
     * Set CSS for alert box in case of dark mode.
     *
     * @param alert alert
     */
    private void setAlertCss(Alert alert) {
        if (!Application.isLightMode()) {
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane
                    .getStylesheets()
                    .add(
                            Objects.requireNonNull(getClass().getResource("/css/Alert-dark.css"))
                                    .toExternalForm());
            dialogPane.getStyleClass().add("alert");
        }
    }
}

package dictionary.ui.controller;

import static dictionary.App.dictionary;

import dictionary.ui.HelperUI;
import dictionary.ui.ImportWordService;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

public class AddWord {
    @FXML private Button browseButton;
    @FXML private HTMLEditor htmlEditor;
    @FXML private TextField inputText;
    @FXML private Label fileLabel;
    @FXML private AnchorPane anchorPane;
    private ImportWordService service;

    /** Focus on the `browseButton` when open the window. */
    @FXML
    private void initialize() {
        Platform.runLater(() -> browseButton.requestFocus());
        if (!Application.isLightMode()) {
            htmlEditor.setHtmlText("<body style='background-color: #262837; color: #babccf'/>");
        }
    }

    /**
     * Save the inputted word and its definition to the dictionary as HTML text format.
     *
     * @param event action event
     */
    @FXML
    public void saveWord(ActionEvent event) {
        String target = inputText.getText();
        byte[] pText = htmlEditor.getHtmlText().getBytes(StandardCharsets.ISO_8859_1);
        String definition = new String(pText, StandardCharsets.UTF_8);
        definition =
                definition.replace(
                        "<html dir=\"ltr\"><head></head><body contenteditable=\"true\">", "");
        definition = definition.replace("</body></html>", "");
        if (dictionary.insertWord(target, definition)) {
            Alert alert = new Alert(AlertType.INFORMATION);
            setAlertCss(alert);
            alert.setTitle("Thông báo");
            alert.setContentText("Thêm từ `" + target + "` thành công!");
            alert.show();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            setAlertCss(alert);
            alert.setTitle("Lỗi");
            alert.setContentText("Thêm từ `" + target + "` không thành công!");
            alert.show();
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Choose the file and print its path into the Label.
     *
     * @param event action event
     */
    @FXML
    public void chooseFile(ActionEvent event) {
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        String file = HelperUI.chooseFile(appStage);
        fileLabel.setText("  " + file);
    }

    /**
     * If user close the window while importing (hasn't finished) then the task will also cancel.
     */
    public void closeWhileImporting() {
        if (service != null) {
            service.cancel();
        }
    }

    /**
     * Import words from the selected file into the dictionary. Make a background task for this to
     * show the progress bar of the task as well.
     */
    @FXML
    public void submitImport() {
        String filePath = fileLabel.getText().strip();
        if (!filePath.isEmpty()) {
            service = new ImportWordService(filePath);
            Region veil = new Region();
            veil.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4)");
            veil.setPrefSize(657, 707);
            ProgressBar pBar = new ProgressBar();
            pBar.setPrefSize(200, 40);
            pBar.setStyle("-fx-progress-color: green;");
            pBar.setLayoutX(228.5);
            pBar.setLayoutY(333.5);
            pBar.progressProperty().bind(service.progressProperty());
            veil.visibleProperty().bind(service.runningProperty());
            pBar.visibleProperty().bind(service.runningProperty());
            anchorPane.getChildren().addAll(veil, pBar);
            service.start();
        }
    }

    /**
     * Quit the window.
     *
     * @param event action event
     */
    @FXML
    public void quitWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
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

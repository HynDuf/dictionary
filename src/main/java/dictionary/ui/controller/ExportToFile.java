package dictionary.ui.controller;

import static dictionary.App.dictionary;

import dictionary.ui.HelperUI;
import java.io.IOException;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ExportToFile {
    @FXML private Button browseButton;
    @FXML private Label dirLabel;
    @FXML private TextField fileName;

    /** Focus on the browseButton when open the window. */
    @FXML
    private void initialize() {
        Platform.runLater(() -> browseButton.requestFocus());
    }

    /**
     * Choose the directory to save the exported file to.
     *
     * @param event action event
     */
    @FXML
    public void chooseDir(ActionEvent event) {
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        String dir = HelperUI.chooseDir(appStage);
        dirLabel.setText("  " + dir);
    }

    /**
     * Export all the words into the selected directory and file name. Each line of the exported
     * file is a word with its definition separated by a TAB character.
     */
    @FXML
    public void submitExport() {
        String file = fileName.getText().strip();
        String dirPath = dirLabel.getText().strip();
        if (!dirPath.isEmpty() && !file.isEmpty()) {
            try {
                dictionary.exportToFile(dirPath + "\\" + file);
                Alert alert = new Alert(AlertType.INFORMATION);
                setAlertCss(alert);
                alert.setTitle("Thông báo");
                alert.setContentText(
                        "Thành công xuất dữ liệu ra file `" + dirPath + "\\" + file + "`");
                alert.show();
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(AlertType.ERROR);
                setAlertCss(alert);
                alert.setTitle("Lỗi");
                alert.setContentText("Không tìm thấy đường dẫn của file!");
                alert.show();
            }
        }
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

package dictionary.ui;

import dictionary.server.Dictionary;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;

public class ExportToFileController {
    @FXML private Button browseButton;
    @FXML
    private void initialize() {
        Platform.runLater(() -> browseButton.requestFocus());
    }
    @FXML private Label dirLabel;

    @FXML
    public void chooseDir(ActionEvent event) {
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        String dir = HelperUI.chooseDir(appStage);
        dirLabel.setText(dir);
    }

    @FXML private TextField fileName;
    @FXML
    public void submitExport() {
        String file = fileName.getText();
        String dirPath = dirLabel.getText();
        if (!dirPath.isEmpty() && !file.isEmpty()) {
            // Dictionary.exportAllWords(dirPath)
        }
    }
}

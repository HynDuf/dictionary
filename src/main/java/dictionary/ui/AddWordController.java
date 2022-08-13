package dictionary.ui;

import dictionary.server.Dictionary;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

public class AddWordController {
    @FXML private Button browseButton;
    @FXML
    private void initialize() {
        Platform.runLater(() -> browseButton.requestFocus());
    }
    @FXML private HTMLEditor htmlEditor;
    @FXML private TextField inputText;


    @FXML
    public void saveWord(ActionEvent event) {
        String target = inputText.getText();
        byte[] ptext = htmlEditor.getHtmlText().getBytes(StandardCharsets.ISO_8859_1);
        String definition = new String(ptext, StandardCharsets.UTF_8);
        definition =
                definition.replace(
                        "<html file=\"ltr\"><head></head><body contenteditable=\"true\">", "");
        definition = definition.replace("</body></html>", "");
        if (Dictionary.insertWord(target, definition)) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setContentText("Thêm từ `" + target + "` thành công!");
            alert.show();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setContentText("Thêm từ `" + target + "` không thành công!");
            alert.show();
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML private Label fileLabel;
    @FXML
    public void chooseFile(ActionEvent event) {
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        String file = HelperUI.chooseFile(appStage);
        fileLabel.setText(file);
    }

    @FXML
    public void submitImport(ActionEvent event) {
        String filePath = fileLabel.getText();
        if (!filePath.isEmpty()) {
            try {
                String content = Dictionary.importFromFile(filePath);
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                TextArea area = new TextArea(content);
                area.setWrapText(true);
                area.setEditable(false);
                alert.getDialogPane().setContent(area);
                alert.show();
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setContentText("Không tìm thấy đường dẫn của file `" + filePath + "`!");
                alert.show();
            }
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }
    @FXML
    public void quitWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}

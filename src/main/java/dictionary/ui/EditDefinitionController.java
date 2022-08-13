package dictionary.ui;

import dictionary.server.Dictionary;
import javafx.event.ActionEvent;
import java.nio.charset.StandardCharsets;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

public class EditDefinitionController {
    private static String editingWord;
    @FXML private Label editLabel;
    @FXML private HTMLEditor htmlEditor;

    public static void setEditingWord(String editingWord) {
        EditDefinitionController.editingWord = editingWord;
    }

    @FXML
    private void initialize() {
        editLabel.setText("Chỉnh sửa giải nghĩa của từ `" + editingWord + "`");
        htmlEditor.setHtmlText(Dictionary.lookUpWord(editingWord));
    }

    @FXML
    public void saveDefinition() {
        byte[] ptext = htmlEditor.getHtmlText().getBytes(StandardCharsets.ISO_8859_1);
        String definition = new String(ptext, StandardCharsets.UTF_8);
        definition = definition.replace("<html dir=\"ltr\"><head></head><body contenteditable=\"true\">", "");
        definition = definition.replace("</body></html>", "");
        definition = definition.replace("\"", "'");
        if (Dictionary.updateWordDefinition(editingWord, definition)) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setContentText("Cập nhật giải nghĩa của từ `" + editingWord + "` thành công!");
            alert.show();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setContentText(
                    "Cập nhật giải nghĩa của từ `" + editingWord + "` không thành công!");
            alert.show();
        }
    }

    @FXML
    public void quitWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}

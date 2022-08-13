package dictionary.ui;

import dictionary.server.Dictionary;
import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ExportToFileController {
    @FXML private Button browseButton;
    @FXML private Label dirLabel;
    @FXML private TextField fileName;

    @FXML
    private void initialize() {
        Platform.runLater(() -> browseButton.requestFocus());
    }

    @FXML
    public void chooseDir(ActionEvent event) {
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        String dir = HelperUI.chooseDir(appStage);
        dirLabel.setText(dir);
    }

    @FXML
    public void submitExport() {
        String file = fileName.getText();
        String dirPath = dirLabel.getText();
        if (!dirPath.isEmpty() && !file.isEmpty()) {
            try {
                Dictionary.exportToFile(dirPath + "\\" + file);
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setContentText("Thành công xuất dữ liệu ra file `" + dirPath + "\\" + file + "`");
                alert.show();
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setContentText("Không tìm thấy đường dẫn của file!");
                alert.show();
            }
        }
    }
}

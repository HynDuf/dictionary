package dictionary.ui;

import java.io.File;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class HelperUI {
    static final FileChooser fileChooser = new FileChooser();

    public static String chooseFile(Stage stage) {
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            return file.getAbsolutePath();
        }
        return "";
    }
}

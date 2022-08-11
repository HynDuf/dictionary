package dictionary.ui;

import java.io.File;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class HelperUI {
    static final FileChooser fileChooser = new FileChooser();
    static final DirectoryChooser dirChooser = new DirectoryChooser();

    public static String chooseFile(Stage stage) {
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            return file.getAbsolutePath();
        }
        return "";
    }

    public static String chooseDir(Stage stage) {
        File dir = dirChooser.showDialog(stage);
        if (dir != null) {
            return dir.getAbsolutePath();
        }
        return "";
    } 
}

package dictionary.ui;

import java.io.File;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class HelperUI {
    static final FileChooser fileChooser = new FileChooser();
    static final DirectoryChooser dirChooser = new DirectoryChooser();

    /**
     * Use FileChooser to open a popup window to choose the file.
     *
     * @param stage the stage to open the FileChooser from
     * @return path of the chosen file
     */
    public static String chooseFile(Stage stage) {
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            return file.getAbsolutePath();
        }
        return "";
    }

    /**
     * Use DirectoryChooser to open a popup window to choose the directory.
     *
     * @param stage the stage to open the DirectoryChooser from
     * @return path of the chosen directory
     */
    public static String chooseDir(Stage stage) {
        File dir = dirChooser.showDialog(stage);
        if (dir != null) {
            return dir.getAbsolutePath();
        }
        return "";
    }
}

package dictionary.ui;

import dictionary.App;
import dictionary.server.Helper;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ImportWordTask extends Task<Void> {
    private final String file;
    private int numWordsInserted = 0;
    private int numWords;

    public ImportWordTask(String file) {
        this.file = file;
    }

    /**
     * Import words to dictionary from `file`. Update the progress bar at the same time.
     *
     * @return nothing
     */
    @Override
    protected Void call() {
        System.out.print("File name: " + file);
        try {
            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(
                                    new FileInputStream(file), StandardCharsets.UTF_8));
            String inputLine;
            numWords = Helper.countNumLinesOfFile(file);
            while ((inputLine = in.readLine()) != null) {
                if (isCancelled()) {
                    return null;
                }
                int pos = inputLine.indexOf("\t");
                if (pos == -1) {
                    continue;
                }
                String target = inputLine.substring(0, pos).strip();
                String definition = inputLine.substring(pos + 1).strip();
                if (App.dictionary.insertWord(target, definition)) {
                    System.out.println("Inserted: " + target);
                    numWordsInserted++;
                    if (numWordsInserted % 5 == 0) {
                        updateProgress(numWordsInserted, numWords);
                    }
                }
            }
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setContentText("Không tìm thấy đường dẫn của file `" + file + "`!");
            alert.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setContentText("Không đọc được file `" + file + "`!");
            alert.show();
        }
        return null;
    }

    /** Popup a successful information box indicating the task has been successfully executed. */
    @Override
    protected void succeeded() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        String content =
                "Thành công thêm "
                        + numWordsInserted
                        + "/"
                        + numWords
                        + " từ vào từ điển.\nCó "
                        + (numWords - numWordsInserted)
                        + " từ không được thêm vào từ điển\n(bị gián đoạn, lỗi format hoặc từ đã tồn tại).";
        alert.setContentText(content);
        alert.show();
    }

    /** Popup a warning box for closing the stage while importing words. */
    @Override
    protected void cancelled() {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Thông báo");
        String content =
                "Quá trình nhập từ file bị gián đoạn.\n"
                        + "Thành công thêm "
                        + numWordsInserted
                        + "/"
                        + numWords
                        + " từ vào từ điển.\nCó "
                        + (numWords - numWordsInserted)
                        + " từ không được thêm vào từ điển\n(bị gián đoạn, lỗi format hoặc từ đã tồn tại).";
        alert.setContentText(content);
        alert.show();
    }

    /** Popup an error box indicating error found while importing the words. */
    @Override
    protected void failed() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Lỗi");
        String content =
                "Quá trình nhập từ file gặp lỗi.\n"
                        + "Thành công thêm "
                        + numWordsInserted
                        + "/"
                        + numWords
                        + " từ vào từ điển.\nCó "
                        + (numWords - numWordsInserted)
                        + " từ không được thêm vào từ điển\n(bị gián đoạn, lỗi format hoặc từ đã tồn tại).";
        alert.setContentText(content);
        alert.show();
    }
}

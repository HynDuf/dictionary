package dictionary.ui;

import dictionary.App;
import dictionary.server.Helper;
import dictionary.ui.controller.Application;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;

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
        try {
            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(
                                    new FileInputStream(file), StandardCharsets.UTF_8));
            String inputLine;
            numWords = Helper.countNumLinesOfFile(file);
            int counter = 0;
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
                counter++;
                if (App.dictionary.insertWord(target, definition)) {
                    System.out.println("Inserted: " + target);
                    numWordsInserted++;
                }
                if (counter % 5 == 0) {
                    updateProgress(counter, numWords);
                }
            }
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            setAlertCss(alert);
            alert.setTitle("Lỗi");
            alert.setContentText("Không tìm thấy đường dẫn của file `" + file + "`!");
            alert.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            setAlertCss(alert);
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
        setAlertCss(alert);
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
        setAlertCss(alert);
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
        setAlertCss(alert);
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

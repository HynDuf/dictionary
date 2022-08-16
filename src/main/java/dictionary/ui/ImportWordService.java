package dictionary.ui;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ImportWordService extends Service<Void> {
    public String file;

    public ImportWordService(String file) {
        this.file = file;
    }

    /**
     * Create and return the task for fetching the data. Note that this method is called on the
     * background thread (all other code in this application is on the JavaFX Application Thread!).
     */
    @Override
    protected Task<Void> createTask() {
        return new ImportWordTask(file);
    }
}

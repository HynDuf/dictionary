package dictionary;

import dictionary.server.DictionaryManagement;
import dictionary.server.History;
import java.util.Objects;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    /**
     * Load the MYSQL Database and History words data.
     *
     * @param args cmd arguments
     */
    public static void main(String[] args) {
        DictionaryManagement.setUpDatabase();
        History.loadHistory();
        launch();
    }

    /** Start Application. */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Dictionary");
        primaryStage.setResizable(false);
        try {
            Parent root =
                    FXMLLoader.load(
                            Objects.requireNonNull(
                                    getClass()
                                            .getClassLoader()
                                            .getResource("fxml/Application.fxml")));
            Scene scene = new Scene(root, 900, 580);
            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.setOnCloseRequest(
                    arg0 -> {
                        History.exportHistory();
                        Platform.exit();
                        System.exit(0);
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

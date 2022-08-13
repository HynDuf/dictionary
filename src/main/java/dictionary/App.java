package dictionary;

import dictionary.server.DictionaryManagement;

import dictionary.server.History;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class App extends Application {

    /** Click Run to run the application. */
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

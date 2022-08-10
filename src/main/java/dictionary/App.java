package dictionary;

import dictionary.server.DictionaryManagement;

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
        DictionaryManagement.initialize();
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
            Scene scene = new Scene(root, 900, 650);
            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.setOnCloseRequest(
                    arg0 -> {
                        Platform.exit();
                        System.exit(0);
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package dictionary.ui;

import dictionary.server.Dictionary;
import dictionary.server.Trie;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

public class ApplicationController {
    public ApplicationController() {}

    @FXML private TextField inputText;
    @FXML private ListView<String> searchList;

    @FXML
    public void searchWord() {
        searchList.getItems().clear();
        String target = inputText.getText();
        System.out.println("target: " + target);
        ArrayList<String> searchedWords = Trie.search(target);
        for (String w : searchedWords) {
            System.out.println(w);
            searchList.getItems().add(w);
        }
    }

    @FXML
    private VBox definitionBox;
    @FXML
    public void lookUpWord() {
        System.out.println("Enter!!!");
        String target = inputText.getText();
        String definition = Dictionary.lookUpWord(target);
        definitionBox.getChildren().clear();
        if (definition.equals("404")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setContentText("Từ này không tồn tại!");
            alert.show();
        } else {
            Label label = new Label();
            label.setText(target + "\n\n" + definition);
            definitionBox.getChildren().add(label);
        }
    }

    @FXML
    public void selectWord(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            String target = searchList.getSelectionModel().getSelectedItem();
            inputText.setText(target);
            lookUpWord();
        }
    }
}

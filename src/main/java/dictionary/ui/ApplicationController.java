package dictionary.ui;

import dictionary.server.Trie;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class ApplicationController {
    public ApplicationController() {}

    @FXML private TextField inputWord;
    @FXML private ListView<String> searchList;

    @FXML
    public void searchWord() {
        searchList.getItems().clear();
        String target = inputWord.getText();
        ArrayList<String> searchedWords = Trie.search(target);
        for (String w : searchedWords) {
            System.out.println(w);
            searchList.getItems().add(w);
        }
    }
}

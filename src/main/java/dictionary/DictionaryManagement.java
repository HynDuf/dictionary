package dictionary;

import java.io.File;
import java.util.Scanner;

public class DictionaryManagement {
    private static String getWord = new String();
    private static String inpWord = new String();
    private static String outWord = new String();

    /** insert word from file */
    public static void insertFromFile() throws Exception {

        /** pass the path to the file as a parameter */
        File file = new File("C:\\Users\\VHC\\Documents\\OOP\\dictionary\\src\\test.txt");

        Scanner sc = new Scanner(file);

        while (sc.hasNextLine()) {


            getWord = sc.nextLine();

            int pos = getWord.indexOf("\t");

            if (pos == -1) continue;

            inpWord = getWord.substring(0, pos);
            outWord = getWord.substring(pos + 1);
            if(App.dictionary != null) {
                App.dictionary.insertWord(inpWord, outWord);
            }
            //System.out.println(getWord);
            //System.out.println(inpWord);
            //System.out.println(outWord);

        }

    }

    public static void main(String[] args) throws Exception {

        insertFromFile();


    }

}
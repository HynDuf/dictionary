package dictionary.server;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class History {
    public static List<String> historySearch = new ArrayList<String>();

    public static void removeFirstLine(String fileName) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
        //Initial write position
        long writePosition = raf.getFilePointer();
        raf.readLine();
        // Shift the next lines upwards.
        long readPosition = raf.getFilePointer();

        byte[] buff = new byte[1024];
        int n;
        while (-1 != (n = raf.read(buff))) {
            raf.seek(writePosition);
            raf.write(buff, 0, n);
            readPosition += n;
            writePosition += n;
            raf.seek(readPosition);
        }
        raf.setLength(writePosition);
        raf.close();
    }

    public static void exportHistory(String target) {
        try {
            File file = new File("History.txt");
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            pw.println(target);
            pw.close();
            historySearch.add(target);
            //System.out.println(historySearch);
            //System.out.println(historySearch.size());
            if (historySearch.size() > 30) {
                removeFirstLine("History.txt");
                historySearch.remove(0);
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("An error occurred.`");
        }
    }

    public static void insertHistory() {
        try {
            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(
                                    new FileInputStream("History.txt"),
                                    StandardCharsets.UTF_8));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                historySearch.add(inputLine);
                //System.out.println(inputLine);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Couldn't find " + "History.txt");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Couldn't read " + "History.txt");
        }
    }

    public static List<String> getHistorySearch() {
        return historySearch;
    }
}

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;

class Homework6 {

    //New header with number of words
    public static final String[] HEADER = {"ID", "Text", "Class", "ScoreTag", "Agreement", "Irony", "Confidence", "Subjectivity", "Fear", "Excited", "Bored", "Happy", "Angry", "Sad", "nbOfChars"};

    //Re Add text to Testing.csv (As I did not found the file processed with the text column
    //Use the final file Testing.csv and the file downloaded from Kaggle
    public static void reAddTexts(String in, String in2, String out) {
        try {
            CSVReader reader = new CSVReaderBuilder(new FileReader(in)).withSkipLines(1).build();
            CSVReader reader2 = new CSVReaderBuilder(new FileReader(in2)).withSkipLines(1).build();
            CSVWriter writer = new CSVWriter(new FileWriter(out));
            writer.writeNext(Homework5.HEADER);

            String[] entries = null;
            while ((entries = reader.readNext()) != null) {
                String[] entries2 = reader2.readNext();
                ArrayList<String> list = new ArrayList<>(Arrays.asList(entries));
                ArrayList<String> list2 = new ArrayList<>();
                list2.add(list.get(0));
                list2.add(entries2[1]);
                for(int i=1; i<list.size(); ++i) list2.add(list.get(i));
                writer.writeNext(list2.toArray(new String[0]));
            }

            reader.close();
            writer.close();
        }
        catch (Exception e) {e.printStackTrace();}
    }

    //in the path of the file to read
    //out the path of the output created
    //Copy all lines of the entry in the output adding for each line the number of characters that contains the text column
    public static void addNbOfChars(String in, String out) {
        try {
            CSVReader reader = new CSVReaderBuilder(new FileReader(in)).withSkipLines(1).build();
            CSVWriter writer = new CSVWriter(new FileWriter(out));
            writer.writeNext(HEADER);
            String[] entries = null;
            while ((entries = reader.readNext()) != null) {
                ArrayList<String> list = new ArrayList<>(Arrays.asList(entries));
                int nbOfChars = countNbOfChars(list.get(1));
                list.add(Integer.toString(nbOfChars));
                writer.writeNext(list.toArray(new String[0]));
            }
            reader.close();
            writer.close();
        }
        catch (Exception e) {e.printStackTrace();}
    }

    public static int countNbOfChars(String t) {
        int count = 0;
        for (int i = 0; i < t.length(); i++) {
            if (t.charAt(i) != ' ') count++;
        }
        return count;
    }
}
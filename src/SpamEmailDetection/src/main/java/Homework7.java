import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;


class Homework7 {
    //New header with number of words
    public static final String[] HEADER = {"ID", "Text", "Class", "ScoreTag", "Agreement", "Irony", "Confidence", "Subjectivity", "Fear", "Excited", "Bored", "Happy", "Angry", "Sad", "nbOfChars", "nbOfSymbols"};

    //in the path of the file to read
    //out the path of the output created
    //Copy all lines of the entry in the output adding for each line the number of symbols (all things that is not a char) that contains the text column
    public static void addNbOfSymbols(String in, String out) {
        try {
            CSVReader reader = new CSVReaderBuilder(new FileReader(in)).withSkipLines(1).build();
            CSVWriter writer = new CSVWriter(new FileWriter(out));
            writer.writeNext(HEADER);
            String[] entries = null;
            while ((entries = reader.readNext()) != null) {
                ArrayList<String> list = new ArrayList<>(Arrays.asList(entries));
                int nbOfChars = countNbOfSymbs(list.get(1));
                list.add(Integer.toString(nbOfChars));
                writer.writeNext(list.toArray(new String[0]));
            }
            reader.close();
            writer.close();
        }
        catch (Exception e) {e.printStackTrace();}
    }

    private static int countNbOfSymbs(String t) {
        int count = 0;
        for(int i = 0; i < t.length(); i++) {
            if (t.charAt(i) != ' ')
            {
                if (!Character.isLetterOrDigit(t.charAt(i))) count++;
            }
        }
        return count;
    }
}
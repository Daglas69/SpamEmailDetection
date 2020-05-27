import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;


class Homework9 {
    //New header with number of words
    public static final String[] HEADER = {"ID", "Text", "Class", "ScoreTag", "Agreement", "Irony", "Confidence", "Subjectivity", "Fear", "Excited", "Bored", "Happy", "Angry", "Sad", "nbOfChars", "nbOfSymbols", "wordDensity", "nbUniqueWords"};

    //in the path of the file to read
    //out the path of the output created
    //Copy all lines of the entry in the output adding for each line the number of unique words as a new column
    public static void addNbOfUniqueWords(String in, String out) {
        try {
            CSVReader reader = new CSVReaderBuilder(new FileReader(in)).withSkipLines(1).build();
            CSVWriter writer = new CSVWriter(new FileWriter(out));
            writer.writeNext(HEADER);
            String[] entries = null;
            while ((entries = reader.readNext()) != null)
            {
                ArrayList<String> list = new ArrayList<>(Arrays.asList(entries));
                int nbUniqueWords = nbOfUniqueWords(list.get(1));
                list.add(Float.toString(nbUniqueWords));
                writer.writeNext(list.toArray(new String[0]));
            }
            reader.close();
            writer.close();
        }
        catch (Exception e) {e.printStackTrace();}
    }

    private static int nbOfUniqueWords(String s)
    {
        String [] arr = s.split("\\s+");
        ArrayList<String> uniq = new ArrayList<>();
        for (String value : arr)
        {
            if (!uniq.contains(value)) uniq.add(value);
        }
        return uniq.size();
    }
}

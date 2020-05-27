import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;


class Homework2 {
    public static void adaptForWeka(String file, String out) {
        try {
            CSVReader reader = new CSVReaderBuilder(new FileReader(file)).build();
            CSVWriter writer = new CSVWriter(new FileWriter(out), ',', CSVWriter.NO_QUOTE_CHARACTER);
            String[] entries = null;
            while ((entries = reader.readNext()) != null)
            {
                ArrayList<String> list = new ArrayList<>(Arrays.asList(entries));
                list.remove(1); //Remove text column
                writer.writeNext(list.toArray(new String[0]));
            }
            reader.close();
            writer.close();
        }
        catch (Exception e) {e.printStackTrace();}
    }
}
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

/* This is the main class of the project "Spam Email Detection", for the
 * Intelligent Systems course, Tecnologico de Monterrey, campus Puebla.
 * All the code used for each homework has been written in a separated class.
 * FinalProject cass contains the code used for the proposed solution to the project.
 */
public class Main {

    //Used as a test function
    //Replace the if statement with what you want to test
    static void testingErrors(String file)
    {
        try {
            //CSVReader reader = new CSVReaderBuilder(new FileReader(file)).withSkipLines(1).build();
            CSVReader reader = new CSVReaderBuilder(new FileReader(file)).build();
            int nb = 0;
            String[] entries = null;
            while ((entries = reader.readNext()) != null)
            {
                ArrayList<String> list = new ArrayList<>(Arrays.asList(entries));

                if (list.size() != FinalProject.HEADER.length-1)
                {
                    System.out.println("ERROR LINE : "+nb);
                }
                nb++;
            }
            System.out.println("[END] FINAL LINE : " + nb);
            reader.close();
        }
        catch (Exception e) {e.printStackTrace();}
    }


    public static void main(String [] args)
    {
        FinalProject.adaptForWeka("in/dTesting.csv", "out/Testing.csv");
        FinalProject.adaptForWeka("in/Training.csv", "out/Training.csv");
    }
}

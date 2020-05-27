import com.opencsv.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;


/* This Java class is used to submit a file to Kaggle competition "Spam Email Detection",
 * For the Intelligent Systems course, being teaching by Dr. Octavio Loyola-Gonzalez.
 * The usage is very specific and tricky as it was for private use. It can be easily improved but it was not useful in my case.
 *
 * removeLines:
 * Remove extra lines from the Weka result buffer and only keep csv output
 *
 * trainingFileToCsv:
 * Connect Email row ID (From DO_NOT_REMOVE.csv) to its corresponding prediction (From Weka Result Buffer).
 * Currently, Weka use the same order the file to classify but with different IDs.
 *
 * process:
 * Call removeLines and trainingFileToCsv.
 * Use the Kaggle API to submit the new processed file.
 *
 * Precautions:
 * 1. Weka result buffer needs to be printed WITHOUT any options activated in the settings, except csv output.
 * 2. Do not remove the file DO_NOT_REMOVE.csv in root directory. It is used to get actual Email row ID.
 * 3. Submitted file will be the same file that the one saved to the parent directory.
 */
public class ToKaggle {

    private static final String TEMP = "temp.csv";
    private static final String TESTING_FILE = "DO_NOT_REMOVE.csv";

    private static final int ATTRS_NB = 3; //the whole number (ID, Class included)
    private static final String FILE_DIR = "../";
    private static final String FILE_TO_PROCESS = "";
    private static final String SUBMISSION_MSG = "";

    private static void removeLines(String file)
    {
        try {
            int firstLine = ATTRS_NB + 10;
            int lastLine = firstLine + 1037;

            File out = new File(TEMP);
            File in = new File(file);
            out.delete();
            out.createNewFile();
            FileWriter writer = new FileWriter(TEMP,false);
            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line= null; int count=1;
            while ((line = bufferedReader.readLine()) != null) {
                if (count>firstLine && count<lastLine) writer.write(line+"\r\n");
                count++;
            }
            reader.close(); writer.close();
            in.delete();
        }
        catch (Exception e) {e.printStackTrace();}
    }

    private static void trainingFileToCsv(String file)
    {
        try {
            //To read ID from file 1
            CSVReader reader1 = new CSVReaderBuilder(new FileReader(TESTING_FILE)).withSkipLines(1).build();
            //To read predicted class from file 2
            CSVReader reader2 = new CSVReaderBuilder(new FileReader(TEMP)).withSkipLines(1).build();
            //To write the new file
            CSVWriter writer = new CSVWriter(new FileWriter(file),',',CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
            writer.writeNext(new String[]{"ID","Class"}); //New header

            String[] entries1 = null; String[] entries2 = null;
            while ((entries2 = reader2.readNext()) != null && (entries1 = reader1.readNext()) != null)
            {
                ArrayList<String> list = new ArrayList<>(Arrays.asList(entries2)); //File 2
                ArrayList<String> list2 = new ArrayList<>();
                list2.add(entries1[0]); //Id of the line
                list2.add(list.get(2).split(":")[1]); //Prediction of the weka file (from a format x:y, and we only want y)
                writer.writeNext(list2.toArray(new String[0]));
            }
            reader1.close();
            reader2.close();
            writer.close();
            new File(TEMP).delete();
        }
        catch (Exception e) {e.printStackTrace();}
    }

    private static void process(String file, String msg) throws IOException, InterruptedException {
        removeLines(file);
        trainingFileToCsv(file);

        String cmd = "kaggle competitions submit -c detecting-spam-emails -f " + file + " -m '" + msg + "'";
        System.out.println("Executing command: " + cmd);

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "-c", cmd);
        Process p = processBuilder.start();
        p.waitFor();
    }

    public static void main(String [] args) throws IOException, InterruptedException {
        process(FILE_DIR + FILE_TO_PROCESS, SUBMISSION_MSG);
    }
}
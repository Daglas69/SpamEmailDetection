import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import org.json.simple.JSONObject;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;


class Homework3 {

    //Constants containing file path to process
    public static final String HW3_PATH = "homework3/";
    public static final String IN_FILE = "../homework3/in/Testing.csv";
    public static final String OUT_FILE = "../homework3/out/Testing.csv";

    //CSV Header
    private static final String[] HEADER = new String[]{"ID", "Text", "Class", "Fear", "Excited", "Bored", "Happy", "Angry"};

    //Class from first Homework
    private Homework1 hw = new Homework1();


    //Process a file adding emotions to each emails
    public void processFile(String in, String out)
    {
        try {
            //Provided by openCSV library
            CSVReader reader = new CSVReaderBuilder(new FileReader(in)).withSkipLines(1).build();
            CSVWriter writer = new CSVWriter(new FileWriter(out));

            //We firstly write the header in the output
            writer.writeNext(HEADER);

            int counter = 0; //To count the current line processed
            int nbEmails = Homework1.totalLines(in); //Total number of lines that contains the input

            //Used because I can't make more than 250 requests per day (to limit the process)
            int TEMP = 0;

            //Loop reading line one by one until the end of the file
            String[] entries = null;
            while ((entries = reader.readNext()) != null)
            {
                counter++;

                //We convert the line read as an array list
                ArrayList<String> list = new ArrayList<>(Arrays.asList(entries));

                //All columns filled -> The line has been treated
                if (list.size()>=HEADER.length)
                {
                    System.out.print("[INFO] Email ID [ " + entries[0] + " ] has been already processed. ");
                }
                else if(TEMP<=250) //TEMP var due to the request restriction with the API parallelDots
                {
                    TEMP++;
                    JSONObject emotions = hw.getEmotions(entries[1]);

                    //We convert emotions as a string array according to header order
                    if (emotions == null) System.out.print("NULL ");
                    String [] e = (emotions == null)
                            ? new String[]{"?", "?", "?", "?", "?"}
                            : new String[]{emotions.get("Fear").toString(), emotions.get("Excited").toString(), emotions.get("Bored").toString(), emotions.get("Happy").toString(), emotions.get("Angry").toString()};;
                    list.addAll(Arrays.asList(e)); //Added to the line

                    System.out.print("[INFO] Email ID [ " + entries[0] + " ] has been processed. ");
                }

                //We write the line in the output
                writer.writeNext(list.toArray(new String[0]));
                System.out.println(counter + " / " + nbEmails + " emails processed.");
            }
            writer.close();
            reader.close();
        }
        catch (Exception e) {
            System.out.println("[WARNING] Something went wrong...");
            e.printStackTrace();
        }
    }


    //Used to process another time the file and catch errors in processing due to API restriction or network error
    public void processFile2(String in, String out)
    {
        try {
            int nb = 0;
            CSVReader reader = new CSVReaderBuilder(new FileReader(in)).withSkipLines(1).build();
            CSVWriter writer = new CSVWriter(new FileWriter(out));
            writer.writeNext(HEADER);
            String[] entries = null;
            while ((entries = reader.readNext()) != null) {
                nb++;
                ArrayList<String> list = new ArrayList<>(Arrays.asList(entries));

                if (list.get(3).equals("?"))
                {
                    ArrayList<String> list2 = new ArrayList<>();
                    for(int i=0; i<9; i++) list2.add(list.get(i));
                    JSONObject emotions = hw.getEmotions(entries[1]);
                    if (emotions == null) System.out.print("NULL ");
                    String [] e = (emotions == null)
                            ? new String[]{"?", "?", "?", "?", "?"}
                            : new String[]{emotions.get("Fear").toString(), emotions.get("Excited").toString(), emotions.get("Bored").toString(), emotions.get("Happy").toString(), emotions.get("Angry").toString()};;
                    list2.addAll(Arrays.asList(e)); //Added to the line
                    writer.writeNext(list2.toArray(new String[0]));
                    System.out.println("LINE "+nb+" PROCESSED");
                }
                else
                {
                    writer.writeNext(list.toArray(new String[0]));
                    System.out.println("LINE "+nb+" ALREADY PROCESSED");
                }
            }
            reader.close();
            writer.close();
        }
        catch (Exception e) {e.printStackTrace();}
    }


    //It takes an input and creates an output without the 5 sentiment columns and the class column
    public static void removeSentiments(String in, String out)
    {
        try {
            CSVReader reader = new CSVReaderBuilder(new FileReader(in)).build();
            CSVWriter writer = new CSVWriter(new FileWriter(out), ',', CSVWriter.NO_QUOTE_CHARACTER);
            String[] entries = null;
            while ((entries = reader.readNext()) != null)
            {
                ArrayList<String> list = new ArrayList<>(Arrays.asList(entries));
                //We remove sentiments columns (index is always 1 because list is dynamic)
                for (int i=0; i<5; ++i) list.remove(2);
                writer.writeNext(list.toArray(new String[0]));
            }
            reader.close();
            writer.close();
        }
        catch (Exception e) {e.printStackTrace();}
    }


    //in1 -> First input containing ID of instances
    //in2 -> Second input containing predicted classes
    //out -> output with ID's associated to predicted classes
    //Take a file1.csv containing a column ID and a file2.csv containing predicted classes as inputs
    //The file 2 is the weka output of the file 1 for K-NN training
    //Make a output associating ID to predicted class
    public static void trainingFileToCsv(String in1, String in2, String out)
    {
        try {
            //To read ID from file 1
            CSVReader reader1 = new CSVReaderBuilder(new FileReader(in1)).withSkipLines(1).build();
            //To read predicted class from file 2
            CSVReader reader2 = new CSVReaderBuilder(new FileReader(in2)).withSkipLines(1).build();
            //To write the new file
            CSVWriter writer = new CSVWriter(new FileWriter(out), ',', CSVWriter.NO_QUOTE_CHARACTER);
            writer.writeNext(new String[]{"ID","Class"}); //New header

            String[] entries1 = null; String[] entries2 = null;
            while ((entries2 = reader2.readNext()) != null && (entries1 = reader1.readNext()) != null)
            {
                ArrayList<String> list = new ArrayList<>(Arrays.asList(entries2)); //File 2
                list.remove(4); //Prediction column
                list.remove(3); //Error column
                list.remove(1); //Actual column
                list.set(1, list.get(1).split(":")[1]); //Format of the col -> x:y and we replace it with y only (the class)
                list.set(0, entries1[0]); //We replace the instance column with the ID associated (read in the first input)
                writer.writeNext(list.toArray(new String[0]));
            }
            reader1.close();
            reader2.close();
            writer.close();
        }
        catch (Exception e) {e.printStackTrace();}
    }
}
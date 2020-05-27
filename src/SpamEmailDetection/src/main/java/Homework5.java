import com.meaningcloud.SentimentResponse;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;


class Homework5 {

    //Input and Output
    private static final String IN_FILE = "in/Training.csv";
    private static final String OUT_FILE = "out/Training.csv";

    //Header structure of the output
    public static final String[] HEADER = {"ID", "Text", "Class", "ScoreTag", "Agreement", "Irony", "Confidence", "Subjectivity", "Fear", "Excited", "Bored", "Happy", "Angry", "Sad"};

    Homework1 hw = new Homework1();


    //Take an input file to process and create an output file containing the input processed
    //To process only sentiments
    public void processFile(String in, String out) {
        try {
            //Provided by openCSV library
            CSVReader reader = new CSVReaderBuilder(new FileReader(in)).withSkipLines(1).build();
            CSVWriter writer = new CSVWriter(new FileWriter(out));

            //We firstly write the header in the output
            writer.writeNext(HEADER);

            int counter = 0; //To count the current line processed
            int nbEmails = Homework1.totalLines(in); //Total number of lines that contains the input

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
                    writer.writeNext(list.toArray(new String[0]));
                }
                else
                {
                    ArrayList<String> list2 = new ArrayList<>();
                    for (int i=0; i<3; ++i) list2.add(list.get(i));

                    SentimentResponse sentiments = hw.getSentiments(entries[1]);

                    //We convert sentiments as a string array according to header order
                    String[] s = (!sentiments.status.code.equals("0")) //If the status code != 0 -> Error in the request
                            ? new String[]{"?", "?", "?", "?", "?"}
                            : new String[]{sentiments.getScoreTag(), sentiments.getAgreement(), sentiments.getIrony(), String.valueOf(sentiments.getConfidence()), sentiments.getSubjectivity()};
                    list2.addAll(Arrays.asList(s)); //Added to the line
                    for (int i=3; i<9; ++i) list2.add(list.get(i));
                    writer.writeNext(list2.toArray(new String[0]));
                    System.out.print("[INFO] Email ID [ " + entries[0] + " ] has been processed. ");
                }

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

                if (list.get(5).equals("?"))
                {
                    ArrayList<String> list2 = new ArrayList<>();
                    for(int i=0; i<3; i++) list2.add(list.get(i));
                    SentimentResponse sentiments = hw.getSentiments(entries[1]);
                    String[] s = (!sentiments.status.code.equals("0")) //If the status code != 0 -> Error in the request
                            ? new String[]{"?", "?", "?", "?", "?"}
                            : new String[]{sentiments.getScoreTag(), sentiments.getAgreement(), sentiments.getIrony(), String.valueOf(sentiments.getConfidence()), sentiments.getSubjectivity()};
                    list2.addAll(Arrays.asList(s)); //Added to the line
                    for(int i=8; i<14; i++) list2.add(list.get(i));
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
}
import com.meaningcloud.*;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.paralleldots.paralleldots.App;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;


class Homework1 {
    //Header structure of the output
    public static final String[] HEADER = {"ID", "Text", "Class", "ScoreTag", "Agreement", "Irony", "Confidence", "Subjectivity", "Fear", "Excited", "Bored", "Happy", "Angry"};

    //API KEYS to make requests to API's
    public static final String APIKEY_MC = "c52098445964a57c5f8430e321226861"; //579588b63f0be3c5ce4727a64dda4e63 c52098445964a57c5f8430e321226861
    public static final String APIKEY_PD = "arku5xZwlMRUZeYS1NQOamwr4uVgFBWzIcZ4ynUWwT8"; //mqfzTSjSxv3dRm29QM1tnJ9OH8DlpgKylL2TIXhJ5rk MyG0cn9VTUR8wrmIJ8hEJTrTQZxrf7wEME5h33H6RvY vD7p6u61TLiIM5cYNftwtA2aW8gSQY1T4DvaNtuf4YY JLANaaPheYQmK9ZXOyHx27Qzpha5IFQoJ2y5I21U8rQ  R3lOfOcJEn8dUpcGjaocgnphthU8CDWvZFfx48YKo0g  THdyQhyHMc5CqRieDsiz7EusCvvuKnC1NglUdkRoehA arku5xZwlMRUZeYS1NQOamwr4uVgFBWzIcZ4ynUWwT8 R6htDO2C9zdVbBNUI9DXqpBLr9U1rQQtXjcVcokPeFk jJlh5j5CvZrCKfmNYsUkMirNh0V41AKlEfhHBAJlZXQ EdWK6eRpufnFpVXaQfbv87hJ9oirj1p3EYKmjrys5zo

    //App object provided by parallelDots API
    private App pd = new App(APIKEY_PD);


    //Make a request to parallelDots API and return a JSON object containing emotions of a text given
    JSONObject getEmotions(String text) throws Exception {
        return (JSONObject) ((JSONObject) (new JSONParser()).parse(pd.emotion(text))).get("emotion");
    }


    //Make a request to meaningcloud API and return an SentimentResponse object containing sentiments of a text given
    SentimentResponse getSentiments(String text) throws Exception {
        return SentimentRequest.build(APIKEY_MC, Request.Language.EN).withText(text).send();
    }


    //Take an input file to process and create an output file containing the input processed
    void processFile(String in, String out) {
        try {
            //Provided by openCSV library
            CSVReader reader = new CSVReaderBuilder(new FileReader(in)).withSkipLines(1).build();
            CSVWriter writer = new CSVWriter(new FileWriter(out));

            //We firstly write the header in the output
            writer.writeNext(HEADER);

            int counter = 0; //To count the current line processed
            int nbEmails = totalLines(in); //Total number of lines that contains the input

            //Used because I can't make more than 500 requests per day (to limit the process)
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
                    JSONObject emotions = getEmotions(entries[1]);
                    SentimentResponse sentiments = getSentiments(entries[1]);

                    //We convert sentiments as a string array according to header order
                    String[] s = (!sentiments.status.code.equals("0")) //If the status code != 0 -> Error in the request
                        ? new String[]{"?", "?", "?", "?", "?"}
                        : new String[]{sentiments.getScoreTag(), sentiments.getAgreement(), sentiments.getIrony(), String.valueOf(sentiments.getConfidence()), sentiments.getSubjectivity()};
                    list.addAll(Arrays.asList(s)); //Added to the line

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


    private static void countProcessedLines(String file)
    {
        try {
            CSVReader reader = new CSVReaderBuilder(new FileReader(file)).withSkipLines(1).build();
            String[] entries = null;
            int processedLines = 0;

            while ((entries = reader.readNext()) != null)
            {
                ArrayList<String> list = new ArrayList<>(Arrays.asList(entries));
                if (list.size()>3) processedLines++;
            }
            System.out.println("[INFO] The file " + file + " has " + processedLines + " processed lines out of a total of " + totalLines(file) + " lines.");
            reader.close();
        }
        catch (Exception e) {e.printStackTrace();}
    }


    public static int totalLines(String file)
    {
        int lines = 0;
        try {
            CSVReader reader = new CSVReaderBuilder(new FileReader(file)).build();
            while (reader.readNext() != null) lines++;
            reader.close();
        }
        catch (Exception e) {e.printStackTrace();}
        return lines;
    }
}
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;

class FinalProject {
    public static final String[] HEADER = {
            "ID", "Text", "Class",
            "ScoreTag", "Agreement", "Irony",
            "Confidence", "Subjectivity", "Fear",
            "Excited", "Bored", "Happy",
            "Angry", "Sad", "nbOfChars",
            "nbOfSymbols", "wordDensity", "nbUniqueWords",
            "nbOfLinks", "nbOfUniqLinks","nbOfLetterWords"
    };

    //in the path of the file to read
    //out the path of the output created
    //Copy all lines of the entry in the output adding for each line additional features
    public static void addAdditionalFeatures(String in, String out) {
        try {
            CSVReader reader = new CSVReaderBuilder(new FileReader(in)).withSkipLines(1).build();
            CSVWriter writer = new CSVWriter(new FileWriter(out));
            writer.writeNext(HEADER);
            String[] entries = null;
            while ((entries = reader.readNext()) != null)
            {
                ArrayList<String> list = new ArrayList<>(Arrays.asList(entries));
                int nbLinks = nbOfLinks(list.get(1));
                int nbUniqLinks = nbOfUniqLinks(list.get(1));
                int nbOfLetterWords = nbOfLetterWords(list.get(1));
                list.add(Integer.toString(nbLinks));
                list.add(Integer.toString(nbUniqLinks));
                list.add(Integer.toString(nbOfLetterWords));
                writer.writeNext(list.toArray(new String[0]));
            }
            reader.close();
            writer.close();
        }
        catch (Exception e) {e.printStackTrace();}
    }

	//Count the number of http links found in a String
    private static int nbOfLinks(String s) {
        String [] arr = s.split("\\s+");
        int nb = 0;
        for (String value : arr)
        {
            if (value.contains("http")) nb++;
        }
        return nb;
    }

	//Count the number of unique http link found in a String
    private static int nbOfUniqLinks(String s) {
        String [] arr = s.split("\\s+");
        ArrayList<String> uniq = new ArrayList<>();
        for (int i = 0; i < arr.length; ++i)
        {
            if (arr[i].contains("http") && arr[i+1].contains(":")
                    && arr[i+2].contains("/") && arr[i+3].contains("/"))
            {
                StringBuilder link = new StringBuilder();
                int dot = 0; int a = 4;
                while (dot == 0)
                {
                    if (i+a>=arr.length) dot = 1;
                    else {
                        if (arr[i + a].contains(".")) {
                            dot = 1;
                        }
                        link.append(arr[i + a]);
                        a++;
                    }
                }
                if (!uniq.contains(link.toString())) uniq.add(link.toString());
            }
        }
        return uniq.size();
    }

	//Count the number of word containing only letters in a String
    private static int nbOfLetterWords(String s) {
        String [] arr = s.split("\\s+");
        int nb = 0;
        for (String value : arr)
        {
            if (value.matches("[a-zA-Z]+"))
            {
                nb++;
            }
        }
        return nb;
    }

    //Transform the .csv file into a Weka readable file
    //Only keep the ID, text field and Class columns
	//Enclose the text field with '' to make it readable by Weka
    public static void adaptForWeka(String file, String out) {
        try {
            CSVReader reader = new CSVReaderBuilder(new FileReader(file)).build();
            CSVWriter writer = new CSVWriter(new FileWriter(out), ',', CSVWriter.NO_QUOTE_CHARACTER);
            String[] entries = null;
            while ((entries = reader.readNext()) != null)
            {
                ArrayList<String> list = new ArrayList<>(Arrays.asList(entries));
                String[] line = new String[]{list.get(0), "'"+adaptText(list.get(1))+"'", list.get(2)};
                writer.writeNext(line);
            }
            reader.close();
            writer.close();
        }
        catch (Exception e) {e.printStackTrace();}
    }

    //Add a \ char before any ' in a String
    //To be able to open the file in weka
    private static String adaptText(String text)
    {
        return text.replaceAll("'", "\\\\'");
    }
}
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

/**
 * receives a file and adds each relevant string line to a list
 */
public class FirstParser {

    // a list that holds all the relevant lines of the text
    private LinkedList<String> globalList;

    private static final String EMPTY_LINE = "\\s*";
    private static final String COMMENT_LINE = "//";


    /**
     * constructor
     * @param file - the file to parse
     */
    public FirstParser(File file){
        globalList = new LinkedList<>();
    }

    /**
     *
     * @param file - the file to parse
     * @return a list of strings, each string is a line of code (and not an empty line or comment line)
     * @throws IOException - if the file is not readable
     */
    LinkedList<String> addLines(File file) throws IOException { //TODO - specify the exception+message
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            while (line != null){
                if (!line.matches(EMPTY_LINE) && !line.startsWith(COMMENT_LINE)){
                    globalList.add(line);
                }
            }
            bufferedReader.close();
        }
        catch (IOException e) {
            throw new IOException(); //TODO specify a message
        }
        return globalList;
    }





}
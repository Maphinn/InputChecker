import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class SequenceParser {

    private String sequenceFile;
    private ArrayList<SequenceEntry> result;

    SequenceParser(String filename){
        sequenceFile = filename;
        result = new ArrayList<>();
    }

    private char nextChar (Scanner in) {
        return in.next().charAt(0);
    }

    private boolean nextCharIs (Scanner in, char c) {
        return in.hasNext(Pattern.quote(c + ""));
    }

    private void readNextchar(Scanner in, char c) throws Exception {
        if (nextCharIs(in, c)) {
            nextChar(in);
        } else {
            throw new Exception("Error: '" + c + "' was expected.\n");
        }
    }

    private void skipCharacter(Scanner in) {
        if (nextCharIs(in, ' ')) {
            nextChar(in);
        }
    }

    private long processTime(Scanner in) throws Exception {
        readNextchar(in,'"');
        long minutes = in.nextInt() * 10;
        minutes += in.nextInt();
        readNextchar(in, ':');
        long seconds = in.nextInt() * 10;
        seconds += in.nextInt();
        readNextchar(in, ':');
        long milisec = in.nextInt() * 100;
        milisec += in.nextInt() * 10;
        milisec += in.nextInt();
        readNextchar(in,'"');
        return milisec + seconds * 1000 + minutes * 60000;
    }

    private String processAction(Scanner in) throws Exception {
        readNextchar(in, '"');
        StringBuilder data = new StringBuilder();
        while (!nextCharIs(in, '"')){
            data.append(nextChar(in));
        }
        readNextchar(in, '"');
        return data.toString();
    }

    private void readExpression(Scanner exprScanner) throws Exception {
        int action = exprScanner.nextInt();
        readNextchar(exprScanner,',');
        skipCharacter(exprScanner);
        String data = processAction(exprScanner);
        readNextchar(exprScanner,',');
        skipCharacter(exprScanner);
        long time = processTime(exprScanner);
        result.add(new SequenceEntry(action, data, time));
    }

    private void processLine(Scanner lineScanner) throws Exception {
        readNextchar(lineScanner, '{');
        readExpression(lineScanner);
        readNextchar(lineScanner, '}');
    }

    private void processFile(Scanner fileScanner) throws Exception {
        readNextchar(fileScanner, '[');
        while (fileScanner.hasNextLine() && nextCharIs(fileScanner, '{')) {
            processLine(fileScanner);
            if (!nextCharIs(fileScanner, ']')) {
                readNextchar(fileScanner, ',');
                skipCharacter(fileScanner);
                readNextchar(fileScanner, '\n');
            } else {
                break;
            }
        }
        readNextchar(fileScanner, ']');
    }

    // Convert the input sequence file to a list.
    public ArrayList<SequenceEntry> parse() {
        File file = new File(sequenceFile);
        Scanner inputScanner = null;
        try {
            inputScanner = new Scanner(file);
            inputScanner.useDelimiter("");
            processFile(inputScanner);
            inputScanner.close();
        } catch (Exception e) {
            System.err.println("Error reading sequence file: \"" + sequenceFile + "\"");
            System.err.println(e.getMessage());
            if (inputScanner != null) {
                System.out.println("'" + inputScanner.next() + "'");
            }
            System.exit(-1);
        }
        return result;
    }
}

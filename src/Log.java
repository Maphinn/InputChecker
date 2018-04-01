import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Log {

    private static final String ANSI_DEFAULT = "\u001B[34m";
    private static final String ANSI_RED     = "\u001B[31m";
    private static final String ANSI_GREEN   = "\u001B[32m";
    private static final String ANSI_PURPLE  = "\u001B[35m";
    private static final String ANSI_RESET   = "\u001B[0m";

    private ArrayList<String> list;
    private Boolean firstAction;
    private long previous_time;
    private int location_x;
    private int location_y;

    Log(){
        list = new ArrayList<>();
        previous_time = System.currentTimeMillis();
        firstAction = true;
        location_x = Integer.MIN_VALUE;
        location_y = Integer.MIN_VALUE;
    }

    public void write(String filename) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename+".seq"))){
            writer.write("[");
            if (list.size() > 0) {
                for (int i = 0; i < list.size() - 1; i++) {
                    writer.write(list.get(i) + ",\n");
                }
                writer.write(list.get(list.size() - 1));
            }
            writer.write("]");
            this.log(true, Recorder.NO_ACTION, "Writing to file complete");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String processLocation(String message) {
        StringBuilder result = new StringBuilder();
        Scanner messageScanner = new Scanner(message);
        messageScanner.useDelimiter(", ");
        int current_x = messageScanner.nextInt();
        int current_y = messageScanner.nextInt();
        if (location_x == Integer.MIN_VALUE && location_y == Integer.MIN_VALUE) {
            location_x = current_x;
            location_y = current_y;
        }
        current_x -= location_x; current_y -= location_y;
        location_x += current_x; location_y += current_y;
        result.append(current_x).append(", ").append(current_y);
        return result.toString();
    }

    private String appendSucces(Boolean succes) {
        StringBuilder result = new StringBuilder();
        result.append('[');
        if (succes == null) {
            result.append(ANSI_RESET);
            result.append('-');
        } else if (succes) {
            result.append(ANSI_GREEN);
            result.append('✔');
        } else {
            result.append(ANSI_RED);
            result.append('x');
        }
        result.append(ANSI_DEFAULT);
        result.append("] ");
        return result.toString();
    }

    private String appendAction(int action) {
        if (action == Recorder.NO_ACTION)
            return "";
        StringBuilder result = new StringBuilder();
        result.append(ANSI_PURPLE);
        if (action == Recorder.ACTION_MOUSE_MOVE) {
            result.append("Mouse Move");
        } else if (action == Recorder.ACTION_MOUSE_CLICK) {
            result.append("Mouse Click");
        } else if (action == Recorder.ACTION_KEYBOARD_KEY) {
            result.append("Key Press");
        }
        result.append(ANSI_DEFAULT + " ");
        return result.toString();
    }

    private String appendTime(int action) {
        long time = System.currentTimeMillis();
        long difference = time-previous_time;
        long minutes = difference / 60000;
        long seconds = (difference / 1000) % 60;
        long milisec = difference % 1000;
        previous_time = time;
        if (firstAction && action != Recorder.NO_ACTION) {
            firstAction = false;
            return "00:00:000";
        }
        return String.format("%02d:%02d:%03d", minutes, seconds, milisec);
    }

    private void recordLog(String time, int action, String message) {
        if (action == Recorder.NO_ACTION)
            return;
        String result = "{" +
                action +
                ", \"" +
                message +
                "\", \"" +
                time +
                "\"}";
        list.add(result);
    }

    private void printLog(Boolean succes, String time, int action, String message) {
        StringBuilder result = new StringBuilder();
        result.append(ANSI_DEFAULT);
        result.append(appendSucces(succes));
        if (action != Recorder.NO_ACTION)
        result.append(time).append(" ");
        result.append(appendAction(action));
        result.append(message);
        result.append(ANSI_RESET);
        System.out.println(result.toString());
    }

    public void log(Boolean succes, int action, String message) {
        String time = appendTime(action);
        if (action != Recorder.NO_ACTION && action <= 5) {
            message = processLocation(message);
        }
        printLog(succes, time, action, message);
        recordLog(time, action, message);
    }
}

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

public class Recorder { //implements Runnable {

    // Actions
    public static final int NO_ACTION = 0;
    public static final int ACTION_MOUSE_MOVE = 1;
    public static final int ACTION_MOUSE_CLICK = 2;
    // public static final int ACTION_MOUSE_PRESS = 3;
    // public static final int ACTION_MOUSE_RELEASE = 4;
    // public static final int ACTION_MOUSE_DRAG = 5;
    public static final int ACTION_KEYBOARD_KEY = 6;

    private Log log;
    private String logName;
    private Boolean recording;
    private Boolean loopable;
    private ArrayList<SequenceEntry> sequence;
    private ArrayList<SequenceEntry> checks;
    private int t;
    private long previous_time;
    private Log placeLog;
    private static final Boolean STATE_RECORDING = true;
    private static final Boolean STATE_TESTING   = false;

    Recorder() {
        log = new Log();
        log.log(null, NO_ACTION, "Started Recorder and Logging functionality");
    }

    private void initialiseGlobalScreen() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("Not able to register the native hook," +
                    " perhaps the operating system is not supported.");
        }
    }

    private void initialiseListeners() {
        GlobalMouseListener listener = new GlobalMouseListener();
        GlobalScreen.addNativeMouseListener(listener);
        GlobalScreen.addNativeMouseMotionListener(listener);
        GlobalKeyListener keyListener = new GlobalKeyListener();
        GlobalScreen.addNativeKeyListener(keyListener);
    }

    private void kill_recording() {
        log.log(true, NO_ACTION, "End of recording");
        log.write(logName);
        System.exit(0);
    }

    private void kill_testing(Boolean success) {
        if (success) {
            log.log(true, NO_ACTION, "End of testing, test successful");
        } else {
            log.log(false, NO_ACTION, "End of testing, test failed");
        }
        System.exit(0);
    }

    public void handle(int action, String data) {
        if (recording) {
            log.log(null, action, data);
        } else {
            check(action, data);
        }
    }

    private void check(int action, String data) {
        long time = System.currentTimeMillis();
        if (previous_time == 0)
            previous_time = time;
        String location = placeLog.processLocation(data);
        long difference = time-previous_time;
        previous_time = time;
        checks.add(new SequenceEntry(action, location, difference));
        if(!checkCheckingSequence())
            kill_testing(false);
        if (loopable)
            if (sequence.size() == checks.size())
                kill_testing(true);
    }

    private void printDebug() {
        for (SequenceEntry se : checks) {
            System.out.print(se + ", ");
        }
        System.out.println();
        for (SequenceEntry se : sequence) {
            System.out.print(se + ", ");
        }
        System.out.println();
    }

    private Boolean checkCheckingSequence() {
        //printDebug();
        if (checks.size() == 1)
            return true;
        for (int i = t; i < sequence.size(); i++){
            if (sequence.get(i).equals(checks.get(1))) {
                int j = i;
                boolean checkedAll = true;
                for (int k = 1; k < checks.size() && checkedAll; k++, j++) {
                    if(j == sequence.size()){
                        j = 0;
                    } else if (!sequence.get(j).equals(checks.get(k))){
                        checkedAll = false;
                    }
                }
                if (checkedAll) {
                    if (j < checks.size()) {
                        if (!loopable) {
                            kill_testing(true);
                        }
                    }
                    t = i;
                    return true;
                }
            }
        }
        return false;
    }

    private class GlobalKeyListener implements NativeKeyListener {

        public void nativeKeyPressed(NativeKeyEvent e) {
            if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
                if (recording)
                    kill_recording();
            }
            handle(ACTION_KEYBOARD_KEY, NativeKeyEvent.getKeyText(e.getKeyCode()));
        }

        public void nativeKeyReleased(NativeKeyEvent e) {}
        public void nativeKeyTyped(NativeKeyEvent e) {}
    }

    private class GlobalMouseListener implements NativeMouseInputListener {

        public void nativeMousePressed(NativeMouseEvent e) {
            handle(ACTION_MOUSE_CLICK, e.getX() + ", " + e.getY());
        }

        public void nativeMouseMoved(NativeMouseEvent e) {
            handle(ACTION_MOUSE_MOVE, e.getX() + ", " + e.getY());
        }

        public void nativeMouseClicked(NativeMouseEvent e) {}
        public void nativeMouseReleased(NativeMouseEvent e) {}
        public void nativeMouseDragged(NativeMouseEvent e) {}
    }

    private void initialise(String filename) {
        // Turn off jnativehook logging
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        // Initialise the recorder
        initialiseGlobalScreen();
        initialiseListeners();
        // Set log name
        logName = filename;
    }

    public void record(String filename) {
        recording = STATE_RECORDING;
        log.log(null, NO_ACTION, "Press the ESC-key to stop recording input");
        log.log(null, NO_ACTION, "Start recording to file: " + filename);
        initialise(filename);
    }

    // Weakness, after 100 loops do X / "bomb" instead.
    // Solution, leave test running the same amount of time it was in the suspects machine.
    public void test(String filename, boolean loopable) {
        recording = STATE_TESTING;
        checks = new ArrayList<>();
        placeLog = new Log();
        t = 0;
        this.loopable = loopable;
        //Read test file
        log.log(null, NO_ACTION, "Reading test sequence file: " + filename);
        SequenceParser parser = new SequenceParser(filename);
        sequence = parser.parse();
        log.log(true, NO_ACTION, "Parsing test sequence finished");
        //Initialise recording and start testing
        log.log(null, NO_ACTION, "Start testing");
        initialise(filename);
    }
}
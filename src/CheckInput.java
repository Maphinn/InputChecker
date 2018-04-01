public class CheckInput {

    private Recorder recorder;
    private static final String logname = "logged_sequence";

    private CheckInput(){
        recorder = new Recorder();
    }

    private void recordSequence(String filename) {
        recorder.record(filename);
    }

    private void testSequence(String filename, boolean loopable) {
        recorder.test(filename, loopable);
    }

    private void start(String[] args) {
        if (args.length == 0) {
            recordSequence(logname);
        } else {
            if (args.length != 2) {
                System.out.println("Usage:\tTo record:\tCheckInput\n\t\tTo test:\tCheckInput <sequence_file> <loopable(y/N)>");
            } else {
                if (args[1].equals("y")) {
                    testSequence(args[0], true);
                } else {
                    testSequence(args[0], false);
                }
            }
        }
    }

    public static void main(String[] args){
        new CheckInput().start(args);
    }
}

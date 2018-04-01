import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public  class GenerateScript {

    // Min size = 0
    // Max size = 500 (514)
    private static final int SCRIPT_SIZE = 86;

    private static final String INITIAL_DATA = "" +
            "// <script_name>\n" +
            "// Include the mouse library\n" +
            "#include <DigiMouse.h>\n\n" +
            "// Time in seconds to sleep, change according to preference\n" +
            "int sleep_seconds = <time>;\n\n" +
            "void setup() {\n" +
            "  // Enable mouse features\n" +
            "  DigiMouse.begin();\n" +
            "  // Wait before starting the program\n" +
            "  DigiMouse.delay(1000);\n" +
            "}\n\n" +
            "void sleep() {\n" +
            "  if (sleep_seconds == 0) {\n" +
            "    DigiMouse.delay(100);\n" +
            "    DigiMouse.update();\n" +
            "  } else {\n" +
            "    int i;\n" +
            "    for (i = 0; i < sleep_seconds; i++)\n" +
            "      DigiMouse.delay(1000); // Sleep 1 second\n" +
            "     DigiMouse.update();\n" +
            "  }\n" +
            "}\n\n" +
            "void moveXY(int x, int y) {\n" +
            "  if (x != 0)\n" +
            "    DigiMouse.moveX(x);\n" +
            "  if (y != 0)\n" +
            "    DigiMouse.moveY(y);\n" +
            "  sleep();\n" +
            "}\n\n" +
            "void loop() {\n" +
            "  // The main Jiggle code\n" +
            "<code>" +
            "}";

    private int x = 0 ,y = 0;

    private void start(String[] args) {
        String script = INITIAL_DATA;
        script = script.replace("<script_name>", args[0]);
        script = script.replace("<time>", args[1]);
        String code = generateCode(args);
        script = script.replace("<code>", code);
        File f = new File(args[0]+".ino");
        try {
            FileWriter fw = new FileWriter(f);
            fw.write(script);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int random(int max) {
        Random rand = new Random();
        return rand.nextInt((max - 1) + 1) + 1;
    }

    private String generateCode(String[] args) {
        StringBuilder code = new StringBuilder();
        randomMovement(args, code);
        moveBackToCenter(code, args[2]);
        return code.toString();
    }

    private void moveBackToCenter(StringBuilder code, String step) {
        while (x != 0){
            if (x > 0){
                code.append("  moveXY(-").append(step).append(", 0);\n");
                x--;
            } else {
                code.append("  moveXY(").append(step).append(", 0);\n");
                x++;
            }
        }
        while (y != 0){
            if (y > 0){
                code.append("  moveXY(0, -").append(step).append(");\n");
                y--;
            } else {
                code.append("  moveXY(0, ").append(step).append(");\n");
                y++;
            }
        }
    }

    private void randomMovement(String[] args, StringBuilder code) {
        int prev = random(4);
        int i = 0;
        while (i + Math.abs(x) + Math.abs(y) < SCRIPT_SIZE) {
            int direction = random(7);
            if (direction > 4)
                direction = prev;
            if (direction == 1) {
                if (x < Integer.parseInt(args[3])) {
                    x += 1;
                    i++;
                    code.append("  moveXY(").append(args[2]).append(", 0);\n");
                }
            } else if (direction == 2) {
                if (y > -Integer.parseInt(args[4])) {
                    y -= 1;
                    i++;
                    code.append("  moveXY(0, -").append(args[2]).append(");\n");
                }
            } else if (direction == 3) {
                if (x > -Integer.parseInt(args[5])) {
                    x -= 1;
                    i++;
                    code.append("  moveXY(-").append(args[2]).append(", 0);\n");
                }
            } else {
                if (y < Integer.parseInt(args[6])) {
                    y += 1;
                    i++;
                    code.append("  moveXY(0, ").append(args[2]).append(");\n");
                }
            }
            prev = direction;
        }
    }

    public static void main(String[] args) {
        if (args.length != 7) {
            System.out.println("Usage:\tGenerateScript\t<file_name> <sleep_time> <step> <right> <up> <left> <down>\n" +
                    "\t\tWhere <file_name> is the name of the file.\n" +
                    "\t\t<sleep_time> is the time it sleeps between steps.\n" +
                    "\t\t<step> is the distance of one movement in px.\n" +
                    "\t\tThe directions (right, up, left and down) are the maximum steps that the mouse can move that way.\n" +
                    "\n" +
                    "For example GenerateScript\tgenerated_script 55 10 2 2 2 2\n" +
                    "Will create a script called generated_script.ino\n" +
                    "Which will sleep 55 seconds between moving the mouse 10 pixels inside a 40px diameter square.\n" +
                    "The mouse will always end up back in the center to make the behaviour repeatable.\n");
        } else {
            new GenerateScript().start(args);
        }
    }
}

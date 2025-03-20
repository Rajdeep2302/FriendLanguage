import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

class PrintStatement {
    boolean printNextLine(String line, int start, int end) {
        // Prevent out-of-bounds error
        if (start < end) {
            System.out.println(line.substring(start, end));
        }
        return true;
    }

    boolean checkNextLine(String finalLine) {
        PrintStatement ps = new PrintStatement();
        boolean flag = false; // Keep track of backslash
        int start = 0;
        boolean foundNewline = false;

        for (int i = 0; i < finalLine.length(); i++) {
            char a = finalLine.charAt(i);

            if (a == '\\') {
                flag = true;  // Found backslash
            } else if (a == 'n' && flag) {
                foundNewline = true; // Mark that \n was found
                ps.printNextLine(finalLine, start, i - 1);
                start = i + 1; // Move start after '\n'
                flag = false;
            } else {
                flag = false; // Reset flag
            }
        }

        // Print remaining part if a newline was found
        if (foundNewline && start < finalLine.length()) {
            ps.printNextLine(finalLine, start, finalLine.length());
        }

        return foundNewline; // Return whether \n was found
    }

    void check(String line) {
        int size = line.length();

        if (size >= 8 &&
                line.substring(5, 6).equals("(") &&
                line.substring(6, 7).equals("\"") &&
                line.substring(size - 2, size - 1).equals("\"") &&
                line.substring(size - 1).equals(")")) {

            // Extract content inside print("...")
            String finalLine = line.substring(7, size - 2);

            PrintStatement ps = new PrintStatement();
            boolean hasNewline = ps.checkNextLine(finalLine);

            if (!hasNewline) { // Only print if no \n was found
                System.out.println(finalLine);
            }

        } else {
            System.out.println("Error: Syntax Error.");
        }
    }
}


public class main { // Corrected class name
    public static void main(String[] args) {
        PrintStatement pt = new PrintStatement();

        // Check if a filename was provided
        if (args.length == 0) {
            System.out.println("Usage: java Main <filename>");
            return;
        }

        String filename = args[0];
        int size = filename.length();

        // Ensure filename has at least 4 characters before extracting substring
        if (size >= 7 && filename.substring(size - 4).equals(".pal")) {
            try {
                // Read all lines into a list
                List<String> lines = Files.readAllLines(Paths.get(filename));

                for (String line : lines) {
                    // Trim whitespace
                    String ExecutableLine = line.trim();

                    // Check if line starts with "print("
                    if (ExecutableLine.startsWith("print(")) {
                        pt.check(ExecutableLine);
                    } else if (ExecutableLine.startsWith("#") || ExecutableLine.startsWith("//")) {
                        //This is the way to implement a single line comment line
                    } else {
                        System.out.println("Error: Syntax Error.");
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading file: " + e.getMessage());
            }
        } else {
            System.out.println("Error: Invalid File Name. File must have a .pal extension and at least 3 letters before extension.");
        }
    }
}

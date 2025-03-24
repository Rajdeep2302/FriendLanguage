
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

class integerWord {
  String v;
  int a;
  integerWord next;

}

class Variable { 
    void check(String line) {
        if (line.length() == 0) {
            System.out.println("Error: Syntax error.");
            return;
        }

        String var = line.substring(0, line.indexOf('=')).trim();
        String value = line.substring(line.indexOf('=') + 1).trim();
        if(value.matches("[0-9]+")) {
            //integerWord new_node = new integerWord(); 
            //code will be change here
        } else {
            System.out.println("its not a integer");
        }
    }
}

class PrintStatement {

    void printTabLine(String finalLine, int start, int end) {
        if (start <= end) {
            System.out.print(finalLine.substring(start, end) + "\t");
        }
    }

    void printBackLine(String finalLine, int start, int end) {
        if (start < end) {  // Ensure at least one character remains
            System.out.print(finalLine.substring(start, end - 1));  // Remove last char
        }
    }

    boolean printNextLine(String line, int start, int end) {
        if (start <= end) {
            System.out.println(line.substring(start, end));
        }
        return true;
    }

    boolean checkNextLine(String finalLine) {
        PrintStatement ps = new PrintStatement();
        boolean flag = false;
        int start = 0;
        boolean foundNewline = false;

        for (int i = 0; i < finalLine.length(); i++) {
            char a = finalLine.charAt(i);

            if (a == '\\') {
                flag = true;
            } else if (a == 'n' && flag) {
                foundNewline = true;
                ps.printNextLine(finalLine, start, i - 1);
                start = i + 1;
                flag = false;
            } else if (a == 'b' && flag) {
                foundNewline = true;
                if (start < i - 1) {
                    ps.printBackLine(finalLine, start, i - 1);
                }
                start = i + 1;
                flag = false;
                boolean demo1 = false;
                try {
                    char demo = finalLine.charAt(i + 1);
                    System.out.print(demo + "\b");
                } catch (Exception e) {
                    demo1 = true;
                }
                if (demo1) {
                    System.out.println();
                }
            } else if (a == 't' && flag) {
                foundNewline = true;
                ps.printTabLine(finalLine, start, i - 1);
                start = i + 1;
                flag = false;
                boolean demo1 = false;
                try {
                    char demo = finalLine.charAt(i + 1);
                    System.out.print(demo + "\b");
                } catch (Exception e) {
                    demo1 = true;
                }
                if (demo1) {
                    System.out.println();
                }
            } else {
                flag = false;
            }
        }

        if (foundNewline && start < finalLine.length()) {
            ps.printNextLine(finalLine, start, finalLine.length());
        }

        return foundNewline;
    }

    void check(String line) {
        int size = line.length();
        int find = 0;

        if (size >= 8 && line.startsWith("print(\"")) {
            boolean printStatementEnd = false;
            String finalLine = "";
            int quoteIndex = line.lastIndexOf('"');
            // System.out.println(quoteIndex);
            String substring = line.substring(7, quoteIndex);
            // System.out.println(substring);
            if (substring.contains("\"")) {
                System.out.println("Error: Syntax error.");
                return;
            }

            for (int i = 0; i < substring.length(); i++) {
                char c = substring.charAt(i);

                if (c == '\\') {
                    // Ensure there's a next character
                    if (i + 1 < substring.length()) {
                        char nextChar = substring.charAt(i + 1);

                        // Check for invalid escape sequences
                        if (nextChar != 'n' && nextChar != 'b' && nextChar != 't' && nextChar != '\\') {
                            System.out.println("Error: Syntax error.");
                            return;
                        }
                    }
                }
            }

            if (quoteIndex != -1 && quoteIndex + 1 < size && line.charAt(quoteIndex + 1) == ')') {
                // System.out.println("Error: Syntax error.");
                finalLine = line.substring(7, quoteIndex);
                if (find == 0) {
                    printStatementEnd = true;
                    find++;
                } else {
                    System.out.println("Error: Syntax error.");
                    return;
                }
            } else {
                System.out.println("Error: Syntax error.");
                return;
            }

            // Remove comment first
            String executableCode = line.split("#")[0].trim();

            if (!executableCode.endsWith(")")) {
                System.out.println("Error: Unexpected Syntax error after the print Statement.");
                return;
            }

            if (printStatementEnd) {
                PrintStatement ps = new PrintStatement();
                boolean hasNewline = ps.checkNextLine(finalLine);

                if (!hasNewline) {
                    System.out.println(finalLine);
                }
            }

        } else if (size >= 7 && line.startsWith("print(")){
        
        } else {
            System.out.println("Error: Syntax Error.");
        }
    }
}

public class main { 

    public static void main(String[] args) {
        PrintStatement pt = new PrintStatement();
        Variable va = new Variable();

        if (args.length == 0) {
            System.out.println("Usage: java Main <filename>");
            return;
        }

        String filename = args[0];
        int size = filename.length();

        if (size >= 7 && filename.endsWith(".pal")) {
            try {
                List<String> lines = Files.readAllLines(Paths.get(filename));

                for (String line : lines) {
                    String ExecutableLine = line.trim();

                    if (ExecutableLine.startsWith("print(")) {
                        pt.check(ExecutableLine);
                    } else if (ExecutableLine.trim().startsWith("#") || ExecutableLine.isEmpty()) {
                        continue;
                    } else if (ExecutableLine.trim().endsWith(":")) {
                        System.out.println("1Error: Syntax Error.");
                    } else if (ExecutableLine.trim().matches("^[1-9@#$%^&*].*")) {
                        System.out.println("Error: Syntax Error.");
                    } else if (ExecutableLine.trim().matches("^[a-zA-Z_].*")) {
                        va.check(ExecutableLine);
                    } else {
                        System.out.println("No match found.");
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

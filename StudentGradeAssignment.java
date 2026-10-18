import java.util.Arrays;
import java.util.Scanner;

public class StudentGradeAssignment {

    private static final int MAXIMUM_LINES = 1000;
    private static final String[] DEMO_MARKS = { "72", "95", "35" };

    public static void main(String[] args) {
        String[] markLines = readInputLines(DEMO_MARKS);
        for (int markIndex = 0; markIndex < markLines.length; markIndex++) {
            int marks = Integer.parseInt(markLines[markIndex]);
            System.out.println("Grade " + assignGrade(marks));
        }
    }

    private static char assignGrade(int marks) {
        if (marks >= 90) {
            return 'A';
        } else if (marks >= 75) {
            return 'B';
        } else if (marks >= 60) {
            return 'C';
        } else if (marks >= 40) {
            return 'D';
        } else {
            return 'F';
        }
    }

    private static String[] readInputLines(String[] demoLines) {
        Scanner inputReader = new Scanner(System.in);
        String[] collectedLines = new String[MAXIMUM_LINES];
        int lineCount = 0;
        while (inputReader.hasNextLine() && lineCount < MAXIMUM_LINES) {
            String typedLine = inputReader.nextLine().trim();
            if (!typedLine.isEmpty()) {
                collectedLines[lineCount] = typedLine;
                lineCount++;
            }
        }
        inputReader.close();
        return lineCount == 0 ? demoLines : Arrays.copyOf(collectedLines, lineCount);
    }
}

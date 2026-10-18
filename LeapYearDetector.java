import java.util.Arrays;
import java.util.Scanner;

public class LeapYearDetector {

    private static final int MAXIMUM_LINES = 1000;
    private static final String[] DEMO_YEARS = { "2024", "1900", "2000" };

    public static void main(String[] args) {
        String[] yearLines = readInputLines(DEMO_YEARS);
        for (int yearIndex = 0; yearIndex < yearLines.length; yearIndex++) {
            int year = Integer.parseInt(yearLines[yearIndex]);
            System.out.println(isLeapYear(year) ? "Leap year" : "Not a leap year");
        }
    }

    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
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

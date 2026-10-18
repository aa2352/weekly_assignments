import java.util.Arrays;
import java.util.Scanner;

public class EvenOddCounter {

    private static final int MAXIMUM_LINES = 1000;
    private static final String[] DEMO_LISTS = { "[3, 8, 12, 5, 7, 10]" };

    public static void main(String[] args) {
        String[] listLines = readInputLines(DEMO_LISTS);
        for (int listIndex = 0; listIndex < listLines.length; listIndex++) {
            int[] numbers = parseNumberList(listLines[listIndex]);
            int evenCount = 0;
            int oddCount = 0;
            for (int numberIndex = 0; numberIndex < numbers.length; numberIndex++) {
                if (numbers[numberIndex] % 2 == 0) {
                    evenCount++;
                } else {
                    oddCount++;
                }
            }
            System.out.println("Even: " + evenCount);
            System.out.println("Odd: " + oddCount);
        }
    }

    private static int[] parseNumberList(String listLine) {
        String innerText = listLine.replace("[", "").replace("]", "").trim();
        String[] numberFields = innerText.split(",");
        int[] numbers = new int[numberFields.length];
        for (int fieldIndex = 0; fieldIndex < numberFields.length; fieldIndex++) {
            numbers[fieldIndex] = Integer.parseInt(numberFields[fieldIndex].trim());
        }
        return numbers;
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

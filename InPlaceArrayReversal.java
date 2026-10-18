import java.util.Arrays;
import java.util.Scanner;

public class InPlaceArrayReversal {

    private static final int MAXIMUM_LINES = 1000;
    private static final String[] DEMO_LISTS = { "[11, 22, 33, 44]" };

    public static void main(String[] args) {
        String[] listLines = readInputLines(DEMO_LISTS);
        for (int listIndex = 0; listIndex < listLines.length; listIndex++) {
            int[] tokens = parseNumberList(listLines[listIndex]);
            reverseInPlace(tokens);
            System.out.println(Arrays.toString(tokens));
        }
    }

    private static void reverseInPlace(int[] tokens) {
        int leftIndex = 0;
        int rightIndex = tokens.length - 1;
        while (leftIndex < rightIndex) {
            int heldToken = tokens[leftIndex];
            tokens[leftIndex] = tokens[rightIndex];
            tokens[rightIndex] = heldToken;
            leftIndex++;
            rightIndex--;
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

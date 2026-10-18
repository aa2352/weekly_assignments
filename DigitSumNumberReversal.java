import java.util.Arrays;
import java.util.Scanner;

public class DigitSumNumberReversal {

    private static final int MAXIMUM_LINES = 1000;
    private static final int BASE_TEN = 10;
    private static final String[] DEMO_NUMBERS = { "4721" };

    public static void main(String[] args) {
        String[] numberLines = readInputLines(DEMO_NUMBERS);
        for (int numberIndex = 0; numberIndex < numberLines.length; numberIndex++) {
            long number = Long.parseLong(numberLines[numberIndex]);
            System.out.println("Sum of digits: " + sumDigits(number));
            System.out.println("Reverse: " + reverseDigits(number));
        }
    }

    private static long sumDigits(long number) {
        long digitSum = 0;
        while (number > 0) {
            digitSum = digitSum + (number % BASE_TEN);
            number = number / BASE_TEN;
        }
        return digitSum;
    }

    private static long reverseDigits(long number) {
        long reversedNumber = 0;
        while (number > 0) {
            reversedNumber = (reversedNumber * BASE_TEN) + (number % BASE_TEN);
            number = number / BASE_TEN;
        }
        return reversedNumber;
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

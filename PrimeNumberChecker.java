import java.util.Arrays;
import java.util.Scanner;

public class PrimeNumberChecker {

    private static final int MAXIMUM_LINES = 1000;
    private static final int SMALLEST_PRIME = 2;
    private static final String[] DEMO_NUMBERS = { "29", "21" };

    public static void main(String[] args) {
        String[] numberLines = readInputLines(DEMO_NUMBERS);
        for (int numberIndex = 0; numberIndex < numberLines.length; numberIndex++) {
            long number = Long.parseLong(numberLines[numberIndex]);
            System.out.println(number + (isPrime(number) ? " is prime" : " is not prime"));
        }
    }

    private static boolean isPrime(long number) {
        if (number < SMALLEST_PRIME) {
            return false;
        }
        for (long divisor = SMALLEST_PRIME; divisor * divisor <= number; divisor++) {
            if (number % divisor == 0) {
                return false;
            }
        }
        return true;
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

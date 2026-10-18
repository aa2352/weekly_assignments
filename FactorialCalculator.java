import java.math.BigInteger;
import java.util.Arrays;
import java.util.Scanner;

public class FactorialCalculator {

    private static final int MAXIMUM_LINES = 1000;
    private static final String[] DEMO_NUMBERS = { "5", "0" };

    public static void main(String[] args) {
        String[] numberLines = readInputLines(DEMO_NUMBERS);
        for (int numberIndex = 0; numberIndex < numberLines.length; numberIndex++) {
            int number = Integer.parseInt(numberLines[numberIndex]);
            if (number < 0) {
                System.out.println("Factorial is not defined for " + number);
                continue;
            }
            System.out.println(factorial(number));
        }
    }

    private static BigInteger factorial(int number) {
        BigInteger product = BigInteger.ONE;
        for (int factor = 2; factor <= number; factor++) {
            product = product.multiply(BigInteger.valueOf(factor));
        }
        return product;
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

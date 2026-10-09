import java.util.NoSuchElementException;
import java.util.Scanner;

public class TwoSumFinder {

    private static final String VALUE_SEPARATOR = ",";
    private static final int MINIMUM_ITEM_COUNT = 2;
    private static final String[] DEMO_PRICE_LINES = { "2,7,11,15", "3,2,4", "3,3", "1,2,3", "5" };
    private static final int[] DEMO_TARGETS = { 9, 6, 6, 100, 5 };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] priceLines = DEMO_PRICE_LINES;
        int[] targets = DEMO_TARGETS;

        String typedPrices = readLine(inputReader, "Enter the item prices (press Enter to use the demo list): ");
        if (!typedPrices.trim().isEmpty()) {
            String typedTarget = readLine(inputReader, "Enter the customer budget: ");
            priceLines = new String[] { typedPrices };
            targets = new int[] { readTarget(typedTarget) };
        }
        System.out.println();

        for (int caseIndex = 0; caseIndex < priceLines.length; caseIndex++) {
            reportTwoSum(priceLines[caseIndex], targets[caseIndex]);
        }
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== Two Sum Finder ===");
        System.out.println("\tInput format:\t\"2,7,11,15\"");
        System.out.println("\tGoal:\ttwo different positions whose prices add up to the budget\n");
    }

    private static String readLine(Scanner inputReader, String prompt) {
        System.out.print(prompt);
        try {
            return inputReader.nextLine();
        } catch (NoSuchElementException | IllegalStateException consoleUnavailable) {
            System.out.println("\n[no console input - running the demo list]");
            return "";
        }
    }

    private static int readTarget(String typedTarget) {
        try {
            return Integer.parseInt(typedTarget.trim());
        } catch (NumberFormatException nonNumericTarget) {
            System.out.println("[the budget is not a whole number - using 0]");
            return 0;
        }
    }

    private static void reportTwoSum(String priceLine, int target) {
        System.out.println("nums = [" + priceLine + "], target = " + target);
        try {
            int[] prices = parseIntArray(priceLine);
            int[] matchedIndexes = twoSum(prices, target);
            int firstIndex = matchedIndexes[0];
            int secondIndex = matchedIndexes[1];

            System.out.println(formatArray(matchedIndexes) + " (nums[" + firstIndex + "] + nums[" + secondIndex
                    + "] = " + prices[firstIndex] + " + " + prices[secondIndex] + " = " + target + ")");
            System.out.println("Array length checked: " + prices.length + "\n");
        } catch (NoMatchingPairException noMatchingPair) {
            System.out.println("No result: " + noMatchingPair.getMessage() + "\n");
        } catch (NumberFormatException nonNumericPrice) {
            System.out.println("No result: every price must be a whole number\n");
        } catch (IllegalArgumentException shortList) {
            System.out.println("No result: " + shortList.getMessage() + "\n");
        }
    }

    private static int[] twoSum(int[] nums, int target) throws NoMatchingPairException {
        if (nums.length < MINIMUM_ITEM_COUNT) {
            throw new IllegalArgumentException("at least " + MINIMUM_ITEM_COUNT
                    + " prices are needed but the array holds " + nums.length);
        }
        for (int firstIndex = 0; firstIndex < nums.length; firstIndex++) {
            for (int secondIndex = firstIndex + 1; secondIndex < nums.length; secondIndex++) {
                if (nums[firstIndex] + nums[secondIndex] == target) {
                    return new int[] { firstIndex, secondIndex };
                }
            }
        }
        throw new NoMatchingPairException("no two prices add up to " + target);
    }

    private static int[] parseIntArray(String valueLine) {
        if (valueLine == null || valueLine.trim().isEmpty()) {
            throw new IllegalArgumentException("the price list is empty");
        }
        String[] valueFields = valueLine.split(VALUE_SEPARATOR);
        int[] values = new int[valueFields.length];
        for (int fieldIndex = 0; fieldIndex < valueFields.length; fieldIndex++) {
            values[fieldIndex] = Integer.parseInt(valueFields[fieldIndex].trim());
        }
        return values;
    }

    private static String formatArray(int[] values) {
        StringBuilder formattedArray = new StringBuilder("[");
        for (int valueIndex = 0; valueIndex < values.length; valueIndex++) {
            if (valueIndex > 0) {
                formattedArray.append(", ");
            }
            formattedArray.append(values[valueIndex]);
        }
        return formattedArray.append("]").toString();
    }
}

class NoMatchingPairException extends Exception {
    NoMatchingPairException(String message) {
        super(message);
    }
}

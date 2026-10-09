import java.util.NoSuchElementException;
import java.util.Scanner;

public class BestTradingStreakFinder {

    private static final String VALUE_SEPARATOR = ",";
    private static final String[] DEMO_PROFIT_LINES = {
            "-2,1,-3,4,-1,2,1,-5,4", "-3,-1,-2", "5,4,-1,7,8", "-7", "1,2,q" };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] profitLines = collectProfitLines(inputReader);
        for (int caseIndex = 0; caseIndex < profitLines.length; caseIndex++) {
            reportBestStreak(profitLines[caseIndex]);
        }
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== Best Trading Streak Finder ===");
        System.out.println("\tInput format:\t\"-2,1,-3,4,-1,2,1,-5,4\"");
        System.out.println("\tRule:\tKadane's algorithm - extend the run or restart from today\n");
    }

    private static String[] collectProfitLines(Scanner inputReader) {
        System.out.print("Enter the daily profit or loss figures (press Enter to use the demo list): ");
        try {
            String typedProfits = inputReader.nextLine();
            if (typedProfits.trim().isEmpty()) {
                System.out.println("[no figures typed - running the demo list]\n");
                return DEMO_PROFIT_LINES;
            }
            System.out.println();
            return new String[] { typedProfits };
        } catch (NoSuchElementException | IllegalStateException consoleUnavailable) {
            System.out.println("\n[no console input - running the demo list]\n");
            return DEMO_PROFIT_LINES;
        }
    }

    private static void reportBestStreak(String profitLine) {
        System.out.println("nums = [" + profitLine + "]");
        try {
            int[] dailyProfits = parseIntArray(profitLine);
            int bestSum = maxSubArray(dailyProfits);
            int[] streakBounds = findBestStreakBounds(dailyProfits);

            System.out.println(bestSum + " (the subarray "
                    + formatRange(dailyProfits, streakBounds[0], streakBounds[1]) + " sums to " + bestSum + ")");
            System.out.println("Days recorded: " + dailyProfits.length + "\n");
        } catch (EmptyProfitLogException emptyLog) {
            System.out.println("No result: " + emptyLog.getMessage() + "\n");
        } catch (NumberFormatException nonNumericFigure) {
            System.out.println("No result: every figure must be a whole number\n");
        } catch (IllegalArgumentException missingLog) {
            System.out.println("No result: " + missingLog.getMessage() + "\n");
        }
    }

    private static int maxSubArray(int[] nums) throws EmptyProfitLogException {
        validateProfitLog(nums);
        int bestSum = nums[0];
        int currentSum = nums[0];

        for (int dayIndex = 1; dayIndex < nums.length; dayIndex++) {
            if (currentSum + nums[dayIndex] > nums[dayIndex]) {
                currentSum = currentSum + nums[dayIndex];
            } else {
                currentSum = nums[dayIndex];
            }
            if (currentSum > bestSum) {
                bestSum = currentSum;
            }
        }
        return bestSum;
    }

    private static int[] findBestStreakBounds(int[] nums) throws EmptyProfitLogException {
        validateProfitLog(nums);
        int bestSum = nums[0];
        int currentSum = nums[0];
        int currentStart = 0;
        int bestStart = 0;
        int bestEnd = 0;

        for (int dayIndex = 1; dayIndex < nums.length; dayIndex++) {
            if (currentSum + nums[dayIndex] > nums[dayIndex]) {
                currentSum = currentSum + nums[dayIndex];
            } else {
                currentSum = nums[dayIndex];
                currentStart = dayIndex;
            }
            if (currentSum > bestSum) {
                bestSum = currentSum;
                bestStart = currentStart;
                bestEnd = dayIndex;
            }
        }
        return new int[] { bestStart, bestEnd };
    }

    private static void validateProfitLog(int[] nums) throws EmptyProfitLogException {
        if (nums.length == 0) {
            throw new EmptyProfitLogException("the profit log holds no days");
        }
    }

    private static int[] parseIntArray(String valueLine) {
        if (valueLine == null || valueLine.trim().isEmpty()) {
            throw new IllegalArgumentException("the profit log is empty");
        }
        String[] valueFields = valueLine.split(VALUE_SEPARATOR);
        int[] values = new int[valueFields.length];
        for (int fieldIndex = 0; fieldIndex < valueFields.length; fieldIndex++) {
            values[fieldIndex] = Integer.parseInt(valueFields[fieldIndex].trim());
        }
        return values;
    }

    private static String formatRange(int[] values, int startIndex, int endIndex) {
        StringBuilder formattedRange = new StringBuilder("[");
        for (int valueIndex = startIndex; valueIndex <= endIndex; valueIndex++) {
            if (valueIndex > startIndex) {
                formattedRange.append(", ");
            }
            formattedRange.append(values[valueIndex]);
        }
        return formattedRange.append("]").toString();
    }
}

class EmptyProfitLogException extends Exception {
    EmptyProfitLogException(String message) {
        super(message);
    }
}

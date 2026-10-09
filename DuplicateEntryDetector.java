import java.util.NoSuchElementException;
import java.util.Scanner;

public class DuplicateEntryDetector {

    private static final String VALUE_SEPARATOR = ",";
    private static final String[] DEMO_ROLL_NUMBER_LINES = {
            "1,2,3,1", "1,2,3,4", "101,102,103,102,105", "7", "4,4,4" };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] rollNumberLines = collectRollNumberLines(inputReader);
        for (int caseIndex = 0; caseIndex < rollNumberLines.length; caseIndex++) {
            reportDuplicateCheck(rollNumberLines[caseIndex]);
        }
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== Duplicate Entry Detector ===");
        System.out.println("\tInput format:\t\"1,2,3,1\"");
        System.out.println("\tGoal:\ttrue if any value appears at two different positions\n");
    }

    private static String[] collectRollNumberLines(Scanner inputReader) {
        System.out.print("Enter the roll numbers (press Enter to use the demo list): ");
        try {
            String typedRollNumbers = inputReader.nextLine();
            if (typedRollNumbers.trim().isEmpty()) {
                System.out.println("[no roll numbers typed - running the demo list]\n");
                return DEMO_ROLL_NUMBER_LINES;
            }
            System.out.println();
            return new String[] { typedRollNumbers };
        } catch (NoSuchElementException | IllegalStateException consoleUnavailable) {
            System.out.println("\n[no console input - running the demo list]\n");
            return DEMO_ROLL_NUMBER_LINES;
        }
    }

    private static void reportDuplicateCheck(String rollNumberLine) {
        System.out.println("nums = [" + rollNumberLine + "]");
        try {
            int[] rollNumbers = parseIntArray(rollNumberLine);
            boolean hasDuplicate = containsDuplicate(rollNumbers);
            System.out.println(hasDuplicate + describeResult(hasDuplicate));

            if (hasDuplicate) {
                System.out.println("First repeated value: " + findFirstDuplicateValue(rollNumbers));
            }
            System.out.println("Entries compared: " + rollNumbers.length + "\n");
        } catch (NoDuplicateFoundException noDuplicateFound) {
            System.out.println("Lookup skipped: " + noDuplicateFound.getMessage() + "\n");
        } catch (NumberFormatException nonNumericRollNumber) {
            System.out.println("No result: every roll number must be a whole number\n");
        } catch (IllegalArgumentException emptyList) {
            System.out.println("No result: " + emptyList.getMessage() + "\n");
        }
    }

    private static boolean containsDuplicate(int[] nums) {
        for (int firstIndex = 0; firstIndex < nums.length; firstIndex++) {
            for (int secondIndex = firstIndex + 1; secondIndex < nums.length; secondIndex++) {
                if (nums[firstIndex] == nums[secondIndex]) {
                    return true;
                }
            }
        }
        return false;
    }

    private static int findFirstDuplicateValue(int[] nums) throws NoDuplicateFoundException {
        for (int firstIndex = 0; firstIndex < nums.length; firstIndex++) {
            for (int secondIndex = firstIndex + 1; secondIndex < nums.length; secondIndex++) {
                if (nums[firstIndex] == nums[secondIndex]) {
                    return nums[firstIndex];
                }
            }
        }
        throw new NoDuplicateFoundException("every value is distinct");
    }

    private static String describeResult(boolean hasDuplicate) {
        if (hasDuplicate) {
            return " (a value appears at two different positions)";
        }
        return " (every value is distinct)";
    }

    private static int[] parseIntArray(String valueLine) {
        if (valueLine == null || valueLine.trim().isEmpty()) {
            throw new IllegalArgumentException("the roll number list is empty");
        }
        String[] valueFields = valueLine.split(VALUE_SEPARATOR);
        int[] values = new int[valueFields.length];
        for (int fieldIndex = 0; fieldIndex < valueFields.length; fieldIndex++) {
            values[fieldIndex] = Integer.parseInt(valueFields[fieldIndex].trim());
        }
        return values;
    }
}

class NoDuplicateFoundException extends Exception {
    NoDuplicateFoundException(String message) {
        super(message);
    }
}

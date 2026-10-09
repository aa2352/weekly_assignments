import java.util.NoSuchElementException;
import java.util.Scanner;

public class RotatedRosterMinimumFinder {

    private static final String VALUE_SEPARATOR = ",";
    private static final String[] DEMO_ROSTER_LINES = {
            "3,4,5,1,2", "4,5,6,7,0,1,2", "11,13,15,17", "2,1", "9", "1,2,m" };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] rosterLines = collectRosterLines(inputReader);
        for (int caseIndex = 0; caseIndex < rosterLines.length; caseIndex++) {
            reportEarliestEntry(rosterLines[caseIndex]);
        }
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== Rotated Roster Minimum Finder ===");
        System.out.println("\tInput format:\t\"3,4,5,1,2\"");
        System.out.println("\tRule:\tbinary search - compare the middle entry with the rightmost entry\n");
    }

    private static String[] collectRosterLines(Scanner inputReader) {
        System.out.print("Enter the rotated roster (press Enter to use the demo list): ");
        try {
            String typedRoster = inputReader.nextLine();
            if (typedRoster.trim().isEmpty()) {
                System.out.println("[no roster typed - running the demo list]\n");
                return DEMO_ROSTER_LINES;
            }
            System.out.println();
            return new String[] { typedRoster };
        } catch (NoSuchElementException | IllegalStateException consoleUnavailable) {
            System.out.println("\n[no console input - running the demo list]\n");
            return DEMO_ROSTER_LINES;
        }
    }

    private static void reportEarliestEntry(String rosterLine) {
        System.out.println("nums = [" + rosterLine + "]");
        try {
            int[] roster = parseIntArray(rosterLine);
            int earliestEntry = findMin(roster);

            System.out.println(earliestEntry + describeRotation(roster, earliestEntry));
            System.out.println("Entries in roster: " + roster.length
                    + " | binary search depth: " + calculateSearchDepth(roster.length) + "\n");
        } catch (EmptyRosterException emptyRoster) {
            System.out.println("No result: " + emptyRoster.getMessage() + "\n");
        } catch (NumberFormatException nonNumericEntry) {
            System.out.println("No result: every entry must be a whole number\n");
        } catch (IllegalArgumentException missingRoster) {
            System.out.println("No result: " + missingRoster.getMessage() + "\n");
        }
    }

    private static int findMin(int[] nums) throws EmptyRosterException {
        if (nums.length == 0) {
            throw new EmptyRosterException("the roster holds no entries");
        }

        int leftIndex = 0;
        int rightIndex = nums.length - 1;

        while (leftIndex < rightIndex) {
            int middleIndex = leftIndex + (rightIndex - leftIndex) / 2;
            if (nums[middleIndex] > nums[rightIndex]) {
                leftIndex = middleIndex + 1;
            } else {
                rightIndex = middleIndex;
            }
        }
        return nums[leftIndex];
    }

    private static String describeRotation(int[] roster, int earliestEntry) {
        if (roster[0] == earliestEntry) {
            return " (no rotation actually occurred)";
        }
        return " (the roster was rotated, so the list starts at " + roster[0] + ")";
    }

    private static int calculateSearchDepth(int rosterLength) {
        int steps = 0;
        int remainingEntries = rosterLength;
        while (remainingEntries > 1) {
            remainingEntries = remainingEntries / 2;
            steps++;
        }
        return steps;
    }

    private static int[] parseIntArray(String valueLine) {
        if (valueLine == null || valueLine.trim().isEmpty()) {
            throw new IllegalArgumentException("the roster is empty");
        }
        String[] valueFields = valueLine.split(VALUE_SEPARATOR);
        int[] values = new int[valueFields.length];
        for (int fieldIndex = 0; fieldIndex < valueFields.length; fieldIndex++) {
            values[fieldIndex] = Integer.parseInt(valueFields[fieldIndex].trim());
        }
        return values;
    }
}

class EmptyRosterException extends Exception {
    EmptyRosterException(String message) {
        super(message);
    }
}

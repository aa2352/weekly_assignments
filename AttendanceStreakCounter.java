import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class AttendanceStreakCounter {

    private static final String VALUE_SEPARATOR = ",";
    private static final int EMPTY_PREFIX_SUM = 0;
    private static final int EMPTY_PREFIX_COUNT = 1;
    private static final String[] DEMO_ATTENDANCE_LINES = { "1,1,1", "1,-1,0", "3,4,7,2,-3,1,4,2", "5", "1,2,k" };
    private static final int[] DEMO_TARGETS = { 2, 0, 7, 5, 3 };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] attendanceLines = DEMO_ATTENDANCE_LINES;
        int[] targets = DEMO_TARGETS;

        String typedLog = readLine(inputReader, "Enter the daily attendance changes (press Enter to use the demo list): ");
        if (!typedLog.trim().isEmpty()) {
            String typedTarget = readLine(inputReader, "Enter the net change to look for: ");
            attendanceLines = new String[] { typedLog };
            targets = new int[] { readTarget(typedTarget) };
        }
        System.out.println();

        for (int caseIndex = 0; caseIndex < attendanceLines.length; caseIndex++) {
            reportSubarrayCount(attendanceLines[caseIndex], targets[caseIndex]);
        }
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== Attendance Streak Counter ===");
        System.out.println("\tInput format:\t\"1,1,1\" with k = 2");
        System.out.println("\tRule:\tprefix sums in a hash map - a sliding window fails on negative days\n");
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
            System.out.println("[the net change is not a whole number - using 0]");
            return 0;
        }
    }

    private static void reportSubarrayCount(String attendanceLine, int target) {
        System.out.println("nums = [" + attendanceLine + "], k = " + target);
        try {
            int[] dailyChanges = parseIntArray(attendanceLine);
            int matchingStretchCount = subarraySum(dailyChanges, target);

            System.out.println(matchingStretchCount + describeResult(matchingStretchCount));
            System.out.println("Days reviewed: " + dailyChanges.length + "\n");
        } catch (EmptyAttendanceLogException emptyLog) {
            System.out.println("No result: " + emptyLog.getMessage() + "\n");
        } catch (NumberFormatException nonNumericChange) {
            System.out.println("No result: every daily change must be a whole number\n");
        } catch (IllegalArgumentException missingLog) {
            System.out.println("No result: " + missingLog.getMessage() + "\n");
        }
    }

    private static int subarraySum(int[] nums, int k) throws EmptyAttendanceLogException {
        if (nums.length == 0) {
            throw new EmptyAttendanceLogException("the attendance log holds no days");
        }

        Map<Integer, Integer> prefixSumCounts = new HashMap<>();
        prefixSumCounts.put(EMPTY_PREFIX_SUM, EMPTY_PREFIX_COUNT);

        int runningSum = EMPTY_PREFIX_SUM;
        int matchingStretchCount = 0;

        for (int dayIndex = 0; dayIndex < nums.length; dayIndex++) {
            runningSum += nums[dayIndex];
            Integer earlierPrefixCount = prefixSumCounts.get(runningSum - k);
            if (earlierPrefixCount != null) {
                matchingStretchCount += earlierPrefixCount;
            }
            Integer currentPrefixCount = prefixSumCounts.get(runningSum);
            prefixSumCounts.put(runningSum, currentPrefixCount == null ? 1 : currentPrefixCount + 1);
        }
        return matchingStretchCount;
    }

    private static String describeResult(int matchingStretchCount) {
        if (matchingStretchCount == 0) {
            return " (no stretch of days reaches that net change)";
        }
        if (matchingStretchCount == 1) {
            return " (one stretch of days reaches that net change)";
        }
        return " (" + matchingStretchCount + " different stretches reach that net change)";
    }

    private static int[] parseIntArray(String valueLine) {
        if (valueLine == null || valueLine.trim().isEmpty()) {
            throw new IllegalArgumentException("the attendance log is empty");
        }
        String[] valueFields = valueLine.split(VALUE_SEPARATOR);
        int[] values = new int[valueFields.length];
        for (int fieldIndex = 0; fieldIndex < valueFields.length; fieldIndex++) {
            values[fieldIndex] = Integer.parseInt(valueFields[fieldIndex].trim());
        }
        return values;
    }
}

class EmptyAttendanceLogException extends Exception {
    EmptyAttendanceLogException(String message) {
        super(message);
    }
}

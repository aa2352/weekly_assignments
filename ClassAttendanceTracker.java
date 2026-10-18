import java.util.Arrays;

public class ClassAttendanceTracker {

    private static final int PRESENT = 1;
    private static final int[][] DEMO_ATTENDANCE = { { 1, 1, 0, 1, 1, 1, 0, 1 }, { 0, 0, 0 } };

    public static void main(String[] args) {
        for (int recordIndex = 0; recordIndex < DEMO_ATTENDANCE.length; recordIndex++) {
            int[] summary = attendanceSummary(DEMO_ATTENDANCE[recordIndex]);
            System.out.println("days = " + Arrays.toString(DEMO_ATTENDANCE[recordIndex])
                    + " -> Present: " + summary[0] + ", Longest streak: " + summary[1]);
        }
    }

    // O(n) time, O(1) space. Both answers come from the same single pass over the days.
    static int[] attendanceSummary(int[] days) {
        int presentDays = 0;
        int currentStreak = 0;
        int longestStreak = 0;
        for (int dayIndex = 0; dayIndex < days.length; dayIndex++) {
            if (days[dayIndex] == PRESENT) {
                presentDays++;
                currentStreak++;
                longestStreak = Math.max(longestStreak, currentStreak);
            } else {
                currentStreak = 0;
            }
        }
        return new int[] { presentDays, longestStreak };
    }
}

import java.util.Arrays;

public class DutyRosterRotation {

    private static final String[] DEMO_NAMES = { "A", "B", "C", "D", "E" };
    private static final int[] DEMO_SHIFTS = { 2, 7, 1000000000 };

    public static void main(String[] args) {
        for (int shiftIndex = 0; shiftIndex < DEMO_SHIFTS.length; shiftIndex++) {
            int k = DEMO_SHIFTS[shiftIndex];
            System.out.println("k = " + k + " -> " + Arrays.toString(rotateRoster(DEMO_NAMES, k)));
        }
    }

    // O(n) time, O(n) space for the new roster.
    // Rotating one place at a time costs O(n * k); with k up to 10^9 that is billions of moves,
    // while k % n turns any k into at most n - 1 places.
    static String[] rotateRoster(String[] names, int k) {
        int rosterSize = names.length;
        int effectiveShift = k % rosterSize;
        String[] rotatedRoster = new String[rosterSize];
        for (int nameIndex = 0; nameIndex < rosterSize; nameIndex++) {
            rotatedRoster[(nameIndex + effectiveShift) % rosterSize] = names[nameIndex];
        }
        return rotatedRoster;
    }
}

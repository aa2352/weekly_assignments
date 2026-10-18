import java.util.Arrays;

public class SecondBestScore {

    private static final int NOT_FOUND = -1;
    private static final int[][] DEMO_SCORES = { { 45, 78, 92, 78, 60 }, { 50, 50, 50 } };

    public static void main(String[] args) {
        for (int caseIndex = 0; caseIndex < DEMO_SCORES.length; caseIndex++) {
            System.out.println("scores = " + Arrays.toString(DEMO_SCORES[caseIndex]) + " -> "
                    + secondHighest(DEMO_SCORES[caseIndex]));
        }
    }

    // One pass: O(n) time, O(1) space.
    // Sorting first would cost O(n log n) time and either modify the array or need an O(n) copy.
    static int secondHighest(int[] scores) {
        int highest = NOT_FOUND;
        int second = NOT_FOUND;
        for (int scoreIndex = 0; scoreIndex < scores.length; scoreIndex++) {
            int score = scores[scoreIndex];
            if (score > highest) {
                second = highest;
                highest = score;
            } else if (score < highest && score > second) {
                second = score;
            }
        }
        return second;
    }
}

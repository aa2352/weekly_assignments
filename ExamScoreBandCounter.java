public class ExamScoreBandCounter {

    private static final int[] DEMO_SCORES = { 35, 42, 42, 50, 58, 58, 58, 63, 71, 88 };
    private static final int[][] DEMO_BANDS = { { 42, 58 }, { 90, 100 } };

    public static void main(String[] args) {
        for (int bandIndex = 0; bandIndex < DEMO_BANDS.length; bandIndex++) {
            int low = DEMO_BANDS[bandIndex][0];
            int high = DEMO_BANDS[bandIndex][1];
            System.out.println("low = " + low + ", high = " + high + " -> " + countInBand(DEMO_SCORES, low, high));
        }
    }

    // Linear scan: O(n) time, O(1) space per query.
    // This solution: two binary searches, O(log n) time, O(1) space per query.
    // Duplicates at the edges mean an ordinary binary search could stop on any copy of low or high,
    // so each search keeps narrowing until it reaches the first position past the boundary.
    static int countInBand(int[] scores, int low, int high) {
        int firstAtLeastLow = firstIndexWhereAbove(scores, low - 1);
        int firstAboveHigh = firstIndexWhereAbove(scores, high);
        return firstAboveHigh - firstAtLeastLow;
    }

    private static int firstIndexWhereAbove(int[] scores, int limit) {
        int leftIndex = 0;
        int rightIndex = scores.length;
        while (leftIndex < rightIndex) {
            int middleIndex = leftIndex + (rightIndex - leftIndex) / 2;
            if (scores[middleIndex] > limit) {
                rightIndex = middleIndex;
            } else {
                leftIndex = middleIndex + 1;
            }
        }
        return leftIndex;
    }
}

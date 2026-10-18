public class BusiestBusRow {

    private static final int[][] DEMO_GRID = {
            { 2, 0, 1 },
            { 3, 3, 1 },
            { 1, 1, 1 }
    };

    public static void main(String[] args) {
        int[] busiest = busiestRow(DEMO_GRID);
        System.out.println("Row " + busiest[0] + ", Total " + busiest[1]);
    }

    // O(r * c) time, O(1) additional space.
    static int[] busiestRow(int[][] grid) {
        int bestRow = 0;
        int bestTotal = -1;
        for (int rowIndex = 0; rowIndex < grid.length; rowIndex++) {
            int rowTotal = 0;
            for (int seatIndex = 0; seatIndex < grid[rowIndex].length; seatIndex++) {
                rowTotal = rowTotal + grid[rowIndex][seatIndex];
            }
            if (rowTotal > bestTotal) {
                bestTotal = rowTotal;
                bestRow = rowIndex;
            }
        }
        return new int[] { bestRow, bestTotal };
    }
}

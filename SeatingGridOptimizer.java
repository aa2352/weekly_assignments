import java.util.NoSuchElementException;
import java.util.Scanner;

public class SeatingGridOptimizer {

    private static final String ROW_SEPARATOR = ";";
    private static final String VALUE_SEPARATOR = ",";
    private static final String QUIET_ZONE = "Quiet Zone";
    private static final String BUZZING_ZONE = "Buzzing Zone";
    private static final String REPORT_SEPARATOR = " | ";
    private static final int DEFAULT_THRESHOLD = 60;
    private static final String[] DEMO_GRID_LINES = {
            "40,50,45;85,90,95;30,20,25",
            "40,50;85;30,20,25,99",
            "60,60,60;59,59,59",
            "40,50;;30,20",
            "   "
    };
    private static final int[] DEMO_THRESHOLDS = { 60, 60, 60, 60, 60 };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] gridLines = DEMO_GRID_LINES;
        int[] thresholds = DEMO_THRESHOLDS;

        String typedGrid = readLine(inputReader, "Enter the seating grid (press Enter to use the demo list): ");
        if (!typedGrid.trim().isEmpty()) {
            String typedThreshold = readLine(inputReader, "Enter the threshold: ");
            gridLines = new String[] { typedGrid };
            thresholds = new int[] { readThreshold(typedThreshold) };
        }
        System.out.println();

        for (int caseIndex = 0; caseIndex < gridLines.length; caseIndex++) {
            reportZoneClassification(gridLines[caseIndex], thresholds[caseIndex]);
        }
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== Hackathon Seating Grid Optimizer ===");
        System.out.println("\tInput format:\t\"40,50,45;85,90,95;30,20,25\" with a threshold of " + DEFAULT_THRESHOLD);
        System.out.println("\tRule:\trows may be jagged, and one helper computes every average\n");
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

    private static int readThreshold(String typedThreshold) {
        try {
            return Integer.parseInt(typedThreshold.trim());
        } catch (NumberFormatException nonNumericThreshold) {
            System.out.println("[the threshold is not a whole number - using " + DEFAULT_THRESHOLD + "]");
            return DEFAULT_THRESHOLD;
        }
    }

    private static void reportZoneClassification(String gridLine, int threshold) {
        System.out.println("seatingScores = {" + gridLine + "}, threshold = " + threshold);
        try {
            int[][] seatingScores = parseGrid(gridLine);
            System.out.println(classifyRows(seatingScores, threshold));
            System.out.println("Rows walked: " + seatingScores.length + "\n");
        } catch (EmptyRowException emptyRow) {
            System.out.println("No walk planned: " + emptyRow.getMessage() + "\n");
        } catch (NumberFormatException nonNumericScore) {
            System.out.println("No walk planned: every seat score must be a whole number\n");
        } catch (IllegalArgumentException emptyGrid) {
            System.out.println("No walk planned: " + emptyGrid.getMessage() + "\n");
        }
    }

    static String classifyRows(int[][] seatingScores, int threshold) throws EmptyRowException {
        validateGrid(seatingScores);

        StringBuilder zoneReport = new StringBuilder();
        for (int rowIndex = 0; rowIndex < seatingScores.length; rowIndex++) {
            if (rowIndex > 0) {
                zoneReport.append(REPORT_SEPARATOR);
            }
            double averageScore = rowAverage(seatingScores[rowIndex]);
            zoneReport.append("Row ").append(rowIndex).append(": ");
            zoneReport.append(averageScore < threshold ? QUIET_ZONE : BUZZING_ZONE);
        }
        return zoneReport.toString();
    }

    private static double rowAverage(int[] row) {
        int rowTotal = 0;
        for (int seatIndex = 0; seatIndex < row.length; seatIndex++) {
            rowTotal += row[seatIndex];
        }
        return (double) rowTotal / row.length;
    }

    private static void validateGrid(int[][] seatingScores) throws EmptyRowException {
        if (seatingScores.length == 0) {
            throw new IllegalArgumentException("the venue has no rows");
        }
        for (int rowIndex = 0; rowIndex < seatingScores.length; rowIndex++) {
            if (seatingScores[rowIndex] == null || seatingScores[rowIndex].length == 0) {
                throw new EmptyRowException("row " + rowIndex + " has no seats to average");
            }
        }
    }

    private static int[][] parseGrid(String gridLine) {
        if (gridLine == null || gridLine.trim().isEmpty()) {
            throw new IllegalArgumentException("the seating grid is empty");
        }
        String[] rowFields = gridLine.split(ROW_SEPARATOR, -1);
        int[][] seatingScores = new int[rowFields.length][];
        for (int rowIndex = 0; rowIndex < rowFields.length; rowIndex++) {
            seatingScores[rowIndex] = parseRow(rowFields[rowIndex]);
        }
        return seatingScores;
    }

    private static int[] parseRow(String rowField) {
        String trimmedRow = rowField.trim();
        if (trimmedRow.isEmpty()) {
            return new int[0];
        }
        String[] valueFields = trimmedRow.split(VALUE_SEPARATOR);
        int[] rowScores = new int[valueFields.length];
        for (int seatIndex = 0; seatIndex < valueFields.length; seatIndex++) {
            rowScores[seatIndex] = Integer.parseInt(valueFields[seatIndex].trim());
        }
        return rowScores;
    }
}

class EmptyRowException extends Exception {
    EmptyRowException(String message) {
        super(message);
    }
}

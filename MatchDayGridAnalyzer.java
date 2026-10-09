import java.util.NoSuchElementException;
import java.util.Scanner;

public class MatchDayGridAnalyzer {

    private static final String ROW_SEPARATOR = ";";
    private static final String VALUE_SEPARATOR = ",";
    private static final String POWER_SURGE = "Power Surge";
    private static final String NORMAL = "Normal";
    private static final String REPORT_SEPARATOR = " | ";
    private static final int DEFAULT_THRESHOLD = 8;
    private static final String[] DEMO_GRID_LINES = {
            "4,6,8;10,12,14;2,3,1",
            "4,6;10,12,14,16;2",
            "8,8,8;7,9,7",
            "4,6;;2,3",
            "   "
    };
    private static final int[] DEMO_THRESHOLDS = { 8, 8, 8, 8, 8 };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] gridLines = DEMO_GRID_LINES;
        int[] thresholds = DEMO_THRESHOLDS;

        String typedGrid = readLine(inputReader, "Enter the runs-per-over grid (press Enter to use the demo list): ");
        if (!typedGrid.trim().isEmpty()) {
            String typedThreshold = readLine(inputReader, "Enter the threshold: ");
            gridLines = new String[] { typedGrid };
            thresholds = new int[] { readThreshold(typedThreshold) };
        }
        System.out.println();

        for (int caseIndex = 0; caseIndex < gridLines.length; caseIndex++) {
            reportMatchClassification(gridLines[caseIndex], thresholds[caseIndex]);
        }
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== Match Day Grid Analyzer ===");
        System.out.println("\tInput format:\t\"4,6,8;10,12,14;2,3,1\" with a threshold of " + DEFAULT_THRESHOLD);
        System.out.println("\tRule:\tmatches may record different numbers of overs\n");
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

    private static void reportMatchClassification(String gridLine, int threshold) {
        System.out.println("runsPerOver = {" + gridLine + "}, threshold = " + threshold);
        try {
            int[][] runsPerOver = parseGrid(gridLine);
            System.out.println(classifyMatches(runsPerOver, threshold));
            System.out.println("Matches analysed: " + runsPerOver.length + "\n");
        } catch (EmptyMatchException emptyMatch) {
            System.out.println("No analysis: " + emptyMatch.getMessage() + "\n");
        } catch (NumberFormatException nonNumericRun) {
            System.out.println("No analysis: every over must record a whole number of runs\n");
        } catch (IllegalArgumentException emptyGrid) {
            System.out.println("No analysis: " + emptyGrid.getMessage() + "\n");
        }
    }

    static String classifyMatches(int[][] runsPerOver, int threshold) throws EmptyMatchException {
        validateGrid(runsPerOver);

        StringBuilder matchReport = new StringBuilder();
        for (int matchIndex = 0; matchIndex < runsPerOver.length; matchIndex++) {
            if (matchIndex > 0) {
                matchReport.append(REPORT_SEPARATOR);
            }
            double averageRuns = rowAverage(runsPerOver[matchIndex]);
            matchReport.append("Match ").append(matchIndex).append(": ");
            matchReport.append(averageRuns >= threshold ? POWER_SURGE : NORMAL);
        }
        return matchReport.toString();
    }

    private static double rowAverage(int[] row) {
        int rowTotal = 0;
        for (int overIndex = 0; overIndex < row.length; overIndex++) {
            rowTotal += row[overIndex];
        }
        return (double) rowTotal / row.length;
    }

    private static void validateGrid(int[][] runsPerOver) throws EmptyMatchException {
        if (runsPerOver.length == 0) {
            throw new IllegalArgumentException("the log holds no matches");
        }
        for (int matchIndex = 0; matchIndex < runsPerOver.length; matchIndex++) {
            if (runsPerOver[matchIndex] == null || runsPerOver[matchIndex].length == 0) {
                throw new EmptyMatchException("match " + matchIndex + " has no overs to average");
            }
        }
    }

    private static int[][] parseGrid(String gridLine) {
        if (gridLine == null || gridLine.trim().isEmpty()) {
            throw new IllegalArgumentException("the match log is empty");
        }
        String[] rowFields = gridLine.split(ROW_SEPARATOR, -1);
        int[][] runsPerOver = new int[rowFields.length][];
        for (int matchIndex = 0; matchIndex < rowFields.length; matchIndex++) {
            runsPerOver[matchIndex] = parseRow(rowFields[matchIndex]);
        }
        return runsPerOver;
    }

    private static int[] parseRow(String rowField) {
        String trimmedRow = rowField.trim();
        if (trimmedRow.isEmpty()) {
            return new int[0];
        }
        String[] valueFields = trimmedRow.split(VALUE_SEPARATOR);
        int[] rowRuns = new int[valueFields.length];
        for (int overIndex = 0; overIndex < valueFields.length; overIndex++) {
            rowRuns[overIndex] = Integer.parseInt(valueFields[overIndex].trim());
        }
        return rowRuns;
    }
}

class EmptyMatchException extends Exception {
    EmptyMatchException(String message) {
        super(message);
    }
}

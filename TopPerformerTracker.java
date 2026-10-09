import java.util.NoSuchElementException;
import java.util.Scanner;

public class TopPerformerTracker {

    private static final String VALUE_SEPARATOR = ",";
    private static final int MINIMUM_TEAM_COUNT = 2;
    private static final String[] DEMO_SCORE_LINES = {
            "45,82,79,90,33,90,61", "10,20", "70,70,70", "55", "12,7,y", "   " };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] scoreLines = collectScoreLines(inputReader);
        for (int caseIndex = 0; caseIndex < scoreLines.length; caseIndex++) {
            reportWeeklyRecap(scoreLines[caseIndex]);
        }
        System.out.println("Weekly recaps published in this session: " + WeeklyRecap.getRecapsPublished());
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== Top Performer Tracker ===");
        System.out.println("\tInput format:\t\"45,82,79,90,33,90,61\"");
        System.out.println("\tRule:\tone pass for both the minimum and the maximum, no sorting\n");
    }

    private static String[] collectScoreLines(Scanner inputReader) {
        System.out.print("Enter the weekly scores (press Enter to use the demo list): ");
        try {
            String typedScores = inputReader.nextLine();
            if (typedScores.trim().isEmpty()) {
                System.out.println("[no scores typed - running the demo list]\n");
                return DEMO_SCORE_LINES;
            }
            System.out.println();
            return new String[] { typedScores };
        } catch (NoSuchElementException | IllegalStateException consoleUnavailable) {
            System.out.println("\n[no console input - running the demo list]\n");
            return DEMO_SCORE_LINES;
        }
    }

    private static void reportWeeklyRecap(String scoreLine) {
        System.out.println("scores = {" + scoreLine + "}");
        try {
            int[] scores = parseIntArray(scoreLine);
            System.out.println(findMinMaxSpread(scores));
            System.out.println("Teams recapped: " + scores.length + "\n");
        } catch (SmallLeagueException smallLeague) {
            System.out.println("No recap: " + smallLeague.getMessage() + "\n");
        } catch (NumberFormatException nonNumericScore) {
            System.out.println("No recap: every score must be a whole number\n");
        } catch (IllegalArgumentException emptyLeague) {
            System.out.println("No recap: " + emptyLeague.getMessage() + "\n");
        }
    }

    static String findMinMaxSpread(int[] scores) throws SmallLeagueException {
        if (scores.length < MINIMUM_TEAM_COUNT) {
            throw new SmallLeagueException("at least " + MINIMUM_TEAM_COUNT
                    + " teams are needed but only " + scores.length + " played");
        }

        int lowestScore = scores[0];
        int highestScore = scores[0];
        for (int teamIndex = 1; teamIndex < scores.length; teamIndex++) {
            int currentScore = scores[teamIndex];
            if (currentScore < lowestScore) {
                lowestScore = currentScore;
            }
            if (currentScore > highestScore) {
                highestScore = currentScore;
            }
        }
        return new WeeklyRecap(lowestScore, highestScore).describe();
    }

    private static int[] parseIntArray(String valueLine) {
        if (valueLine == null || valueLine.trim().isEmpty()) {
            throw new IllegalArgumentException("no team has scored yet");
        }
        String[] valueFields = valueLine.split(VALUE_SEPARATOR);
        int[] values = new int[valueFields.length];
        for (int fieldIndex = 0; fieldIndex < valueFields.length; fieldIndex++) {
            values[fieldIndex] = Integer.parseInt(valueFields[fieldIndex].trim());
        }
        return values;
    }
}

class WeeklyRecap {

    private static int recapsPublished = 0;

    private final int lowestScore;
    private final int highestScore;
    private final int spread;

    WeeklyRecap(int onlyScore) {
        this(onlyScore, onlyScore);
    }

    WeeklyRecap(int lowestScore, int highestScore) {
        recapsPublished++;
        this.lowestScore = lowestScore;
        this.highestScore = highestScore;
        this.spread = highestScore - lowestScore;
    }

    String describe() {
        return "Min: " + this.lowestScore + " | Max: " + this.highestScore + " | Spread: " + this.spread;
    }

    static int getRecapsPublished() {
        return recapsPublished;
    }
}

class SmallLeagueException extends Exception {
    SmallLeagueException(String message) {
        super(message);
    }
}

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class HackathonScoreCurveBooster {

    private static final String VALUE_SEPARATOR = ",";
    private static final String[] DEMO_SCORE_LINES = { "70,85,60", "45,90,33,61", "100,100", "70,x,60", "   " };
    private static final int[] DEMO_BONUSES = { 10, 5, 0, 10, 10 };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] scoreLines = DEMO_SCORE_LINES;
        int[] bonuses = DEMO_BONUSES;

        String typedScores = readLine(inputReader, "Enter the team scores (press Enter to use the demo list): ");
        if (!typedScores.trim().isEmpty()) {
            String typedBonus = readLine(inputReader, "Enter the flat bonus: ");
            scoreLines = new String[] { typedScores };
            bonuses = new int[] { readBonus(typedBonus) };
        }
        System.out.println();

        for (int caseIndex = 0; caseIndex < scoreLines.length; caseIndex++) {
            reportCurvedLeaderboard(scoreLines[caseIndex], bonuses[caseIndex]);
        }
        System.out.println("Curve rounds applied in this session: " + CurveRound.getRoundsApplied());
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== Hackathon Score Curve Booster ===");
        System.out.println("\tInput format:\t\"70,85,60\" with a bonus of " + CurveRound.STANDARD_BONUS);
        System.out.println("\tRule:\tthe method edits the caller's own array and returns nothing\n");
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

    private static int readBonus(String typedBonus) {
        try {
            return Integer.parseInt(typedBonus.trim());
        } catch (NumberFormatException nonNumericBonus) {
            System.out.println("[the bonus is not a whole number - using " + CurveRound.STANDARD_BONUS + "]");
            return CurveRound.STANDARD_BONUS;
        }
    }

    private static void reportCurvedLeaderboard(String scoreLine, int bonus) {
        System.out.println("scores = [" + scoreLine + "], bonus = " + bonus);
        try {
            int[] scores = parseIntArray(scoreLine);
            CurveRound curveRound = createCurveRound(bonus);

            System.out.println("Curve round " + curveRound.getRoundNumber()
                    + " (bonus +" + curveRound.getBonus() + ")");
            System.out.println("Before: " + Arrays.toString(scores));
            curveScores(scores, curveRound.getBonus());
            System.out.println("After : " + Arrays.toString(scores));
            System.out.println("Teams on the leaderboard: " + scores.length + "\n");
        } catch (EmptyScoreboardException emptyScoreboard) {
            System.out.println("No curve applied: " + emptyScoreboard.getMessage() + "\n");
        } catch (NumberFormatException nonNumericScore) {
            System.out.println("No curve applied: every score must be a whole number\n");
        } catch (IllegalArgumentException invalidBonus) {
            System.out.println("No curve applied: " + invalidBonus.getMessage() + "\n");
        }
    }

    private static CurveRound createCurveRound(int bonus) {
        if (bonus == CurveRound.STANDARD_BONUS) {
            return new CurveRound();
        }
        return new CurveRound(bonus);
    }

    static void curveScores(int[] scores, int bonus) {
        if (bonus < 0) {
            throw new IllegalArgumentException("the bonus " + bonus + " cannot be negative");
        }
        for (int teamIndex = 0; teamIndex < scores.length; teamIndex++) {
            scores[teamIndex] = scores[teamIndex] + bonus;
        }
    }

    private static int[] parseIntArray(String valueLine) throws EmptyScoreboardException {
        if (valueLine == null || valueLine.trim().isEmpty()) {
            throw new EmptyScoreboardException("the leaderboard holds no teams");
        }
        String[] valueFields = valueLine.split(VALUE_SEPARATOR);
        int[] values = new int[valueFields.length];
        for (int fieldIndex = 0; fieldIndex < valueFields.length; fieldIndex++) {
            values[fieldIndex] = Integer.parseInt(valueFields[fieldIndex].trim());
        }
        return values;
    }
}

class CurveRound {

    static final int STANDARD_BONUS = 10;

    private static int roundsApplied = 0;

    private final int roundNumber;
    private final int bonus;

    CurveRound() {
        this(STANDARD_BONUS);
    }

    CurveRound(int bonus) {
        roundsApplied++;
        this.roundNumber = roundsApplied;
        this.bonus = bonus;
    }

    int getRoundNumber() {
        return this.roundNumber;
    }

    int getBonus() {
        return this.bonus;
    }

    static int getRoundsApplied() {
        return roundsApplied;
    }
}

class EmptyScoreboardException extends Exception {
    EmptyScoreboardException(String message) {
        super(message);
    }
}

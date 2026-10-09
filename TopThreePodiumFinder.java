import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class TopThreePodiumFinder {

    private static final String VALUE_SEPARATOR = ",";
    private static final int PODIUM_SIZE = 3;
    private static final String[] DEMO_SCORE_LINES = {
            "45,82,79,90,33,90,61", "10,20,30", "90,90,90,5", "70,65", "12,7,y" };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] scoreLines = collectScoreLines(inputReader);
        for (int caseIndex = 0; caseIndex < scoreLines.length; caseIndex++) {
            reportPodium(scoreLines[caseIndex]);
        }
        System.out.println("Podiums announced in this session: " + Podium.getPodiumsAnnounced());
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== Top-3 Podium Finder ===");
        System.out.println("\tInput format:\t\"45,82,79,90,33,90,61\"");
        System.out.println("\tRule:\tone left-to-right pass, no sorting of any kind\n");
    }

    private static String[] collectScoreLines(Scanner inputReader) {
        System.out.print("Enter the team scores (press Enter to use the demo list): ");
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

    private static void reportPodium(String scoreLine) {
        System.out.println("scores = {" + scoreLine + "}");
        try {
            int[] scores = parseIntArray(scoreLine);
            int[] topThreeScores = findTopThreeScores(scores);
            Podium podium = new Podium(topThreeScores);

            System.out.println(Arrays.toString(topThreeScores));
            System.out.println(podium.describe());
            System.out.println("Teams scanned: " + scores.length + "\n");
        } catch (SmallFieldException smallField) {
            System.out.println("No podium: " + smallField.getMessage() + "\n");
        } catch (NumberFormatException nonNumericScore) {
            System.out.println("No podium: every score must be a whole number\n");
        } catch (IllegalArgumentException emptyField) {
            System.out.println("No podium: " + emptyField.getMessage() + "\n");
        }
    }

    static int[] findTopThreeScores(int[] scores) throws SmallFieldException {
        if (scores.length < PODIUM_SIZE) {
            throw new SmallFieldException("at least " + PODIUM_SIZE
                    + " teams are needed but only " + scores.length + " scored");
        }

        int firstPlace = Integer.MIN_VALUE;
        int secondPlace = Integer.MIN_VALUE;
        int thirdPlace = Integer.MIN_VALUE;

        for (int teamIndex = 0; teamIndex < scores.length; teamIndex++) {
            int currentScore = scores[teamIndex];
            if (currentScore > firstPlace) {
                thirdPlace = secondPlace;
                secondPlace = firstPlace;
                firstPlace = currentScore;
            } else if (currentScore > secondPlace) {
                thirdPlace = secondPlace;
                secondPlace = currentScore;
            } else if (currentScore > thirdPlace) {
                thirdPlace = currentScore;
            }
        }
        return new int[] { firstPlace, secondPlace, thirdPlace };
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

class Podium {

    private static int podiumsAnnounced = 0;

    private final int podiumNumber;
    private final int firstPlace;
    private final int secondPlace;
    private final int thirdPlace;

    Podium(int[] topThreeScores) {
        this(topThreeScores[0], topThreeScores[1], topThreeScores[2]);
    }

    Podium(int firstPlace, int secondPlace, int thirdPlace) {
        podiumsAnnounced++;
        this.podiumNumber = podiumsAnnounced;
        this.firstPlace = firstPlace;
        this.secondPlace = secondPlace;
        this.thirdPlace = thirdPlace;
    }

    String describe() {
        return "Podium " + this.podiumNumber + " - Gold: " + this.firstPlace
                + " | Silver: " + this.secondPlace + " | Bronze: " + this.thirdPlace;
    }

    static int getPodiumsAnnounced() {
        return podiumsAnnounced;
    }
}

class SmallFieldException extends Exception {
    SmallFieldException(String message) {
        super(message);
    }
}

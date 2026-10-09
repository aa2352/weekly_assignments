import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class FantasyTeamScoreMultiplier {

    private static final String VALUE_SEPARATOR = ",";
    private static final int ROLE_FIELD_COUNT = 2;
    private static final String[] DEMO_SCORE_LINES = {
            "40,55,30,62", "10,20,30", "12.5,18,7", "40,55", "40,55", "70,x,60", "   " };
    private static final String[] DEMO_ROLE_LINES = {
            "1,3", "0,2", "2,0", "0,5", "1,1", "0,1", "0,1" };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] scoreLines = DEMO_SCORE_LINES;
        String[] roleLines = DEMO_ROLE_LINES;

        String typedScores = readLine(inputReader, "Enter the lineup scores (press Enter to use the demo list): ");
        if (!typedScores.trim().isEmpty()) {
            String typedRoles = readLine(inputReader, "Enter the captain and vice-captain index (e.g. 1,3): ");
            scoreLines = new String[] { typedScores };
            roleLines = new String[] { typedRoles };
        }
        System.out.println();

        for (int caseIndex = 0; caseIndex < scoreLines.length; caseIndex++) {
            reportBoostedLineup(scoreLines[caseIndex], roleLines[caseIndex]);
        }
        System.out.println("Boost slips applied in this session: " + BoostSlip.getSlipsApplied());
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== Fantasy Team Score Multiplier ===");
        System.out.println("\tInput format:\t\"40,55,30,62\" with roles \"1,3\"");
        System.out.println("\tCaptain:\tx" + BoostSlip.CAPTAIN_MULTIPLIER
                + "\tVice-Captain:\tx" + BoostSlip.VICE_CAPTAIN_MULTIPLIER);
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

    private static void reportBoostedLineup(String scoreLine, String roleLine) {
        System.out.println("scores = [" + scoreLine + "], roles = [" + roleLine + "]");
        try {
            double[] playerScores = parseDoubleArray(scoreLine);
            int[] roleIndexes = parseRoleIndexes(roleLine);
            validateRoleIndexes(playerScores, roleIndexes);
            BoostSlip boostSlip = new BoostSlip();

            System.out.println("Boost slip " + boostSlip.getSlipNumber());
            System.out.println("Before: " + Arrays.toString(playerScores));
            applyMultipliers(playerScores, roleIndexes[0], roleIndexes[1]);
            System.out.println("After : " + Arrays.toString(playerScores));
            System.out.println("Players in the lineup: " + playerScores.length + "\n");
        } catch (InvalidRoleSelectionException invalidRoleSelection) {
            System.out.println("No boost applied: " + invalidRoleSelection.getMessage() + "\n");
        } catch (ArrayIndexOutOfBoundsException missingPlayer) {
            System.out.println("No boost applied: a chosen index is outside the lineup\n");
        } catch (NumberFormatException nonNumericValue) {
            System.out.println("No boost applied: scores and indexes must be numbers\n");
        } catch (IllegalArgumentException emptyLineup) {
            System.out.println("No boost applied: " + emptyLineup.getMessage() + "\n");
        }
    }

    static void applyMultipliers(double[] playerScores, int captainIndex, int viceCaptainIndex) {
        playerScores[captainIndex] = playerScores[captainIndex] * BoostSlip.CAPTAIN_MULTIPLIER;
        playerScores[viceCaptainIndex] = playerScores[viceCaptainIndex] * BoostSlip.VICE_CAPTAIN_MULTIPLIER;
    }

    private static double[] parseDoubleArray(String valueLine) {
        if (valueLine == null || valueLine.trim().isEmpty()) {
            throw new IllegalArgumentException("the lineup holds no players");
        }
        String[] valueFields = valueLine.split(VALUE_SEPARATOR);
        double[] values = new double[valueFields.length];
        for (int fieldIndex = 0; fieldIndex < valueFields.length; fieldIndex++) {
            values[fieldIndex] = Double.parseDouble(valueFields[fieldIndex].trim());
        }
        return values;
    }

    private static int[] parseRoleIndexes(String roleLine) throws InvalidRoleSelectionException {
        String[] roleFields = roleLine.split(VALUE_SEPARATOR);
        if (roleFields.length != ROLE_FIELD_COUNT) {
            throw new InvalidRoleSelectionException("exactly two indexes are needed but found "
                    + roleFields.length);
        }
        int captainIndex = Integer.parseInt(roleFields[0].trim());
        int viceCaptainIndex = Integer.parseInt(roleFields[1].trim());
        if (captainIndex == viceCaptainIndex) {
            throw new InvalidRoleSelectionException("the captain and vice-captain must be different players");
        }
        return new int[] { captainIndex, viceCaptainIndex };
    }

    private static void validateRoleIndexes(double[] playerScores, int[] roleIndexes)
            throws InvalidRoleSelectionException {
        for (int roleIndex = 0; roleIndex < roleIndexes.length; roleIndex++) {
            int chosenIndex = roleIndexes[roleIndex];
            if (chosenIndex < 0 || chosenIndex >= playerScores.length) {
                throw new InvalidRoleSelectionException("index " + chosenIndex
                        + " is outside a lineup of " + playerScores.length + " players");
            }
        }
    }
}

class BoostSlip {

    static final double CAPTAIN_MULTIPLIER = 2.0;
    static final double VICE_CAPTAIN_MULTIPLIER = 1.5;

    private static int slipsApplied = 0;

    private final int slipNumber;
    private final double captainMultiplier;
    private final double viceCaptainMultiplier;

    BoostSlip() {
        this(CAPTAIN_MULTIPLIER, VICE_CAPTAIN_MULTIPLIER);
    }

    BoostSlip(double captainMultiplier, double viceCaptainMultiplier) {
        slipsApplied++;
        this.slipNumber = slipsApplied;
        this.captainMultiplier = captainMultiplier;
        this.viceCaptainMultiplier = viceCaptainMultiplier;
    }

    int getSlipNumber() {
        return this.slipNumber;
    }

    double getCaptainMultiplier() {
        return this.captainMultiplier;
    }

    double getViceCaptainMultiplier() {
        return this.viceCaptainMultiplier;
    }

    static int getSlipsApplied() {
        return slipsApplied;
    }
}

class InvalidRoleSelectionException extends Exception {
    InvalidRoleSelectionException(String message) {
        super(message);
    }
}

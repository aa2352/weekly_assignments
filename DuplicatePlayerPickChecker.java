import java.util.NoSuchElementException;
import java.util.Scanner;

public class DuplicatePlayerPickChecker {

    private static final String NAME_SEPARATOR = ",";
    private static final String DUPLICATE_PREFIX = "Duplicate Found: ";
    private static final String NO_DUPLICATE_MESSAGE = "No Duplicates Found";
    private static final int MAX_LINEUP_SIZE = 11;
    private static final String[] DEMO_LINEUP_LINES = {
            "Kohli,Bumrah,Kohli,Rohit",
            "Kohli,Bumrah,Rohit",
            "kohli,Kohli,Bumrah",
            "Gill,Pant,Gill,Pant",
            "   "
    };
    private static final Object[] DEMO_DRAFT_TAPS = {
            "Kohli", Integer.valueOf(18), "Bumrah", null, "Kohli" };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] lineupLines = collectLineupLines(inputReader);
        for (int caseIndex = 0; caseIndex < lineupLines.length; caseIndex++) {
            reportDuplicatePick(lineupLines[caseIndex]);
        }
        reportRawDraftTaps();
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== Duplicate Player Pick Checker ===");
        System.out.println("\tInput format:\t\"Kohli,Bumrah,Kohli,Rohit\"");
        System.out.println("\tRule:\tnested loops only, and \"Kohli\" differs from \"kohli\"\n");
    }

    private static String[] collectLineupLines(Scanner inputReader) {
        System.out.print("Enter the submitted lineup (press Enter to use the demo list): ");
        try {
            String typedLineup = inputReader.nextLine();
            if (typedLineup.trim().isEmpty()) {
                System.out.println("[no lineup typed - running the demo list]\n");
                return DEMO_LINEUP_LINES;
            }
            System.out.println();
            return new String[] { typedLineup };
        } catch (NoSuchElementException | IllegalStateException consoleUnavailable) {
            System.out.println("\n[no console input - running the demo list]\n");
            return DEMO_LINEUP_LINES;
        }
    }

    private static void reportDuplicatePick(String lineupLine) {
        System.out.println("playerNames = {" + lineupLine + "}");
        try {
            String[] playerNames = parsePlayerNames(lineupLine);
            System.out.println(findDuplicatePick(playerNames));
            System.out.println("Picks checked: " + playerNames.length + "\n");
        } catch (EmptyLineupException emptyLineup) {
            System.out.println("Lineup rejected: " + emptyLineup.getMessage() + "\n");
        } catch (IllegalArgumentException oversizedLineup) {
            System.out.println("Lineup rejected: " + oversizedLineup.getMessage() + "\n");
        }
    }

    private static void reportRawDraftTaps() {
        System.out.println("Raw draft-screen taps (names mixed with stray UI values):");
        try {
            System.out.println(findDuplicatePick(DEMO_DRAFT_TAPS));
            System.out.println("Taps received: " + DEMO_DRAFT_TAPS.length);
        } catch (EmptyLineupException emptyLineup) {
            System.out.println("Lineup rejected: " + emptyLineup.getMessage());
        }
    }

    static String findDuplicatePick(String[] playerNames) {
        for (int firstIndex = 0; firstIndex < playerNames.length; firstIndex++) {
            for (int secondIndex = firstIndex + 1; secondIndex < playerNames.length; secondIndex++) {
                if (playerNames[firstIndex].equals(playerNames[secondIndex])) {
                    return DUPLICATE_PREFIX + playerNames[firstIndex];
                }
            }
        }
        return NO_DUPLICATE_MESSAGE;
    }

    static String findDuplicatePick(Object[] draftTaps) throws EmptyLineupException {
        int nameCount = 0;
        for (int tapIndex = 0; tapIndex < draftTaps.length; tapIndex++) {
            if (draftTaps[tapIndex] instanceof String) {
                nameCount++;
            }
        }
        if (nameCount == 0) {
            throw new EmptyLineupException("no tap on the draft screen is a player name");
        }

        String[] playerNames = new String[nameCount];
        int nameIndex = 0;
        for (int tapIndex = 0; tapIndex < draftTaps.length; tapIndex++) {
            if (draftTaps[tapIndex] instanceof String) {
                playerNames[nameIndex] = (String) draftTaps[tapIndex];
                nameIndex++;
            }
        }
        System.out.println("Usable player names after instanceof filtering: " + nameCount
                + " of " + draftTaps.length);
        return findDuplicatePick(playerNames);
    }

    private static String[] parsePlayerNames(String lineupLine) throws EmptyLineupException {
        if (lineupLine == null) {
            throw new IllegalArgumentException("the lineup reference is null");
        }
        if (lineupLine.trim().isEmpty()) {
            throw new EmptyLineupException("no player has been picked yet");
        }
        String[] nameFields = lineupLine.split(NAME_SEPARATOR);
        if (nameFields.length > MAX_LINEUP_SIZE) {
            throw new IllegalArgumentException("a lineup holds at most " + MAX_LINEUP_SIZE
                    + " players but found " + nameFields.length);
        }
        String[] playerNames = new String[nameFields.length];
        for (int fieldIndex = 0; fieldIndex < nameFields.length; fieldIndex++) {
            playerNames[fieldIndex] = nameFields[fieldIndex].trim();
        }
        return playerNames;
    }
}

class EmptyLineupException extends Exception {
    EmptyLineupException(String message) {
        super(message);
    }
}

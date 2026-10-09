import java.util.NoSuchElementException;
import java.util.Scanner;

public class DuplicateTeamNameFinder {

    private static final String NAME_SEPARATOR = ",";
    private static final String DUPLICATE_PREFIX = "Duplicate Found: ";
    private static final String NO_DUPLICATE_MESSAGE = "No Duplicates Found";
    private static final String[] DEMO_REGISTRATION_LINES = {
            "ByteForce,CodeCrafters,ByteForce",
            "ByteForce,CodeCrafters,NullPointers",
            "byteforce,ByteForce,CodeCrafters",
            "StackOverflow,NullPointers,StackOverflow,NullPointers",
            "   "
    };
    private static final Object[] DEMO_MIXED_ENTRIES = {
            "ByteForce", Integer.valueOf(404), "CodeCrafters", null, "ByteForce" };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] registrationLines = collectRegistrationLines(inputReader);
        for (int caseIndex = 0; caseIndex < registrationLines.length; caseIndex++) {
            reportDuplicateTeam(registrationLines[caseIndex]);
        }
        reportMixedRegistrationDesk();
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== Duplicate Team Name Finder ===");
        System.out.println("\tInput format:\t\"ByteForce,CodeCrafters,ByteForce\"");
        System.out.println("\tRule:\tnested loops only, and \"ByteForce\" differs from \"byteforce\"\n");
    }

    private static String[] collectRegistrationLines(Scanner inputReader) {
        System.out.print("Enter the registered team names (press Enter to use the demo list): ");
        try {
            String typedNames = inputReader.nextLine();
            if (typedNames.trim().isEmpty()) {
                System.out.println("[no names typed - running the demo list]\n");
                return DEMO_REGISTRATION_LINES;
            }
            System.out.println();
            return new String[] { typedNames };
        } catch (NoSuchElementException | IllegalStateException consoleUnavailable) {
            System.out.println("\n[no console input - running the demo list]\n");
            return DEMO_REGISTRATION_LINES;
        }
    }

    private static void reportDuplicateTeam(String registrationLine) {
        System.out.println("teamNames = {" + registrationLine + "}");
        try {
            String[] teamNames = parseTeamNames(registrationLine);
            System.out.println(findDuplicateTeam(teamNames));
            System.out.println("Entries checked: " + teamNames.length + "\n");
        } catch (EmptyRegistrationException emptyRegistration) {
            System.out.println("No check run: " + emptyRegistration.getMessage() + "\n");
        } catch (IllegalArgumentException missingEntry) {
            System.out.println("No check run: " + missingEntry.getMessage() + "\n");
        }
    }

    private static void reportMixedRegistrationDesk() {
        System.out.println("Mixed registration desk entries (Strings, numbers and blanks):");
        try {
            System.out.println(findDuplicateTeam(DEMO_MIXED_ENTRIES));
            System.out.println("Entries received: " + DEMO_MIXED_ENTRIES.length);
        } catch (EmptyRegistrationException emptyRegistration) {
            System.out.println("No check run: " + emptyRegistration.getMessage());
        }
    }

    static String findDuplicateTeam(String[] teamNames) {
        for (int firstIndex = 0; firstIndex < teamNames.length; firstIndex++) {
            for (int secondIndex = firstIndex + 1; secondIndex < teamNames.length; secondIndex++) {
                if (teamNames[firstIndex].equals(teamNames[secondIndex])) {
                    return DUPLICATE_PREFIX + teamNames[firstIndex];
                }
            }
        }
        return NO_DUPLICATE_MESSAGE;
    }

    static String findDuplicateTeam(Object[] registrationEntries) throws EmptyRegistrationException {
        int teamNameCount = 0;
        for (int entryIndex = 0; entryIndex < registrationEntries.length; entryIndex++) {
            if (registrationEntries[entryIndex] instanceof String) {
                teamNameCount++;
            }
        }
        if (teamNameCount == 0) {
            throw new EmptyRegistrationException("no entry on the desk is a team name");
        }

        String[] teamNames = new String[teamNameCount];
        int nameIndex = 0;
        for (int entryIndex = 0; entryIndex < registrationEntries.length; entryIndex++) {
            if (registrationEntries[entryIndex] instanceof String) {
                teamNames[nameIndex] = (String) registrationEntries[entryIndex];
                nameIndex++;
            }
        }
        System.out.println("Usable team names after instanceof filtering: " + teamNameCount
                + " of " + registrationEntries.length);
        return findDuplicateTeam(teamNames);
    }

    private static String[] parseTeamNames(String registrationLine) throws EmptyRegistrationException {
        if (registrationLine == null) {
            throw new IllegalArgumentException("the registration list reference is null");
        }
        if (registrationLine.trim().isEmpty()) {
            throw new EmptyRegistrationException("no team has registered yet");
        }
        String[] nameFields = registrationLine.split(NAME_SEPARATOR);
        String[] teamNames = new String[nameFields.length];
        for (int fieldIndex = 0; fieldIndex < nameFields.length; fieldIndex++) {
            teamNames[fieldIndex] = nameFields[fieldIndex].trim();
        }
        return teamNames;
    }
}

class EmptyRegistrationException extends Exception {
    EmptyRegistrationException(String message) {
        super(message);
    }
}

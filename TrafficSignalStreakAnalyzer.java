import java.util.NoSuchElementException;
import java.util.Scanner;

public class TrafficSignalStreakAnalyzer {

    private static final char SIGNAL_RED = 'R';
    private static final char SIGNAL_YELLOW = 'Y';
    private static final char SIGNAL_GREEN = 'G';
    private static final char ASCII_LOWER_A = 'a';
    private static final char ASCII_LOWER_Z = 'z';
    private static final int ASCII_CASE_GAP = 'a' - 'A';
    private static final String[] DEMO_SIGNAL_LOGS = { "RRGGGYRR", "RRRRYYGG", "gggg", "RRBBG", "   " };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] signalLogs = collectSignalLogs(inputReader);
        for (int logIndex = 0; logIndex < signalLogs.length; logIndex++) {
            findLongestStreak(signalLogs[logIndex]);
        }
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== The Traffic Signal Streak Analyzer ===");
        System.out.println("\tCodes:\t'R' = Red\t'Y' = Yellow\t'G' = Green");
        System.out.println("\tLog file:\tC:\\traffic\\junction-log.txt\n");
    }

    private static String[] collectSignalLogs(Scanner inputReader) {
        System.out.print("Enter a signal log (press Enter to use the demo list): ");
        try {
            String typedLog = inputReader.nextLine();
            if (typedLog.trim().isEmpty()) {
                System.out.println("[no log typed - running the demo list]\n");
                return DEMO_SIGNAL_LOGS;
            }
            System.out.println();
            return new String[] { typedLog };
        } catch (NoSuchElementException | IllegalStateException consoleUnavailable) {
            System.out.println("\n[no console input - running the demo list]\n");
            return DEMO_SIGNAL_LOGS;
        }
    }

    private static void findLongestStreak(String signalLog) {
        System.out.println("Signal log: \"" + signalLog + "\"");
        try {
            String cleanedLog = validateSignalLog(signalLog);
            char longestStreakColour = cleanedLog.charAt(0);
            int longestStreakLength = 1;
            int currentStreakLength = 1;

            for (int charIndex = 1; charIndex < cleanedLog.length(); charIndex++) {
                if (cleanedLog.charAt(charIndex) == cleanedLog.charAt(charIndex - 1)) {
                    currentStreakLength++;
                } else {
                    currentStreakLength = 1;
                }
                if (currentStreakLength > longestStreakLength) {
                    longestStreakLength = currentStreakLength;
                    longestStreakColour = cleanedLog.charAt(charIndex);
                }
            }

            System.out.println("Longest Streak: '" + longestStreakColour + "' repeated "
                    + longestStreakLength + " times (" + describeColour(longestStreakColour) + ")\n");
        } catch (InvalidSignalLogException invalidLog) {
            System.out.println("Analysis stopped: " + invalidLog.getMessage() + "\n");
        } catch (IllegalArgumentException emptyLog) {
            System.out.println("Analysis stopped: " + emptyLog.getMessage() + "\n");
        }
    }

    private static String validateSignalLog(String signalLog) throws InvalidSignalLogException {
        if (signalLog == null || signalLog.trim().isEmpty()) {
            throw new IllegalArgumentException("the signal log is empty");
        }
        String trimmedLog = signalLog.trim();
        StringBuilder cleanedLog = new StringBuilder();
        for (int charIndex = 0; charIndex < trimmedLog.length(); charIndex++) {
            char currentChar = toUpperCaseUsingAscii(trimmedLog.charAt(charIndex));
            if (currentChar != SIGNAL_RED && currentChar != SIGNAL_YELLOW && currentChar != SIGNAL_GREEN) {
                throw new InvalidSignalLogException("'" + currentChar + "' (ASCII code " + (int) currentChar
                        + ") is not a valid signal code");
            }
            cleanedLog.append(currentChar);
        }
        return cleanedLog.toString();
    }

    private static char toUpperCaseUsingAscii(char signalChar) {
        if (signalChar >= ASCII_LOWER_A && signalChar <= ASCII_LOWER_Z) {
            return (char) (signalChar - ASCII_CASE_GAP);
        }
        return signalChar;
    }

    private static String describeColour(char signalColour) {
        if (signalColour == SIGNAL_RED) {
            return "Red";
        }
        if (signalColour == SIGNAL_YELLOW) {
            return "Yellow";
        }
        return "Green";
    }
}

class InvalidSignalLogException extends Exception {
    InvalidSignalLogException(String message) {
        super(message);
    }
}

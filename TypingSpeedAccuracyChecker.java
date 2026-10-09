import java.util.NoSuchElementException;
import java.util.Scanner;

public class TypingSpeedAccuracyChecker {

    private static final String[] DEMO_ORIGINAL_PASSAGES = { "hello world", "coding", "java practice", "typing", "" };
    private static final String[] DEMO_TYPED_ATTEMPTS = { "hello worlt", "coding", "java pract1ce", "typin", "" };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] originalPassages = DEMO_ORIGINAL_PASSAGES;
        String[] typedAttempts = DEMO_TYPED_ATTEMPTS;

        String typedOriginal = readLine(inputReader, "Enter the original passage (press Enter to use the demo list): ");
        if (!typedOriginal.trim().isEmpty()) {
            String typedAttempt = readLine(inputReader, "Enter your typed attempt: ");
            originalPassages = new String[] { typedOriginal };
            typedAttempts = new String[] { typedAttempt };
        }
        System.out.println();

        for (int testIndex = 0; testIndex < originalPassages.length; testIndex++) {
            checkTypingAccuracy(originalPassages[testIndex], typedAttempts[testIndex]);
        }
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== The Typing Speed Test Accuracy Checker ===");
        System.out.println("\tRule:\tthe attempt must match the passage \"character by character\"\n");
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

    private static void checkTypingAccuracy(String original, String typed) {
        System.out.println("Original: \"" + original + "\"");
        System.out.println("Typed   : \"" + typed + "\"");
        try {
            validateAttempt(original, typed);
            int matchedCount = countMatchingCharacters(original, typed);
            int firstMismatchPosition = findFirstMismatchPosition(original, typed);
            double accuracyPercentage = calculateAccuracyPercentage(matchedCount, original.length());

            System.out.printf("Matched: %d/%d | Accuracy: %.2f%% | %s%n%n",
                    matchedCount, original.length(), accuracyPercentage,
                    describeFirstMismatch(original, typed, firstMismatchPosition));
        } catch (LengthMismatchException lengthMismatch) {
            System.out.println("Accuracy not measured: " + lengthMismatch.getMessage() + "\n");
        } catch (ArithmeticException emptyPassage) {
            System.out.println("Accuracy not measured: " + emptyPassage.getMessage() + "\n");
        } catch (IllegalArgumentException missingText) {
            System.out.println("Accuracy not measured: " + missingText.getMessage() + "\n");
        }
    }

    private static void validateAttempt(String original, String typed) throws LengthMismatchException {
        if (original == null || typed == null) {
            throw new IllegalArgumentException("the passage or the attempt is null");
        }
        if (original.length() != typed.length()) {
            throw new LengthMismatchException("the passage has " + original.length()
                    + " characters but the attempt has " + typed.length());
        }
    }

    private static int countMatchingCharacters(String original, String typed) {
        int matchedCount = 0;
        for (int charIndex = 0; charIndex < original.length(); charIndex++) {
            if (original.charAt(charIndex) == typed.charAt(charIndex)) {
                matchedCount++;
            }
        }
        return matchedCount;
    }

    private static int findFirstMismatchPosition(String original, String typed) {
        for (int charIndex = 0; charIndex < original.length(); charIndex++) {
            if (original.charAt(charIndex) != typed.charAt(charIndex)) {
                return charIndex + 1;
            }
        }
        return 0;
    }

    private static double calculateAccuracyPercentage(int matchedCount, int totalCharacters) {
        if (totalCharacters <= 0) {
            throw new ArithmeticException("the passage has no characters to compare");
        }
        return (matchedCount * 100.0) / totalCharacters;
    }

    private static String describeFirstMismatch(String original, String typed, int mismatchPosition) {
        if (mismatchPosition == 0) {
            return "No Mismatches";
        }
        char expectedChar = original.charAt(mismatchPosition - 1);
        char typedChar = typed.charAt(mismatchPosition - 1);
        return "First Mismatch at position " + mismatchPosition
                + " ('" + expectedChar + "' vs '" + typedChar + "')"
                + " [ASCII " + (int) expectedChar + " vs " + (int) typedChar + "]";
    }
}

class LengthMismatchException extends Exception {
    LengthMismatchException(String message) {
        super(message);
    }
}

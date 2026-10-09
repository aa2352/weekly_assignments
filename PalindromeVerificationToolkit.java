import java.util.NoSuchElementException;
import java.util.Scanner;

public class PalindromeVerificationToolkit {

    private static final String[] DEMO_TEXTS = {
            "madam", "hello", "Never odd or even", "A man, a plan, a canal: Panama", "   " };
    private static final String LABEL_PALINDROME = "Palindrome";
    private static final String LABEL_NOT_PALINDROME = "Not Palindrome";
    private static final char ASCII_UPPER_A = 'A';
    private static final char ASCII_UPPER_Z = 'Z';
    private static final char ASCII_LOWER_A = 'a';
    private static final char ASCII_LOWER_Z = 'z';
    private static final char ASCII_DIGIT_ZERO = '0';
    private static final char ASCII_DIGIT_NINE = '9';
    private static final int ASCII_CASE_GAP = ASCII_LOWER_A - ASCII_UPPER_A;

    public static void main(String[] args) {
        System.out.println("=== The QA Text Verification Toolkit ===\n");

        Scanner inputReader = new Scanner(System.in);
        String[] textsToVerify = collectTextsToVerify(inputReader);
        for (int textIndex = 0; textIndex < textsToVerify.length; textIndex++) {
            verifyAndReport(textsToVerify[textIndex]);
        }
        inputReader.close();
    }

    private static String[] collectTextsToVerify(Scanner inputReader) {
        System.out.print("Enter a text to verify (press Enter to use the demo list): ");
        try {
            String typedText = inputReader.nextLine();
            if (typedText.trim().isEmpty()) {
                System.out.println("[no text typed - running the demo list]\n");
                return DEMO_TEXTS;
            }
            System.out.println();
            return new String[] { typedText };
        } catch (NoSuchElementException | IllegalStateException consoleUnavailable) {
            System.out.println("\n[no console input - running the demo list]\n");
            return DEMO_TEXTS;
        }
    }

    private static void verifyAndReport(String text) {
        System.out.println("Input: \"" + text + "\"");
        try {
            String comparableText = prepareForComparison(text);
            boolean iterativeResult = isPalindromeIterative(comparableText);
            boolean recursiveResult = isPalindromeRecursive(comparableText);
            boolean arrayReversalResult = isPalindromeArrayReversal(comparableText);

            System.out.println("Iterative: " + describeResult(iterativeResult)
                    + " | Recursive: " + describeResult(recursiveResult)
                    + " | Array Reversal: " + describeResult(arrayReversalResult));
            System.out.println("All three approaches agree: "
                    + (iterativeResult == recursiveResult && recursiveResult == arrayReversalResult) + "\n");
        } catch (EmptyTextException emptyText) {
            System.out.println("Cannot verify: " + emptyText.getMessage() + "\n");
        } catch (IllegalArgumentException missingText) {
            System.out.println("Cannot verify: " + missingText.getMessage() + "\n");
        }
    }

    private static String prepareForComparison(String text) throws EmptyTextException {
        if (text == null) {
            throw new IllegalArgumentException("the text reference is null");
        }
        StringBuilder cleanedText = new StringBuilder();
        for (int charIndex = 0; charIndex < text.length(); charIndex++) {
            char currentChar = text.charAt(charIndex);
            if (currentChar >= ASCII_UPPER_A && currentChar <= ASCII_UPPER_Z) {
                cleanedText.append((char) (currentChar + ASCII_CASE_GAP));
            } else if (currentChar >= ASCII_LOWER_A && currentChar <= ASCII_LOWER_Z) {
                cleanedText.append(currentChar);
            } else if (currentChar >= ASCII_DIGIT_ZERO && currentChar <= ASCII_DIGIT_NINE) {
                cleanedText.append(currentChar);
            }
        }
        if (cleanedText.length() == 0) {
            throw new EmptyTextException("the text has no letters or digits to compare");
        }
        return cleanedText.toString();
    }

    private static boolean isPalindromeIterative(String text) {
        int leftIndex = 0;
        int rightIndex = text.length() - 1;
        while (leftIndex < rightIndex) {
            if (text.charAt(leftIndex) != text.charAt(rightIndex)) {
                return false;
            }
            leftIndex++;
            rightIndex--;
        }
        return true;
    }

    private static boolean isPalindromeRecursive(String text) {
        if (text.length() <= 1) {
            return true;
        }
        if (text.charAt(0) != text.charAt(text.length() - 1)) {
            return false;
        }
        return isPalindromeRecursive(text.substring(1, text.length() - 1));
    }

    private static boolean isPalindromeArrayReversal(String text) {
        char[] originalChars = text.toCharArray();
        char[] reversedChars = new char[originalChars.length];
        for (int charIndex = 0; charIndex < originalChars.length; charIndex++) {
            reversedChars[charIndex] = originalChars[originalChars.length - 1 - charIndex];
        }
        return text.equals(new String(reversedChars));
    }

    private static String describeResult(boolean isPalindrome) {
        return isPalindrome ? LABEL_PALINDROME : LABEL_NOT_PALINDROME;
    }
}

class EmptyTextException extends Exception {
    EmptyTextException(String message) {
        super(message);
    }
}

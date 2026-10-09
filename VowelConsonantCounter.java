import java.util.NoSuchElementException;
import java.util.Scanner;

public class VowelConsonantCounter {

    private static final String VOWELS = "aeiou";
    private static final char ASCII_UPPER_A = 'A';
    private static final char ASCII_UPPER_Z = 'Z';
    private static final char ASCII_LOWER_A = 'a';
    private static final char ASCII_LOWER_Z = 'z';
    private static final int ASCII_CASE_GAP = 'a' - 'A';
    private static final String[] DEMO_TEXTS = {
            "Java Programming", "AEIOU xyz", "Library Orientation Kiosk", "12345", "   " };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] textsToCount = collectTexts(inputReader);
        for (int textIndex = 0; textIndex < textsToCount.length; textIndex++) {
            countVowelsAndConsonants(textsToCount[textIndex]);
        }
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== Vowel & Consonant Counter ===");
        System.out.println("\tVowels:\t\"" + VOWELS + "\" (case-insensitive)\n");
    }

    private static String[] collectTexts(Scanner inputReader) {
        System.out.print("Enter a book title (press Enter to use the demo list): ");
        try {
            String typedText = inputReader.nextLine();
            if (typedText.trim().isEmpty()) {
                System.out.println("[no title typed - running the demo list]\n");
                return DEMO_TEXTS;
            }
            System.out.println();
            return new String[] { typedText };
        } catch (NoSuchElementException | IllegalStateException consoleUnavailable) {
            System.out.println("\n[no console input - running the demo list]\n");
            return DEMO_TEXTS;
        }
    }

    private static void countVowelsAndConsonants(String text) {
        System.out.println("Title: \"" + text + "\"");
        try {
            validateText(text);
            int vowelCount = 0;
            int consonantCount = 0;
            int ignoredCount = 0;

            for (int charIndex = 0; charIndex < text.length(); charIndex++) {
                char currentChar = toLowerCaseUsingAscii(text.charAt(charIndex));
                if (!isLetter(currentChar)) {
                    ignoredCount++;
                } else if (isVowel(currentChar)) {
                    vowelCount++;
                } else {
                    consonantCount++;
                }
            }

            if (vowelCount + consonantCount == 0) {
                throw new NoLetterFoundException("the title has no letters to count");
            }
            System.out.println("Vowels: " + vowelCount + " | Consonants: " + consonantCount
                    + " | Ignored: " + ignoredCount + "\n");
        } catch (NoLetterFoundException noLetterFound) {
            System.out.println("Count not available: " + noLetterFound.getMessage() + "\n");
        } catch (IllegalArgumentException missingText) {
            System.out.println("Count not available: " + missingText.getMessage() + "\n");
        }
    }

    private static void validateText(String text) {
        if (text == null) {
            throw new IllegalArgumentException("the title reference is null");
        }
    }

    private static char toLowerCaseUsingAscii(char currentChar) {
        if (currentChar >= ASCII_UPPER_A && currentChar <= ASCII_UPPER_Z) {
            return (char) (currentChar + ASCII_CASE_GAP);
        }
        return currentChar;
    }

    private static boolean isLetter(char currentChar) {
        return currentChar >= ASCII_LOWER_A && currentChar <= ASCII_LOWER_Z;
    }

    private static boolean isVowel(char lowerCaseChar) {
        return VOWELS.indexOf(lowerCaseChar) >= 0;
    }
}

class NoLetterFoundException extends Exception {
    NoLetterFoundException(String message) {
        super(message);
    }
}

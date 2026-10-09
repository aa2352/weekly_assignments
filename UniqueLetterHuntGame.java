import java.util.NoSuchElementException;
import java.util.Scanner;

public class UniqueLetterHuntGame {

    private static final int ASCII_TABLE_SIZE = 128;
    private static final String[] DEMO_TEXTS = { "swiss", "aabbcc", "Coding Arcade", "café", "" };

    public static void main(String[] args) {
        System.out.println("=== The Unique Letter Hunt Mini-Game ===\n");

        Scanner inputReader = new Scanner(System.in);
        String[] textsToHunt = collectTextsToHunt(inputReader);
        for (int textIndex = 0; textIndex < textsToHunt.length; textIndex++) {
            huntAndReport(textsToHunt[textIndex]);
        }
        inputReader.close();
    }

    private static String[] collectTextsToHunt(Scanner inputReader) {
        System.out.print("Type a word or sentence (press Enter to use the demo list): ");
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

    private static void huntAndReport(String text) {
        System.out.println("Input: \"" + text + "\"");
        try {
            char uniqueChar = findFirstNonRepeatingChar(text);
            System.out.println("First Non-Repeating Character: '" + uniqueChar
                    + "' (ASCII code " + (int) uniqueChar + ")\n");
        } catch (NoUniqueCharacterException noUniqueCharacter) {
            System.out.println(noUniqueCharacter.getMessage() + "\n");
        } catch (ArrayIndexOutOfBoundsException nonAsciiCharacter) {
            System.out.println("Hunt stopped: the text contains a character outside the "
                    + ASCII_TABLE_SIZE + "-slot ASCII table\n");
        } catch (IllegalArgumentException emptyText) {
            System.out.println("Hunt stopped: " + emptyText.getMessage() + "\n");
        }
    }

    private static char findFirstNonRepeatingChar(String text) throws NoUniqueCharacterException {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("the text is empty");
        }

        int[] characterFrequencies = countCharacterFrequencies(text);
        for (int charIndex = 0; charIndex < text.length(); charIndex++) {
            char currentChar = text.charAt(charIndex);
            if (characterFrequencies[currentChar] == 1) {
                return currentChar;
            }
        }
        throw new NoUniqueCharacterException("No Non-Repeating Character Found");
    }

    private static int[] countCharacterFrequencies(String text) {
        int[] characterFrequencies = new int[ASCII_TABLE_SIZE];
        for (int charIndex = 0; charIndex < text.length(); charIndex++) {
            characterFrequencies[text.charAt(charIndex)]++;
        }
        return characterFrequencies;
    }
}

class NoUniqueCharacterException extends Exception {
    NoUniqueCharacterException(String message) {
        super(message);
    }
}

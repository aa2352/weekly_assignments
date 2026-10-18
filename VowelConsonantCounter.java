import java.util.Arrays;
import java.util.Scanner;

public class VowelConsonantCounter {

    private static final int MAXIMUM_LINES = 1000;
    private static final String VOWELS = "aeiou";
    private static final String[] DEMO_WORDS = { "Programming" };

    public static void main(String[] args) {
        String[] words = readInputLines(DEMO_WORDS);
        for (int wordIndex = 0; wordIndex < words.length; wordIndex++) {
            int[] counts = countVowelsAndConsonants(words[wordIndex]);
            System.out.println("Vowels: " + counts[0]);
            System.out.println("Consonants: " + counts[1]);
        }
    }

    private static int[] countVowelsAndConsonants(String word) {
        String lowerCaseWord = word.toLowerCase();
        int vowelCount = 0;
        int consonantCount = 0;
        for (int charIndex = 0; charIndex < lowerCaseWord.length(); charIndex++) {
            char letter = lowerCaseWord.charAt(charIndex);
            if (letter < 'a' || letter > 'z') {
                continue;
            }
            if (VOWELS.indexOf(letter) >= 0) {
                vowelCount++;
            } else {
                consonantCount++;
            }
        }
        return new int[] { vowelCount, consonantCount };
    }

    private static String[] readInputLines(String[] demoLines) {
        Scanner inputReader = new Scanner(System.in);
        String[] collectedLines = new String[MAXIMUM_LINES];
        int lineCount = 0;
        while (inputReader.hasNextLine() && lineCount < MAXIMUM_LINES) {
            String typedLine = inputReader.nextLine().trim();
            if (!typedLine.isEmpty()) {
                collectedLines[lineCount] = typedLine;
                lineCount++;
            }
        }
        inputReader.close();
        return lineCount == 0 ? demoLines : Arrays.copyOf(collectedLines, lineCount);
    }
}

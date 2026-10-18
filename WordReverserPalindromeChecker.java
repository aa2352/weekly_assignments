import java.util.Arrays;
import java.util.Scanner;

public class WordReverserPalindromeChecker {

    private static final int MAXIMUM_LINES = 1000;
    private static final String[] DEMO_WORDS = { "level", "java" };

    public static void main(String[] args) {
        String[] words = readInputLines(DEMO_WORDS);
        for (int wordIndex = 0; wordIndex < words.length; wordIndex++) {
            String originalWord = words[wordIndex];
            String reversedWord = reverseWord(originalWord);
            String verdict = originalWord.equals(reversedWord) ? "palindrome" : "not a palindrome";
            System.out.println(reversedWord + " - " + verdict);
        }
    }

    private static String reverseWord(String word) {
        StringBuilder wordBuilder = new StringBuilder(word);
        return wordBuilder.reverse().toString();
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

import java.util.NoSuchElementException;
import java.util.Scanner;

public class WordReversalEncoder {

    private static final String WORD_SEPARATOR = " ";
    private static final int PERFORMANCE_ITERATIONS = 50000;
    private static final String[] DEMO_SENTENCES = {
            "hello club",
            "mirror text mini game",
            "Java Strings Are Immutable",
            "   "
    };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] sentences = collectSentences(inputReader);
        for (int sentenceIndex = 0; sentenceIndex < sentences.length; sentenceIndex++) {
            reportEncodedSentence(sentences[sentenceIndex]);
        }
        comparePerformance(DEMO_SENTENCES[1]);
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== Word Reversal Encoder ===");
        System.out.println("\tRule:\t\"hello club\" becomes \"olleh bulc\"\n");
    }

    private static String[] collectSentences(Scanner inputReader) {
        System.out.print("Enter a sentence (press Enter to use the demo list): ");
        try {
            String typedSentence = inputReader.nextLine();
            if (typedSentence.trim().isEmpty()) {
                System.out.println("[no sentence typed - running the demo list]\n");
                return DEMO_SENTENCES;
            }
            System.out.println();
            return new String[] { typedSentence };
        } catch (NoSuchElementException | IllegalStateException consoleUnavailable) {
            System.out.println("\n[no console input - running the demo list]\n");
            return DEMO_SENTENCES;
        }
    }

    private static void reportEncodedSentence(String sentence) {
        System.out.println("Sentence: \"" + sentence + "\"");
        try {
            String encodedSentence = reverseEachWord(sentence);
            System.out.println("Encoded : " + encodedSentence);
            System.out.println("Matches built-in reverse(): "
                    + encodedSentence.equals(reverseEachWordUsingBuiltIn(sentence)) + "\n");
        } catch (EmptySentenceException emptySentence) {
            System.out.println("Not encoded: " + emptySentence.getMessage() + "\n");
        } catch (IllegalArgumentException missingSentence) {
            System.out.println("Not encoded: " + missingSentence.getMessage() + "\n");
        }
    }

    private static String reverseEachWord(String sentence) throws EmptySentenceException {
        String[] words = splitIntoWords(sentence);
        StringBuilder encodedSentence = new StringBuilder();
        for (int wordIndex = 0; wordIndex < words.length; wordIndex++) {
            if (wordIndex > 0) {
                encodedSentence.append(WORD_SEPARATOR);
            }
            encodedSentence.append(reverseWord(words[wordIndex]));
        }
        return encodedSentence.toString();
    }

    private static String reverseEachWordUsingBuiltIn(String sentence) throws EmptySentenceException {
        String[] words = splitIntoWords(sentence);
        StringBuilder encodedSentence = new StringBuilder();
        for (int wordIndex = 0; wordIndex < words.length; wordIndex++) {
            if (wordIndex > 0) {
                encodedSentence.append(WORD_SEPARATOR);
            }
            encodedSentence.append(new StringBuilder(words[wordIndex]).reverse());
        }
        return encodedSentence.toString();
    }

    private static String reverseWord(String word) {
        StringBuilder reversedWord = new StringBuilder();
        for (int charIndex = word.length() - 1; charIndex >= 0; charIndex--) {
            reversedWord.append(word.charAt(charIndex));
        }
        return reversedWord.toString();
    }

    private static String[] splitIntoWords(String sentence) throws EmptySentenceException {
        if (sentence == null) {
            throw new IllegalArgumentException("the sentence reference is null");
        }
        String trimmedSentence = sentence.trim();
        if (trimmedSentence.isEmpty()) {
            throw new EmptySentenceException("the sentence is blank");
        }
        return trimmedSentence.split(WORD_SEPARATOR);
    }

    private static void comparePerformance(String sentence) {
        try {
            long manualLoopNanos = measureManualLoop(sentence);
            long builtInReverseNanos = measureBuiltInReverse(sentence);
            long stringBufferNanos = measureStringBuffer(sentence);

            System.out.println("Encoding \"" + sentence + "\" " + PERFORMANCE_ITERATIONS + " times:");
            System.out.printf("\tStringBuilder loop     : %8.3f ms%n", manualLoopNanos / 1_000_000.0);
            System.out.printf("\tStringBuilder reverse(): %8.3f ms%n", builtInReverseNanos / 1_000_000.0);
            System.out.printf("\tStringBuffer reverse() : %8.3f ms%n", stringBufferNanos / 1_000_000.0);
        } catch (EmptySentenceException emptySentence) {
            System.out.println("Performance not measured: " + emptySentence.getMessage());
        }
    }

    private static long measureManualLoop(String sentence) throws EmptySentenceException {
        long startTime = System.nanoTime();
        int encodedLength = 0;
        for (int iteration = 0; iteration < PERFORMANCE_ITERATIONS; iteration++) {
            encodedLength += reverseEachWord(sentence).length();
        }
        long elapsedTime = System.nanoTime() - startTime;
        return encodedLength > 0 ? elapsedTime : 0;
    }

    private static long measureBuiltInReverse(String sentence) throws EmptySentenceException {
        long startTime = System.nanoTime();
        int encodedLength = 0;
        for (int iteration = 0; iteration < PERFORMANCE_ITERATIONS; iteration++) {
            encodedLength += reverseEachWordUsingBuiltIn(sentence).length();
        }
        long elapsedTime = System.nanoTime() - startTime;
        return encodedLength > 0 ? elapsedTime : 0;
    }

    private static long measureStringBuffer(String sentence) throws EmptySentenceException {
        String[] words = splitIntoWords(sentence);
        long startTime = System.nanoTime();
        int encodedLength = 0;
        for (int iteration = 0; iteration < PERFORMANCE_ITERATIONS; iteration++) {
            StringBuffer encodedSentence = new StringBuffer();
            for (int wordIndex = 0; wordIndex < words.length; wordIndex++) {
                if (wordIndex > 0) {
                    encodedSentence.append(WORD_SEPARATOR);
                }
                encodedSentence.append(new StringBuffer(words[wordIndex]).reverse());
            }
            encodedLength += encodedSentence.toString().length();
        }
        long elapsedTime = System.nanoTime() - startTime;
        return encodedLength > 0 ? elapsedTime : 0;
    }
}

class EmptySentenceException extends Exception {
    EmptySentenceException(String message) {
        super(message);
    }
}

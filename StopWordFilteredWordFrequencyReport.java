import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class StopWordFilteredWordFrequencyReport {

    private static final String[] STOP_WORDS = { "the", "was", "and", "a", "is", "of", "in" };
    private static final String[] PUNCTUATION_MARKS = { ".", ",", "!", "?", ";", ":", "\"", "'" };
    private static final String WHITESPACE_PATTERN = "\\s+";
    private static final String[] DEMO_FEEDBACK = {
            "The mentor was great, the session was great and clear.",
            "The placement drive was well organised; the mentors in the lab were helpful and patient.",
            "the was and a is of in",
            "   "
    };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] feedbackTexts = collectFeedback(inputReader);
        for (int feedbackIndex = 0; feedbackIndex < feedbackTexts.length; feedbackIndex++) {
            printFilteredWordFrequency(feedbackTexts[feedbackIndex]);
        }
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== Stop-Word-Filtered Word Frequency Report ===");
        System.out.print("\tStop words:\t");
        for (int stopWordIndex = 0; stopWordIndex < STOP_WORDS.length; stopWordIndex++) {
            System.out.print("\"" + STOP_WORDS[stopWordIndex] + "\" ");
        }
        System.out.println("\n");
    }

    private static String[] collectFeedback(Scanner inputReader) {
        System.out.print("Paste a feedback paragraph (press Enter to use the demo list): ");
        try {
            String typedFeedback = inputReader.nextLine();
            if (typedFeedback.trim().isEmpty()) {
                System.out.println("[no feedback typed - running the demo list]\n");
                return DEMO_FEEDBACK;
            }
            System.out.println();
            return new String[] { typedFeedback };
        } catch (NoSuchElementException | IllegalStateException consoleUnavailable) {
            System.out.println("\n[no console input - running the demo list]\n");
            return DEMO_FEEDBACK;
        }
    }

    private static void printFilteredWordFrequency(String feedback) {
        System.out.println("Feedback: \"" + feedback + "\"");
        try {
            String cleanedFeedback = normalizeFeedback(feedback);
            String[] words = cleanedFeedback.split(WHITESPACE_PATTERN);
            Map<String, Integer> wordCounts = countMeaningfulWords(words);

            if (wordCounts.isEmpty()) {
                throw new NoMeaningfulWordException("every word in the feedback is a stop word");
            }

            Map.Entry<String, Integer>[] countedWords = sortByCountDescending(wordCounts);
            for (int entryIndex = 0; entryIndex < countedWords.length; entryIndex++) {
                System.out.println("\t" + countedWords[entryIndex].getKey() + ": "
                        + countedWords[entryIndex].getValue());
            }
            System.out.println();
        } catch (NoMeaningfulWordException noMeaningfulWord) {
            System.out.println("No report: " + noMeaningfulWord.getMessage() + "\n");
        } catch (IllegalArgumentException missingFeedback) {
            System.out.println("No report: " + missingFeedback.getMessage() + "\n");
        }
    }

    private static String normalizeFeedback(String feedback) {
        if (feedback == null || feedback.trim().isEmpty()) {
            throw new IllegalArgumentException("the feedback is empty");
        }
        String cleanedFeedback = feedback.toLowerCase().trim();
        for (int markIndex = 0; markIndex < PUNCTUATION_MARKS.length; markIndex++) {
            cleanedFeedback = cleanedFeedback.replace(PUNCTUATION_MARKS[markIndex], "");
        }
        return cleanedFeedback;
    }

    private static Map<String, Integer> countMeaningfulWords(String[] words) {
        Map<String, Integer> wordCounts = new LinkedHashMap<>();
        for (int wordIndex = 0; wordIndex < words.length; wordIndex++) {
            String word = words[wordIndex].trim();
            if (word.isEmpty() || isStopWord(word)) {
                continue;
            }
            Integer previousCount = wordCounts.get(word);
            wordCounts.put(word, previousCount == null ? 1 : previousCount + 1);
        }
        return wordCounts;
    }

    private static boolean isStopWord(String word) {
        for (int stopWordIndex = 0; stopWordIndex < STOP_WORDS.length; stopWordIndex++) {
            if (STOP_WORDS[stopWordIndex].equals(word)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private static Map.Entry<String, Integer>[] sortByCountDescending(Map<String, Integer> wordCounts) {
        Map.Entry<String, Integer>[] countedWords = wordCounts.entrySet().toArray(new Map.Entry[0]);
        Arrays.sort(countedWords, Comparator.comparing(Map.Entry<String, Integer>::getValue).reversed());
        return countedWords;
    }
}

class NoMeaningfulWordException extends Exception {
    NoMeaningfulWordException(String message) {
        super(message);
    }
}

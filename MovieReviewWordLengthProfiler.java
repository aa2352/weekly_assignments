import java.util.NoSuchElementException;
import java.util.Scanner;

public class MovieReviewWordLengthProfiler {

    private static final String WORD_SEPARATOR = "\\s+";
    private static final int SHORT_WORD_LIMIT = 4;
    private static final int MEDIUM_WORD_LIMIT = 8;
    private static final String CATEGORY_SHORT = "Short";
    private static final String CATEGORY_MEDIUM = "Medium";
    private static final String CATEGORY_LONG = "Long";
    private static final char ASCII_UPPER_A = 'A';
    private static final char ASCII_UPPER_Z = 'Z';
    private static final char ASCII_LOWER_A = 'a';
    private static final char ASCII_LOWER_Z = 'z';
    private static final String[] DEMO_REVIEWS = {
            "This movie was absolutely fantastic and thrilling",
            "Bad plot, weak acting, forgettable characters everywhere!",
            "!!! ???",
            "   "
    };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] reviewsToProfile = collectReviews(inputReader);
        for (int reviewIndex = 0; reviewIndex < reviewsToProfile.length; reviewIndex++) {
            classifyWordLengths(reviewsToProfile[reviewIndex]);
        }
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== The Movie Review Word Length Profiler ===");
        System.out.println("\tBuckets:\tShort 1-" + SHORT_WORD_LIMIT
                + "\tMedium " + (SHORT_WORD_LIMIT + 1) + "-" + MEDIUM_WORD_LIMIT
                + "\tLong " + (MEDIUM_WORD_LIMIT + 1) + "+\n");
    }

    private static String[] collectReviews(Scanner inputReader) {
        System.out.print("Paste a movie review (press Enter to use the demo list): ");
        try {
            String typedReview = inputReader.nextLine();
            if (typedReview.trim().isEmpty()) {
                System.out.println("[no review typed - running the demo list]\n");
                return DEMO_REVIEWS;
            }
            System.out.println();
            return new String[] { typedReview };
        } catch (NoSuchElementException | IllegalStateException consoleUnavailable) {
            System.out.println("\n[no console input - running the demo list]\n");
            return DEMO_REVIEWS;
        }
    }

    private static void classifyWordLengths(String review) {
        System.out.println("Review: \"" + review + "\"");
        try {
            String[] reviewWords = splitIntoWords(review);
            int shortWordCount = 0;
            int mediumWordCount = 0;
            int longWordCount = 0;

            for (int wordIndex = 0; wordIndex < reviewWords.length; wordIndex++) {
                int letterCount = countLetters(reviewWords[wordIndex]);
                if (letterCount == 0) {
                    continue;
                }
                String category = classifyWordLength(letterCount);
                System.out.printf("\t%-14s %2d letter(s) -> %s%n", reviewWords[wordIndex], letterCount, category);
                if (category.equals(CATEGORY_SHORT)) {
                    shortWordCount++;
                } else if (category.equals(CATEGORY_MEDIUM)) {
                    mediumWordCount++;
                } else {
                    longWordCount++;
                }
            }

            if (shortWordCount + mediumWordCount + longWordCount == 0) {
                throw new EmptyReviewException("the review has no words made of letters");
            }
            System.out.println("Short: " + shortWordCount + " | Medium: " + mediumWordCount
                    + " | Long: " + longWordCount + "\n");
        } catch (EmptyReviewException emptyReview) {
            System.out.println("Profile not generated: " + emptyReview.getMessage() + "\n");
        } catch (IllegalArgumentException missingReview) {
            System.out.println("Profile not generated: " + missingReview.getMessage() + "\n");
        }
    }

    private static String[] splitIntoWords(String review) throws EmptyReviewException {
        if (review == null) {
            throw new IllegalArgumentException("the review reference is null");
        }
        String trimmedReview = review.trim();
        if (trimmedReview.isEmpty()) {
            throw new EmptyReviewException("the review is blank");
        }
        return trimmedReview.split(WORD_SEPARATOR);
    }

    private static int countLetters(String word) {
        int letterCount = 0;
        for (int charIndex = 0; charIndex < word.length(); charIndex++) {
            char currentChar = word.charAt(charIndex);
            boolean isUpperCaseLetter = currentChar >= ASCII_UPPER_A && currentChar <= ASCII_UPPER_Z;
            boolean isLowerCaseLetter = currentChar >= ASCII_LOWER_A && currentChar <= ASCII_LOWER_Z;
            if (isUpperCaseLetter || isLowerCaseLetter) {
                letterCount++;
            }
        }
        return letterCount;
    }

    private static String classifyWordLength(int letterCount) {
        if (letterCount <= SHORT_WORD_LIMIT) {
            return CATEGORY_SHORT;
        }
        if (letterCount <= MEDIUM_WORD_LIMIT) {
            return CATEGORY_MEDIUM;
        }
        return CATEGORY_LONG;
    }
}

class EmptyReviewException extends Exception {
    EmptyReviewException(String message) {
        super(message);
    }
}

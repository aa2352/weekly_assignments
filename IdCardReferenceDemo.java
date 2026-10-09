import java.util.NoSuchElementException;
import java.util.Scanner;

public class IdCardReferenceDemo {

    private static final String FIELD_SEPARATOR = ",";
    private static final int EXPECTED_FIELD_COUNT = 2;
    private static final int OPENING_BOOKS_ISSUED = 0;
    private static final String[] DEMO_CARD_LINES = { "Ravi,3", "Anitha,5", "Karthik", "Divya,-2", "   " };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] cardLines = collectCardLines(inputReader);
        for (int caseIndex = 0; caseIndex < cardLines.length; caseIndex++) {
            demonstrateReferences(cardLines[caseIndex]);
        }
        System.out.println("Id cards created in this session: " + IdCard.getCardsCreated());
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== Library ID Card Management ===");
        System.out.println("\tInput format:\t\"Ravi,3\" (card holder, books issued later)");
        System.out.println("\tRule:\ttwo variables can name one object, and == compares references\n");
    }

    private static String[] collectCardLines(Scanner inputReader) {
        System.out.print("Enter a card holder and books issued (press Enter to use the demo list): ");
        try {
            String typedCard = inputReader.nextLine();
            if (typedCard.trim().isEmpty()) {
                System.out.println("[no card typed - running the demo list]\n");
                return DEMO_CARD_LINES;
            }
            System.out.println();
            return new String[] { typedCard };
        } catch (NoSuchElementException | IllegalStateException consoleUnavailable) {
            System.out.println("\n[no console input - running the demo list]\n");
            return DEMO_CARD_LINES;
        }
    }

    private static void demonstrateReferences(String cardLine) {
        System.out.println("Card: \"" + cardLine + "\"");
        try {
            String[] cardFields = parseCardFields(cardLine);
            String holderName = cardFields[0];
            int booksToIssue = Integer.parseInt(cardFields[1]);
            validateBooksIssued(booksToIssue);

            IdCard original = new IdCard(holderName, OPENING_BOOKS_ISSUED);
            IdCard duplicate = original;
            duplicate.booksIssued = booksToIssue;
            IdCard separate = new IdCard(holderName, booksToIssue);

            String referenceLabel = holderName.toLowerCase();
            System.out.println(holderName + "'s booksIssued (via first variable): " + original.booksIssued);
            System.out.println("duplicate == " + referenceLabel + ": " + (duplicate == original));
            System.out.println("separate == " + referenceLabel + ": " + (separate == original));
            System.out.println("separate holds the same field values: " + separate.hasSameValuesAs(original) + "\n");
        } catch (MalformedCardException malformedCard) {
            System.out.println("Card rejected: " + malformedCard.getMessage() + "\n");
        } catch (NumberFormatException nonNumericCount) {
            System.out.println("Card rejected: the books issued count must be a whole number\n");
        } catch (IllegalArgumentException invalidCard) {
            System.out.println("Card rejected: " + invalidCard.getMessage() + "\n");
        }
    }

    private static void validateBooksIssued(int booksIssued) {
        if (booksIssued < 0) {
            throw new IllegalArgumentException("a card cannot have " + booksIssued + " books issued");
        }
    }

    private static String[] parseCardFields(String cardLine) throws MalformedCardException {
        if (cardLine == null || cardLine.trim().isEmpty()) {
            throw new IllegalArgumentException("no card details were given");
        }
        String[] cardFields = cardLine.split(FIELD_SEPARATOR);
        if (cardFields.length != EXPECTED_FIELD_COUNT) {
            throw new MalformedCardException("\"" + cardLine.trim()
                    + "\" needs a holder name and a books issued count");
        }
        return new String[] { cardFields[0].trim(), cardFields[1].trim() };
    }
}

class IdCard {

    private static int cardsCreated = 0;

    String name;
    int booksIssued;

    IdCard(String name, int booksIssued) {
        cardsCreated++;
        this.name = name;
        this.booksIssued = booksIssued;
    }

    boolean hasSameValuesAs(IdCard other) {
        return this.name.equals(other.name) && this.booksIssued == other.booksIssued;
    }

    static int getCardsCreated() {
        return cardsCreated;
    }
}

class MalformedCardException extends Exception {
    MalformedCardException(String message) {
        super(message);
    }
}

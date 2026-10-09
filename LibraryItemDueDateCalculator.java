import java.time.LocalDate;
import java.util.Scanner;

public class LibraryItemDueDateCalculator {

    private static final LocalDate CURRENT_DATE = LocalDate.of(2023, 10, 26);
    private static final char QUOTE = '"';
    private static final String[] DEMO_ITEM_LINES = {
            "BOOK \"1984\"",
            "DVD \"The Matrix\"",
            "MAGAZINE \"Forbes Issue 500\""
    };

    public static void main(String[] args) {
        String[] itemLines = readItemLines();

        LibraryItem[] libraryItems = new LibraryItem[itemLines.length];
        for (int itemIndex = 0; itemIndex < itemLines.length; itemIndex++) {
            libraryItems[itemIndex] = createItem(itemLines[itemIndex]);
        }

        for (int itemIndex = 0; itemIndex < libraryItems.length; itemIndex++) {
            System.out.println(libraryItems[itemIndex].getTitle() + ": "
                    + libraryItems[itemIndex].calculateDueDate(CURRENT_DATE));
        }
    }

    private static String[] readItemLines() {
        Scanner inputReader = new Scanner(System.in);
        String countLine = nextFilledLine(inputReader);
        if (countLine.isEmpty()) {
            inputReader.close();
            return DEMO_ITEM_LINES;
        }
        int itemCount = Integer.parseInt(countLine);
        String[] itemLines = new String[itemCount];
        for (int itemIndex = 0; itemIndex < itemCount; itemIndex++) {
            itemLines[itemIndex] = nextFilledLine(inputReader);
        }
        inputReader.close();
        return itemLines;
    }

    private static String nextFilledLine(Scanner inputReader) {
        while (inputReader.hasNextLine()) {
            String typedLine = inputReader.nextLine().trim();
            if (!typedLine.isEmpty()) {
                return typedLine;
            }
        }
        return "";
    }

    private static LibraryItem createItem(String itemLine) {
        int typeEnd = itemLine.indexOf(' ');
        String itemType = itemLine.substring(0, typeEnd).toUpperCase();
        String title = stripQuotes(itemLine.substring(typeEnd + 1).trim());

        switch (itemType) {
            case "BOOK":
                return new Book(title);
            case "DVD":
                return new Dvd(title);
            case "MAGAZINE":
                return new Magazine(title);
            default:
                throw new IllegalArgumentException("unknown item type: " + itemType);
        }
    }

    private static String stripQuotes(String quotedTitle) {
        int lastIndex = quotedTitle.length() - 1;
        if (lastIndex > 0 && quotedTitle.charAt(0) == QUOTE && quotedTitle.charAt(lastIndex) == QUOTE) {
            return quotedTitle.substring(1, lastIndex);
        }
        return quotedTitle;
    }
}

abstract class LibraryItem {

    private final String title;

    LibraryItem(String title) {
        this.title = title;
    }

    abstract int getBorrowingDays();

    LocalDate calculateDueDate(LocalDate borrowedOn) {
        return borrowedOn.plusDays(getBorrowingDays());
    }

    String getTitle() {
        return this.title;
    }
}

class Book extends LibraryItem {

    private static final int BORROWING_DAYS = 14;

    Book(String title) {
        super(title);
    }

    @Override
    int getBorrowingDays() {
        return BORROWING_DAYS;
    }
}

class Dvd extends LibraryItem {

    private static final int BORROWING_DAYS = 7;

    Dvd(String title) {
        super(title);
    }

    @Override
    int getBorrowingDays() {
        return BORROWING_DAYS;
    }
}

class Magazine extends LibraryItem {

    private static final int BORROWING_DAYS = 3;

    Magazine(String title) {
        super(title);
    }

    @Override
    int getBorrowingDays() {
        return BORROWING_DAYS;
    }
}

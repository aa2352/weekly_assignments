import java.util.NoSuchElementException;
import java.util.Scanner;

public class LibraryInventoryManager {

    private static final String RECORD_SEPARATOR = ";";
    private static final String FIELD_SEPARATOR = ",";
    private static final int EXPECTED_FIELD_COUNT = 3;
    private static final String[] DEMO_BATCHES = {
            "Clean Code,Robert C. Martin,3;Effective Java,Joshua Bloch,5;"
                    + "Refactoring,Martin Fowler,0;Design Patterns,GoF,2",
            "The Pragmatic Programmer,Andrew Hunt,4;Head First Java,Kathy Sierra,7",
            "Clean Code,Robert C. Martin",
            "Broken Book,Unknown Author,-1",
            "   "
    };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] batches = collectBatches(inputReader);
        for (int batchIndex = 0; batchIndex < batches.length; batchIndex++) {
            printInventory(batches[batchIndex]);
        }
        System.out.println("Inventory entries created in this session: " + BookInventory.getEntriesCreated());
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== Library Inventory Management ===");
        System.out.println("\tInput format:\t\"Clean Code,Robert C. Martin,3;Effective Java,Joshua Bloch,5\"");
        System.out.println("\tRule:\tone object per book, held in an array of BookInventory\n");
    }

    private static String[] collectBatches(Scanner inputReader) {
        System.out.print("Enter the inventory entries (press Enter to use the demo list): ");
        try {
            String typedBatch = inputReader.nextLine();
            if (typedBatch.trim().isEmpty()) {
                System.out.println("[no entries typed - running the demo list]\n");
                return DEMO_BATCHES;
            }
            System.out.println();
            return new String[] { typedBatch };
        } catch (NoSuchElementException | IllegalStateException consoleUnavailable) {
            System.out.println("\n[no console input - running the demo list]\n");
            return DEMO_BATCHES;
        }
    }

    private static void printInventory(String batchLine) {
        System.out.println("Shelf: \"" + batchLine + "\"");
        try {
            BookInventory[] bookInventories = parseEntries(batchLine);
            for (int entryIndex = 0; entryIndex < bookInventories.length; entryIndex++) {
                bookInventories[entryIndex].printEntry();
            }
            System.out.println("Titles on the shelf: " + bookInventories.length
                    + " | copies in stock: " + countTotalCopies(bookInventories)
                    + " | out of stock: " + countOutOfStock(bookInventories) + "\n");
        } catch (MalformedBookEntryException malformedEntry) {
            System.out.println("Shelf rejected: " + malformedEntry.getMessage() + "\n");
        } catch (NumberFormatException nonNumericCopies) {
            System.out.println("Shelf rejected: the copy count must be a whole number\n");
        } catch (IllegalArgumentException invalidEntry) {
            System.out.println("Shelf rejected: " + invalidEntry.getMessage() + "\n");
        }
    }

    private static int countTotalCopies(BookInventory[] bookInventories) {
        int totalCopies = 0;
        for (int entryIndex = 0; entryIndex < bookInventories.length; entryIndex++) {
            totalCopies += bookInventories[entryIndex].getCopiesAvailable();
        }
        return totalCopies;
    }

    private static int countOutOfStock(BookInventory[] bookInventories) {
        int outOfStockCount = 0;
        for (int entryIndex = 0; entryIndex < bookInventories.length; entryIndex++) {
            if (bookInventories[entryIndex].getCopiesAvailable() == 0) {
                outOfStockCount++;
            }
        }
        return outOfStockCount;
    }

    private static BookInventory[] parseEntries(String batchLine) throws MalformedBookEntryException {
        if (batchLine == null || batchLine.trim().isEmpty()) {
            throw new IllegalArgumentException("the shelf holds no entries");
        }
        String[] recordFields = batchLine.split(RECORD_SEPARATOR);
        BookInventory[] bookInventories = new BookInventory[recordFields.length];
        for (int entryIndex = 0; entryIndex < recordFields.length; entryIndex++) {
            bookInventories[entryIndex] = parseEntry(recordFields[entryIndex]);
        }
        return bookInventories;
    }

    private static BookInventory parseEntry(String recordField) throws MalformedBookEntryException {
        String[] bookFields = recordField.split(FIELD_SEPARATOR);
        if (bookFields.length != EXPECTED_FIELD_COUNT) {
            throw new MalformedBookEntryException("\"" + recordField.trim()
                    + "\" needs a title, an author and a copy count");
        }
        String title = bookFields[0].trim();
        String author = bookFields[1].trim();
        int copiesAvailable = Integer.parseInt(bookFields[2].trim());
        return new BookInventory(title, author, copiesAvailable);
    }
}

class BookInventory {

    private static int entriesCreated = 0;

    private final String title;
    private final String author;
    private int copiesAvailable;

    public BookInventory(String title, String author, int copiesAvailable) {
        validateCopies(title, copiesAvailable);
        entriesCreated++;
        this.title = title;
        this.author = author;
        this.copiesAvailable = copiesAvailable;
    }

    void printEntry() {
        System.out.println(this.title + " by " + this.author + " - " + this.copiesAvailable + " copies available");
    }

    void setCopiesAvailable(int copiesAvailable) {
        validateCopies(this.title, copiesAvailable);
        this.copiesAvailable = copiesAvailable;
    }

    int getCopiesAvailable() {
        return this.copiesAvailable;
    }

    String getTitle() {
        return this.title;
    }

    String getAuthor() {
        return this.author;
    }

    private static void validateCopies(String title, int copiesAvailable) {
        if (copiesAvailable < 0) {
            throw new IllegalArgumentException("\"" + title + "\" cannot have " + copiesAvailable + " copies");
        }
    }

    static int getEntriesCreated() {
        return entriesCreated;
    }
}

class MalformedBookEntryException extends Exception {
    MalformedBookEntryException(String message) {
        super(message);
    }
}

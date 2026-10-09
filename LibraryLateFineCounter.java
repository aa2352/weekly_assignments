import java.util.Scanner;

public class LibraryLateFineCounter {

    private static final String FIELD_SEPARATOR = "\\s+";
    private static final int TYPE_INDEX = 0;
    private static final int TITLE_INDEX = 1;
    private static final int DAYS_LATE_INDEX = 2;
    private static final String[] DEMO_ITEM_LINES = { "BOOK Algebra 4", "DVD Inception 12", "MAGAZINE Sports 3" };

    public static void main(String[] args) {
        String[] itemLines = readItemLines();

        LateItem[] lateItems = new LateItem[itemLines.length];
        for (int itemIndex = 0; itemIndex < itemLines.length; itemIndex++) {
            lateItems[itemIndex] = createItem(itemLines[itemIndex]);
        }

        double totalFines = 0.0;
        for (int itemIndex = 0; itemIndex < lateItems.length; itemIndex++) {
            double fine = lateItems[itemIndex].calculateFine();
            System.out.printf("%s: %.2f%n", lateItems[itemIndex].getTitle(), fine);
            totalFines = totalFines + fine;
        }
        System.out.printf("Total Fines: %.2f%n", totalFines);
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

    private static LateItem createItem(String itemLine) {
        String[] itemFields = itemLine.split(FIELD_SEPARATOR);
        String itemType = itemFields[TYPE_INDEX].toUpperCase();
        String title = itemFields[TITLE_INDEX];
        int daysLate = Integer.parseInt(itemFields[DAYS_LATE_INDEX]);

        switch (itemType) {
            case "BOOK":
                return new LateBook(title, daysLate);
            case "DVD":
                return new LateDvd(title, daysLate);
            case "MAGAZINE":
                return new LateMagazine(title, daysLate);
            default:
                throw new IllegalArgumentException("unknown item type: " + itemType);
        }
    }
}

abstract class LateItem {

    private final String title;
    protected final int daysLate;

    LateItem(String title, int daysLate) {
        this.title = title;
        this.daysLate = daysLate;
    }

    abstract double calculateFine();

    String getTitle() {
        return this.title;
    }
}

class LateBook extends LateItem {

    private static final double FINE_PER_DAY = 2.0;

    LateBook(String title, int daysLate) {
        super(title, daysLate);
    }

    @Override
    double calculateFine() {
        return FINE_PER_DAY * this.daysLate;
    }
}

class LateDvd extends LateItem {

    private static final double FINE_PER_DAY = 5.0;
    private static final double MAXIMUM_FINE = 50.0;

    LateDvd(String title, int daysLate) {
        super(title, daysLate);
    }

    @Override
    double calculateFine() {
        return Math.min(FINE_PER_DAY * this.daysLate, MAXIMUM_FINE);
    }
}

class LateMagazine extends LateItem {

    private static final double FINE_PER_DAY = 1.0;

    LateMagazine(String title, int daysLate) {
        super(title, daysLate);
    }

    @Override
    double calculateFine() {
        return FINE_PER_DAY * this.daysLate;
    }
}

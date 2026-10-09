import java.util.NoSuchElementException;
import java.util.Scanner;

public class ProductInventoryCsvParser {

    private static final String FIELD_SEPARATOR = ",";
    private static final int EXPECTED_FIELD_COUNT = 3;
    private static final int PRODUCT_NAME_INDEX = 0;
    private static final int SKU_INDEX = 1;
    private static final int QUANTITY_INDEX = 2;
    private static final String INVALID_RECORD_MESSAGE = "Invalid Record";
    private static final String[] DEMO_CSV_LINES = {
            "Wireless Mouse,WM-2201,150",
            "Wireless Mouse,150",
            "  Mechanical Keyboard , mk-7788 , 40 ",
            "USB Hub,UH-3301,many",
            "Monitor Stand,,25",
            "   "
    };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] csvLines = collectCsvLines(inputReader);
        for (int lineIndex = 0; lineIndex < csvLines.length; lineIndex++) {
            parseInventoryRecord(csvLines[lineIndex]);
        }
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== Product Inventory CSV Parser ===");
        System.out.println("\tExpected format:\t\"ProductName,SKU,Quantity\"");
        System.out.println("\tSource file:\tC:\\warehouse\\inventory-updates.csv\n");
    }

    private static String[] collectCsvLines(Scanner inputReader) {
        System.out.print("Enter an inventory record (press Enter to use the demo list): ");
        try {
            String typedLine = inputReader.nextLine();
            if (typedLine.trim().isEmpty()) {
                System.out.println("[no record typed - running the demo list]\n");
                return DEMO_CSV_LINES;
            }
            System.out.println();
            return new String[] { typedLine };
        } catch (NoSuchElementException | IllegalStateException consoleUnavailable) {
            System.out.println("\n[no console input - running the demo list]\n");
            return DEMO_CSV_LINES;
        }
    }

    private static void parseInventoryRecord(String csvLine) {
        System.out.println("Record: \"" + csvLine + "\"");
        try {
            String[] recordFields = splitRecord(csvLine);
            String productName = recordFields[PRODUCT_NAME_INDEX].trim();
            String stockKeepingUnit = recordFields[SKU_INDEX].trim().toUpperCase();
            String quantityField = recordFields[QUANTITY_INDEX].trim();

            validateField(productName, "Product");
            validateField(stockKeepingUnit, "SKU");
            int quantity = Integer.parseInt(quantityField);
            validateQuantity(quantity);

            System.out.println("Product: " + productName + " | SKU: " + stockKeepingUnit
                    + " | Qty: " + quantity + "\n");
        } catch (IncompleteInventoryRecordException incompleteRecord) {
            System.out.println(INVALID_RECORD_MESSAGE + " - " + incompleteRecord.getMessage() + "\n");
        } catch (NumberFormatException nonNumericQuantity) {
            System.out.println(INVALID_RECORD_MESSAGE + " - the quantity is not a whole number\n");
        } catch (ArrayIndexOutOfBoundsException missingField) {
            System.out.println(INVALID_RECORD_MESSAGE + " - a field is missing\n");
        } catch (IllegalArgumentException emptyLine) {
            System.out.println(INVALID_RECORD_MESSAGE + " - " + emptyLine.getMessage() + "\n");
        }
    }

    private static String[] splitRecord(String csvLine) throws IncompleteInventoryRecordException {
        if (csvLine == null || csvLine.trim().isEmpty()) {
            throw new IllegalArgumentException("the record is empty");
        }
        String[] recordFields = csvLine.split(FIELD_SEPARATOR);
        if (recordFields.length != EXPECTED_FIELD_COUNT) {
            throw new IncompleteInventoryRecordException("expected " + EXPECTED_FIELD_COUNT
                    + " fields but found " + recordFields.length);
        }
        return recordFields;
    }

    private static void validateField(String fieldValue, String fieldName) throws IncompleteInventoryRecordException {
        if (fieldValue.isEmpty()) {
            throw new IncompleteInventoryRecordException(fieldName + " is blank");
        }
    }

    private static void validateQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("the quantity cannot be negative");
        }
    }
}

class IncompleteInventoryRecordException extends Exception {
    IncompleteInventoryRecordException(String message) {
        super(message);
    }
}

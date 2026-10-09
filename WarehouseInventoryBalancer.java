import java.util.NoSuchElementException;
import java.util.Scanner;

public class WarehouseInventoryBalancer {

    private static final String QUANTITY_SEPARATOR = ",";
    private static final String SECTION_A_NAME = "Section A";
    private static final String SECTION_B_NAME = "Section B";
    private static final String STATUS_BALANCED = "Balanced";
    private static final String STATUS_NOT_BALANCED = "Not Balanced";
    private static final String[] DEMO_SECTION_A_LINES = { "20,15,30", "40,10,5", "12,8,9", "10,20" };
    private static final String[] DEMO_SECTION_B_LINES = { "25,10,30", "12,60,7", "12,8,-9", "10,20,30" };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] sectionALines = DEMO_SECTION_A_LINES;
        String[] sectionBLines = DEMO_SECTION_B_LINES;

        String typedSectionA = readLine(inputReader, "Enter Section A quantities (press Enter to use the demo list): ");
        if (!typedSectionA.trim().isEmpty()) {
            String typedSectionB = readLine(inputReader, "Enter Section B quantities: ");
            sectionALines = new String[] { typedSectionA };
            sectionBLines = new String[] { typedSectionB };
        }
        System.out.println();

        for (int stockIndex = 0; stockIndex < sectionALines.length; stockIndex++) {
            inspectStockPair(sectionALines[stockIndex], sectionBLines[stockIndex]);
        }
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== The Warehouse Inventory Balancer ===");
        System.out.println("\tInput format:\t\"20,15,30\"");
        System.out.println("\tStock file:\tC:\\warehouse\\monthly-stock.csv\n");
    }

    private static String readLine(Scanner inputReader, String prompt) {
        System.out.print(prompt);
        try {
            return inputReader.nextLine();
        } catch (NoSuchElementException | IllegalStateException consoleUnavailable) {
            System.out.println("\n[no console input - running the demo list]");
            return "";
        }
    }

    private static void inspectStockPair(String sectionALine, String sectionBLine) {
        System.out.println("Section A: \"" + sectionALine + "\" | Section B: \"" + sectionBLine + "\"");
        try {
            int[] sectionAQuantities = parseQuantities(sectionALine);
            int[] sectionBQuantities = parseQuantities(sectionBLine);
            analyzeInventory(sectionAQuantities, sectionBQuantities);
        } catch (SectionSizeMismatchException sizeMismatch) {
            System.out.println("Analysis stopped: " + sizeMismatch.getMessage());
        } catch (NumberFormatException nonNumericQuantity) {
            System.out.println("Analysis stopped: every quantity must be a whole number");
        } catch (IllegalArgumentException invalidQuantity) {
            System.out.println("Analysis stopped: " + invalidQuantity.getMessage());
        }
        System.out.println();
    }

    private static int[] parseQuantities(String quantityLine) {
        if (quantityLine == null || quantityLine.trim().isEmpty()) {
            throw new IllegalArgumentException("a section has no quantities");
        }
        String[] quantityFields = quantityLine.split(QUANTITY_SEPARATOR);
        int[] quantities = new int[quantityFields.length];
        for (int fieldIndex = 0; fieldIndex < quantityFields.length; fieldIndex++) {
            quantities[fieldIndex] = Integer.parseInt(quantityFields[fieldIndex].trim());
            if (quantities[fieldIndex] < 0) {
                throw new IllegalArgumentException("quantity " + quantities[fieldIndex] + " cannot be negative");
            }
        }
        return quantities;
    }

    private static void analyzeInventory(int[] sectionA, int[] sectionB) throws SectionSizeMismatchException {
        if (sectionA.length != sectionB.length) {
            throw new SectionSizeMismatchException(SECTION_A_NAME + " holds " + sectionA.length + " items but "
                    + SECTION_B_NAME + " holds " + sectionB.length);
        }

        int sectionATotal = calculateTotal(sectionA);
        int sectionBTotal = calculateTotal(sectionB);
        String balanceStatus = sectionATotal == sectionBTotal ? STATUS_BALANCED : STATUS_NOT_BALANCED;

        int highestQuantity = sectionA[0];
        String highestSectionName = SECTION_A_NAME;
        int highestItemNumber = 1;

        for (int itemIndex = 0; itemIndex < sectionA.length; itemIndex++) {
            if (sectionA[itemIndex] > highestQuantity) {
                highestQuantity = sectionA[itemIndex];
                highestSectionName = SECTION_A_NAME;
                highestItemNumber = itemIndex + 1;
            }
        }
        for (int itemIndex = 0; itemIndex < sectionB.length; itemIndex++) {
            if (sectionB[itemIndex] > highestQuantity) {
                highestQuantity = sectionB[itemIndex];
                highestSectionName = SECTION_B_NAME;
                highestItemNumber = itemIndex + 1;
            }
        }

        System.out.println(SECTION_A_NAME + " Total: " + sectionATotal
                + " | " + SECTION_B_NAME + " Total: " + sectionBTotal
                + " | Status: " + balanceStatus
                + " | Highest Quantity: " + highestQuantity
                + " (" + highestSectionName + ", Item " + highestItemNumber + ")");
    }

    private static int calculateTotal(int[] sectionQuantities) {
        int total = 0;
        for (int itemIndex = 0; itemIndex < sectionQuantities.length; itemIndex++) {
            total += sectionQuantities[itemIndex];
        }
        return total;
    }
}

class SectionSizeMismatchException extends Exception {
    SectionSizeMismatchException(String message) {
        super(message);
    }
}

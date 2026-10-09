import java.util.Scanner;

public class CanteenBillingCounter {

    private static final String FIELD_SEPARATOR = "\\s+";
    private static final int TYPE_INDEX = 0;
    private static final int AMOUNT_INDEX = 1;
    private static final String[] DEMO_BILL_LINES = { "STUDENT 200", "STAFF 300", "GUEST 150" };

    public static void main(String[] args) {
        String[] billLines = readBillLines();

        Customer[] customers = new Customer[billLines.length];
        for (int billIndex = 0; billIndex < billLines.length; billIndex++) {
            customers[billIndex] = createCustomer(billLines[billIndex]);
        }

        double grandTotal = 0.0;
        for (int billIndex = 0; billIndex < customers.length; billIndex++) {
            double finalAmount = customers[billIndex].calculateFinalAmount();
            System.out.printf("%s: %.2f%n", customers[billIndex].getTypeLabel(), finalAmount);
            grandTotal = grandTotal + finalAmount;
        }
        System.out.printf("Total: %.2f%n", grandTotal);
    }

    private static String[] readBillLines() {
        Scanner inputReader = new Scanner(System.in);
        String countLine = nextFilledLine(inputReader);
        if (countLine.isEmpty()) {
            inputReader.close();
            return DEMO_BILL_LINES;
        }
        int billCount = Integer.parseInt(countLine);
        String[] billLines = new String[billCount];
        for (int billIndex = 0; billIndex < billCount; billIndex++) {
            billLines[billIndex] = nextFilledLine(inputReader);
        }
        inputReader.close();
        return billLines;
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

    private static Customer createCustomer(String billLine) {
        String[] billFields = billLine.split(FIELD_SEPARATOR);
        String customerType = billFields[TYPE_INDEX].toUpperCase();
        double billAmount = Double.parseDouble(billFields[AMOUNT_INDEX]);

        switch (customerType) {
            case "STUDENT":
                return new StudentCustomer(billAmount);
            case "STAFF":
                return new StaffCustomer(billAmount);
            case "GUEST":
                return new GuestCustomer(billAmount);
            default:
                throw new IllegalArgumentException("unknown customer type: " + customerType);
        }
    }
}

abstract class Customer {

    private final String typeLabel;
    protected final double billAmount;

    Customer(String typeLabel, double billAmount) {
        this.typeLabel = typeLabel;
        this.billAmount = billAmount;
    }

    abstract double calculateFinalAmount();

    String getTypeLabel() {
        return this.typeLabel;
    }
}

class StudentCustomer extends Customer {

    private static final double DISCOUNT_RATE = 0.10;

    StudentCustomer(double billAmount) {
        super("STUDENT", billAmount);
    }

    @Override
    double calculateFinalAmount() {
        return this.billAmount - (this.billAmount * DISCOUNT_RATE);
    }
}

class StaffCustomer extends Customer {

    private static final double DISCOUNT_RATE = 0.05;

    StaffCustomer(double billAmount) {
        super("STAFF", billAmount);
    }

    @Override
    double calculateFinalAmount() {
        return this.billAmount - (this.billAmount * DISCOUNT_RATE);
    }
}

class GuestCustomer extends Customer {

    private static final double SERVICE_CHARGE = 10.0;

    GuestCustomer(double billAmount) {
        super("GUEST", billAmount);
    }

    @Override
    double calculateFinalAmount() {
        return this.billAmount + SERVICE_CHARGE;
    }
}

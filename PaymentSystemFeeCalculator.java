import java.util.Scanner;

public class PaymentSystemFeeCalculator {

    private static final String FIELD_SEPARATOR = "\\s+";
    private static final int TYPE_INDEX = 0;
    private static final int AMOUNT_INDEX = 1;
    private static final String[] DEMO_TRANSACTION_LINES = { "CARD 1000", "WALLET 500", "BANKTRANSFER 2000" };

    public static void main(String[] args) {
        String[] transactionLines = readTransactionLines();

        Payment[] payments = new Payment[transactionLines.length];
        for (int transactionIndex = 0; transactionIndex < transactionLines.length; transactionIndex++) {
            payments[transactionIndex] = createPayment(transactionLines[transactionIndex]);
        }

        double grandTotal = 0.0;
        for (int transactionIndex = 0; transactionIndex < payments.length; transactionIndex++) {
            double adjustedAmount = payments[transactionIndex].calculateAdjustedAmount();
            System.out.printf("%s: %.2f%n", payments[transactionIndex].getTypeLabel(), adjustedAmount);
            grandTotal = grandTotal + adjustedAmount;
        }
        System.out.printf("Total: %.2f%n", grandTotal);
    }

    private static String[] readTransactionLines() {
        Scanner inputReader = new Scanner(System.in);
        String countLine = nextFilledLine(inputReader);
        if (countLine.isEmpty()) {
            inputReader.close();
            return DEMO_TRANSACTION_LINES;
        }
        int transactionCount = Integer.parseInt(countLine);
        String[] transactionLines = new String[transactionCount];
        for (int transactionIndex = 0; transactionIndex < transactionCount; transactionIndex++) {
            transactionLines[transactionIndex] = nextFilledLine(inputReader);
        }
        inputReader.close();
        return transactionLines;
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

    private static Payment createPayment(String transactionLine) {
        String[] transactionFields = transactionLine.split(FIELD_SEPARATOR);
        String paymentType = transactionFields[TYPE_INDEX].toUpperCase();
        double amount = Double.parseDouble(transactionFields[AMOUNT_INDEX]);

        switch (paymentType) {
            case "CARD":
                return new CardPayment(amount);
            case "WALLET":
                return new WalletPayment(amount);
            case "BANKTRANSFER":
                return new BankTransferPayment(amount);
            default:
                throw new IllegalArgumentException("unknown payment type: " + paymentType);
        }
    }
}

abstract class Payment {

    private final String typeLabel;
    protected final double amount;

    Payment(String typeLabel, double amount) {
        this.typeLabel = typeLabel;
        this.amount = amount;
    }

    abstract double calculateAdjustedAmount();

    String getTypeLabel() {
        return this.typeLabel;
    }
}

class CardPayment extends Payment {

    private static final double FEE_RATE = 0.02;

    CardPayment(double amount) {
        super("CARD", amount);
    }

    @Override
    double calculateAdjustedAmount() {
        return this.amount + (this.amount * FEE_RATE);
    }
}

class WalletPayment extends Payment {

    private static final double FEE_RATE = 0.01;

    WalletPayment(double amount) {
        super("WALLET", amount);
    }

    @Override
    double calculateAdjustedAmount() {
        return this.amount + (this.amount * FEE_RATE);
    }
}

class BankTransferPayment extends Payment {

    BankTransferPayment(double amount) {
        super("BANKTRANSFER", amount);
    }

    @Override
    double calculateAdjustedAmount() {
        return this.amount;
    }
}

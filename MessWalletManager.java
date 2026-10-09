import java.util.NoSuchElementException;
import java.util.Scanner;

public class MessWalletManager {

    private static final String VALUE_SEPARATOR = ",";
    private static final int EXPECTED_AMOUNT_COUNT = 2;
    private static final String[] DEMO_OPENING_BALANCES = { "500", "1000", "-250", "300", "abc" };
    private static final String[] DEMO_OPERATIONS = { "200,1000", "0,400", "100,50", "150,-20", "100,100" };
    private static final String[] DEMO_OWNERS = { "Ravi", "Anitha", "Karthik", "Divya", "Manoj" };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] openingBalances = DEMO_OPENING_BALANCES;
        String[] operations = DEMO_OPERATIONS;
        String[] owners = DEMO_OWNERS;

        String typedOpening = readLine(inputReader, "Enter the opening balance (press Enter to use the demo list): ");
        if (!typedOpening.trim().isEmpty()) {
            String typedOperations = readLine(inputReader, "Enter the top-up and deduct amounts (e.g. 200,1000): ");
            openingBalances = new String[] { typedOpening };
            operations = new String[] { typedOperations };
            owners = new String[] { "Card holder" };
        }
        System.out.println();

        for (int caseIndex = 0; caseIndex < openingBalances.length; caseIndex++) {
            runWalletSession(owners[caseIndex], openingBalances[caseIndex], operations[caseIndex]);
        }
        System.out.println("Wallets opened in this session: " + MessWallet.getWalletsOpened());
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== Hostel Mess Wallet Management ===");
        System.out.println("\tInput format:\topening \"500\" with operations \"200,1000\"");
        System.out.println("\tRule:\tthe balance is private and can never go negative\n");
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

    private static void runWalletSession(String ownerName, String openingLine, String operationLine) {
        System.out.println("opening = " + openingLine + ", operations = " + operationLine);
        try {
            double openingBalance = Double.parseDouble(openingLine.trim());
            double[] amounts = parseAmounts(operationLine);

            MessWallet messWallet = new MessWallet(ownerName, openingBalance);
            messWallet.topUp(amounts[0]);
            messWallet.deduct(amounts[1]);
            System.out.println("Final balance: " + messWallet.getBalance() + "\n");
        } catch (MalformedOperationException malformedOperation) {
            System.out.println("Session skipped: " + malformedOperation.getMessage() + "\n");
        } catch (NumberFormatException nonNumericAmount) {
            System.out.println("Session skipped: every amount must be a number\n");
        } catch (IllegalArgumentException emptyOperation) {
            System.out.println("Session skipped: " + emptyOperation.getMessage() + "\n");
        }
    }

    private static double[] parseAmounts(String operationLine) throws MalformedOperationException {
        if (operationLine == null || operationLine.trim().isEmpty()) {
            throw new IllegalArgumentException("no operations were requested");
        }
        String[] amountFields = operationLine.split(VALUE_SEPARATOR);
        if (amountFields.length != EXPECTED_AMOUNT_COUNT) {
            throw new MalformedOperationException("exactly one top-up and one deduction are needed but found "
                    + amountFields.length);
        }
        double[] amounts = new double[amountFields.length];
        for (int amountIndex = 0; amountIndex < amountFields.length; amountIndex++) {
            amounts[amountIndex] = Double.parseDouble(amountFields[amountIndex].trim());
        }
        return amounts;
    }
}

class MessWallet {

    private static final double EMPTY_BALANCE = 0.0;

    private static int walletsOpened = 0;

    private final String ownerName;
    private double balance;

    public MessWallet(String ownerName) {
        this(ownerName, EMPTY_BALANCE);
    }

    public MessWallet(String ownerName, double openingBalance) {
        walletsOpened++;
        this.ownerName = ownerName;
        if (openingBalance < EMPTY_BALANCE) {
            System.out.println("Warning: a negative opening balance was given - starting at " + EMPTY_BALANCE);
            this.balance = EMPTY_BALANCE;
        } else {
            this.balance = openingBalance;
        }
    }

    public void topUp(double amount) {
        if (amount <= EMPTY_BALANCE) {
            System.out.println("Top-up rejected: the amount must be greater than zero");
            return;
        }
        this.balance = this.balance + amount;
        System.out.println("Balance after top-up: " + this.balance);
    }

    public void deduct(double amount) {
        if (amount <= EMPTY_BALANCE) {
            System.out.println("Deduct rejected: the amount must be greater than zero");
            return;
        }
        if (amount > this.balance) {
            System.out.println("Deduct rejected: insufficient balance");
            return;
        }
        this.balance = this.balance - amount;
        System.out.println("Balance after deduction: " + this.balance);
    }

    public double getBalance() {
        return this.balance;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    static int getWalletsOpened() {
        return walletsOpened;
    }
}

class MalformedOperationException extends Exception {
    MalformedOperationException(String message) {
        super(message);
    }
}

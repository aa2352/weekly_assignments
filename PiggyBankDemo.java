public class PiggyBankDemo {

    private static final int[] DEMO_DEPOSITS = { 100 };
    private static final int[] DEMO_WITHDRAWALS = { 30, 500 };

    public static void main(String[] args) {
        PiggyBank piggyBank = new PiggyBank("PB-1");
        System.out.println("Piggy bank " + piggyBank.getId() + " opened, savings = " + piggyBank.getSavings());

        for (int depositIndex = 0; depositIndex < DEMO_DEPOSITS.length; depositIndex++) {
            int amount = DEMO_DEPOSITS[depositIndex];
            boolean accepted = piggyBank.deposit(amount);
            printOutcome("deposit(" + amount + ")", accepted, piggyBank);
        }

        for (int withdrawalIndex = 0; withdrawalIndex < DEMO_WITHDRAWALS.length; withdrawalIndex++) {
            int amount = DEMO_WITHDRAWALS[withdrawalIndex];
            boolean accepted = piggyBank.withdraw(amount);
            printOutcome("withdraw(" + amount + ")", accepted, piggyBank);
        }
    }

    private static void printOutcome(String action, boolean accepted, PiggyBank piggyBank) {
        if (accepted) {
            System.out.println(action + " -> savings = " + piggyBank.getSavings());
        } else {
            System.out.println(action + " -> rejected, savings stays " + piggyBank.getSavings());
        }
    }
}

class PiggyBank {

    private final String id;
    private int savings;

    PiggyBank(String id) {
        this.id = id;
        this.savings = 0;
    }

    boolean deposit(int amount) {
        if (amount <= 0) {
            return false;
        }
        this.savings = this.savings + amount;
        return true;
    }

    boolean withdraw(int amount) {
        if (amount <= 0 || amount > this.savings) {
            return false;
        }
        this.savings = this.savings - amount;
        return true;
    }

    int getSavings() {
        return this.savings;
    }

    String getId() {
        return this.id;
    }
}

import java.util.NoSuchElementException;
import java.util.Scanner;

public class PayrollSalaryManager {

    private static final String VALUE_SEPARATOR = ",";
    private static final int EXPECTED_OPERATION_COUNT = 2;
    private static final String[] DEMO_BASIC_SALARIES = { "50000", "80000", "-5000", "40000", "abc" };
    private static final String[] DEMO_OPERATIONS = { "5000,10", "0,15", "1000,20", "2000,150", "100,10" };
    private static final String[] DEMO_EMPLOYEES = { "Divya", "Arjun", "Meera", "Rahul", "Nisha" };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] basicSalaries = DEMO_BASIC_SALARIES;
        String[] operations = DEMO_OPERATIONS;
        String[] employees = DEMO_EMPLOYEES;

        String typedSalary = readLine(inputReader, "Enter the basic salary (press Enter to use the demo list): ");
        if (!typedSalary.trim().isEmpty()) {
            String typedOperations = readLine(inputReader, "Enter the bonus and tax percent (e.g. 5000,10): ");
            basicSalaries = new String[] { typedSalary };
            operations = new String[] { typedOperations };
            employees = new String[] { "Employee" };
        }
        System.out.println();

        for (int caseIndex = 0; caseIndex < basicSalaries.length; caseIndex++) {
            runPayrollSession(employees[caseIndex], basicSalaries[caseIndex], operations[caseIndex]);
        }
        System.out.println("Payroll accounts opened in this session: " + PayrollAccount.getAccountsOpened());
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== Payroll Salary Management ===");
        System.out.println("\tInput format:\tbasic \"50000\" with operations \"5000,10\"");
        System.out.println("\tRule:\tbasic salary and bonus are private with no public setter\n");
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

    private static void runPayrollSession(String employeeName, String salaryLine, String operationLine) {
        System.out.println("basicSalary = " + salaryLine + ", operations = " + operationLine);
        try {
            double basicSalary = Double.parseDouble(salaryLine.trim());
            double[] operationValues = parseOperations(operationLine);

            PayrollAccount payrollAccount = new PayrollAccount(employeeName, basicSalary);
            payrollAccount.creditBonus(operationValues[0]);
            payrollAccount.deductTax(operationValues[1]);
            System.out.println("Net salary: Rs " + payrollAccount.getNetSalary() + "\n");
        } catch (MalformedPayrollOperationException malformedOperation) {
            System.out.println("Session skipped: " + malformedOperation.getMessage() + "\n");
        } catch (NumberFormatException nonNumericValue) {
            System.out.println("Session skipped: every amount must be a number\n");
        } catch (IllegalArgumentException emptyOperation) {
            System.out.println("Session skipped: " + emptyOperation.getMessage() + "\n");
        }
    }

    private static double[] parseOperations(String operationLine) throws MalformedPayrollOperationException {
        if (operationLine == null || operationLine.trim().isEmpty()) {
            throw new IllegalArgumentException("no operations were requested");
        }
        String[] operationFields = operationLine.split(VALUE_SEPARATOR);
        if (operationFields.length != EXPECTED_OPERATION_COUNT) {
            throw new MalformedPayrollOperationException("exactly one bonus and one tax percent are needed but found "
                    + operationFields.length);
        }
        double[] operationValues = new double[operationFields.length];
        for (int valueIndex = 0; valueIndex < operationFields.length; valueIndex++) {
            operationValues[valueIndex] = Double.parseDouble(operationFields[valueIndex].trim());
        }
        return operationValues;
    }
}

class PayrollAccount {

    private static final double NO_MONEY = 0.0;
    private static final double MIN_TAX_PERCENT = 0.0;
    private static final double MAX_TAX_PERCENT = 100.0;
    private static final double FULL_PERCENT = 100.0;

    private static int accountsOpened = 0;

    private final String employeeName;
    private double basicSalary;
    private double bonus;

    public PayrollAccount(String employeeName) {
        this(employeeName, NO_MONEY);
    }

    public PayrollAccount(String employeeName, double openingBasicSalary) {
        accountsOpened++;
        this.employeeName = employeeName;
        this.bonus = NO_MONEY;
        if (openingBasicSalary < NO_MONEY) {
            System.out.println("Warning: a negative basic salary was given - starting at " + NO_MONEY);
            this.basicSalary = NO_MONEY;
        } else {
            this.basicSalary = openingBasicSalary;
        }
    }

    public void creditBonus(double amount) {
        if (amount <= NO_MONEY) {
            System.out.println("Bonus rejected: the amount must be greater than zero");
            return;
        }
        this.bonus = this.bonus + amount;
        System.out.println("Bonus credited: Rs " + amount);
    }

    public void deductTax(double percent) {
        if (percent < MIN_TAX_PERCENT || percent > MAX_TAX_PERCENT) {
            System.out.println("Tax rejected: the percent must be between " + MIN_TAX_PERCENT
                    + " and " + MAX_TAX_PERCENT);
            return;
        }
        this.basicSalary = this.basicSalary - (this.basicSalary * percent / FULL_PERCENT);
        System.out.println("Tax deducted: " + formatPercent(percent) + "%");
    }

    public double getNetSalary() {
        return this.basicSalary + this.bonus;
    }

    public String getEmployeeName() {
        return this.employeeName;
    }

    private static String formatPercent(double percent) {
        if (percent == Math.floor(percent)) {
            return String.valueOf((long) percent);
        }
        return String.valueOf(percent);
    }

    static int getAccountsOpened() {
        return accountsOpened;
    }
}

class MalformedPayrollOperationException extends Exception {
    MalformedPayrollOperationException(String message) {
        super(message);
    }
}

import java.util.Scanner;

public class FestivalBonusCalculator {

    private static final String FIELD_SEPARATOR = "\\s+";
    private static final int TYPE_INDEX = 0;
    private static final int NAME_INDEX = 1;
    private static final int SALARY_INDEX = 2;
    private static final String[] DEMO_EMPLOYEE_LINES = {
            "FULLTIME Asha 50000",
            "PARTTIME Ravi 30000",
            "INTERN Neha 15000"
    };

    public static void main(String[] args) {
        String[] employeeLines = readEmployeeLines();

        Employee[] employees = new Employee[employeeLines.length];
        for (int employeeIndex = 0; employeeIndex < employeeLines.length; employeeIndex++) {
            employees[employeeIndex] = createEmployee(employeeLines[employeeIndex]);
        }

        double totalBonus = 0.0;
        for (int employeeIndex = 0; employeeIndex < employees.length; employeeIndex++) {
            double bonus = employees[employeeIndex].calculateBonus();
            System.out.printf("%s: %.2f%n", employees[employeeIndex].getName(), bonus);
            totalBonus = totalBonus + bonus;
        }
        System.out.printf("Total Bonus: %.2f%n", totalBonus);
    }

    private static String[] readEmployeeLines() {
        Scanner inputReader = new Scanner(System.in);
        String countLine = nextFilledLine(inputReader);
        if (countLine.isEmpty()) {
            inputReader.close();
            return DEMO_EMPLOYEE_LINES;
        }
        int employeeCount = Integer.parseInt(countLine);
        String[] employeeLines = new String[employeeCount];
        for (int employeeIndex = 0; employeeIndex < employeeCount; employeeIndex++) {
            employeeLines[employeeIndex] = nextFilledLine(inputReader);
        }
        inputReader.close();
        return employeeLines;
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

    private static Employee createEmployee(String employeeLine) {
        String[] employeeFields = employeeLine.split(FIELD_SEPARATOR);
        String employeeType = employeeFields[TYPE_INDEX].toUpperCase();
        String name = employeeFields[NAME_INDEX];
        double monthlySalary = Double.parseDouble(employeeFields[SALARY_INDEX]);

        switch (employeeType) {
            case "FULLTIME":
                return new FullTimeEmployee(name, monthlySalary);
            case "PARTTIME":
                return new PartTimeEmployee(name, monthlySalary);
            case "INTERN":
                return new Intern(name, monthlySalary);
            default:
                throw new IllegalArgumentException("unknown employee type: " + employeeType);
        }
    }
}

abstract class Employee {

    private final String name;
    protected final double monthlySalary;

    Employee(String name, double monthlySalary) {
        this.name = name;
        this.monthlySalary = monthlySalary;
    }

    abstract double calculateBonus();

    String getName() {
        return this.name;
    }
}

class FullTimeEmployee extends Employee {

    private static final double BONUS_RATE = 0.10;

    FullTimeEmployee(String name, double monthlySalary) {
        super(name, monthlySalary);
    }

    @Override
    double calculateBonus() {
        return this.monthlySalary * BONUS_RATE;
    }
}

class PartTimeEmployee extends Employee {

    private static final double BONUS_RATE = 0.05;

    PartTimeEmployee(String name, double monthlySalary) {
        super(name, monthlySalary);
    }

    @Override
    double calculateBonus() {
        return this.monthlySalary * BONUS_RATE;
    }
}

class Intern extends Employee {

    private static final double FIXED_BONUS = 2000.0;

    Intern(String name, double monthlySalary) {
        super(name, monthlySalary);
    }

    @Override
    double calculateBonus() {
        return FIXED_BONUS;
    }
}

import java.util.NoSuchElementException;
import java.util.Scanner;

public class EmployeeCompanyInfoManager {

    private static final String RECORD_SEPARATOR = ";";
    private static final String FIELD_SEPARATOR = ",";
    private static final int NAME_ONLY_FIELD_COUNT = 1;
    private static final int NAME_AND_SALARY_FIELD_COUNT = 2;
    private static final String[] DEMO_BATCHES = {
            "Divya,65000;Arjun,48000;Meera,72000",
            "Rahul;Nisha,55000",
            "Sameer,-4000",
            "   "
    };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] batches = collectBatches(inputReader);
        for (int batchIndex = 0; batchIndex < batches.length; batchIndex++) {
            hireBatch(batches[batchIndex]);
        }

        System.out.println("Company information read through the class name, not an object:");
        Employee.printCompanyInfo();
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== Employee and Company Information Management ===");
        System.out.println("\tInput format:\t\"Divya,65000;Arjun,48000;Meera,72000\"");
        System.out.println("\tRule:\tthe company name is stored once for the class, not once per employee\n");
    }

    private static String[] collectBatches(Scanner inputReader) {
        System.out.print("Enter the employees (press Enter to use the demo list): ");
        try {
            String typedBatch = inputReader.nextLine();
            if (typedBatch.trim().isEmpty()) {
                System.out.println("[no employees typed - running the demo list]\n");
                return DEMO_BATCHES;
            }
            System.out.println();
            return new String[] { typedBatch };
        } catch (NoSuchElementException | IllegalStateException consoleUnavailable) {
            System.out.println("\n[no console input - running the demo list]\n");
            return DEMO_BATCHES;
        }
    }

    private static void hireBatch(String batchLine) {
        System.out.println("Batch: \"" + batchLine + "\"");
        try {
            Employee[] employees = parseEmployees(batchLine);
            for (int employeeIndex = 0; employeeIndex < employees.length; employeeIndex++) {
                employees[employeeIndex].printEmployee();
            }
            System.out.println("Employees hired from this batch: " + employees.length + "\n");
        } catch (MalformedStaffRecordException malformedStaffRecord) {
            System.out.println("Batch rejected: " + malformedStaffRecord.getMessage() + "\n");
        } catch (NumberFormatException nonNumericSalary) {
            System.out.println("Batch rejected: the salary must be a number\n");
        } catch (IllegalArgumentException invalidEmployee) {
            System.out.println("Batch rejected: " + invalidEmployee.getMessage() + "\n");
        }
    }

    private static Employee[] parseEmployees(String batchLine) throws MalformedStaffRecordException {
        if (batchLine == null || batchLine.trim().isEmpty()) {
            throw new IllegalArgumentException("the batch holds no employees");
        }
        String[] recordFields = batchLine.split(RECORD_SEPARATOR);
        Employee[] employees = new Employee[recordFields.length];
        for (int employeeIndex = 0; employeeIndex < recordFields.length; employeeIndex++) {
            employees[employeeIndex] = parseEmployee(recordFields[employeeIndex]);
        }
        return employees;
    }

    private static Employee parseEmployee(String recordField) throws MalformedStaffRecordException {
        String[] employeeFields = recordField.split(FIELD_SEPARATOR);
        if (employeeFields.length != NAME_ONLY_FIELD_COUNT
                && employeeFields.length != NAME_AND_SALARY_FIELD_COUNT) {
            throw new MalformedStaffRecordException("\"" + recordField.trim()
                    + "\" needs a name and optionally a salary");
        }
        String empName = employeeFields[0].trim();
        if (employeeFields.length == NAME_ONLY_FIELD_COUNT) {
            return new Employee(empName);
        }
        double salary = Double.parseDouble(employeeFields[1].trim());
        return new Employee(empName, salary);
    }
}

class Employee {

    private static final double NO_SALARY = 0.0;

    static String companyName = "Bright Horizon Technologies";
    private static int employeeCount = 0;

    private final String empName;
    private double salary;

    Employee(String empName) {
        this(empName, NO_SALARY);
    }

    Employee(String empName, double salary) {
        validateSalary(empName, salary);
        employeeCount++;
        this.empName = empName;
        this.salary = salary;
    }

    void setSalary(double salary) {
        validateSalary(this.empName, salary);
        this.salary = salary;
    }

    double getSalary() {
        return this.salary;
    }

    String getEmpName() {
        return this.empName;
    }

    void printEmployee() {
        System.out.println(this.empName + " -> Rs " + this.salary);
    }

    static void printCompanyInfo() {
        System.out.println(companyName);
        System.out.println("Employees on record: " + employeeCount);
    }

    private static void validateSalary(String empName, double salary) {
        if (salary < NO_SALARY) {
            throw new IllegalArgumentException("the salary for " + empName + " cannot be negative");
        }
    }
}

class MalformedStaffRecordException extends Exception {
    MalformedStaffRecordException(String message) {
        super(message);
    }
}

import java.util.NoSuchElementException;
import java.util.Scanner;

public class EmployeeProfileCreator {

    private static final String RECORD_SEPARATOR = ";";
    private static final String FIELD_SEPARATOR = ",";
    private static final int INTERN_FIELD_COUNT = 2;
    private static final int PERMANENT_FIELD_COUNT = 3;
    private static final String[] DEMO_BATCHES = {
            "E-101,Divya,65000;E-102,Arjun",
            "E-201,Kavya,90000;E-202,Rahul;E-203,Nisha,45000",
            "E-301",
            "E-401,Sameer,-1000",
            "   "
    };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] batches = collectBatches(inputReader);
        for (int batchIndex = 0; batchIndex < batches.length; batchIndex++) {
            printProfiles(batches[batchIndex]);
        }
        System.out.println("Employee profiles created in this session: " + Employee.getProfilesCreated());
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== Employee Profile Creation ===");
        System.out.println("\tPermanent:\t\"E-101,Divya,65000\"");
        System.out.println("\tIntern:\t\"E-102,Arjun\"");
        System.out.println("\tRule:\tthe intern constructor chains to the three-argument one with this(...)\n");
    }

    private static String[] collectBatches(Scanner inputReader) {
        System.out.print("Enter the employee records (press Enter to use the demo list): ");
        try {
            String typedBatch = inputReader.nextLine();
            if (typedBatch.trim().isEmpty()) {
                System.out.println("[no records typed - running the demo list]\n");
                return DEMO_BATCHES;
            }
            System.out.println();
            return new String[] { typedBatch };
        } catch (NoSuchElementException | IllegalStateException consoleUnavailable) {
            System.out.println("\n[no console input - running the demo list]\n");
            return DEMO_BATCHES;
        }
    }

    private static void printProfiles(String batchLine) {
        System.out.println("Batch: \"" + batchLine + "\"");
        try {
            Employee[] employees = parseEmployees(batchLine);
            for (int employeeIndex = 0; employeeIndex < employees.length; employeeIndex++) {
                employees[employeeIndex].printProfile();
            }
            System.out.println("Profiles created: " + employees.length
                    + " | interns: " + countInterns(employees) + "\n");
        } catch (MalformedEmployeeException malformedEmployee) {
            System.out.println("Batch rejected: " + malformedEmployee.getMessage() + "\n");
        } catch (NumberFormatException nonNumericSalary) {
            System.out.println("Batch rejected: the salary must be a number\n");
        } catch (IllegalArgumentException invalidEmployee) {
            System.out.println("Batch rejected: " + invalidEmployee.getMessage() + "\n");
        }
    }

    private static int countInterns(Employee[] employees) {
        int internCount = 0;
        for (int employeeIndex = 0; employeeIndex < employees.length; employeeIndex++) {
            if (employees[employeeIndex].isIntern()) {
                internCount++;
            }
        }
        return internCount;
    }

    private static Employee[] parseEmployees(String batchLine) throws MalformedEmployeeException {
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

    private static Employee parseEmployee(String recordField) throws MalformedEmployeeException {
        String[] employeeFields = recordField.split(FIELD_SEPARATOR);
        if (employeeFields.length != INTERN_FIELD_COUNT && employeeFields.length != PERMANENT_FIELD_COUNT) {
            throw new MalformedEmployeeException("\"" + recordField.trim()
                    + "\" needs an id, a name and optionally a salary");
        }
        String empId = employeeFields[0].trim();
        String empName = employeeFields[1].trim();
        if (employeeFields.length == INTERN_FIELD_COUNT) {
            return new Employee(empId, empName);
        }
        double salary = Double.parseDouble(employeeFields[2].trim());
        return new Employee(empId, empName, salary);
    }
}

class Employee {

    private static final double NO_SALARY = 0.0;

    private static int profilesCreated = 0;

    private final String empId;
    private final String empName;
    private double salary;
    private boolean isIntern;

    public Employee(String empId, String empName, double salary) {
        validateSalary(empName, salary);
        profilesCreated++;
        this.empId = empId;
        this.empName = empName;
        this.salary = salary;
        this.isIntern = false;
    }

    public Employee(String empId, String empName) {
        this(empId, empName, NO_SALARY);
        this.isIntern = true;
    }

    void printProfile() {
        System.out.println(this.empId + " | " + this.empName + " | Rs " + this.salary
                + " | Intern: " + this.isIntern);
    }

    void setSalary(double salary) {
        validateSalary(this.empName, salary);
        this.salary = salary;
    }

    double getSalary() {
        return this.salary;
    }

    boolean isIntern() {
        return this.isIntern;
    }

    String getEmpId() {
        return this.empId;
    }

    String getEmpName() {
        return this.empName;
    }

    private static void validateSalary(String empName, double salary) {
        if (salary < NO_SALARY) {
            throw new IllegalArgumentException("the salary for " + empName + " cannot be negative");
        }
    }

    static int getProfilesCreated() {
        return profilesCreated;
    }
}

class MalformedEmployeeException extends Exception {
    MalformedEmployeeException(String message) {
        super(message);
    }
}

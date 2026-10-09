import java.util.NoSuchElementException;
import java.util.Scanner;

public class StudentCollegeInfoManager {

    private static final String RECORD_SEPARATOR = ";";
    private static final String FIELD_SEPARATOR = ",";
    private static final int NAME_ONLY_FIELD_COUNT = 1;
    private static final int NAME_AND_ATTENDANCE_FIELD_COUNT = 2;
    private static final String[] DEMO_BATCHES = {
            "Ravi,82;Anitha,91",
            "Karthik;Divya,67;Manoj,45",
            "Sneha,120",
            "   "
    };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] batches = collectBatches(inputReader);
        for (int batchIndex = 0; batchIndex < batches.length; batchIndex++) {
            enrollBatch(batches[batchIndex]);
        }

        System.out.println("College information read through the class name, not an object:");
        Student.printCollegeInfo();
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== Student and College Information Management ===");
        System.out.println("\tInput format:\t\"Ravi,82;Anitha,91\"");
        System.out.println("\tRule:\tthe college name is stored once for the class, not once per student\n");
    }

    private static String[] collectBatches(Scanner inputReader) {
        System.out.print("Enter the students (press Enter to use the demo list): ");
        try {
            String typedBatch = inputReader.nextLine();
            if (typedBatch.trim().isEmpty()) {
                System.out.println("[no students typed - running the demo list]\n");
                return DEMO_BATCHES;
            }
            System.out.println();
            return new String[] { typedBatch };
        } catch (NoSuchElementException | IllegalStateException consoleUnavailable) {
            System.out.println("\n[no console input - running the demo list]\n");
            return DEMO_BATCHES;
        }
    }

    private static void enrollBatch(String batchLine) {
        System.out.println("Batch: \"" + batchLine + "\"");
        try {
            Student[] students = parseStudents(batchLine);
            for (int studentIndex = 0; studentIndex < students.length; studentIndex++) {
                students[studentIndex].printStudent();
            }
            System.out.println("Students enrolled from this batch: " + students.length + "\n");
        } catch (MalformedStudentException malformedStudent) {
            System.out.println("Batch rejected: " + malformedStudent.getMessage() + "\n");
        } catch (NumberFormatException nonNumericAttendance) {
            System.out.println("Batch rejected: attendance must be a whole number\n");
        } catch (IllegalArgumentException invalidStudent) {
            System.out.println("Batch rejected: " + invalidStudent.getMessage() + "\n");
        }
    }

    private static Student[] parseStudents(String batchLine) throws MalformedStudentException {
        if (batchLine == null || batchLine.trim().isEmpty()) {
            throw new IllegalArgumentException("the batch holds no students");
        }
        String[] recordFields = batchLine.split(RECORD_SEPARATOR);
        Student[] students = new Student[recordFields.length];
        for (int studentIndex = 0; studentIndex < recordFields.length; studentIndex++) {
            students[studentIndex] = parseStudent(recordFields[studentIndex]);
        }
        return students;
    }

    private static Student parseStudent(String recordField) throws MalformedStudentException {
        String[] studentFields = recordField.split(FIELD_SEPARATOR);
        if (studentFields.length != NAME_ONLY_FIELD_COUNT
                && studentFields.length != NAME_AND_ATTENDANCE_FIELD_COUNT) {
            throw new MalformedStudentException("\"" + recordField.trim()
                    + "\" needs a name and optionally an attendance percentage");
        }
        String name = studentFields[0].trim();
        if (studentFields.length == NAME_ONLY_FIELD_COUNT) {
            return new Student(name);
        }
        int attendance = Integer.parseInt(studentFields[1].trim());
        return new Student(name, attendance);
    }
}

class Student {

    private static final int NO_ATTENDANCE = 0;
    private static final int MAX_ATTENDANCE = 100;

    static String collegeName = "SRM Institute of Science and Technology";
    private static int studentCount = 0;

    private final String name;
    private int attendance;

    Student(String name) {
        this(name, NO_ATTENDANCE);
    }

    Student(String name, int attendance) {
        validateAttendance(name, attendance);
        studentCount++;
        this.name = name;
        this.attendance = attendance;
    }

    void setAttendance(int attendance) {
        validateAttendance(this.name, attendance);
        this.attendance = attendance;
    }

    private static void validateAttendance(String name, int attendance) {
        if (attendance < NO_ATTENDANCE || attendance > MAX_ATTENDANCE) {
            throw new IllegalArgumentException("attendance for " + name + " must be between "
                    + NO_ATTENDANCE + " and " + MAX_ATTENDANCE);
        }
    }

    int getAttendance() {
        return this.attendance;
    }

    String getName() {
        return this.name;
    }

    void printStudent() {
        System.out.println(this.name + " -> attendance " + this.attendance + "%");
    }

    static void printCollegeInfo() {
        System.out.println(collegeName);
        System.out.println("Students created: " + studentCount);
    }
}

class MalformedStudentException extends Exception {
    MalformedStudentException(String message) {
        super(message);
    }
}

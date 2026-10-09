import java.util.Scanner;

public class CollegeFeeCounter {

    private static final String FIELD_SEPARATOR = "\\s+";
    private static final int TYPE_INDEX = 0;
    private static final int NAME_INDEX = 1;
    private static final String[] DEMO_STUDENT_LINES = { "DAY_SCHOLAR Asha", "HOSTELLER Ravi", "SCHOLAR Neha" };

    public static void main(String[] args) {
        String[] studentLines = readStudentLines();

        Student[] students = new Student[studentLines.length];
        for (int studentIndex = 0; studentIndex < studentLines.length; studentIndex++) {
            students[studentIndex] = createStudent(studentLines[studentIndex]);
        }

        double totalCollected = 0.0;
        for (int studentIndex = 0; studentIndex < students.length; studentIndex++) {
            Student student = students[studentIndex];
            double fee = student.calculateCollegeFee();
            if (student instanceof BusUser) {
                fee = fee + ((BusUser) student).getTransportFee();
            }
            System.out.printf("%s: %.2f%n", student.getName(), fee);
            totalCollected = totalCollected + fee;
        }
        System.out.printf("Total Collected: %.2f%n", totalCollected);
    }

    private static String[] readStudentLines() {
        Scanner inputReader = new Scanner(System.in);
        String countLine = nextFilledLine(inputReader);
        if (countLine.isEmpty()) {
            inputReader.close();
            return DEMO_STUDENT_LINES;
        }
        int studentCount = Integer.parseInt(countLine);
        String[] studentLines = new String[studentCount];
        for (int studentIndex = 0; studentIndex < studentCount; studentIndex++) {
            studentLines[studentIndex] = nextFilledLine(inputReader);
        }
        inputReader.close();
        return studentLines;
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

    private static Student createStudent(String studentLine) {
        String[] studentFields = studentLine.split(FIELD_SEPARATOR);
        String studentType = studentFields[TYPE_INDEX].toUpperCase();
        String name = studentFields[NAME_INDEX];

        switch (studentType) {
            case "DAY_SCHOLAR":
                return new DayScholar(name);
            case "HOSTELLER":
                return new Hosteller(name);
            case "SCHOLAR":
                return new ScholarshipStudent(name);
            default:
                throw new IllegalArgumentException("unknown student type: " + studentType);
        }
    }
}

interface BusUser {

    double TRANSPORT_FEE = 12000.0;

    default double getTransportFee() {
        return TRANSPORT_FEE;
    }
}

abstract class Student {

    protected static final double NORMAL_TUITION = 40000.0;

    private final String name;

    Student(String name) {
        this.name = name;
    }

    abstract double calculateCollegeFee();

    String getName() {
        return this.name;
    }
}

class DayScholar extends Student implements BusUser {

    DayScholar(String name) {
        super(name);
    }

    @Override
    double calculateCollegeFee() {
        return NORMAL_TUITION;
    }
}

class Hosteller extends Student {

    private static final double HOSTEL_FEE = 60000.0;

    Hosteller(String name) {
        super(name);
    }

    @Override
    double calculateCollegeFee() {
        return NORMAL_TUITION + HOSTEL_FEE;
    }
}

class ScholarshipStudent extends Student implements BusUser {

    private static final double TUITION_SHARE = 0.5;

    ScholarshipStudent(String name) {
        super(name);
    }

    @Override
    double calculateCollegeFee() {
        return NORMAL_TUITION * TUITION_SHARE;
    }
}

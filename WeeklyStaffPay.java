import java.util.Scanner;

public class WeeklyStaffPay {

    private static final String FIELD_SEPARATOR = "\\s+";
    private static final int TYPE_INDEX = 0;
    private static final int NAME_INDEX = 1;
    private static final int FIRST_VALUE_INDEX = 2;
    private static final int SECOND_VALUE_INDEX = 3;
    private static final String[] DEMO_STAFF_LINES = { "FULLTIME Asha 12000", "HOURLY Ravi 45 200", "INTERN Neha 5000" };

    public static void main(String[] args) {
        String[] staffLines = readStaffLines();

        StaffMember[] staffMembers = new StaffMember[staffLines.length];
        for (int staffIndex = 0; staffIndex < staffLines.length; staffIndex++) {
            staffMembers[staffIndex] = createStaffMember(staffLines[staffIndex]);
        }

        double totalPayroll = 0.0;
        for (int staffIndex = 0; staffIndex < staffMembers.length; staffIndex++) {
            double pay = staffMembers[staffIndex].calculatePay();
            System.out.printf("%s: %.2f%n", staffMembers[staffIndex].getName(), pay);
            totalPayroll = totalPayroll + pay;
        }
        System.out.printf("Total Payroll: %.2f%n", totalPayroll);
    }

    private static String[] readStaffLines() {
        Scanner inputReader = new Scanner(System.in);
        String countLine = nextFilledLine(inputReader);
        if (countLine.isEmpty()) {
            inputReader.close();
            return DEMO_STAFF_LINES;
        }
        int staffCount = Integer.parseInt(countLine);
        String[] staffLines = new String[staffCount];
        for (int staffIndex = 0; staffIndex < staffCount; staffIndex++) {
            staffLines[staffIndex] = nextFilledLine(inputReader);
        }
        inputReader.close();
        return staffLines;
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

    private static StaffMember createStaffMember(String staffLine) {
        String[] staffFields = staffLine.split(FIELD_SEPARATOR);
        String staffType = staffFields[TYPE_INDEX].toUpperCase();
        String name = staffFields[NAME_INDEX];
        double firstValue = Double.parseDouble(staffFields[FIRST_VALUE_INDEX]);

        switch (staffType) {
            case "FULLTIME":
                return new FullTimeStaff(name, firstValue);
            case "HOURLY":
                return new HourlyStaff(name, firstValue, Double.parseDouble(staffFields[SECOND_VALUE_INDEX]));
            case "INTERN":
                return new InternStaff(name, firstValue);
            default:
                throw new IllegalArgumentException("unknown staff type: " + staffType);
        }
    }
}

abstract class StaffMember {

    private final String name;

    StaffMember(String name) {
        this.name = name;
    }

    abstract double calculatePay();

    String getName() {
        return this.name;
    }
}

class FullTimeStaff extends StaffMember {

    private final double weeklySalary;

    FullTimeStaff(String name, double weeklySalary) {
        super(name);
        this.weeklySalary = weeklySalary;
    }

    @Override
    double calculatePay() {
        return this.weeklySalary;
    }
}

class HourlyStaff extends StaffMember {

    private static final double REGULAR_HOURS_LIMIT = 40.0;
    private static final double OVERTIME_MULTIPLIER = 1.5;

    private final double hoursWorked;
    private final double hourlyRate;

    HourlyStaff(String name, double hoursWorked, double hourlyRate) {
        super(name);
        this.hoursWorked = hoursWorked;
        this.hourlyRate = hourlyRate;
    }

    @Override
    double calculatePay() {
        double regularHours = Math.min(this.hoursWorked, REGULAR_HOURS_LIMIT);
        double overtimeHours = Math.max(this.hoursWorked - REGULAR_HOURS_LIMIT, 0.0);
        return (regularHours * this.hourlyRate) + (overtimeHours * this.hourlyRate * OVERTIME_MULTIPLIER);
    }
}

class InternStaff extends StaffMember {

    private final double stipend;

    InternStaff(String name, double stipend) {
        super(name);
        this.stipend = stipend;
    }

    @Override
    double calculatePay() {
        return this.stipend;
    }
}

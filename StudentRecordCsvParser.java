import java.util.NoSuchElementException;
import java.util.Scanner;

public class StudentRecordCsvParser {

    private static final String FIELD_SEPARATOR = ",";
    private static final int EXPECTED_FIELD_COUNT = 3;
    private static final int NAME_INDEX = 0;
    private static final int ROLL_NUMBER_INDEX = 1;
    private static final int DEPARTMENT_INDEX = 2;
    private static final String INVALID_RECORD_MESSAGE = "Invalid Record";
    private static final String[] DEMO_CSV_LINES = {
            "Ananya Verma,RA2211003010123,CSE",
            "Ananya Verma,CSE",
            "  Rahul Nair , RA2211003010999 , ece ",
            "Priya Menon,,IT",
            "Dev Sharma,RA2211003010456,CSE,Extra",
            "   "
    };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] csvLines = collectCsvLines(inputReader);
        for (int lineIndex = 0; lineIndex < csvLines.length; lineIndex++) {
            parseStudentRecord(csvLines[lineIndex]);
        }
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== CSV Student Record Parser ===");
        System.out.println("\tExpected format:\t\"Name,RollNumber,Department\"");
        System.out.println("\tSource file:\tC:\\tnp\\registrations.csv\n");
    }

    private static String[] collectCsvLines(Scanner inputReader) {
        System.out.print("Enter a CSV record (press Enter to use the demo list): ");
        try {
            String typedLine = inputReader.nextLine();
            if (typedLine.trim().isEmpty()) {
                System.out.println("[no record typed - running the demo list]\n");
                return DEMO_CSV_LINES;
            }
            System.out.println();
            return new String[] { typedLine };
        } catch (NoSuchElementException | IllegalStateException consoleUnavailable) {
            System.out.println("\n[no console input - running the demo list]\n");
            return DEMO_CSV_LINES;
        }
    }

    private static void parseStudentRecord(String csvLine) {
        System.out.println("Record: \"" + csvLine + "\"");
        try {
            String[] recordFields = splitRecord(csvLine);
            String studentName = recordFields[NAME_INDEX].trim();
            String rollNumber = recordFields[ROLL_NUMBER_INDEX].trim();
            String department = recordFields[DEPARTMENT_INDEX].trim().toUpperCase();

            validateField(studentName, "Name");
            validateField(rollNumber, "Roll No");
            validateField(department, "Dept");

            System.out.println("Name: " + studentName + " | Roll No: " + rollNumber + " | Dept: " + department + "\n");
        } catch (IncompleteRecordException incompleteRecord) {
            System.out.println(INVALID_RECORD_MESSAGE + " - " + incompleteRecord.getMessage() + "\n");
        } catch (ArrayIndexOutOfBoundsException missingField) {
            System.out.println(INVALID_RECORD_MESSAGE + " - a field is missing\n");
        } catch (IllegalArgumentException missingLine) {
            System.out.println(INVALID_RECORD_MESSAGE + " - " + missingLine.getMessage() + "\n");
        }
    }

    private static String[] splitRecord(String csvLine) throws IncompleteRecordException {
        if (csvLine == null || csvLine.trim().isEmpty()) {
            throw new IllegalArgumentException("the record is empty");
        }
        String[] recordFields = csvLine.split(FIELD_SEPARATOR);
        if (recordFields.length != EXPECTED_FIELD_COUNT) {
            throw new IncompleteRecordException("expected " + EXPECTED_FIELD_COUNT
                    + " fields but found " + recordFields.length);
        }
        return recordFields;
    }

    private static void validateField(String fieldValue, String fieldName) throws IncompleteRecordException {
        if (fieldValue.isEmpty()) {
            throw new IncompleteRecordException(fieldName + " is blank");
        }
    }
}

class IncompleteRecordException extends Exception {
    IncompleteRecordException(String message) {
        super(message);
    }
}

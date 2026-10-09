import java.util.NoSuchElementException;
import java.util.Scanner;

public class PlacementRecordManager {

    private static final String RECORD_SEPARATOR = ";";
    private static final String FIELD_SEPARATOR = ",";
    private static final int EXPECTED_FIELD_COUNT = 3;
    private static final String[] DEMO_BATCHES = {
            "Ravi,TCS,4.5;Anitha,Zoho,6.2;Karthik,Infosys,4.0",
            "Divya,Freshworks,9.5;Manoj,Wipro,3.6;Sneha,Amazon,12.0",
            "Ravi,TCS",
            "Vikas,HCL,-2.0",
            "   "
    };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] batches = collectBatches(inputReader);
        for (int batchIndex = 0; batchIndex < batches.length; batchIndex++) {
            printPlacementBoard(batches[batchIndex]);
        }
        System.out.println("Placement records created in this session: " + PlacementRecord.getRecordsCreated());
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== Student Placement Record Management ===");
        System.out.println("\tInput format:\t\"Ravi,TCS,4.5;Anitha,Zoho,6.2\"");
        System.out.println("\tRule:\tone object per student, held in an array of PlacementRecord\n");
    }

    private static String[] collectBatches(Scanner inputReader) {
        System.out.print("Enter the placement records (press Enter to use the demo list): ");
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

    private static void printPlacementBoard(String batchLine) {
        System.out.println("Batch: \"" + batchLine + "\"");
        try {
            PlacementRecord[] placementRecords = parseRecords(batchLine);
            for (int recordIndex = 0; recordIndex < placementRecords.length; recordIndex++) {
                placementRecords[recordIndex].printRecord();
            }
            System.out.println("Students placed: " + placementRecords.length
                    + " | highest package: " + findHighestPackage(placementRecords) + " LPA\n");
        } catch (MalformedPlacementException malformedPlacement) {
            System.out.println("Batch rejected: " + malformedPlacement.getMessage() + "\n");
        } catch (NumberFormatException nonNumericPackage) {
            System.out.println("Batch rejected: the package must be a number\n");
        } catch (IllegalArgumentException invalidRecord) {
            System.out.println("Batch rejected: " + invalidRecord.getMessage() + "\n");
        }
    }

    private static double findHighestPackage(PlacementRecord[] placementRecords) {
        double highestPackage = placementRecords[0].getPackageLpa();
        for (int recordIndex = 1; recordIndex < placementRecords.length; recordIndex++) {
            if (placementRecords[recordIndex].getPackageLpa() > highestPackage) {
                highestPackage = placementRecords[recordIndex].getPackageLpa();
            }
        }
        return highestPackage;
    }

    private static PlacementRecord[] parseRecords(String batchLine) throws MalformedPlacementException {
        if (batchLine == null || batchLine.trim().isEmpty()) {
            throw new IllegalArgumentException("the batch holds no records");
        }
        String[] recordFields = batchLine.split(RECORD_SEPARATOR);
        PlacementRecord[] placementRecords = new PlacementRecord[recordFields.length];
        for (int recordIndex = 0; recordIndex < recordFields.length; recordIndex++) {
            placementRecords[recordIndex] = parseRecord(recordFields[recordIndex]);
        }
        return placementRecords;
    }

    private static PlacementRecord parseRecord(String recordField) throws MalformedPlacementException {
        String[] placementFields = recordField.split(FIELD_SEPARATOR);
        if (placementFields.length != EXPECTED_FIELD_COUNT) {
            throw new MalformedPlacementException("\"" + recordField.trim()
                    + "\" needs a student, a company and a package");
        }
        String studentName = placementFields[0].trim();
        String company = placementFields[1].trim();
        double packageLpa = Double.parseDouble(placementFields[2].trim());
        return new PlacementRecord(studentName, company, packageLpa);
    }
}

class PlacementRecord {

    private static int recordsCreated = 0;

    private final String studentName;
    private final String company;
    private final double packageLpa;

    public PlacementRecord(String studentName, String company, double packageLpa) {
        if (packageLpa <= 0) {
            throw new IllegalArgumentException("the package for " + studentName + " must be greater than zero");
        }
        recordsCreated++;
        this.studentName = studentName;
        this.company = company;
        this.packageLpa = packageLpa;
    }

    void printRecord() {
        System.out.println(this.studentName + " -> " + this.company + " @ " + this.packageLpa + " LPA");
    }

    String getStudentName() {
        return this.studentName;
    }

    String getCompany() {
        return this.company;
    }

    double getPackageLpa() {
        return this.packageLpa;
    }

    static int getRecordsCreated() {
        return recordsCreated;
    }
}

class MalformedPlacementException extends Exception {
    MalformedPlacementException(String message) {
        super(message);
    }
}

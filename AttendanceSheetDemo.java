public class AttendanceSheetDemo {

    private static final int MAXIMUM_CLASS_SIZE = 30;
    private static final String[] DEMO_ARRIVALS = { "Ana", "Ben", "Ana" };
    private static final String[] DEMO_LOOKUPS = { "Ben", "Chen" };

    public static void main(String[] args) {
        AttendanceSheet attendanceSheet = new AttendanceSheet(MAXIMUM_CLASS_SIZE);

        for (int arrivalIndex = 0; arrivalIndex < DEMO_ARRIVALS.length; arrivalIndex++) {
            boolean added = attendanceSheet.markPresent(DEMO_ARRIVALS[arrivalIndex]);
            System.out.println("markPresent(\"" + DEMO_ARRIVALS[arrivalIndex] + "\") -> "
                    + (added ? "marked" : "already present or sheet full"));
        }

        System.out.println("getPresentCount() -> " + attendanceSheet.getPresentCount());

        for (int lookupIndex = 0; lookupIndex < DEMO_LOOKUPS.length; lookupIndex++) {
            System.out.println("isPresent(\"" + DEMO_LOOKUPS[lookupIndex] + "\") -> "
                    + attendanceSheet.isPresent(DEMO_LOOKUPS[lookupIndex]));
        }
    }
}

class AttendanceSheet {

    private final String[] presentNames;
    private int presentCount;

    AttendanceSheet(int maximumClassSize) {
        this.presentNames = new String[maximumClassSize];
        this.presentCount = 0;
    }

    boolean markPresent(String studentName) {
        if (isPresent(studentName) || this.presentCount >= this.presentNames.length) {
            return false;
        }
        this.presentNames[this.presentCount] = studentName;
        this.presentCount++;
        return true;
    }

    int getPresentCount() {
        return this.presentCount;
    }

    boolean isPresent(String studentName) {
        for (int nameIndex = 0; nameIndex < this.presentCount; nameIndex++) {
            if (this.presentNames[nameIndex].equals(studentName)) {
                return true;
            }
        }
        return false;
    }
}

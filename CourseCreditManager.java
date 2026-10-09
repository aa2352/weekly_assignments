import java.util.NoSuchElementException;
import java.util.Scanner;

public class CourseCreditManager {

    private static final String RECORD_SEPARATOR = ";";
    private static final String FIELD_SEPARATOR = ",";
    private static final int THEORY_FIELD_COUNT = 3;
    private static final int LAB_FIELD_COUNT = 4;
    private static final String[] DEMO_BATCHES = {
            "21CSC201J,Data Structures,4;21CSC205L,DSA Lab,3,1",
            "21CSC301T,Operating Systems,3;21CSC302P,OS Lab,2,2;21MAB204T,Probability,4",
            "21CSC401J,Machine Learning",
            "21CSC501J,Compiler Design,-3",
            "   "
    };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] batches = collectBatches(inputReader);
        for (int batchIndex = 0; batchIndex < batches.length; batchIndex++) {
            printCreditSheet(batches[batchIndex]);
        }
        System.out.println("Courses registered in this session: " + Course.getCoursesCreated());
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== Course Credit Management ===");
        System.out.println("\tTheory course:\t\"21CSC201J,Data Structures,4\"");
        System.out.println("\tLab course:\t\"21CSC205L,DSA Lab,3,1\"");
        System.out.println("\tRule:\tthe theory constructor chains to the four-argument one with this(...)\n");
    }

    private static String[] collectBatches(Scanner inputReader) {
        System.out.print("Enter the courses (press Enter to use the demo list): ");
        try {
            String typedBatch = inputReader.nextLine();
            if (typedBatch.trim().isEmpty()) {
                System.out.println("[no courses typed - running the demo list]\n");
                return DEMO_BATCHES;
            }
            System.out.println();
            return new String[] { typedBatch };
        } catch (NoSuchElementException | IllegalStateException consoleUnavailable) {
            System.out.println("\n[no console input - running the demo list]\n");
            return DEMO_BATCHES;
        }
    }

    private static void printCreditSheet(String batchLine) {
        System.out.println("Courses: \"" + batchLine + "\"");
        try {
            Course[] courses = parseCourses(batchLine);
            int semesterCredits = 0;
            for (int courseIndex = 0; courseIndex < courses.length; courseIndex++) {
                System.out.println(courses[courseIndex].getCode() + " total credits: "
                        + courses[courseIndex].totalCredits());
                semesterCredits += courses[courseIndex].totalCredits();
            }
            System.out.println("Courses registered: " + courses.length
                    + " | semester credits: " + semesterCredits + "\n");
        } catch (MalformedCourseException malformedCourse) {
            System.out.println("Batch rejected: " + malformedCourse.getMessage() + "\n");
        } catch (NumberFormatException nonNumericCredit) {
            System.out.println("Batch rejected: credits must be whole numbers\n");
        } catch (IllegalArgumentException invalidCourse) {
            System.out.println("Batch rejected: " + invalidCourse.getMessage() + "\n");
        }
    }

    private static Course[] parseCourses(String batchLine) throws MalformedCourseException {
        if (batchLine == null || batchLine.trim().isEmpty()) {
            throw new IllegalArgumentException("the batch holds no courses");
        }
        String[] recordFields = batchLine.split(RECORD_SEPARATOR);
        Course[] courses = new Course[recordFields.length];
        for (int courseIndex = 0; courseIndex < recordFields.length; courseIndex++) {
            courses[courseIndex] = parseCourse(recordFields[courseIndex]);
        }
        return courses;
    }

    private static Course parseCourse(String recordField) throws MalformedCourseException {
        String[] courseFields = recordField.split(FIELD_SEPARATOR);
        if (courseFields.length != THEORY_FIELD_COUNT && courseFields.length != LAB_FIELD_COUNT) {
            throw new MalformedCourseException("\"" + recordField.trim()
                    + "\" needs a code, a title, credits and optionally lab credits");
        }
        String code = courseFields[0].trim();
        String title = courseFields[1].trim();
        int credits = Integer.parseInt(courseFields[2].trim());

        if (courseFields.length == THEORY_FIELD_COUNT) {
            return new Course(code, title, credits);
        }
        int labCredits = Integer.parseInt(courseFields[3].trim());
        return new Course(code, title, credits, labCredits);
    }
}

class Course {

    private static final int NO_LAB_CREDITS = 0;

    private static int coursesCreated = 0;

    private final String code;
    private final String title;
    private final int credits;
    private final int labCredits;

    public Course(String code, String title, int credits) {
        this(code, title, credits, NO_LAB_CREDITS);
    }

    public Course(String code, String title, int credits, int labCredits) {
        if (credits < 0 || labCredits < 0) {
            throw new IllegalArgumentException("credits for " + code + " cannot be negative");
        }
        coursesCreated++;
        this.code = code;
        this.title = title;
        this.credits = credits;
        this.labCredits = labCredits;
    }

    int totalCredits() {
        return this.credits + this.labCredits;
    }

    String getCode() {
        return this.code;
    }

    String getTitle() {
        return this.title;
    }

    int getCredits() {
        return this.credits;
    }

    int getLabCredits() {
        return this.labCredits;
    }

    static int getCoursesCreated() {
        return coursesCreated;
    }
}

class MalformedCourseException extends Exception {
    MalformedCourseException(String message) {
        super(message);
    }
}

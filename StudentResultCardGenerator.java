import java.util.Arrays;
import java.util.Scanner;

public class StudentResultCardGenerator {

    private static final int MAXIMUM_LINES = 1000;
    private static final String[] DEMO_STUDENT_LINES = { "Asha [80, 90, 70]", "Ravi [60, 55, 50]" };

    public static void main(String[] args) {
        String[] studentLines = readInputLines(DEMO_STUDENT_LINES);
        for (int studentIndex = 0; studentIndex < studentLines.length; studentIndex++) {
            ResultCard resultCard = parseResultCard(studentLines[studentIndex]);
            System.out.printf("%s: Average %.1f, Grade %c%n", resultCard.getName().toUpperCase(),
                    resultCard.calculateAverage(), resultCard.assignGrade());
        }
    }

    private static ResultCard parseResultCard(String studentLine) {
        int listStart = studentLine.indexOf('[');
        String name = studentLine.substring(0, listStart).trim();
        String innerText = studentLine.substring(listStart + 1, studentLine.indexOf(']'));
        String[] markFields = innerText.split(",");
        int[] marks = new int[markFields.length];
        for (int markIndex = 0; markIndex < markFields.length; markIndex++) {
            marks[markIndex] = Integer.parseInt(markFields[markIndex].trim());
        }
        return new ResultCard(name, marks);
    }

    private static String[] readInputLines(String[] demoLines) {
        Scanner inputReader = new Scanner(System.in);
        String[] collectedLines = new String[MAXIMUM_LINES];
        int lineCount = 0;
        while (inputReader.hasNextLine() && lineCount < MAXIMUM_LINES) {
            String typedLine = inputReader.nextLine().trim();
            if (!typedLine.isEmpty()) {
                collectedLines[lineCount] = typedLine;
                lineCount++;
            }
        }
        inputReader.close();
        return lineCount == 0 ? demoLines : Arrays.copyOf(collectedLines, lineCount);
    }
}

class ResultCard {

    private final String name;
    private final int[] marks;

    ResultCard(String name, int[] marks) {
        this.name = name;
        this.marks = marks;
    }

    double calculateAverage() {
        int totalMarks = 0;
        for (int markIndex = 0; markIndex < this.marks.length; markIndex++) {
            totalMarks = totalMarks + this.marks[markIndex];
        }
        return (double) totalMarks / this.marks.length;
    }

    char assignGrade() {
        double average = calculateAverage();
        if (average >= 90) {
            return 'A';
        } else if (average >= 75) {
            return 'B';
        } else if (average >= 60) {
            return 'C';
        } else if (average >= 40) {
            return 'D';
        } else {
            return 'F';
        }
    }

    String getName() {
        return this.name;
    }
}

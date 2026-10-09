import java.util.NoSuchElementException;
import java.util.Scanner;

public class SortedScoreMerger {

    private static final String VALUE_SEPARATOR = ",";
    private static final String[] DEMO_FIRST_SECTIONS = { "1,3,5", "", "10,20,30", "5,4,3", "2,4" };
    private static final String[] DEMO_SECOND_SECTIONS = { "2,4,6", "1,2,3", "", "1,2,3", "1,3,x" };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] firstSections = DEMO_FIRST_SECTIONS;
        String[] secondSections = DEMO_SECOND_SECTIONS;

        String typedFirstSection = readLine(inputReader,
                "Enter section A scores, sorted (press Enter to use the demo list): ");
        if (!typedFirstSection.trim().isEmpty()) {
            String typedSecondSection = readLine(inputReader, "Enter section B scores, sorted: ");
            firstSections = new String[] { typedFirstSection };
            secondSections = new String[] { typedSecondSection };
        }
        System.out.println();

        for (int caseIndex = 0; caseIndex < firstSections.length; caseIndex++) {
            reportMergedScores(firstSections[caseIndex], secondSections[caseIndex]);
        }
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== Sorted Score Merger ===");
        System.out.println("\tInput format:\t\"1,3,5\" and \"2,4,6\"");
        System.out.println("\tRule:\tboth lists arrive sorted, so merge with two index pointers\n");
    }

    private static String readLine(Scanner inputReader, String prompt) {
        System.out.print(prompt);
        try {
            return inputReader.nextLine();
        } catch (NoSuchElementException | IllegalStateException consoleUnavailable) {
            System.out.println("\n[no console input - running the demo list]");
            return "";
        }
    }

    private static void reportMergedScores(String firstSectionLine, String secondSectionLine) {
        System.out.println("arr1 = [" + firstSectionLine + "], arr2 = [" + secondSectionLine + "]");
        try {
            int[] firstSection = parseIntArray(firstSectionLine);
            int[] secondSection = parseIntArray(secondSectionLine);
            int[] mergedScores = mergeSortedArrays(firstSection, secondSection);

            System.out.println(formatArray(mergedScores));
            System.out.println("Lengths: " + firstSection.length + " + " + secondSection.length
                    + " = " + mergedScores.length + "\n");
        } catch (UnsortedSectionException unsortedSection) {
            System.out.println("Merge stopped: " + unsortedSection.getMessage() + "\n");
        } catch (NumberFormatException nonNumericScore) {
            System.out.println("Merge stopped: every score must be a whole number\n");
        } catch (IllegalArgumentException missingSection) {
            System.out.println("Merge stopped: " + missingSection.getMessage() + "\n");
        }
    }

    private static int[] mergeSortedArrays(int[] arr1, int[] arr2) throws UnsortedSectionException {
        validateSorted(arr1, "section A");
        validateSorted(arr2, "section B");

        int[] mergedScores = new int[arr1.length + arr2.length];
        int firstIndex = 0;
        int secondIndex = 0;
        int mergedIndex = 0;

        while (firstIndex < arr1.length && secondIndex < arr2.length) {
            if (arr1[firstIndex] <= arr2[secondIndex]) {
                mergedScores[mergedIndex] = arr1[firstIndex];
                firstIndex++;
            } else {
                mergedScores[mergedIndex] = arr2[secondIndex];
                secondIndex++;
            }
            mergedIndex++;
        }
        while (firstIndex < arr1.length) {
            mergedScores[mergedIndex] = arr1[firstIndex];
            firstIndex++;
            mergedIndex++;
        }
        while (secondIndex < arr2.length) {
            mergedScores[mergedIndex] = arr2[secondIndex];
            secondIndex++;
            mergedIndex++;
        }
        return mergedScores;
    }

    private static void validateSorted(int[] scores, String sectionName) throws UnsortedSectionException {
        for (int scoreIndex = 1; scoreIndex < scores.length; scoreIndex++) {
            if (scores[scoreIndex] < scores[scoreIndex - 1]) {
                throw new UnsortedSectionException(sectionName + " is not sorted: " + scores[scoreIndex]
                        + " follows " + scores[scoreIndex - 1]);
            }
        }
    }

    private static int[] parseIntArray(String valueLine) {
        if (valueLine == null) {
            throw new IllegalArgumentException("a section reference is null");
        }
        String trimmedLine = valueLine.trim();
        if (trimmedLine.isEmpty()) {
            return new int[0];
        }
        String[] valueFields = trimmedLine.split(VALUE_SEPARATOR);
        int[] values = new int[valueFields.length];
        for (int fieldIndex = 0; fieldIndex < valueFields.length; fieldIndex++) {
            values[fieldIndex] = Integer.parseInt(valueFields[fieldIndex].trim());
        }
        return values;
    }

    private static String formatArray(int[] values) {
        StringBuilder formattedArray = new StringBuilder("[");
        for (int valueIndex = 0; valueIndex < values.length; valueIndex++) {
            if (valueIndex > 0) {
                formattedArray.append(", ");
            }
            formattedArray.append(values[valueIndex]);
        }
        return formattedArray.append("]").toString();
    }
}

class UnsortedSectionException extends Exception {
    UnsortedSectionException(String message) {
        super(message);
    }
}

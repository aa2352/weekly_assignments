import java.util.NoSuchElementException;
import java.util.Scanner;

public class ExamHallSeatDuplicationChecker {

    private static final String SEAT_SEPARATOR = ",";
    private static final char ASCII_DIGIT_ZERO = '0';
    private static final char ASCII_DIGIT_NINE = '9';
    private static final String[] DEMO_SEAT_LINES = {
            "101,102,103,102,105",
            "101,102,103,104,105",
            "201,202,201,204,202",
            "301,302,30A,304",
            "401,999999999999,403",
            "   "
    };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] seatLines = collectSeatLines(inputReader);
        for (int lineIndex = 0; lineIndex < seatLines.length; lineIndex++) {
            inspectSeatLine(seatLines[lineIndex]);
        }
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== The Exam Hall Seat Duplication Checker ===");
        System.out.println("\tInput format:\t\"101,102,103\"");
        System.out.println("\tData source:\tC:\\exam-cell\\seating-sheet\n");
    }

    private static String[] collectSeatLines(Scanner inputReader) {
        System.out.print("Enter the assigned seat numbers (press Enter to use the demo list): ");
        try {
            String typedLine = inputReader.nextLine();
            if (typedLine.trim().isEmpty()) {
                System.out.println("[no seats typed - running the demo list]\n");
                return DEMO_SEAT_LINES;
            }
            System.out.println();
            return new String[] { typedLine };
        } catch (NoSuchElementException | IllegalStateException consoleUnavailable) {
            System.out.println("\n[no console input - running the demo list]\n");
            return DEMO_SEAT_LINES;
        }
    }

    private static void inspectSeatLine(String seatLine) {
        System.out.println("Seat list: \"" + seatLine + "\"");
        try {
            int[] seatNumbers = parseSeatNumbers(seatLine);
            checkDuplicateSeats(seatNumbers);
        } catch (InvalidSeatDataException invalidSeatData) {
            System.out.println("Check aborted: " + invalidSeatData.getMessage());
        } catch (NumberFormatException oversizedSeatNumber) {
            System.out.println("Check aborted: a seat number is too large to store as an integer");
        } catch (IllegalArgumentException emptySeatLine) {
            System.out.println("Check aborted: " + emptySeatLine.getMessage());
        }
        System.out.println();
    }

    private static int[] parseSeatNumbers(String seatLine) throws InvalidSeatDataException {
        if (seatLine == null || seatLine.trim().isEmpty()) {
            throw new IllegalArgumentException("the seat list is empty");
        }
        String[] seatFields = seatLine.split(SEAT_SEPARATOR);
        int[] seatNumbers = new int[seatFields.length];
        for (int fieldIndex = 0; fieldIndex < seatFields.length; fieldIndex++) {
            String seatField = seatFields[fieldIndex].trim();
            validateSeatField(seatField);
            seatNumbers[fieldIndex] = Integer.parseInt(seatField);
        }
        return seatNumbers;
    }

    private static void validateSeatField(String seatField) throws InvalidSeatDataException {
        if (seatField.isEmpty()) {
            throw new InvalidSeatDataException("a seat number is missing between two separators");
        }
        for (int charIndex = 0; charIndex < seatField.length(); charIndex++) {
            char currentChar = seatField.charAt(charIndex);
            if (currentChar < ASCII_DIGIT_ZERO || currentChar > ASCII_DIGIT_NINE) {
                throw new InvalidSeatDataException("'" + currentChar + "' (ASCII code " + (int) currentChar
                        + ") in \"" + seatField + "\" is not a digit");
            }
        }
    }

    private static void checkDuplicateSeats(int[] seatNumbers) {
        boolean[] alreadyReported = new boolean[seatNumbers.length];
        int duplicateCount = 0;

        for (int firstIndex = 0; firstIndex < seatNumbers.length; firstIndex++) {
            if (alreadyReported[firstIndex]) {
                continue;
            }
            for (int secondIndex = firstIndex + 1; secondIndex < seatNumbers.length; secondIndex++) {
                if (seatNumbers[firstIndex] == seatNumbers[secondIndex] && !alreadyReported[secondIndex]) {
                    if (!alreadyReported[firstIndex]) {
                        System.out.println("Duplicate Seat Number Found: " + seatNumbers[firstIndex]);
                        alreadyReported[firstIndex] = true;
                        duplicateCount++;
                    }
                    alreadyReported[secondIndex] = true;
                }
            }
        }

        if (duplicateCount == 0) {
            System.out.println("No Duplicate Seats Found");
        } else {
            System.out.println("Total duplicated seat numbers: " + duplicateCount);
        }
    }
}

class InvalidSeatDataException extends Exception {
    InvalidSeatDataException(String message) {
        super(message);
    }
}

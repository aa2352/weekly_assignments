import java.util.NoSuchElementException;
import java.util.Scanner;

public class MaskedPhoneNumberFormatter {

    private static final int REQUIRED_DIGIT_COUNT = 10;
    private static final int VISIBLE_DIGIT_COUNT = 4;
    private static final String MASK_BLOCK = "XXXXXX";
    private static final String MASK_SEPARATOR = "-";
    private static final String INVALID_PHONE_MESSAGE = "Invalid phone number";
    private static final char ASCII_DIGIT_ZERO = '0';
    private static final char ASCII_DIGIT_NINE = '9';
    private static final int PERFORMANCE_ITERATIONS = 100000;
    private static final int APPEND_ITERATIONS = 5000;
    private static final String[] DEMO_PHONE_NUMBERS = {
            "9876543210", "98765", "98765abcde", "  9123456789  ", "   " };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] phoneNumbers = collectPhoneNumbers(inputReader);
        for (int phoneIndex = 0; phoneIndex < phoneNumbers.length; phoneIndex++) {
            reportMaskedNumber(phoneNumbers[phoneIndex]);
        }
        comparePerformance(DEMO_PHONE_NUMBERS[0]);
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== Masked Phone Number Formatter ===");
        System.out.println("\tPattern:\t\"" + MASK_BLOCK + MASK_SEPARATOR + "1234\"");
        System.out.println("\tRule:\texactly " + REQUIRED_DIGIT_COUNT + " digits\n");
    }

    private static String[] collectPhoneNumbers(Scanner inputReader) {
        System.out.print("Enter a phone number (press Enter to use the demo list): ");
        try {
            String typedPhone = inputReader.nextLine();
            if (typedPhone.trim().isEmpty()) {
                System.out.println("[no number typed - running the demo list]\n");
                return DEMO_PHONE_NUMBERS;
            }
            System.out.println();
            return new String[] { typedPhone };
        } catch (NoSuchElementException | IllegalStateException consoleUnavailable) {
            System.out.println("\n[no console input - running the demo list]\n");
            return DEMO_PHONE_NUMBERS;
        }
    }

    private static void reportMaskedNumber(String phone) {
        System.out.println("Phone: \"" + phone + "\"");
        try {
            System.out.println(maskPhoneNumber(phone) + "\n");
        } catch (InvalidPhoneNumberException invalidPhone) {
            System.out.println(INVALID_PHONE_MESSAGE + " - " + invalidPhone.getMessage() + "\n");
        } catch (StringIndexOutOfBoundsException shortPhone) {
            System.out.println(INVALID_PHONE_MESSAGE + " - the number is too short to mask\n");
        } catch (IllegalArgumentException emptyPhone) {
            System.out.println(INVALID_PHONE_MESSAGE + " - " + emptyPhone.getMessage() + "\n");
        }
    }

    private static String maskPhoneNumber(String phone) throws InvalidPhoneNumberException {
        String cleanedPhone = validatePhoneNumber(phone);
        String visibleDigits = cleanedPhone.substring(cleanedPhone.length() - VISIBLE_DIGIT_COUNT);

        StringBuilder maskedNumber = new StringBuilder();
        maskedNumber.append(MASK_BLOCK);
        maskedNumber.append(visibleDigits);
        maskedNumber.insert(MASK_BLOCK.length(), MASK_SEPARATOR);
        return maskedNumber.toString();
    }

    private static String validatePhoneNumber(String phone) throws InvalidPhoneNumberException {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("the number is empty");
        }
        String cleanedPhone = phone.trim();
        if (cleanedPhone.length() != REQUIRED_DIGIT_COUNT) {
            throw new InvalidPhoneNumberException("expected " + REQUIRED_DIGIT_COUNT
                    + " digits but found " + cleanedPhone.length());
        }
        for (int charIndex = 0; charIndex < cleanedPhone.length(); charIndex++) {
            char currentChar = cleanedPhone.charAt(charIndex);
            if (currentChar < ASCII_DIGIT_ZERO || currentChar > ASCII_DIGIT_NINE) {
                throw new InvalidPhoneNumberException("'" + currentChar + "' (ASCII code " + (int) currentChar
                        + ") is not a digit");
            }
        }
        return cleanedPhone;
    }

    private static void comparePerformance(String samplePhone) {
        String visibleDigits = samplePhone.substring(samplePhone.length() - VISIBLE_DIGIT_COUNT);

        long concatenationNanos = measureConcatenation(visibleDigits);
        long builderNanos = measureStringBuilder(visibleDigits);
        long bufferNanos = measureStringBuffer(visibleDigits);

        System.out.println("Building one mask, " + PERFORMANCE_ITERATIONS + " times:");
        System.out.printf("\tString concatenation: %8.3f ms%n", concatenationNanos / 1_000_000.0);
        System.out.printf("\tStringBuilder       : %8.3f ms%n", builderNanos / 1_000_000.0);
        System.out.printf("\tStringBuffer        : %8.3f ms%n", bufferNanos / 1_000_000.0);

        long appendConcatenationNanos = measureRepeatedConcatenation(visibleDigits);
        long appendBuilderNanos = measureRepeatedStringBuilder(visibleDigits);
        long appendBufferNanos = measureRepeatedStringBuffer(visibleDigits);

        System.out.println("\nAppending " + APPEND_ITERATIONS + " masks into one growing text:");
        System.out.printf("\tString concatenation: %8.3f ms%n", appendConcatenationNanos / 1_000_000.0);
        System.out.printf("\tStringBuilder       : %8.3f ms%n", appendBuilderNanos / 1_000_000.0);
        System.out.printf("\tStringBuffer        : %8.3f ms%n", appendBufferNanos / 1_000_000.0);
    }

    private static long measureRepeatedConcatenation(String visibleDigits) {
        long startTime = System.nanoTime();
        String report = "";
        for (int iteration = 0; iteration < APPEND_ITERATIONS; iteration++) {
            report += MASK_BLOCK + MASK_SEPARATOR + visibleDigits;
        }
        long elapsedTime = System.nanoTime() - startTime;
        return report.length() > 0 ? elapsedTime : 0;
    }

    private static long measureRepeatedStringBuilder(String visibleDigits) {
        long startTime = System.nanoTime();
        StringBuilder report = new StringBuilder();
        for (int iteration = 0; iteration < APPEND_ITERATIONS; iteration++) {
            report.append(MASK_BLOCK).append(MASK_SEPARATOR).append(visibleDigits);
        }
        long elapsedTime = System.nanoTime() - startTime;
        return report.length() > 0 ? elapsedTime : 0;
    }

    private static long measureRepeatedStringBuffer(String visibleDigits) {
        long startTime = System.nanoTime();
        StringBuffer report = new StringBuffer();
        for (int iteration = 0; iteration < APPEND_ITERATIONS; iteration++) {
            report.append(MASK_BLOCK).append(MASK_SEPARATOR).append(visibleDigits);
        }
        long elapsedTime = System.nanoTime() - startTime;
        return report.length() > 0 ? elapsedTime : 0;
    }

    private static long measureConcatenation(String visibleDigits) {
        long startTime = System.nanoTime();
        int builtLength = 0;
        for (int iteration = 0; iteration < PERFORMANCE_ITERATIONS; iteration++) {
            String maskedNumber = MASK_BLOCK + MASK_SEPARATOR + visibleDigits;
            builtLength += maskedNumber.length();
        }
        long elapsedTime = System.nanoTime() - startTime;
        return builtLength > 0 ? elapsedTime : 0;
    }

    private static long measureStringBuilder(String visibleDigits) {
        long startTime = System.nanoTime();
        int builtLength = 0;
        for (int iteration = 0; iteration < PERFORMANCE_ITERATIONS; iteration++) {
            StringBuilder maskedNumber = new StringBuilder(MASK_BLOCK);
            maskedNumber.append(visibleDigits);
            maskedNumber.insert(MASK_BLOCK.length(), MASK_SEPARATOR);
            builtLength += maskedNumber.toString().length();
        }
        long elapsedTime = System.nanoTime() - startTime;
        return builtLength > 0 ? elapsedTime : 0;
    }

    private static long measureStringBuffer(String visibleDigits) {
        long startTime = System.nanoTime();
        int builtLength = 0;
        for (int iteration = 0; iteration < PERFORMANCE_ITERATIONS; iteration++) {
            StringBuffer maskedNumber = new StringBuffer(MASK_BLOCK);
            maskedNumber.append(visibleDigits);
            maskedNumber.insert(MASK_BLOCK.length(), MASK_SEPARATOR);
            builtLength += maskedNumber.toString().length();
        }
        long elapsedTime = System.nanoTime() - startTime;
        return builtLength > 0 ? elapsedTime : 0;
    }
}

class InvalidPhoneNumberException extends Exception {
    InvalidPhoneNumberException(String message) {
        super(message);
    }
}

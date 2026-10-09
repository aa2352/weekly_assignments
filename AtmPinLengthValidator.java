import java.util.NoSuchElementException;
import java.util.Scanner;

public class AtmPinLengthValidator {

    private static final int REQUIRED_PIN_LENGTH = 4;
    private static final String LENGTH_OK_MESSAGE = "PIN length OK.";
    private static final String LENGTH_INVALID_MESSAGE = "Invalid PIN - must be exactly 4 digits.";
    private static final char ASCII_DIGIT_ZERO = '0';
    private static final char ASCII_DIGIT_NINE = '9';
    private static final String[] DEMO_PINS = { "482", "4820", "48201", "4a20", "   " };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] pinsToCheck = collectPins(inputReader);
        for (int pinIndex = 0; pinIndex < pinsToCheck.length; pinIndex++) {
            reportPinCheck(pinsToCheck[pinIndex]);
        }
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== ATM PIN Length Validator ===");
        System.out.println("\tRule:\texactly " + REQUIRED_PIN_LENGTH + " characters\n");
    }

    private static String[] collectPins(Scanner inputReader) {
        System.out.print("Enter a PIN (press Enter to use the demo list): ");
        try {
            String typedPin = inputReader.nextLine();
            if (typedPin.trim().isEmpty()) {
                System.out.println("[no PIN typed - running the demo list]\n");
                return DEMO_PINS;
            }
            System.out.println();
            return new String[] { typedPin };
        } catch (NoSuchElementException | IllegalStateException consoleUnavailable) {
            System.out.println("\n[no console input - running the demo list]\n");
            return DEMO_PINS;
        }
    }

    private static void reportPinCheck(String pin) {
        System.out.println("PIN: \"" + pin + "\"");
        try {
            checkPinLength(pin);
            reportDigitCheck(pin);
        } catch (NonDigitPinException nonDigitPin) {
            System.out.println("Digit check: " + nonDigitPin.getMessage());
        } catch (IllegalArgumentException missingPin) {
            System.out.println(LENGTH_INVALID_MESSAGE + " (" + missingPin.getMessage() + ")");
        }
        System.out.println();
    }

    private static void checkPinLength(String pin) {
        if (pin == null) {
            throw new IllegalArgumentException("the PIN reference is null");
        }
        if (pin.length() != REQUIRED_PIN_LENGTH) {
            System.out.println(LENGTH_INVALID_MESSAGE + " (length " + pin.length() + ")");
        } else {
            System.out.println(LENGTH_OK_MESSAGE);
        }
    }

    private static void reportDigitCheck(String pin) throws NonDigitPinException {
        for (int charIndex = 0; charIndex < pin.length(); charIndex++) {
            char currentChar = pin.charAt(charIndex);
            if (currentChar < ASCII_DIGIT_ZERO || currentChar > ASCII_DIGIT_NINE) {
                throw new NonDigitPinException("'" + currentChar + "' (ASCII code " + (int) currentChar
                        + ") at position " + (charIndex + 1) + " is not a digit");
            }
        }
        System.out.println("Digit check: every character is a digit");
    }
}

class NonDigitPinException extends Exception {
    NonDigitPinException(String message) {
        super(message);
    }
}

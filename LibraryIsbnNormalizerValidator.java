import java.util.NoSuchElementException;
import java.util.Scanner;

public class LibraryIsbnNormalizerValidator {

    private static final int CODE_LENGTH = 13;
    private static final int PUBLISHER_CODE_LENGTH = 3;
    private static final int YEAR_LENGTH = 4;
    private static final int CATALOG_START = PUBLISHER_CODE_LENGTH + YEAR_LENGTH;
    private static final String[] DEMO_RAW_CODES = {
            "  pen2026004251  ",
            "12N2026004251",
            "PEN202600425",
            "PEN2O26004251",
            "oup2024991233",
            "   "
    };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] rawCodes = collectRawCodes(inputReader);
        for (int codeIndex = 0; codeIndex < rawCodes.length; codeIndex++) {
            processCode(rawCodes[codeIndex]);
        }
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== Library ISBN Normalizer & Validator ===");
        System.out.println("\tLayout:\t3 letters + 4 digits (year) + 6 digits (catalog)");
        System.out.println("\tExample:\t\"PEN2026004251\"\n");
    }

    private static String[] collectRawCodes(Scanner inputReader) {
        System.out.print("Scan a book code (press Enter to use the demo list): ");
        try {
            String typedCode = inputReader.nextLine();
            if (typedCode.trim().isEmpty()) {
                System.out.println("[no code typed - running the demo list]\n");
                return DEMO_RAW_CODES;
            }
            System.out.println();
            return new String[] { typedCode };
        } catch (NoSuchElementException | IllegalStateException consoleUnavailable) {
            System.out.println("\n[no console input - running the demo list]\n");
            return DEMO_RAW_CODES;
        }
    }

    private static void processCode(String rawCode) {
        System.out.println("Raw code  : \"" + rawCode + "\"");
        try {
            String normalizedCode = normalizeCode(rawCode);
            System.out.println("Normalized: \"" + normalizedCode + "\"");
            System.out.println(validateAndFormat(normalizedCode) + "\n");
        } catch (InvalidBookCodeException invalidCode) {
            System.out.println("Invalid: " + invalidCode.getMessage() + "\n");
        } catch (StringIndexOutOfBoundsException shortCode) {
            System.out.println("Invalid: the code is too short to read\n");
        } catch (IllegalArgumentException emptyCode) {
            System.out.println("Invalid: " + emptyCode.getMessage() + "\n");
        }
    }

    private static String normalizeCode(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            throw new IllegalArgumentException("the code is empty");
        }
        String trimmedCode = raw.trim();
        if (trimmedCode.length() < PUBLISHER_CODE_LENGTH) {
            return trimmedCode.toUpperCase();
        }
        return trimmedCode.substring(0, PUBLISHER_CODE_LENGTH).toUpperCase()
                + trimmedCode.substring(PUBLISHER_CODE_LENGTH);
    }

    private static String validateAndFormat(String code) throws InvalidBookCodeException {
        if (code.length() != CODE_LENGTH) {
            throw new InvalidBookCodeException("code must be " + CODE_LENGTH
                    + " characters but is " + code.length());
        }
        validatePublisherCode(code);
        validateBody(code);

        StringBuilder formattedCode = new StringBuilder();
        formattedCode.append("[").append(code.substring(0, PUBLISHER_CODE_LENGTH)).append("]");
        formattedCode.append(" YEAR: ").append(code, PUBLISHER_CODE_LENGTH, CATALOG_START);
        formattedCode.append(" | CATALOG: ").append(code.substring(CATALOG_START));
        return formattedCode.toString();
    }

    private static void validatePublisherCode(String code) throws InvalidBookCodeException {
        for (int charIndex = 0; charIndex < PUBLISHER_CODE_LENGTH; charIndex++) {
            char currentChar = code.charAt(charIndex);
            if (!Character.isLetter(currentChar)) {
                throw new InvalidBookCodeException("publisher code must be 3 letters ('" + currentChar
                        + "' has ASCII code " + (int) currentChar + ")");
            }
        }
    }

    private static void validateBody(String code) throws InvalidBookCodeException {
        for (int charIndex = PUBLISHER_CODE_LENGTH; charIndex < code.length(); charIndex++) {
            char currentChar = code.charAt(charIndex);
            if (!Character.isDigit(currentChar)) {
                throw new InvalidBookCodeException("position " + (charIndex + 1) + " must be a digit but holds '"
                        + currentChar + "' (ASCII code " + (int) currentChar + ")");
            }
        }
    }
}

class InvalidBookCodeException extends Exception {
    InvalidBookCodeException(String message) {
        super(message);
    }
}

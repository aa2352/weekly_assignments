import java.util.NoSuchElementException;
import java.util.Scanner;

public class BankTransactionReferenceValidator {

    private static final int REFERENCE_LENGTH = 14;
    private static final int BANK_CODE_LENGTH = 3;
    private static final int DATE_LENGTH = 6;
    private static final int DAY_START = BANK_CODE_LENGTH;
    private static final int MONTH_START = DAY_START + 2;
    private static final int YEAR_START = MONTH_START + 2;
    private static final int SEQUENCE_START = BANK_CODE_LENGTH + DATE_LENGTH;
    private static final String[] DEMO_RAW_REFERENCES = {
            "  hdf03022600042  ",
            "12F03022600042",
            "HDF0302260004",
            "HDFO3022600042",
            "sbi15082512345",
            "   "
    };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] rawReferences = collectRawReferences(inputReader);
        for (int referenceIndex = 0; referenceIndex < rawReferences.length; referenceIndex++) {
            processReference(rawReferences[referenceIndex]);
        }
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== Bank Transaction Reference Generator & Validator ===");
        System.out.println("\tLayout:\t3 letters + 6 digits (ddMMyy) + 5 digits (sequence)");
        System.out.println("\tExample:\t\"HDF03022600042\"\n");
    }

    private static String[] collectRawReferences(Scanner inputReader) {
        System.out.print("Enter a transaction reference (press Enter to use the demo list): ");
        try {
            String typedReference = inputReader.nextLine();
            if (typedReference.trim().isEmpty()) {
                System.out.println("[no reference typed - running the demo list]\n");
                return DEMO_RAW_REFERENCES;
            }
            System.out.println();
            return new String[] { typedReference };
        } catch (NoSuchElementException | IllegalStateException consoleUnavailable) {
            System.out.println("\n[no console input - running the demo list]\n");
            return DEMO_RAW_REFERENCES;
        }
    }

    private static void processReference(String rawReference) {
        System.out.println("Raw reference: \"" + rawReference + "\"");
        try {
            String normalizedReference = normalizeReference(rawReference);
            System.out.println("Normalized   : \"" + normalizedReference + "\"");
            System.out.println(validateAndFormat(normalizedReference) + "\n");
        } catch (InvalidReferenceException invalidReference) {
            System.out.println("Invalid: " + invalidReference.getMessage() + "\n");
        } catch (StringIndexOutOfBoundsException shortReference) {
            System.out.println("Invalid: the reference is too short to read\n");
        } catch (IllegalArgumentException emptyReference) {
            System.out.println("Invalid: " + emptyReference.getMessage() + "\n");
        }
    }

    private static String normalizeReference(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            throw new IllegalArgumentException("the reference is empty");
        }
        String trimmedReference = raw.trim();
        if (trimmedReference.length() < BANK_CODE_LENGTH) {
            return trimmedReference.toUpperCase();
        }
        return trimmedReference.substring(0, BANK_CODE_LENGTH).toUpperCase()
                + trimmedReference.substring(BANK_CODE_LENGTH);
    }

    private static String validateAndFormat(String reference) throws InvalidReferenceException {
        if (reference.length() != REFERENCE_LENGTH) {
            throw new InvalidReferenceException("reference must be " + REFERENCE_LENGTH
                    + " characters but is " + reference.length());
        }
        validateBankCode(reference);
        validateBody(reference);

        StringBuilder formattedReference = new StringBuilder();
        formattedReference.append("[").append(reference.substring(0, BANK_CODE_LENGTH)).append("]");
        formattedReference.append(" DATE: ").append(reference, DAY_START, MONTH_START);
        formattedReference.append("/").append(reference, MONTH_START, YEAR_START);
        formattedReference.append("/").append(reference, YEAR_START, SEQUENCE_START);
        formattedReference.append(" | SEQ: ").append(reference.substring(SEQUENCE_START));
        return formattedReference.toString();
    }

    private static void validateBankCode(String reference) throws InvalidReferenceException {
        for (int charIndex = 0; charIndex < BANK_CODE_LENGTH; charIndex++) {
            char currentChar = reference.charAt(charIndex);
            if (!Character.isLetter(currentChar)) {
                throw new InvalidReferenceException("bank code must be 3 letters ('" + currentChar
                        + "' has ASCII code " + (int) currentChar + ")");
            }
        }
    }

    private static void validateBody(String reference) throws InvalidReferenceException {
        for (int charIndex = BANK_CODE_LENGTH; charIndex < reference.length(); charIndex++) {
            char currentChar = reference.charAt(charIndex);
            if (!Character.isDigit(currentChar)) {
                throw new InvalidReferenceException("position " + (charIndex + 1) + " must be a digit but holds '"
                        + currentChar + "' (ASCII code " + (int) currentChar + ")");
            }
        }
    }
}

class InvalidReferenceException extends Exception {
    InvalidReferenceException(String message) {
        super(message);
    }
}

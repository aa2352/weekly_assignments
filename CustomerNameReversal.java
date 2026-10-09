import java.util.NoSuchElementException;
import java.util.Scanner;

public class CustomerNameReversal {

    private static final String[] DEMO_CUSTOMER_NAMES = { "Sunil", "Akshay Kumar", "R", "   ", "Priya123" };
    private static final char ASCII_UPPER_A = 'A';
    private static final char ASCII_UPPER_Z = 'Z';
    private static final char ASCII_LOWER_A = 'a';
    private static final char ASCII_LOWER_Z = 'z';

    public static void main(String[] args) {
        System.out.println("=== The Customer Identity Verification System ===\n");

        Scanner inputReader = new Scanner(System.in);
        String[] customerNames = collectCustomerNames(inputReader);
        for (int nameIndex = 0; nameIndex < customerNames.length; nameIndex++) {
            verifyCustomerName(customerNames[nameIndex]);
        }
        inputReader.close();
    }

    private static String[] collectCustomerNames(Scanner inputReader) {
        System.out.print("Enter the customer name (press Enter to use the demo list): ");
        try {
            String typedName = inputReader.nextLine();
            if (typedName.trim().isEmpty()) {
                System.out.println("[no name typed - running the demo list]\n");
                return DEMO_CUSTOMER_NAMES;
            }
            System.out.println();
            return new String[] { typedName };
        } catch (NoSuchElementException | IllegalStateException consoleUnavailable) {
            System.out.println("\n[no console input - running the demo list]\n");
            return DEMO_CUSTOMER_NAMES;
        }
    }

    private static void verifyCustomerName(String customerName) {
        try {
            String reversedName = reverseCustomerName(customerName);
            System.out.println("Original Name: " + customerName);
            System.out.println("Reversed Name: " + reversedName);
            System.out.println("Original Name after reversal: " + customerName);
            System.out.println("Reversal verified by StringBuilder: "
                    + reversedName.equals(reverseUsingStringBuilder(customerName)) + "\n");
        } catch (InvalidCustomerNameException invalidName) {
            System.out.println("Original Name: \"" + customerName + "\"");
            System.out.println("Verification failed: " + invalidName.getMessage() + "\n");
        } catch (IllegalArgumentException missingName) {
            System.out.println("Verification failed: " + missingName.getMessage() + "\n");
        }
    }

    private static String reverseCustomerName(String customerName) throws InvalidCustomerNameException {
        String trimmedName = validateCustomerName(customerName);
        char[] nameCharacters = trimmedName.toCharArray();
        char[] reversedCharacters = new char[nameCharacters.length];
        for (int charIndex = 0; charIndex < nameCharacters.length; charIndex++) {
            reversedCharacters[charIndex] = nameCharacters[nameCharacters.length - 1 - charIndex];
        }
        return new String(reversedCharacters);
    }

    private static String reverseUsingStringBuilder(String customerName) throws InvalidCustomerNameException {
        String trimmedName = validateCustomerName(customerName);
        return new StringBuilder(trimmedName).reverse().toString();
    }

    private static String validateCustomerName(String customerName) throws InvalidCustomerNameException {
        if (customerName == null) {
            throw new IllegalArgumentException("the customer name reference is null");
        }
        String trimmedName = customerName.trim();
        if (trimmedName.isEmpty()) {
            throw new InvalidCustomerNameException("the customer name is blank");
        }
        for (int charIndex = 0; charIndex < trimmedName.length(); charIndex++) {
            char currentChar = trimmedName.charAt(charIndex);
            if (!isNameCharacter(currentChar)) {
                throw new InvalidCustomerNameException("'" + currentChar + "' (ASCII code "
                        + (int) currentChar + ") is not allowed in a customer name");
            }
        }
        return trimmedName;
    }

    private static boolean isNameCharacter(char candidate) {
        boolean isUpperCaseLetter = candidate >= ASCII_UPPER_A && candidate <= ASCII_UPPER_Z;
        boolean isLowerCaseLetter = candidate >= ASCII_LOWER_A && candidate <= ASCII_LOWER_Z;
        boolean isSpace = candidate == ' ';
        return isUpperCaseLetter || isLowerCaseLetter || isSpace;
    }
}

class InvalidCustomerNameException extends Exception {
    InvalidCustomerNameException(String message) {
        super(message);
    }
}

import java.util.NoSuchElementException;
import java.util.Scanner;

public class FileExtensionValidator {

    private static final String[] ACCEPTED_EXTENSIONS = { "pdf", "docx", "zip" };
    private static final char EXTENSION_MARKER = '.';
    private static final String RESULT_ACCEPTED = "Accepted";
    private static final String RESULT_REJECTED = "Rejected - invalid file type";
    private static final String[] DEMO_FILENAMES = {
            "Assignment1.PDF", "notes.txt", "report.docx", "archive.ZIP", "readme", "backup.", "   " };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] filenames = collectFilenames(inputReader);
        for (int fileIndex = 0; fileIndex < filenames.length; fileIndex++) {
            reportValidation(filenames[fileIndex]);
        }
        demonstrateStringComparisons();
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== File Extension Validator ===");
        System.out.print("\tAccepted types:\t");
        for (int extensionIndex = 0; extensionIndex < ACCEPTED_EXTENSIONS.length; extensionIndex++) {
            System.out.print("\"" + ACCEPTED_EXTENSIONS[extensionIndex] + "\" ");
        }
        System.out.println("\n\tUpload folder:\tC:\\portal\\uploads\n");
    }

    private static String[] collectFilenames(Scanner inputReader) {
        System.out.print("Enter a filename (press Enter to use the demo list): ");
        try {
            String typedFilename = inputReader.nextLine();
            if (typedFilename.trim().isEmpty()) {
                System.out.println("[no filename typed - running the demo list]\n");
                return DEMO_FILENAMES;
            }
            System.out.println();
            return new String[] { typedFilename };
        } catch (NoSuchElementException | IllegalStateException consoleUnavailable) {
            System.out.println("\n[no console input - running the demo list]\n");
            return DEMO_FILENAMES;
        }
    }

    private static void reportValidation(String filename) {
        System.out.println("Filename: \"" + filename + "\"");
        try {
            System.out.println(validateFileExtension(filename) + "\n");
        } catch (MissingExtensionException missingExtension) {
            System.out.println(RESULT_REJECTED + " (" + missingExtension.getMessage() + ")\n");
        } catch (StringIndexOutOfBoundsException brokenFilename) {
            System.out.println(RESULT_REJECTED + " (the extension could not be read)\n");
        } catch (IllegalArgumentException emptyFilename) {
            System.out.println(RESULT_REJECTED + " (" + emptyFilename.getMessage() + ")\n");
        }
    }

    private static String validateFileExtension(String filename) throws MissingExtensionException {
        String extension = extractExtension(filename);
        for (int extensionIndex = 0; extensionIndex < ACCEPTED_EXTENSIONS.length; extensionIndex++) {
            if (ACCEPTED_EXTENSIONS[extensionIndex].equalsIgnoreCase(extension)) {
                return RESULT_ACCEPTED + " (extension \"" + extension.toLowerCase() + "\")";
            }
        }
        return RESULT_REJECTED + " (extension \"" + extension.toLowerCase() + "\")";
    }

    private static String extractExtension(String filename) throws MissingExtensionException {
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("the filename is empty");
        }
        String trimmedFilename = filename.trim();
        int lastDotPosition = trimmedFilename.lastIndexOf(EXTENSION_MARKER);
        if (lastDotPosition < 0) {
            throw new MissingExtensionException("no '" + EXTENSION_MARKER + "' found in the filename");
        }
        String extension = trimmedFilename.substring(lastDotPosition + 1);
        if (extension.isEmpty()) {
            throw new MissingExtensionException("nothing follows the '" + EXTENSION_MARKER + "'");
        }
        return extension;
    }

    private static void demonstrateStringComparisons() {
        String acceptedExtension = ACCEPTED_EXTENSIONS[0];
        String typedExtension = new String("PDF");
        String lowerCasedExtension = typedExtension.toLowerCase();

        System.out.println("String comparison methods on \"" + acceptedExtension
                + "\" and \"" + typedExtension + "\":");
        System.out.println("\tequals()          : " + acceptedExtension.equals(typedExtension));
        System.out.println("\tequalsIgnoreCase(): " + acceptedExtension.equalsIgnoreCase(typedExtension));
        System.out.println("\tcompareTo()       : " + acceptedExtension.compareTo(typedExtension));
        System.out.println("\treference ==      : " + (acceptedExtension == lowerCasedExtension));
        System.out.println("\tequals() on lower : " + acceptedExtension.equals(lowerCasedExtension));
    }
}

class MissingExtensionException extends Exception {
    MissingExtensionException(String message) {
        super(message);
    }
}

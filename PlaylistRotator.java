import java.util.NoSuchElementException;
import java.util.Scanner;

public class PlaylistRotator {

    private static final String VALUE_SEPARATOR = ",";
    private static final String[] DEMO_PLAYLIST_LINES = { "1,2,3,4,5,6,7", "1,2", "10,20,30", "", "4,5,6" };
    private static final int[] DEMO_ROTATIONS = { 3, 3, 3, 2, -1 };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] playlistLines = DEMO_PLAYLIST_LINES;
        int[] rotations = DEMO_ROTATIONS;

        String typedPlaylist = readLine(inputReader, "Enter the playlist (press Enter to use the demo list): ");
        if (!typedPlaylist.trim().isEmpty()) {
            String typedRotation = readLine(inputReader, "Enter how many positions to rotate right: ");
            playlistLines = new String[] { typedPlaylist };
            rotations = new int[] { readRotation(typedRotation) };
        }
        System.out.println();

        for (int caseIndex = 0; caseIndex < playlistLines.length; caseIndex++) {
            reportRotation(playlistLines[caseIndex], rotations[caseIndex]);
        }
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== Playlist Rotator ===");
        System.out.println("\tInput format:\t\"1,2,3,4,5,6,7\" with k = 3");
        System.out.println("\tRule:\tnewArray[(i + k) % length] = nums[i]\n");
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

    private static int readRotation(String typedRotation) {
        try {
            return Integer.parseInt(typedRotation.trim());
        } catch (NumberFormatException nonNumericRotation) {
            System.out.println("[the rotation count is not a whole number - using 0]");
            return 0;
        }
    }

    private static void reportRotation(String playlistLine, int rotationCount) {
        System.out.println("nums = [" + playlistLine + "], k = " + rotationCount);
        try {
            int[] playlist = parseIntArray(playlistLine);
            int[] rotatedPlaylist = rotateArray(playlist, rotationCount);

            System.out.println(formatArray(rotatedPlaylist)
                    + " (k % length = " + rotationCount + " % " + playlist.length
                    + " = " + rotationCount % playlist.length + ")");
            System.out.println("Original array after rotateArray(): " + formatArray(playlist));

            rotateInPlace(playlist, rotationCount);
            System.out.println("Original array after rotateInPlace(): " + formatArray(playlist) + "\n");
        } catch (InvalidRotationException invalidRotation) {
            System.out.println("Rotation stopped: " + invalidRotation.getMessage() + "\n");
        } catch (ArithmeticException emptyPlaylist) {
            System.out.println("Rotation stopped: an empty playlist cannot be rotated\n");
        } catch (NumberFormatException nonNumericTrack) {
            System.out.println("Rotation stopped: every track number must be a whole number\n");
        } catch (IllegalArgumentException missingPlaylist) {
            System.out.println("Rotation stopped: " + missingPlaylist.getMessage() + "\n");
        }
    }

    private static int[] rotateArray(int[] nums, int k) throws InvalidRotationException {
        if (k < 0) {
            throw new InvalidRotationException("the rotation count " + k + " cannot be negative");
        }
        int shift = k % nums.length;
        int[] rotatedArray = new int[nums.length];
        for (int trackIndex = 0; trackIndex < nums.length; trackIndex++) {
            rotatedArray[(trackIndex + shift) % nums.length] = nums[trackIndex];
        }
        return rotatedArray;
    }

    private static void rotateInPlace(int[] nums, int k) throws InvalidRotationException {
        int[] rotatedArray = rotateArray(nums, k);
        for (int trackIndex = 0; trackIndex < nums.length; trackIndex++) {
            nums[trackIndex] = rotatedArray[trackIndex];
        }
    }

    private static int[] parseIntArray(String valueLine) {
        if (valueLine == null) {
            throw new IllegalArgumentException("the playlist reference is null");
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

class InvalidRotationException extends Exception {
    InvalidRotationException(String message) {
        super(message);
    }
}

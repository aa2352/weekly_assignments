import java.util.NoSuchElementException;
import java.util.Scanner;

public class ZeroSumTransactionFinder {

    private static final String VALUE_SEPARATOR = ",";
    private static final int TRIPLET_SIZE = 3;
    private static final int INITIAL_TRIPLET_CAPACITY = 4;
    private static final int TARGET_SUM = 0;
    private static final String[] DEMO_TRANSACTION_LINES = {
            "-1,0,1,2,-1,-4", "0,0,0", "-2,0,1,1,2", "1,2,3", "5,5", "1,2,w" };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] transactionLines = collectTransactionLines(inputReader);
        for (int caseIndex = 0; caseIndex < transactionLines.length; caseIndex++) {
            reportZeroSumTriplets(transactionLines[caseIndex]);
        }
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== Zero Sum Transaction Finder ===");
        System.out.println("\tInput format:\t\"-1,0,1,2,-1,-4\"");
        System.out.println("\tRule:\tsort a copy, then two pointers inward, skipping duplicates\n");
    }

    private static String[] collectTransactionLines(Scanner inputReader) {
        System.out.print("Enter the transaction amounts (press Enter to use the demo list): ");
        try {
            String typedAmounts = inputReader.nextLine();
            if (typedAmounts.trim().isEmpty()) {
                System.out.println("[no amounts typed - running the demo list]\n");
                return DEMO_TRANSACTION_LINES;
            }
            System.out.println();
            return new String[] { typedAmounts };
        } catch (NoSuchElementException | IllegalStateException consoleUnavailable) {
            System.out.println("\n[no console input - running the demo list]\n");
            return DEMO_TRANSACTION_LINES;
        }
    }

    private static void reportZeroSumTriplets(String transactionLine) {
        System.out.println("nums = [" + transactionLine + "]");
        try {
            int[] amounts = parseIntArray(transactionLine);
            int[][] triplets = threeSum(amounts);

            System.out.println(formatTriplets(triplets));
            System.out.println("Transactions scanned: " + amounts.length
                    + " | array after threeSum(): " + formatArray(amounts));

            sortAscending(amounts);
            System.out.println("Array after sortAscending(): " + formatArray(amounts) + "\n");
        } catch (NoZeroSumTripletException noTripletFound) {
            System.out.println("[] (" + noTripletFound.getMessage() + ")\n");
        } catch (InsufficientTransactionException insufficientTransactions) {
            System.out.println("No result: " + insufficientTransactions.getMessage() + "\n");
        } catch (NumberFormatException nonNumericAmount) {
            System.out.println("No result: every amount must be a whole number\n");
        } catch (IllegalArgumentException emptyHistory) {
            System.out.println("No result: " + emptyHistory.getMessage() + "\n");
        }
    }

    private static int[][] threeSum(int[] nums)
            throws InsufficientTransactionException, NoZeroSumTripletException {
        if (nums.length < TRIPLET_SIZE) {
            throw new InsufficientTransactionException("at least " + TRIPLET_SIZE
                    + " transactions are needed but the history holds " + nums.length);
        }

        int[] sortedAmounts = copyArray(nums);
        sortAscending(sortedAmounts);

        int[][] triplets = new int[INITIAL_TRIPLET_CAPACITY][];
        int tripletCount = 0;

        for (int firstIndex = 0; firstIndex < sortedAmounts.length - 2; firstIndex++) {
            if (firstIndex > 0 && sortedAmounts[firstIndex] == sortedAmounts[firstIndex - 1]) {
                continue;
            }
            int leftIndex = firstIndex + 1;
            int rightIndex = sortedAmounts.length - 1;

            while (leftIndex < rightIndex) {
                int currentSum = sortedAmounts[firstIndex] + sortedAmounts[leftIndex] + sortedAmounts[rightIndex];
                if (currentSum < TARGET_SUM) {
                    leftIndex++;
                } else if (currentSum > TARGET_SUM) {
                    rightIndex--;
                } else {
                    triplets = storeTriplet(triplets, tripletCount, new int[] {
                            sortedAmounts[firstIndex], sortedAmounts[leftIndex], sortedAmounts[rightIndex] });
                    tripletCount++;

                    while (leftIndex < rightIndex && sortedAmounts[leftIndex] == sortedAmounts[leftIndex + 1]) {
                        leftIndex++;
                    }
                    while (leftIndex < rightIndex && sortedAmounts[rightIndex] == sortedAmounts[rightIndex - 1]) {
                        rightIndex--;
                    }
                    leftIndex++;
                    rightIndex--;
                }
            }
        }

        if (tripletCount == 0) {
            throw new NoZeroSumTripletException("no three amounts add up to " + TARGET_SUM);
        }
        return trimTriplets(triplets, tripletCount);
    }

    private static int[][] storeTriplet(int[][] triplets, int tripletCount, int[] triplet) {
        int[][] targetArray = triplets;
        if (tripletCount == targetArray.length) {
            int[][] grownArray = new int[targetArray.length * 2][];
            for (int tripletIndex = 0; tripletIndex < targetArray.length; tripletIndex++) {
                grownArray[tripletIndex] = targetArray[tripletIndex];
            }
            targetArray = grownArray;
        }
        targetArray[tripletCount] = triplet;
        return targetArray;
    }

    private static int[][] trimTriplets(int[][] triplets, int tripletCount) {
        int[][] trimmedTriplets = new int[tripletCount][];
        for (int tripletIndex = 0; tripletIndex < tripletCount; tripletIndex++) {
            trimmedTriplets[tripletIndex] = triplets[tripletIndex];
        }
        return trimmedTriplets;
    }

    private static void sortAscending(int[] values) {
        for (int sortIndex = 1; sortIndex < values.length; sortIndex++) {
            int valueToPlace = values[sortIndex];
            int compareIndex = sortIndex - 1;
            while (compareIndex >= 0 && values[compareIndex] > valueToPlace) {
                values[compareIndex + 1] = values[compareIndex];
                compareIndex--;
            }
            values[compareIndex + 1] = valueToPlace;
        }
    }

    private static int[] copyArray(int[] values) {
        int[] copiedValues = new int[values.length];
        for (int valueIndex = 0; valueIndex < values.length; valueIndex++) {
            copiedValues[valueIndex] = values[valueIndex];
        }
        return copiedValues;
    }

    private static int[] parseIntArray(String valueLine) {
        if (valueLine == null || valueLine.trim().isEmpty()) {
            throw new IllegalArgumentException("the transaction history is empty");
        }
        String[] valueFields = valueLine.split(VALUE_SEPARATOR);
        int[] values = new int[valueFields.length];
        for (int fieldIndex = 0; fieldIndex < valueFields.length; fieldIndex++) {
            values[fieldIndex] = Integer.parseInt(valueFields[fieldIndex].trim());
        }
        return values;
    }

    private static String formatTriplets(int[][] triplets) {
        StringBuilder formattedTriplets = new StringBuilder("[");
        for (int tripletIndex = 0; tripletIndex < triplets.length; tripletIndex++) {
            if (tripletIndex > 0) {
                formattedTriplets.append(", ");
            }
            formattedTriplets.append(formatArray(triplets[tripletIndex]));
        }
        return formattedTriplets.append("]").toString();
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

class InsufficientTransactionException extends Exception {
    InsufficientTransactionException(String message) {
        super(message);
    }
}

class NoZeroSumTripletException extends Exception {
    NoZeroSumTripletException(String message) {
        super(message);
    }
}

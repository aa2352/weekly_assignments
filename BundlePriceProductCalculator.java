import java.util.NoSuchElementException;
import java.util.Scanner;

public class BundlePriceProductCalculator {

    private static final String VALUE_SEPARATOR = ",";
    private static final int MINIMUM_PRODUCT_COUNT = 2;
    private static final int NEUTRAL_PRODUCT = 1;
    private static final String[] DEMO_BUNDLE_LINES = { "1,2,3,4", "-1,1,0,-3,3", "2,0,0,5", "7", "3,4,z" };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] bundleLines = collectBundleLines(inputReader);
        for (int caseIndex = 0; caseIndex < bundleLines.length; caseIndex++) {
            reportProductExceptSelf(bundleLines[caseIndex]);
        }
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== Bundle Price Product Calculator ===");
        System.out.println("\tInput format:\t\"1,2,3,4\"");
        System.out.println("\tRule:\tno division anywhere - one forward pass and one backward pass\n");
    }

    private static String[] collectBundleLines(Scanner inputReader) {
        System.out.print("Enter the bundle prices (press Enter to use the demo list): ");
        try {
            String typedPrices = inputReader.nextLine();
            if (typedPrices.trim().isEmpty()) {
                System.out.println("[no prices typed - running the demo list]\n");
                return DEMO_BUNDLE_LINES;
            }
            System.out.println();
            return new String[] { typedPrices };
        } catch (NoSuchElementException | IllegalStateException consoleUnavailable) {
            System.out.println("\n[no console input - running the demo list]\n");
            return DEMO_BUNDLE_LINES;
        }
    }

    private static void reportProductExceptSelf(String bundleLine) {
        System.out.println("nums = [" + bundleLine + "]");
        try {
            int[] prices = parseIntArray(bundleLine);
            int[] answer = productExceptSelf(prices);

            System.out.println(formatArray(answer));
            System.out.println("Bundle size: " + prices.length
                    + " | input unchanged: " + formatArray(prices) + "\n");
        } catch (InsufficientBundleException insufficientBundle) {
            System.out.println("No result: " + insufficientBundle.getMessage() + "\n");
        } catch (NumberFormatException nonNumericPrice) {
            System.out.println("No result: every price must be a whole number\n");
        } catch (IllegalArgumentException emptyBundle) {
            System.out.println("No result: " + emptyBundle.getMessage() + "\n");
        }
    }

    private static int[] productExceptSelf(int[] nums) throws InsufficientBundleException {
        if (nums.length < MINIMUM_PRODUCT_COUNT) {
            throw new InsufficientBundleException("at least " + MINIMUM_PRODUCT_COUNT
                    + " products are needed but the bundle holds " + nums.length);
        }

        int[] answer = new int[nums.length];
        int runningLeftProduct = NEUTRAL_PRODUCT;
        for (int priceIndex = 0; priceIndex < nums.length; priceIndex++) {
            answer[priceIndex] = runningLeftProduct;
            runningLeftProduct *= nums[priceIndex];
        }

        int runningRightProduct = NEUTRAL_PRODUCT;
        for (int priceIndex = nums.length - 1; priceIndex >= 0; priceIndex--) {
            answer[priceIndex] *= runningRightProduct;
            runningRightProduct *= nums[priceIndex];
        }
        return answer;
    }

    private static int[] parseIntArray(String valueLine) {
        if (valueLine == null || valueLine.trim().isEmpty()) {
            throw new IllegalArgumentException("the bundle is empty");
        }
        String[] valueFields = valueLine.split(VALUE_SEPARATOR);
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

class InsufficientBundleException extends Exception {
    InsufficientBundleException(String message) {
        super(message);
    }
}

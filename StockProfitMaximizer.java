import java.util.NoSuchElementException;
import java.util.Scanner;

public class StockProfitMaximizer {

    private static final String VALUE_SEPARATOR = ",";
    private static final int MINIMUM_DAY_COUNT = 2;
    private static final int NO_PROFIT = 0;
    private static final String[] DEMO_PRICE_LINES = { "7,1,5,3,6,4", "7,6,4,3,1", "2,4,1,9", "5", "1,2,x" };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] priceLines = collectPriceLines(inputReader);
        for (int caseIndex = 0; caseIndex < priceLines.length; caseIndex++) {
            reportBestTrade(priceLines[caseIndex]);
        }
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== Stock Profit Maximizer ===");
        System.out.println("\tInput format:\t\"7,1,5,3,6,4\"");
        System.out.println("\tRule:\tbuy first, sell later, one pass over the array\n");
    }

    private static String[] collectPriceLines(Scanner inputReader) {
        System.out.print("Enter the daily prices (press Enter to use the demo list): ");
        try {
            String typedPrices = inputReader.nextLine();
            if (typedPrices.trim().isEmpty()) {
                System.out.println("[no prices typed - running the demo list]\n");
                return DEMO_PRICE_LINES;
            }
            System.out.println();
            return new String[] { typedPrices };
        } catch (NoSuchElementException | IllegalStateException consoleUnavailable) {
            System.out.println("\n[no console input - running the demo list]\n");
            return DEMO_PRICE_LINES;
        }
    }

    private static void reportBestTrade(String priceLine) {
        System.out.println("prices = [" + priceLine + "]");
        try {
            int[] prices = parseIntArray(priceLine);
            int bestProfit = maxProfit(prices);

            if (bestProfit == NO_PROFIT) {
                System.out.println(NO_PROFIT + " (prices only fall, so no trade is profitable)");
            } else {
                int[] tradeDays = findBestTradeDays(prices);
                int buyDay = tradeDays[0];
                int sellDay = tradeDays[1];
                System.out.println(bestProfit + " (buy on day " + buyDay + " at price " + prices[buyDay - 1]
                        + ", sell on day " + sellDay + " at price " + prices[sellDay - 1] + ")");
            }
            System.out.println("Days recorded: " + prices.length + "\n");
        } catch (InsufficientPriceDataException insufficientData) {
            System.out.println("No result: " + insufficientData.getMessage() + "\n");
        } catch (NumberFormatException nonNumericPrice) {
            System.out.println("No result: every price must be a whole number\n");
        } catch (IllegalArgumentException emptySeries) {
            System.out.println("No result: " + emptySeries.getMessage() + "\n");
        }
    }

    private static int maxProfit(int[] prices) throws InsufficientPriceDataException {
        validatePriceCount(prices);
        int lowestPriceSoFar = prices[0];
        int bestProfit = NO_PROFIT;

        for (int dayIndex = 1; dayIndex < prices.length; dayIndex++) {
            int profitIfSoldToday = prices[dayIndex] - lowestPriceSoFar;
            if (profitIfSoldToday > bestProfit) {
                bestProfit = profitIfSoldToday;
            }
            if (prices[dayIndex] < lowestPriceSoFar) {
                lowestPriceSoFar = prices[dayIndex];
            }
        }
        return bestProfit;
    }

    private static int[] findBestTradeDays(int[] prices) throws InsufficientPriceDataException {
        validatePriceCount(prices);
        int lowestPriceIndex = 0;
        int bestProfit = NO_PROFIT;
        int buyIndex = 0;
        int sellIndex = 0;

        for (int dayIndex = 1; dayIndex < prices.length; dayIndex++) {
            int profitIfSoldToday = prices[dayIndex] - prices[lowestPriceIndex];
            if (profitIfSoldToday > bestProfit) {
                bestProfit = profitIfSoldToday;
                buyIndex = lowestPriceIndex;
                sellIndex = dayIndex;
            }
            if (prices[dayIndex] < prices[lowestPriceIndex]) {
                lowestPriceIndex = dayIndex;
            }
        }
        return new int[] { buyIndex + 1, sellIndex + 1 };
    }

    private static void validatePriceCount(int[] prices) throws InsufficientPriceDataException {
        if (prices.length < MINIMUM_DAY_COUNT) {
            throw new InsufficientPriceDataException("at least " + MINIMUM_DAY_COUNT
                    + " days are needed but the array holds " + prices.length);
        }
    }

    private static int[] parseIntArray(String valueLine) {
        if (valueLine == null || valueLine.trim().isEmpty()) {
            throw new IllegalArgumentException("the price series is empty");
        }
        String[] valueFields = valueLine.split(VALUE_SEPARATOR);
        int[] values = new int[valueFields.length];
        for (int fieldIndex = 0; fieldIndex < valueFields.length; fieldIndex++) {
            values[fieldIndex] = Integer.parseInt(valueFields[fieldIndex].trim());
        }
        return values;
    }
}

class InsufficientPriceDataException extends Exception {
    InsufficientPriceDataException(String message) {
        super(message);
    }
}

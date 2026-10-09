import java.util.Scanner;

public class ElectricityConnectionBilling {

    private static final String FIELD_SEPARATOR = "\\s+";
    private static final int TYPE_INDEX = 0;
    private static final int UNITS_INDEX = 1;
    private static final String[] DEMO_CONNECTION_LINES = { "HOME 150", "SHOP 90", "FACTORY 120" };

    public static void main(String[] args) {
        String[] connectionLines = readConnectionLines();

        Connection[] connections = new Connection[connectionLines.length];
        for (int connectionIndex = 0; connectionIndex < connectionLines.length; connectionIndex++) {
            connections[connectionIndex] = createConnection(connectionLines[connectionIndex]);
        }

        double totalBilled = 0.0;
        for (int connectionIndex = 0; connectionIndex < connections.length; connectionIndex++) {
            double bill = connections[connectionIndex].calculateBill();
            System.out.printf("%s: %.2f%n", connections[connectionIndex].getTypeLabel(), bill);
            totalBilled = totalBilled + bill;
        }
        System.out.printf("Total: %.2f%n", totalBilled);
    }

    private static String[] readConnectionLines() {
        Scanner inputReader = new Scanner(System.in);
        String countLine = nextFilledLine(inputReader);
        if (countLine.isEmpty()) {
            inputReader.close();
            return DEMO_CONNECTION_LINES;
        }
        int connectionCount = Integer.parseInt(countLine);
        String[] connectionLines = new String[connectionCount];
        for (int connectionIndex = 0; connectionIndex < connectionCount; connectionIndex++) {
            connectionLines[connectionIndex] = nextFilledLine(inputReader);
        }
        inputReader.close();
        return connectionLines;
    }

    private static String nextFilledLine(Scanner inputReader) {
        while (inputReader.hasNextLine()) {
            String typedLine = inputReader.nextLine().trim();
            if (!typedLine.isEmpty()) {
                return typedLine;
            }
        }
        return "";
    }

    private static Connection createConnection(String connectionLine) {
        String[] connectionFields = connectionLine.split(FIELD_SEPARATOR);
        String connectionType = connectionFields[TYPE_INDEX].toUpperCase();
        double unitsUsed = Double.parseDouble(connectionFields[UNITS_INDEX]);

        switch (connectionType) {
            case "HOME":
                return new HomeConnection(unitsUsed);
            case "SHOP":
                return new ShopConnection(unitsUsed);
            case "FACTORY":
                return new FactoryConnection(unitsUsed);
            default:
                throw new IllegalArgumentException("unknown connection type: " + connectionType);
        }
    }
}

abstract class Connection {

    private final String typeLabel;
    protected final double unitsUsed;

    Connection(String typeLabel, double unitsUsed) {
        this.typeLabel = typeLabel;
        this.unitsUsed = unitsUsed;
    }

    abstract double calculateBill();

    String getTypeLabel() {
        return this.typeLabel;
    }
}

class HomeConnection extends Connection {

    private static final double LOWER_SLAB_LIMIT = 100.0;
    private static final double LOWER_SLAB_RATE = 5.0;
    private static final double UPPER_SLAB_RATE = 7.0;

    HomeConnection(double unitsUsed) {
        super("HOME", unitsUsed);
    }

    @Override
    double calculateBill() {
        double lowerSlabUnits = Math.min(this.unitsUsed, LOWER_SLAB_LIMIT);
        double upperSlabUnits = Math.max(this.unitsUsed - LOWER_SLAB_LIMIT, 0.0);
        return (lowerSlabUnits * LOWER_SLAB_RATE) + (upperSlabUnits * UPPER_SLAB_RATE);
    }
}

class ShopConnection extends Connection {

    private static final double RATE_PER_UNIT = 8.0;
    private static final double FIXED_CHARGE = 100.0;

    ShopConnection(double unitsUsed) {
        super("SHOP", unitsUsed);
    }

    @Override
    double calculateBill() {
        return (RATE_PER_UNIT * this.unitsUsed) + FIXED_CHARGE;
    }
}

class FactoryConnection extends Connection {

    private static final double RATE_PER_UNIT = 6.0;
    private static final double MINIMUM_BILL = 1000.0;

    FactoryConnection(double unitsUsed) {
        super("FACTORY", unitsUsed);
    }

    @Override
    double calculateBill() {
        return Math.max(RATE_PER_UNIT * this.unitsUsed, MINIMUM_BILL);
    }
}

import java.util.Scanner;

public class DeliveryFeeCalculator {

    private static final String FIELD_SEPARATOR = "\\s+";
    private static final int TYPE_INDEX = 0;
    private static final int WEIGHT_INDEX = 1;
    private static final int DISTANCE_INDEX = 2;
    private static final int CUSTOMS_FEE_INDEX = 3;
    private static final String[] DEMO_DELIVERY_LINES = {
            "STANDARD 10 50",
            "EXPRESS 5 20",
            "INTERNATIONAL 20 100 30"
    };

    public static void main(String[] args) {
        String[] deliveryLines = readDeliveryLines();

        Delivery[] deliveries = new Delivery[deliveryLines.length];
        for (int deliveryIndex = 0; deliveryIndex < deliveryLines.length; deliveryIndex++) {
            deliveries[deliveryIndex] = createDelivery(deliveryLines[deliveryIndex]);
        }

        double grandTotalFee = 0.0;
        for (int deliveryIndex = 0; deliveryIndex < deliveries.length; deliveryIndex++) {
            double calculatedFee = deliveries[deliveryIndex].calculateFee();
            System.out.printf("%s: %.2f%n", deliveries[deliveryIndex].getTypeLabel(), calculatedFee);
            grandTotalFee = grandTotalFee + calculatedFee;
        }
        System.out.printf("Total: %.2f%n", grandTotalFee);
    }

    private static String[] readDeliveryLines() {
        Scanner inputReader = new Scanner(System.in);
        String countLine = nextFilledLine(inputReader);
        if (countLine.isEmpty()) {
            inputReader.close();
            return DEMO_DELIVERY_LINES;
        }
        int deliveryCount = Integer.parseInt(countLine);
        String[] deliveryLines = new String[deliveryCount];
        for (int deliveryIndex = 0; deliveryIndex < deliveryCount; deliveryIndex++) {
            deliveryLines[deliveryIndex] = nextFilledLine(inputReader);
        }
        inputReader.close();
        return deliveryLines;
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

    private static Delivery createDelivery(String deliveryLine) {
        String[] deliveryFields = deliveryLine.split(FIELD_SEPARATOR);
        String deliveryType = deliveryFields[TYPE_INDEX].toUpperCase();
        double weight = Double.parseDouble(deliveryFields[WEIGHT_INDEX]);
        double distance = Double.parseDouble(deliveryFields[DISTANCE_INDEX]);

        switch (deliveryType) {
            case "STANDARD":
                return new StandardDelivery(weight, distance);
            case "EXPRESS":
                return new ExpressDelivery(weight, distance);
            case "INTERNATIONAL":
                return new InternationalDelivery(weight, distance,
                        Double.parseDouble(deliveryFields[CUSTOMS_FEE_INDEX]));
            default:
                throw new IllegalArgumentException("unknown delivery type: " + deliveryType);
        }
    }
}

abstract class Delivery {

    private final String typeLabel;
    protected final double weight;
    protected final double distance;

    Delivery(String typeLabel, double weight, double distance) {
        this.typeLabel = typeLabel;
        this.weight = weight;
        this.distance = distance;
    }

    abstract double getBaseFee();

    abstract double getRatePerKg();

    abstract double getRatePerKm();

    double calculateFee() {
        return getBaseFee() + (getRatePerKg() * this.weight) + (getRatePerKm() * this.distance);
    }

    String getTypeLabel() {
        return this.typeLabel;
    }
}

class StandardDelivery extends Delivery {

    StandardDelivery(double weight, double distance) {
        super("STANDARD", weight, distance);
    }

    @Override
    double getBaseFee() {
        return 5.0;
    }

    @Override
    double getRatePerKg() {
        return 0.50;
    }

    @Override
    double getRatePerKm() {
        return 0.10;
    }
}

class ExpressDelivery extends Delivery {

    ExpressDelivery(double weight, double distance) {
        super("EXPRESS", weight, distance);
    }

    @Override
    double getBaseFee() {
        return 15.0;
    }

    @Override
    double getRatePerKg() {
        return 1.00;
    }

    @Override
    double getRatePerKm() {
        return 0.20;
    }
}

class InternationalDelivery extends Delivery {

    private final double customsFee;

    InternationalDelivery(double weight, double distance, double customsFee) {
        super("INTERNATIONAL", weight, distance);
        this.customsFee = customsFee;
    }

    @Override
    double getBaseFee() {
        return 25.0;
    }

    @Override
    double getRatePerKg() {
        return 2.00;
    }

    @Override
    double getRatePerKm() {
        return 0.50;
    }

    @Override
    double calculateFee() {
        return super.calculateFee() + this.customsFee;
    }
}

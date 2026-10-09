import java.util.Scanner;

public class PublicTransportFareCalculator {

    private static final String FIELD_SEPARATOR = "\\s+";
    private static final int TYPE_INDEX = 0;
    private static final int DISTANCE_INDEX = 1;
    private static final int PEAK_HOUR_FACTOR_INDEX = 2;
    private static final String[] DEMO_JOURNEY_LINES = { "BUS 15", "TRAIN 50", "METRO 10 1.5" };

    public static void main(String[] args) {
        String[] journeyLines = readJourneyLines();

        Journey[] journeys = new Journey[journeyLines.length];
        for (int journeyIndex = 0; journeyIndex < journeyLines.length; journeyIndex++) {
            journeys[journeyIndex] = createJourney(journeyLines[journeyIndex]);
        }

        double grandTotalFare = 0.0;
        for (int journeyIndex = 0; journeyIndex < journeys.length; journeyIndex++) {
            double calculatedFare = journeys[journeyIndex].calculateFare();
            System.out.printf("%s: %.2f%n", journeys[journeyIndex].getTypeLabel(), calculatedFare);
            grandTotalFare = grandTotalFare + calculatedFare;
        }
        System.out.printf("Total: %.2f%n", grandTotalFare);
    }

    private static String[] readJourneyLines() {
        Scanner inputReader = new Scanner(System.in);
        String countLine = nextFilledLine(inputReader);
        if (countLine.isEmpty()) {
            inputReader.close();
            return DEMO_JOURNEY_LINES;
        }
        int journeyCount = Integer.parseInt(countLine);
        String[] journeyLines = new String[journeyCount];
        for (int journeyIndex = 0; journeyIndex < journeyCount; journeyIndex++) {
            journeyLines[journeyIndex] = nextFilledLine(inputReader);
        }
        inputReader.close();
        return journeyLines;
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

    private static Journey createJourney(String journeyLine) {
        String[] journeyFields = journeyLine.split(FIELD_SEPARATOR);
        String transportType = journeyFields[TYPE_INDEX].toUpperCase();
        double distance = Double.parseDouble(journeyFields[DISTANCE_INDEX]);

        switch (transportType) {
            case "BUS":
                return new BusJourney(distance);
            case "TRAIN":
                return new TrainJourney(distance);
            case "METRO":
                return new MetroJourney(distance, Double.parseDouble(journeyFields[PEAK_HOUR_FACTOR_INDEX]));
            default:
                throw new IllegalArgumentException("unknown transport type: " + transportType);
        }
    }
}

abstract class Journey {

    private final String typeLabel;
    protected final double distance;

    Journey(String typeLabel, double distance) {
        this.typeLabel = typeLabel;
        this.distance = distance;
    }

    abstract double calculateFare();

    String getTypeLabel() {
        return this.typeLabel;
    }
}

class BusJourney extends Journey {

    private static final double BASE_FARE = 2.0;
    private static final double RATE_PER_KM = 0.10;
    private static final double MAXIMUM_FARE = 10.0;

    BusJourney(double distance) {
        super("BUS", distance);
    }

    @Override
    double calculateFare() {
        return Math.min(BASE_FARE + (RATE_PER_KM * this.distance), MAXIMUM_FARE);
    }
}

class TrainJourney extends Journey {

    private static final double BASE_FARE = 3.0;
    private static final double RATE_PER_KM = 0.15;

    TrainJourney(double distance) {
        super("TRAIN", distance);
    }

    @Override
    double calculateFare() {
        return BASE_FARE + (RATE_PER_KM * this.distance);
    }
}

class MetroJourney extends Journey {

    private static final double BASE_FARE = 1.50;
    private static final double RATE_PER_KM = 0.20;

    private final double peakHourFactor;

    MetroJourney(double distance, double peakHourFactor) {
        super("METRO", distance);
        this.peakHourFactor = peakHourFactor;
    }

    @Override
    double calculateFare() {
        return (BASE_FARE + (RATE_PER_KM * this.distance)) * this.peakHourFactor;
    }
}

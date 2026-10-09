import java.util.Scanner;

public class CityCabFareMeter {

    private static final String FIELD_SEPARATOR = "\\s+";
    private static final int CAB_INDEX = 0;
    private static final int DISTANCE_INDEX = 1;
    private static final int TIME_INDEX = 2;
    private static final String NIGHT_TIME = "NIGHT";
    private static final String[] DEMO_TRIP_LINES = { "MINI 8 DAY", "SEDAN 10 NIGHT", "SUV 20 DAY", "MINI 5 NIGHT" };

    public static void main(String[] args) {
        String[] tripLines = readTripLines();

        Cab[] cabs = new Cab[tripLines.length];
        boolean[] nightTrips = new boolean[tripLines.length];
        for (int tripIndex = 0; tripIndex < tripLines.length; tripIndex++) {
            String[] tripFields = tripLines[tripIndex].split(FIELD_SEPARATOR);
            cabs[tripIndex] = createCab(tripFields[CAB_INDEX].toUpperCase(),
                    Double.parseDouble(tripFields[DISTANCE_INDEX]));
            nightTrips[tripIndex] = NIGHT_TIME.equalsIgnoreCase(tripFields[TIME_INDEX]);
        }

        double totalFare = 0.0;
        for (int tripIndex = 0; tripIndex < cabs.length; tripIndex++) {
            Cab cab = cabs[tripIndex];
            double fare = cab.calculateFare();
            if (nightTrips[tripIndex]) {
                if (!(cab instanceof NightService)) {
                    System.out.println(cab.getTypeLabel() + ": night service not available");
                    continue;
                }
                fare = ((NightService) cab).applyNightSurcharge(fare);
            }
            System.out.printf("%s: %.2f%n", cab.getTypeLabel(), fare);
            totalFare = totalFare + fare;
        }
        System.out.printf("Total: %.2f%n", totalFare);
    }

    private static String[] readTripLines() {
        Scanner inputReader = new Scanner(System.in);
        String countLine = nextFilledLine(inputReader);
        if (countLine.isEmpty()) {
            inputReader.close();
            return DEMO_TRIP_LINES;
        }
        int tripCount = Integer.parseInt(countLine);
        String[] tripLines = new String[tripCount];
        for (int tripIndex = 0; tripIndex < tripCount; tripIndex++) {
            tripLines[tripIndex] = nextFilledLine(inputReader);
        }
        inputReader.close();
        return tripLines;
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

    private static Cab createCab(String cabType, double distanceKm) {
        switch (cabType) {
            case "MINI":
                return new MiniCab(distanceKm);
            case "SEDAN":
                return new SedanCab(distanceKm);
            case "SUV":
                return new SuvCab(distanceKm);
            default:
                throw new IllegalArgumentException("unknown cab type: " + cabType);
        }
    }
}

interface NightService {

    double NIGHT_SURCHARGE_RATE = 0.20;

    default double applyNightSurcharge(double fare) {
        return fare + (fare * NIGHT_SURCHARGE_RATE);
    }
}

abstract class Cab {

    private static final double MINIMUM_FARE = 100.0;

    private final String typeLabel;
    private final double distanceKm;

    Cab(String typeLabel, double distanceKm) {
        this.typeLabel = typeLabel;
        this.distanceKm = distanceKm;
    }

    abstract double getRatePerKm();

    final double calculateFare() {
        return Math.max(this.distanceKm * getRatePerKm(), MINIMUM_FARE);
    }

    String getTypeLabel() {
        return this.typeLabel;
    }
}

class MiniCab extends Cab {

    private static final double RATE_PER_KM = 10.0;

    MiniCab(double distanceKm) {
        super("MINI", distanceKm);
    }

    @Override
    double getRatePerKm() {
        return RATE_PER_KM;
    }
}

class SedanCab extends Cab implements NightService {

    private static final double RATE_PER_KM = 14.0;

    SedanCab(double distanceKm) {
        super("SEDAN", distanceKm);
    }

    @Override
    double getRatePerKm() {
        return RATE_PER_KM;
    }
}

class SuvCab extends Cab implements NightService {

    private static final double RATE_PER_KM = 18.0;

    SuvCab(double distanceKm) {
        super("SUV", distanceKm);
    }

    @Override
    double getRatePerKm() {
        return RATE_PER_KM;
    }
}

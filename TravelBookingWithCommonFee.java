import java.util.Scanner;

public class TravelBookingWithCommonFee {

    private static final String FIELD_SEPARATOR = "\\s+";
    private static final int MODE_INDEX = 0;
    private static final int DISTANCE_INDEX = 1;
    private static final String[] DEMO_BOOKING_LINES = { "BUS 200", "TRAIN 300", "FLIGHT 500" };

    public static void main(String[] args) {
        String[] bookingLines = readBookingLines();

        TravelBooking[] travelBookings = new TravelBooking[bookingLines.length];
        for (int bookingIndex = 0; bookingIndex < bookingLines.length; bookingIndex++) {
            travelBookings[bookingIndex] = createBooking(bookingLines[bookingIndex]);
        }

        for (int bookingIndex = 0; bookingIndex < travelBookings.length; bookingIndex++) {
            System.out.printf("%s: %.2f%n", travelBookings[bookingIndex].getModeLabel(),
                    travelBookings[bookingIndex].calculateTotal());
        }
    }

    private static String[] readBookingLines() {
        Scanner inputReader = new Scanner(System.in);
        String countLine = nextFilledLine(inputReader);
        if (countLine.isEmpty()) {
            inputReader.close();
            return DEMO_BOOKING_LINES;
        }
        int bookingCount = Integer.parseInt(countLine);
        String[] bookingLines = new String[bookingCount];
        for (int bookingIndex = 0; bookingIndex < bookingCount; bookingIndex++) {
            bookingLines[bookingIndex] = nextFilledLine(inputReader);
        }
        inputReader.close();
        return bookingLines;
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

    private static TravelBooking createBooking(String bookingLine) {
        String[] bookingFields = bookingLine.split(FIELD_SEPARATOR);
        String travelMode = bookingFields[MODE_INDEX].toUpperCase();
        double distanceKm = Double.parseDouble(bookingFields[DISTANCE_INDEX]);

        switch (travelMode) {
            case "BUS":
                return new BusBooking(distanceKm);
            case "TRAIN":
                return new TrainBooking(distanceKm);
            case "FLIGHT":
                return new FlightBooking(distanceKm);
            default:
                throw new IllegalArgumentException("unknown travel mode: " + travelMode);
        }
    }
}

abstract class TravelBooking {

    private static final double BOOKING_FEE = 50.0;

    private final String modeLabel;
    protected final double distanceKm;

    TravelBooking(String modeLabel, double distanceKm) {
        this.modeLabel = modeLabel;
        this.distanceKm = distanceKm;
    }

    abstract double calculateFare();

    final double calculateTotal() {
        return calculateFare() + BOOKING_FEE;
    }

    String getModeLabel() {
        return this.modeLabel;
    }
}

class BusBooking extends TravelBooking {

    private static final double RATE_PER_KM = 2.0;

    BusBooking(double distanceKm) {
        super("BUS", distanceKm);
    }

    @Override
    double calculateFare() {
        return RATE_PER_KM * this.distanceKm;
    }
}

class TrainBooking extends TravelBooking {

    private static final double RATE_PER_KM = 1.5;

    TrainBooking(double distanceKm) {
        super("TRAIN", distanceKm);
    }

    @Override
    double calculateFare() {
        return RATE_PER_KM * this.distanceKm;
    }
}

class FlightBooking extends TravelBooking {

    private static final double BASE_FARE = 2500.0;
    private static final double RATE_PER_KM = 4.0;

    FlightBooking(double distanceKm) {
        super("FLIGHT", distanceKm);
    }

    @Override
    double calculateFare() {
        return BASE_FARE + (RATE_PER_KM * this.distanceKm);
    }
}

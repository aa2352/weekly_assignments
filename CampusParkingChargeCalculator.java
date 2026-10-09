import java.util.Scanner;

public class CampusParkingChargeCalculator {

    private static final String FIELD_SEPARATOR = "\\s+";
    private static final int TYPE_INDEX = 0;
    private static final int HOURS_INDEX = 1;
    private static final String[] DEMO_VEHICLE_LINES = { "BIKE 3", "CAR 4", "TRUCK 1", "CAR 1" };

    public static void main(String[] args) {
        String[] vehicleLines = readVehicleLines();

        ParkedVehicle[] parkedVehicles = new ParkedVehicle[vehicleLines.length];
        for (int vehicleIndex = 0; vehicleIndex < vehicleLines.length; vehicleIndex++) {
            parkedVehicles[vehicleIndex] = createVehicle(vehicleLines[vehicleIndex]);
        }

        double grandTotal = 0.0;
        for (int vehicleIndex = 0; vehicleIndex < parkedVehicles.length; vehicleIndex++) {
            double parkingCharge = parkedVehicles[vehicleIndex].calculateCharge();
            System.out.printf("%s: %.2f%n", parkedVehicles[vehicleIndex].getTypeLabel(), parkingCharge);
            grandTotal = grandTotal + parkingCharge;
        }
        System.out.printf("Total: %.2f%n", grandTotal);
    }

    private static String[] readVehicleLines() {
        Scanner inputReader = new Scanner(System.in);
        String countLine = nextFilledLine(inputReader);
        if (countLine.isEmpty()) {
            inputReader.close();
            return DEMO_VEHICLE_LINES;
        }
        int vehicleCount = Integer.parseInt(countLine);
        String[] vehicleLines = new String[vehicleCount];
        for (int vehicleIndex = 0; vehicleIndex < vehicleCount; vehicleIndex++) {
            vehicleLines[vehicleIndex] = nextFilledLine(inputReader);
        }
        inputReader.close();
        return vehicleLines;
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

    private static ParkedVehicle createVehicle(String vehicleLine) {
        String[] vehicleFields = vehicleLine.split(FIELD_SEPARATOR);
        String vehicleType = vehicleFields[TYPE_INDEX].toUpperCase();
        int hoursParked = Integer.parseInt(vehicleFields[HOURS_INDEX]);

        switch (vehicleType) {
            case "BIKE":
                return new Bike(hoursParked);
            case "CAR":
                return new Car(hoursParked);
            case "TRUCK":
                return new Truck(hoursParked);
            default:
                throw new IllegalArgumentException("unknown vehicle type: " + vehicleType);
        }
    }
}

abstract class ParkedVehicle {

    private final String typeLabel;
    protected final int hoursParked;

    ParkedVehicle(String typeLabel, int hoursParked) {
        this.typeLabel = typeLabel;
        this.hoursParked = hoursParked;
    }

    abstract double calculateCharge();

    String getTypeLabel() {
        return this.typeLabel;
    }
}

class Bike extends ParkedVehicle {

    private static final double RATE_PER_HOUR = 10.0;

    Bike(int hoursParked) {
        super("BIKE", hoursParked);
    }

    @Override
    double calculateCharge() {
        return RATE_PER_HOUR * this.hoursParked;
    }
}

class Car extends ParkedVehicle {

    private static final double FIRST_HOUR_RATE = 30.0;
    private static final double ADDITIONAL_HOUR_RATE = 20.0;

    Car(int hoursParked) {
        super("CAR", hoursParked);
    }

    @Override
    double calculateCharge() {
        int additionalHours = this.hoursParked - 1;
        return FIRST_HOUR_RATE + (ADDITIONAL_HOUR_RATE * additionalHours);
    }
}

class Truck extends ParkedVehicle {

    private static final double RATE_PER_HOUR = 50.0;
    private static final double MINIMUM_CHARGE = 100.0;

    Truck(int hoursParked) {
        super("TRUCK", hoursParked);
    }

    @Override
    double calculateCharge() {
        return Math.max(RATE_PER_HOUR * this.hoursParked, MINIMUM_CHARGE);
    }
}

import java.util.Scanner;

public class HomeApplianceEnergyReport {

    private static final String FIELD_SEPARATOR = "\\s+";
    private static final int APPLIANCE_INDEX = 0;
    private static final int HOURS_INDEX = 1;
    private static final int MODE_INDEX = 2;
    private static final String SAVER_MODE = "SAVER";
    private static final String[] DEMO_APPLIANCE_LINES = { "FRIDGE 24", "AC 8 SAVER", "TV 5", "WASHER 2 SAVER" };

    public static void main(String[] args) {
        String[] applianceLines = readApplianceLines();

        Appliance[] appliances = new Appliance[applianceLines.length];
        boolean[] saverRequested = new boolean[applianceLines.length];
        for (int applianceIndex = 0; applianceIndex < applianceLines.length; applianceIndex++) {
            String[] applianceFields = applianceLines[applianceIndex].split(FIELD_SEPARATOR);
            appliances[applianceIndex] = createAppliance(applianceFields[APPLIANCE_INDEX].toUpperCase(),
                    Double.parseDouble(applianceFields[HOURS_INDEX]));
            saverRequested[applianceIndex] = applianceFields.length > MODE_INDEX
                    && SAVER_MODE.equalsIgnoreCase(applianceFields[MODE_INDEX]);
        }

        double totalCost = 0.0;
        for (int applianceIndex = 0; applianceIndex < appliances.length; applianceIndex++) {
            Appliance appliance = appliances[applianceIndex];
            double units = appliance.calculateUnits();
            if (saverRequested[applianceIndex]) {
                if (!(appliance instanceof SaverCapable)) {
                    System.out.println(appliance.getTypeLabel() + ": saver mode not supported");
                    continue;
                }
                units = ((SaverCapable) appliance).applySaverMode(units);
            }
            double cost = appliance.calculateCost(units);
            System.out.printf("%s: Units=%.2f Cost=%.2f%n", appliance.getTypeLabel(), units, cost);
            totalCost = totalCost + cost;
        }
        System.out.printf("Total Cost: %.2f%n", totalCost);
    }

    private static String[] readApplianceLines() {
        Scanner inputReader = new Scanner(System.in);
        String countLine = nextFilledLine(inputReader);
        if (countLine.isEmpty()) {
            inputReader.close();
            return DEMO_APPLIANCE_LINES;
        }
        int applianceCount = Integer.parseInt(countLine);
        String[] applianceLines = new String[applianceCount];
        for (int applianceIndex = 0; applianceIndex < applianceCount; applianceIndex++) {
            applianceLines[applianceIndex] = nextFilledLine(inputReader);
        }
        inputReader.close();
        return applianceLines;
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

    private static Appliance createAppliance(String applianceType, double hoursUsed) {
        switch (applianceType) {
            case "FRIDGE":
                return new Fridge(hoursUsed);
            case "AC":
                return new AirConditioner(hoursUsed);
            case "TV":
                return new Television(hoursUsed);
            case "WASHER":
                return new WashingMachine(hoursUsed);
            default:
                throw new IllegalArgumentException("unknown appliance: " + applianceType);
        }
    }
}

interface SaverCapable {

    double SAVER_REDUCTION_RATE = 0.25;

    default double applySaverMode(double units) {
        return units - (units * SAVER_REDUCTION_RATE);
    }
}

abstract class Appliance {

    private static final double WATTS_PER_KILOWATT = 1000.0;
    private static final double COST_PER_UNIT = 8.0;

    private final String typeLabel;
    private final double hoursUsed;

    Appliance(String typeLabel, double hoursUsed) {
        this.typeLabel = typeLabel;
        this.hoursUsed = hoursUsed;
    }

    abstract double getPowerWatts();

    final double calculateUnits() {
        return (getPowerWatts() * this.hoursUsed) / WATTS_PER_KILOWATT;
    }

    final double calculateCost(double units) {
        return units * COST_PER_UNIT;
    }

    String getTypeLabel() {
        return this.typeLabel;
    }
}

class Fridge extends Appliance {

    private static final double POWER_WATTS = 150.0;

    Fridge(double hoursUsed) {
        super("FRIDGE", hoursUsed);
    }

    @Override
    double getPowerWatts() {
        return POWER_WATTS;
    }
}

class AirConditioner extends Appliance implements SaverCapable {

    private static final double POWER_WATTS = 1500.0;

    AirConditioner(double hoursUsed) {
        super("AC", hoursUsed);
    }

    @Override
    double getPowerWatts() {
        return POWER_WATTS;
    }
}

class Television extends Appliance {

    private static final double POWER_WATTS = 100.0;

    Television(double hoursUsed) {
        super("TV", hoursUsed);
    }

    @Override
    double getPowerWatts() {
        return POWER_WATTS;
    }
}

class WashingMachine extends Appliance implements SaverCapable {

    private static final double POWER_WATTS = 500.0;

    WashingMachine(double hoursUsed) {
        super("WASHER", hoursUsed);
    }

    @Override
    double getPowerWatts() {
        return POWER_WATTS;
    }
}

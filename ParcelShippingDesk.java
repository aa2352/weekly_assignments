import java.util.Scanner;

public class ParcelShippingDesk {

    private static final String FIELD_SEPARATOR = "\\s+";
    private static final int TYPE_INDEX = 0;
    private static final int WEIGHT_INDEX = 1;
    private static final int DECLARED_VALUE_INDEX = 2;
    private static final double NO_INSURANCE = 0.0;
    private static final String[] DEMO_PARCEL_LINES = { "STANDARD 3 500", "EXPRESS 2 1000", "FRAGILE 4 2000" };

    public static void main(String[] args) {
        String[] parcelLines = readParcelLines();

        Parcel[] parcels = new Parcel[parcelLines.length];
        for (int parcelIndex = 0; parcelIndex < parcelLines.length; parcelIndex++) {
            parcels[parcelIndex] = createParcel(parcelLines[parcelIndex]);
        }

        double grandTotal = 0.0;
        for (int parcelIndex = 0; parcelIndex < parcels.length; parcelIndex++) {
            Parcel parcel = parcels[parcelIndex];
            double charge = parcel.calculateCharge();
            double insurance = NO_INSURANCE;
            if (parcel instanceof Insurable) {
                insurance = ((Insurable) parcel).calculateInsurance();
            }
            double parcelTotal = charge + insurance;
            System.out.printf("%s: Charge=%.2f Insurance=%.2f Total=%.2f%n", parcel.getTypeLabel(),
                    charge, insurance, parcelTotal);
            grandTotal = grandTotal + parcelTotal;
        }
        System.out.printf("Grand Total: %.2f%n", grandTotal);
    }

    private static String[] readParcelLines() {
        Scanner inputReader = new Scanner(System.in);
        String countLine = nextFilledLine(inputReader);
        if (countLine.isEmpty()) {
            inputReader.close();
            return DEMO_PARCEL_LINES;
        }
        int parcelCount = Integer.parseInt(countLine);
        String[] parcelLines = new String[parcelCount];
        for (int parcelIndex = 0; parcelIndex < parcelCount; parcelIndex++) {
            parcelLines[parcelIndex] = nextFilledLine(inputReader);
        }
        inputReader.close();
        return parcelLines;
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

    private static Parcel createParcel(String parcelLine) {
        String[] parcelFields = parcelLine.split(FIELD_SEPARATOR);
        String parcelType = parcelFields[TYPE_INDEX].toUpperCase();
        double weightKg = Double.parseDouble(parcelFields[WEIGHT_INDEX]);
        double declaredValue = Double.parseDouble(parcelFields[DECLARED_VALUE_INDEX]);

        switch (parcelType) {
            case "STANDARD":
                return new StandardParcel(weightKg, declaredValue);
            case "EXPRESS":
                return new ExpressParcel(weightKg, declaredValue);
            case "FRAGILE":
                return new FragileParcel(weightKg, declaredValue);
            default:
                throw new IllegalArgumentException("unknown parcel type: " + parcelType);
        }
    }
}

interface Insurable {

    double INSURANCE_RATE = 0.02;

    double getDeclaredValue();

    default double calculateInsurance() {
        return getDeclaredValue() * INSURANCE_RATE;
    }
}

abstract class Parcel {

    private final String typeLabel;
    protected final double weightKg;
    private final double declaredValue;

    Parcel(String typeLabel, double weightKg, double declaredValue) {
        this.typeLabel = typeLabel;
        this.weightKg = weightKg;
        this.declaredValue = declaredValue;
    }

    abstract double calculateCharge();

    public double getDeclaredValue() {
        return this.declaredValue;
    }

    String getTypeLabel() {
        return this.typeLabel;
    }
}

class StandardParcel extends Parcel {

    private static final double BASE_CHARGE = 40.0;
    private static final double RATE_PER_KG = 10.0;

    StandardParcel(double weightKg, double declaredValue) {
        this("STANDARD", weightKg, declaredValue);
    }

    protected StandardParcel(String typeLabel, double weightKg, double declaredValue) {
        super(typeLabel, weightKg, declaredValue);
    }

    @Override
    double calculateCharge() {
        return BASE_CHARGE + (RATE_PER_KG * this.weightKg);
    }
}

class ExpressParcel extends Parcel implements Insurable {

    private static final double BASE_CHARGE = 80.0;
    private static final double RATE_PER_KG = 15.0;

    ExpressParcel(double weightKg, double declaredValue) {
        super("EXPRESS", weightKg, declaredValue);
    }

    @Override
    double calculateCharge() {
        return BASE_CHARGE + (RATE_PER_KG * this.weightKg);
    }
}

class FragileParcel extends StandardParcel implements Insurable {

    private static final double HANDLING_FEE = 50.0;

    FragileParcel(double weightKg, double declaredValue) {
        super("FRAGILE", weightKg, declaredValue);
    }

    @Override
    double calculateCharge() {
        return super.calculateCharge() + HANDLING_FEE;
    }
}

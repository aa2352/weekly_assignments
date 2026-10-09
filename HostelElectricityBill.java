import java.util.Scanner;

public class HostelElectricityBill {

    private static final String FIELD_SEPARATOR = "\\s+";
    private static final int TYPE_INDEX = 0;
    private static final int UNITS_INDEX = 1;
    private static final int OCCUPANTS_INDEX = 2;
    private static final String[] DEMO_ROOM_LINES = { "SINGLE 120", "SHARED 150 3", "AC 100" };

    public static void main(String[] args) {
        String[] roomLines = readRoomLines();

        HostelRoom[] hostelRooms = new HostelRoom[roomLines.length];
        for (int roomIndex = 0; roomIndex < roomLines.length; roomIndex++) {
            hostelRooms[roomIndex] = createRoom(roomLines[roomIndex]);
        }

        double grandTotal = 0.0;
        for (int roomIndex = 0; roomIndex < hostelRooms.length; roomIndex++) {
            double billAmount = hostelRooms[roomIndex].calculateBill();
            System.out.printf("%s: %.2f%n", hostelRooms[roomIndex].getTypeLabel(), billAmount);
            grandTotal = grandTotal + billAmount;
        }
        System.out.printf("Total: %.2f%n", grandTotal);
    }

    private static String[] readRoomLines() {
        Scanner inputReader = new Scanner(System.in);
        String countLine = nextFilledLine(inputReader);
        if (countLine.isEmpty()) {
            inputReader.close();
            return DEMO_ROOM_LINES;
        }
        int roomCount = Integer.parseInt(countLine);
        String[] roomLines = new String[roomCount];
        for (int roomIndex = 0; roomIndex < roomCount; roomIndex++) {
            roomLines[roomIndex] = nextFilledLine(inputReader);
        }
        inputReader.close();
        return roomLines;
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

    private static HostelRoom createRoom(String roomLine) {
        String[] roomFields = roomLine.split(FIELD_SEPARATOR);
        String roomType = roomFields[TYPE_INDEX].toUpperCase();
        int unitsConsumed = Integer.parseInt(roomFields[UNITS_INDEX]);

        switch (roomType) {
            case "SINGLE":
                return new SingleRoom(unitsConsumed);
            case "SHARED":
                return new SharedRoom(unitsConsumed, Integer.parseInt(roomFields[OCCUPANTS_INDEX]));
            case "AC":
                return new AcRoom(unitsConsumed);
            default:
                throw new IllegalArgumentException("unknown room type: " + roomType);
        }
    }
}

abstract class HostelRoom {

    private final String typeLabel;
    protected final int unitsConsumed;

    HostelRoom(String typeLabel, int unitsConsumed) {
        this.typeLabel = typeLabel;
        this.unitsConsumed = unitsConsumed;
    }

    abstract double calculateBill();

    String getTypeLabel() {
        return this.typeLabel;
    }
}

class SingleRoom extends HostelRoom {

    private static final double RATE_PER_UNIT = 8.0;

    SingleRoom(int unitsConsumed) {
        super("SINGLE", unitsConsumed);
    }

    @Override
    double calculateBill() {
        return RATE_PER_UNIT * this.unitsConsumed;
    }
}

class SharedRoom extends HostelRoom {

    private static final double RATE_PER_UNIT = 6.0;

    private final int occupants;

    SharedRoom(int unitsConsumed, int occupants) {
        super("SHARED", unitsConsumed);
        this.occupants = occupants;
    }

    @Override
    double calculateBill() {
        return (RATE_PER_UNIT * this.unitsConsumed) / this.occupants;
    }
}

class AcRoom extends HostelRoom {

    private static final double RATE_PER_UNIT = 10.0;
    private static final double FIXED_CHARGE = 200.0;

    AcRoom(int unitsConsumed) {
        super("AC", unitsConsumed);
    }

    @Override
    double calculateBill() {
        return (RATE_PER_UNIT * this.unitsConsumed) + FIXED_CHARGE;
    }
}

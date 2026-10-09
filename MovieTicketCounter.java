import java.util.Scanner;

public class MovieTicketCounter {

    private static final String FIELD_SEPARATOR = "\\s+";
    private static final int SEAT_INDEX = 0;
    private static final int COUNT_INDEX = 1;
    private static final String[] DEMO_BOOKING_LINES = { "REGULAR 3", "PREMIUM 2", "RECLINER 1" };

    public static void main(String[] args) {
        String[] bookingLines = readBookingLines();

        Ticket[] tickets = new Ticket[bookingLines.length];
        for (int bookingIndex = 0; bookingIndex < bookingLines.length; bookingIndex++) {
            tickets[bookingIndex] = createTicket(bookingLines[bookingIndex]);
        }

        double totalCollected = 0.0;
        for (int bookingIndex = 0; bookingIndex < tickets.length; bookingIndex++) {
            double amount = tickets[bookingIndex].calculateAmount();
            System.out.printf("%s: %.2f%n", tickets[bookingIndex].getSeatLabel(), amount);
            totalCollected = totalCollected + amount;
        }
        System.out.printf("Total: %.2f%n", totalCollected);
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

    private static Ticket createTicket(String bookingLine) {
        String[] bookingFields = bookingLine.split(FIELD_SEPARATOR);
        String seatType = bookingFields[SEAT_INDEX].toUpperCase();
        int ticketCount = Integer.parseInt(bookingFields[COUNT_INDEX]);

        switch (seatType) {
            case "REGULAR":
                return new RegularTicket(ticketCount);
            case "PREMIUM":
                return new PremiumTicket(ticketCount);
            case "RECLINER":
                return new ReclinerTicket(ticketCount);
            default:
                throw new IllegalArgumentException("unknown seat type: " + seatType);
        }
    }
}

abstract class Ticket {

    private static final double CONVENIENCE_FEE = 20.0;

    private final String seatLabel;
    private final int ticketCount;

    Ticket(String seatLabel, int ticketCount) {
        this.seatLabel = seatLabel;
        this.ticketCount = ticketCount;
    }

    abstract double getPricePerTicket();

    final double calculateAmount() {
        return this.ticketCount * (getPricePerTicket() + CONVENIENCE_FEE);
    }

    String getSeatLabel() {
        return this.seatLabel;
    }
}

class RegularTicket extends Ticket {

    private static final double PRICE_PER_TICKET = 150.0;

    RegularTicket(int ticketCount) {
        super("REGULAR", ticketCount);
    }

    @Override
    double getPricePerTicket() {
        return PRICE_PER_TICKET;
    }
}

class PremiumTicket extends Ticket {

    private static final double PRICE_PER_TICKET = 250.0;

    PremiumTicket(int ticketCount) {
        super("PREMIUM", ticketCount);
    }

    @Override
    double getPricePerTicket() {
        return PRICE_PER_TICKET;
    }
}

class ReclinerTicket extends Ticket {

    private static final double PRICE_PER_TICKET = 400.0;

    ReclinerTicket(int ticketCount) {
        super("RECLINER", ticketCount);
    }

    @Override
    double getPricePerTicket() {
        return PRICE_PER_TICKET;
    }
}

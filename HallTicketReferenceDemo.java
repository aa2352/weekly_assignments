import java.util.NoSuchElementException;
import java.util.Scanner;

public class HallTicketReferenceDemo {

    private static final String FIELD_SEPARATOR = ",";
    private static final int EXPECTED_FIELD_COUNT = 2;
    private static final int UNALLOTTED_SEAT = 0;
    private static final String[] DEMO_TICKET_LINES = { "Priya,45", "Ravi,12", "Sameer", "Anu,-3", "   " };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] ticketLines = collectTicketLines(inputReader);
        for (int caseIndex = 0; caseIndex < ticketLines.length; caseIndex++) {
            demonstrateReferences(ticketLines[caseIndex]);
        }
        System.out.println("Hall tickets created in this session: " + HallTicket.getTicketsCreated());
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== Exam Hall Ticket Reference Management ===");
        System.out.println("\tInput format:\t\"Priya,45\" (student, seat allotted later)");
        System.out.println("\tRule:\ttwo variables can name one object, and == compares references\n");
    }

    private static String[] collectTicketLines(Scanner inputReader) {
        System.out.print("Enter a student and seat number (press Enter to use the demo list): ");
        try {
            String typedTicket = inputReader.nextLine();
            if (typedTicket.trim().isEmpty()) {
                System.out.println("[no ticket typed - running the demo list]\n");
                return DEMO_TICKET_LINES;
            }
            System.out.println();
            return new String[] { typedTicket };
        } catch (NoSuchElementException | IllegalStateException consoleUnavailable) {
            System.out.println("\n[no console input - running the demo list]\n");
            return DEMO_TICKET_LINES;
        }
    }

    private static void demonstrateReferences(String ticketLine) {
        System.out.println("Ticket: \"" + ticketLine + "\"");
        try {
            String[] ticketFields = parseTicketFields(ticketLine);
            String studentName = ticketFields[0];
            int allottedSeat = Integer.parseInt(ticketFields[1]);
            validateSeatNumber(allottedSeat);

            HallTicket original = new HallTicket(studentName, UNALLOTTED_SEAT);
            HallTicket copy = original;
            copy.seatNumber = allottedSeat;
            HallTicket separate = new HallTicket(studentName, allottedSeat);

            String referenceLabel = studentName.toLowerCase();
            System.out.println(studentName + "'s seatNumber (via first variable): " + original.seatNumber);
            System.out.println("copy == " + referenceLabel + ": " + (copy == original));
            System.out.println("separate == " + referenceLabel + ": " + (separate == original));
            System.out.println("separate holds the same field values: " + separate.hasSameValuesAs(original) + "\n");
        } catch (MalformedTicketException malformedTicket) {
            System.out.println("Ticket rejected: " + malformedTicket.getMessage() + "\n");
        } catch (NumberFormatException nonNumericSeat) {
            System.out.println("Ticket rejected: the seat number must be a whole number\n");
        } catch (IllegalArgumentException invalidTicket) {
            System.out.println("Ticket rejected: " + invalidTicket.getMessage() + "\n");
        }
    }

    private static void validateSeatNumber(int seatNumber) {
        if (seatNumber < 0) {
            throw new IllegalArgumentException("seat " + seatNumber + " does not exist in the hall");
        }
    }

    private static String[] parseTicketFields(String ticketLine) throws MalformedTicketException {
        if (ticketLine == null || ticketLine.trim().isEmpty()) {
            throw new IllegalArgumentException("no ticket details were given");
        }
        String[] ticketFields = ticketLine.split(FIELD_SEPARATOR);
        if (ticketFields.length != EXPECTED_FIELD_COUNT) {
            throw new MalformedTicketException("\"" + ticketLine.trim()
                    + "\" needs a student name and a seat number");
        }
        return new String[] { ticketFields[0].trim(), ticketFields[1].trim() };
    }
}

class HallTicket {

    private static int ticketsCreated = 0;

    String studentName;
    int seatNumber;

    HallTicket(String studentName, int seatNumber) {
        ticketsCreated++;
        this.studentName = studentName;
        this.seatNumber = seatNumber;
    }

    boolean hasSameValuesAs(HallTicket other) {
        return this.studentName.equals(other.studentName) && this.seatNumber == other.seatNumber;
    }

    static int getTicketsCreated() {
        return ticketsCreated;
    }
}

class MalformedTicketException extends Exception {
    MalformedTicketException(String message) {
        super(message);
    }
}

import java.time.LocalDate;
import java.util.Scanner;

public class StreamingPlanRenewalReminder {

    private static final String FIELD_SEPARATOR = "\\s+";
    private static final int TYPE_INDEX = 0;
    private static final int NAME_INDEX = 1;
    private static final int START_DATE_INDEX = 2;
    private static final String[] DEMO_SUBSCRIBER_LINES = {
            "BASIC Asha 2024-01-15",
            "STANDARD Ravi 2024-02-01",
            "PREMIUM Neha 2024-03-10",
            "BASIC Kiran 2024-12-20"
    };

    public static void main(String[] args) {
        String[] subscriberLines = readSubscriberLines();

        SubscriptionPlan[] subscriptionPlans = new SubscriptionPlan[subscriberLines.length];
        for (int subscriberIndex = 0; subscriberIndex < subscriberLines.length; subscriberIndex++) {
            subscriptionPlans[subscriberIndex] = createPlan(subscriberLines[subscriberIndex]);
        }

        for (int subscriberIndex = 0; subscriberIndex < subscriptionPlans.length; subscriberIndex++) {
            System.out.println(subscriptionPlans[subscriberIndex].getSubscriberName() + ": "
                    + subscriptionPlans[subscriberIndex].calculateRenewalDate());
        }
    }

    private static String[] readSubscriberLines() {
        Scanner inputReader = new Scanner(System.in);
        String countLine = nextFilledLine(inputReader);
        if (countLine.isEmpty()) {
            inputReader.close();
            return DEMO_SUBSCRIBER_LINES;
        }
        int subscriberCount = Integer.parseInt(countLine);
        String[] subscriberLines = new String[subscriberCount];
        for (int subscriberIndex = 0; subscriberIndex < subscriberCount; subscriberIndex++) {
            subscriberLines[subscriberIndex] = nextFilledLine(inputReader);
        }
        inputReader.close();
        return subscriberLines;
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

    private static SubscriptionPlan createPlan(String subscriberLine) {
        String[] subscriberFields = subscriberLine.split(FIELD_SEPARATOR);
        String planType = subscriberFields[TYPE_INDEX].toUpperCase();
        String subscriberName = subscriberFields[NAME_INDEX];
        LocalDate startDate = LocalDate.parse(subscriberFields[START_DATE_INDEX]);

        switch (planType) {
            case "BASIC":
                return new BasicPlan(subscriberName, startDate);
            case "STANDARD":
                return new StandardPlan(subscriberName, startDate);
            case "PREMIUM":
                return new PremiumPlan(subscriberName, startDate);
            default:
                throw new IllegalArgumentException("unknown plan type: " + planType);
        }
    }
}

abstract class SubscriptionPlan {

    private final String subscriberName;
    private final LocalDate startDate;

    SubscriptionPlan(String subscriberName, LocalDate startDate) {
        this.subscriberName = subscriberName;
        this.startDate = startDate;
    }

    abstract int getValidityDays();

    LocalDate calculateRenewalDate() {
        return this.startDate.plusDays(getValidityDays());
    }

    String getSubscriberName() {
        return this.subscriberName;
    }
}

class BasicPlan extends SubscriptionPlan {

    private static final int VALIDITY_DAYS = 30;

    BasicPlan(String subscriberName, LocalDate startDate) {
        super(subscriberName, startDate);
    }

    @Override
    int getValidityDays() {
        return VALIDITY_DAYS;
    }
}

class StandardPlan extends SubscriptionPlan {

    private static final int VALIDITY_DAYS = 90;

    StandardPlan(String subscriberName, LocalDate startDate) {
        super(subscriberName, startDate);
    }

    @Override
    int getValidityDays() {
        return VALIDITY_DAYS;
    }
}

class PremiumPlan extends SubscriptionPlan {

    private static final int VALIDITY_DAYS = 365;

    PremiumPlan(String subscriberName, LocalDate startDate) {
        super(subscriberName, startDate);
    }

    @Override
    int getValidityDays() {
        return VALIDITY_DAYS;
    }
}

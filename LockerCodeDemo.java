public class LockerCodeDemo {

    private static final String[] DEMO_CURRENT_CODES = { "1234", "0000", "5678" };
    private static final String[] DEMO_NEW_CODES = { "5678", "9999", "2468" };

    public static void main(String[] args) {
        Locker locker = new Locker(101, "1234");
        System.out.println("Locker " + locker.getLockerNumber() + " created");

        for (int attemptIndex = 0; attemptIndex < DEMO_CURRENT_CODES.length; attemptIndex++) {
            String currentCode = DEMO_CURRENT_CODES[attemptIndex];
            String newCode = DEMO_NEW_CODES[attemptIndex];
            boolean changed = locker.changeCode(currentCode, newCode);
            System.out.println("changeCode(\"" + currentCode + "\", \"" + newCode + "\") -> "
                    + (changed ? "success" : "rejected, code is unchanged"));
        }
    }
}

class Locker {

    private final int lockerNumber;
    private String code;

    Locker(int lockerNumber, String code) {
        this.lockerNumber = lockerNumber;
        this.code = code;
    }

    boolean changeCode(String currentCode, String newCode) {
        if (!this.code.equals(currentCode)) {
            return false;
        }
        this.code = newCode;
        return true;
    }

    int getLockerNumber() {
        return this.lockerNumber;
    }
}

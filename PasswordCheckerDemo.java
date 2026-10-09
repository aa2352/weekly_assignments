public class PasswordCheckerDemo {

    private static final String[] DEMO_PASSWORDS = { "abcd", "abcdefgh", "abcdefghij", "abcdefghijkl" };

    public static void main(String[] args) {
        for (int passwordIndex = 0; passwordIndex < DEMO_PASSWORDS.length; passwordIndex++) {
            PasswordChecker passwordChecker = new PasswordChecker(DEMO_PASSWORDS[passwordIndex]);
            System.out.println(DEMO_PASSWORDS[passwordIndex].length() + "-character password -> \""
                    + passwordChecker.getStrength() + "\"");
        }
    }
}

class PasswordChecker {

    private static final int MEDIUM_MINIMUM_LENGTH = 6;
    private static final int STRONG_MINIMUM_LENGTH = 10;

    private final String password;

    PasswordChecker(String password) {
        this.password = password;
    }

    String getStrength() {
        int passwordLength = this.password.length();
        if (passwordLength >= STRONG_MINIMUM_LENGTH) {
            return "Strong";
        }
        if (passwordLength >= MEDIUM_MINIMUM_LENGTH) {
            return "Medium";
        }
        return "Weak";
    }
}

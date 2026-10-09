public class NicknameTagDemo {

    private static final String[] DEMO_FULL_NAMES = { "Maria Gomez", "Ravi Kumar", "Maria Gomez" };

    public static void main(String[] args) {
        NameTag[] nameTags = new NameTag[DEMO_FULL_NAMES.length];
        for (int tagIndex = 0; tagIndex < DEMO_FULL_NAMES.length; tagIndex++) {
            nameTags[tagIndex] = new NameTag(DEMO_FULL_NAMES[tagIndex]);
            System.out.println("new NameTag(\"" + DEMO_FULL_NAMES[tagIndex] + "\").getNickname() -> \""
                    + nameTags[tagIndex].getNickname() + "\"");
        }

        int lastIndex = nameTags.length - 1;
        System.out.println("First and last tags give the same nickname: "
                + nameTags[0].getNickname().equals(nameTags[lastIndex].getNickname()));
        System.out.println("First and last tags are the same object: " + (nameTags[0] == nameTags[lastIndex]));
    }
}

final class NameTag {

    private static final String NAME_SEPARATOR = " ";
    private static final int FIRST_NAME_INDEX = 0;
    private static final int LAST_NAME_INDEX = 1;

    private final String firstName;
    private final char lastInitial;

    NameTag(String fullName) {
        String[] nameParts = fullName.split(NAME_SEPARATOR);
        this.firstName = nameParts[FIRST_NAME_INDEX];
        this.lastInitial = nameParts[LAST_NAME_INDEX].charAt(0);
    }

    String getNickname() {
        return this.firstName + " " + this.lastInitial + ".";
    }
}

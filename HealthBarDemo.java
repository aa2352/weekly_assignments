public class HealthBarDemo {

    private static final int MAXIMUM_HEALTH = 100;

    public static void main(String[] args) {
        Character character = new Character(MAXIMUM_HEALTH);
        System.out.println("Character created, health = " + character.getHealth()
                + " of " + character.getMaxHealth());

        character.takeDamage(30);
        System.out.println("takeDamage(30) -> health = " + character.getHealth());

        character.heal(50);
        System.out.println("heal(50) -> health = " + character.getHealth() + " (capped)");

        character.takeDamage(150);
        System.out.println("takeDamage(150) -> health = " + character.getHealth() + " (floored)");
    }
}

class Character {

    private static final int MINIMUM_HEALTH = 0;

    private final int maxHealth;
    private int health;

    Character(int maxHealth) {
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    void takeDamage(int amount) {
        if (amount <= 0) {
            return;
        }
        int reducedHealth = this.health - amount;
        this.health = Math.max(reducedHealth, MINIMUM_HEALTH);
    }

    void heal(int amount) {
        if (amount <= 0) {
            return;
        }
        int restoredHealth = this.health + amount;
        this.health = Math.min(restoredHealth, this.maxHealth);
    }

    int getHealth() {
        return this.health;
    }

    int getMaxHealth() {
        return this.maxHealth;
    }
}

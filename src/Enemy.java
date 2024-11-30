import java.util.Random;

public class Enemy extends Entity {
    private final int level;

    Enemy(String name, int maxHitPoints, int damage, int level) {
        super(name, maxHitPoints, damage);
        this.level = level;
    }

    Item getLoot() {
        Random random = new Random();

        Item item = null;
        switch (level) {
            case 3:
                if (Main.randomChance(30)) {
                    item = new Weapon("The Great Sword", 7);
                    break;
                }
            case 2:
                if (Main.randomChance(50)) {
                    item = new Potion("Elixir of Wellbeing", 5);
                    break;
                }
            case 1:
                if (Main.randomChance(70)) {
                    item = new Food("Disgusting Steak", 2);
                    break;
                }
        }
        return item;
    }
}

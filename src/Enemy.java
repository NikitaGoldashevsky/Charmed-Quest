import java.util.Random;

public class Enemy extends Entity {
    private int level;

    public Enemy(String name, int maxHitPoints, int damage, int level) {
        super(name, maxHitPoints, damage);
        this.level = level;
    }

    public Enemy(Enemy other) {
        super(other.name, other.maxHitPoints, other.damage);
        this.level = other.level;
    }

    Item getLoot() {
        Random random = new Random();

        Item item = null;
        switch (level) {
            case 6:
                if (Main.randomChance(20)) {
                    item = new Weapon("Ten-Meter Sword", 7);
                    break;
                }
            case 5:
                if (Main.randomChance(50)) {
                    item = new Weapon("Empowered Elixir", 8);
                    break;
                }
            case 4:
                if (Main.randomChance(30)) {
                    item = new Weapon("The Great Spear", 7);
                    break;
                }
            case 3:
                if (Main.randomChance(60)) {
                    item = new Potion("Elixir of Wellbeing", 5);
                    break;
                }
            case 2:
                if (Main.randomChance(40)) {
                    item = new Weapon("Rusty Sword", 4);
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

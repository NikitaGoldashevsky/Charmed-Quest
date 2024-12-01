import java.util.Random;

public class Enemy extends Entity {
    private final int level;
    private final int location;

    public Enemy(String name, int maxHitPoints, int damage, int location, int level) {
        super(name, maxHitPoints, damage);
        this.level = level;
        this.location = location;
    }

    public Enemy(Enemy other) {
        super(other.name, other.maxHitPoints, other.damage);
        this.level = other.level;
        this.location = other.location;
    }

    public int getLevel() {
        return this.level;
    }

    public int getLocation() {
        return this.location;
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

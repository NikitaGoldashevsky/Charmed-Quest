public class Weapon extends Item {
    private final int damage;

    public Weapon(String name, int damage) {
        super(name);
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    @Override
    public String toString() {
        return String.format("%s (damage: %d)", name, damage);
    }
}

public class Weapon extends Item {
    private final int damage;

    public Weapon(String name, int damage) {
        super(name);
        this.damage = damage;
    }

    @Override
    public String toString() {
        return name + "(damage: " + damage + ")";
    }
}

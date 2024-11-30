public class Entity implements IPrintable {
    private int hitPoints;
    private final int maxHitPoints;
    private final String name;
    private final int damage;

    protected Entity(String name, int maxHitPoints, int damage) {
        this.name = name;
        this.hitPoints = maxHitPoints;
        this.maxHitPoints = maxHitPoints;
        this.damage = damage;
    }

    protected Entity(String name, int hitPoints, int maxHitPoints, int damage) {
        this(name, maxHitPoints, damage);
        this.hitPoints = hitPoints;
    }

    public int getHP() {
        return hitPoints;
    }

    public int getMaxHP() {
        return maxHitPoints;
    }

    public int getDamage() {
        return damage;
    }

    public void looseHP(int hitPoints) {
        this.hitPoints = Math.max(this.hitPoints - hitPoints, 0);
    }

    public boolean isAlive() {
        return this.hitPoints > 0;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("%s (HP: %d/%d, damage: %d)", name, hitPoints, maxHitPoints, damage);
    }
}
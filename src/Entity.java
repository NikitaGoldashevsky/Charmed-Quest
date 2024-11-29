public abstract class Entity implements IPrintable {
    protected int hitPoints;
    protected final int maxHitPoints;
    protected String name;

    protected Entity(String name, int maxHitPoints) {
        this.name = name;
        this.hitPoints = maxHitPoints;
        this.maxHitPoints = maxHitPoints;
    }

    protected Entity(String name, int hitPoints, int maxHitPoints) {
        this(name, maxHitPoints);
        this.hitPoints = hitPoints;
    }

    public int getHP() {
        return hitPoints;
    }

    public boolean looseHP(int hitPoints) {
        this.hitPoints -=  hitPoints;
        return isAlive();
    }

    public boolean isAlive() {
        return this.hitPoints > 0;
    }

    public String getName() {
        return name;
    }

    @Override
    public abstract String toString();
}
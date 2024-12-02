public class Potion extends HealthRestorer {
    private final int damageIncrease;

    public Potion(String name, int healthRestore, int damageIncrease) {
        super(name, healthRestore);
        this.damageIncrease = damageIncrease;
    }

    public int getDamageIncrease() {
        return this.damageIncrease;
    }

    @Override
    public String toString() {
        return String.format("%s (restores %d HP, increases power: %d)", name, restoreAmount, damageIncrease);
    }
}

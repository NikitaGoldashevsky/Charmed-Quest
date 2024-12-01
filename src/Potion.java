public class Potion extends HealthRestorer {
    private final int damageIncrease;

    public Potion(String name, int healthRestore, int damageIncrease) {
        super(name, healthRestore);
        this.damageIncrease = damageIncrease;
    }

    public int getDamageIncrease() {
        return this.damageIncrease;
    }
}

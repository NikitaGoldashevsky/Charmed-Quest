public abstract class HealthRestorer extends Item implements IHealthRestoring {
    protected int restoreAmount;

    public HealthRestorer(String name, int restoreAmount) {
        super(name);
        this.restoreAmount = restoreAmount;
    }

    public int getRestoreAmount() {
        return restoreAmount;
    }

    @Override
    public String toString() {
        return name + " (restores " + restoreAmount + " HP)";
    }
}

public class Boss extends Entity {
    private int healingAmount = 0;
    private int weakeningAmount = 0;

    public Boss(String name, int maxHitPoints, int damage, int healingAmount, int weakeningAmount) {
        super(name, maxHitPoints, damage);
        this.healingAmount = healingAmount;
        this.weakeningAmount = weakeningAmount;
    }

//    public void setHealingAmount(int healingAmount) {
//        this.healingAmount = healingAmount;
//    }
//
//    public void setWeakeningAmount(int weakeningAmount) {
//        this.weakeningAmount = weakeningAmount;
//    }

    public int getWeakeningAmount() {
        return weakeningAmount;
    }

    public int getHealingAmount() {
        return healingAmount;
    }

    public void healItself() {
        hitPoints = Math.min(hitPoints + healingAmount, maxHitPoints);
    }

    public void weakenPlayer(Player player) {
        player.decreaseDamage(weakeningAmount);
    }
}

import java.util.ArrayList;

public class Player extends Entity {
    private final Inventory inventory;

    public Player(int hitPoints, int maxHitPoints, int damage) {
        super("Hero", hitPoints, maxHitPoints, 0);
        inventory = new Inventory();
    }

    public void restoreHealth(int restoreAmount) {
        hitPoints += restoreAmount;
    }

    @Override
    public int getDamage() {
        return super.getDamage() + inventory.getWeapon().getDamage();
    }

    @Override
    public String toString() {
        return String.format("%s (HP: %d/%d, damage: %d)", getName(), getHP(), getMaxHP(), getDamage());
    }

    public Inventory getInventory() {
        return inventory;
    }

    public class Inventory {
        private Weapon weapon;
        private ArrayList<Item> items;

        public Inventory() {
            items = new ArrayList<>();
        }

        public Weapon getWeapon() {
            return weapon;
        }

        public void addItem(Item item) {
            if (item instanceof Weapon) {
                weapon = (Weapon)item;
            }
            else {
                items.add(item);
            }
        }

        public Item getItemAt(int index) {
            return items.get(index);
        }

        public int itemsCount() {
            return items.size();
        }
    }
}

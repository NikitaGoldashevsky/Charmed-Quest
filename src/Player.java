import java.util.ArrayList;

public class Player extends Entity {
    private final Inventory inventory;

    public Player(int hitPoints, int maxHitPoints, int damage) {
        super("Player", hitPoints, maxHitPoints, 0);
        inventory = new Inventory();
    }

    @Override
    public int getDamage() {
        return super.getDamage() + inventory.getWeapon().getDamage();
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
            items.add(item);
        }

        public Item getItemAt(int index) {
            return items.get(index);
        }

        public int itemsCount() {
            return items.size();
        }
    }
}

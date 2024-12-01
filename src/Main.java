import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.Random;

public class Main {
    private static boolean gameIsRunning = true;

    private final static Map<String, Enemy> enemyPresets = Map.of(
        "rat", new Enemy("Rat", 2, 1, 1, 1),
        "goblin", new Enemy("Goblin", 3, 2, 1, 2),
        "spider", new Enemy("Spider", 4, 2, 2, 3),
        "ogre", new Enemy("Ogre", 8, 3, 2, 4),
        "wizard", new Enemy("Wizard", 5, 7, 3, 5),
        "dragon", new Enemy("Dragon", 10, 4, 3, 6)
    );

    private static class GameLocation {
        public enum Location {
            START, FOREST, CAVE, TOWER, VILLAGE, END;

            public Location next() {
                int nextIndex = (this.ordinal() + 1) % Location.values().length;
                return Location.values()[nextIndex];
            }
        }

        public Location location;
        public boolean firstEntrance = true;

        GameLocation(Location location) {
            this.location = location;
        }
    }

    public static void main(String[] args) {
        handleGame();
    }

    private static void handleGame() {
        Player player = new Player(4, 10, 3);
        player.getInventory().addItem(new Weapon("Heavy stick", 2));
        player.getInventory().addItem(new Food("Apple", 3));

        GameLocation currentLocation = new GameLocation(GameLocation.Location.START);

        while (gameIsRunning) {
            switch (currentLocation.location) {
                case VILLAGE:
                    handleInput(player, currentLocation);
                    break;
                case END:
                    gameIsRunning = false;
                    break;
                default:
                    handleLocation(player, currentLocation);
                    break;
            }
        }
        System.out.println("The game ended!");
    }

    private static void handleLocation(Player player, GameLocation location) {
        if (location.location == GameLocation.Location.START) {
            handleInput(player, location);
        }

        int locationOrdinal = location.location.ordinal();
        ArrayList<Enemy> locationEnemies = new ArrayList<>();

        for (Enemy enemyPreset : enemyPresets.values()) {
            if (enemyPreset.getLocation() == locationOrdinal) {
                locationEnemies.add(new Enemy(enemyPreset));
            }
        }
        int enemyChance = 50 + locationOrdinal * 10;
        Enemy currentEnemy = new Enemy(locationEnemies.get( randomChance(enemyChance) ? 0 : 1));

        System.out.printf("The %s is approaching you!\n", currentEnemy.getName());
        handleFight(player, currentEnemy);

        if (gameIsRunning) {
            System.out.println("\nThat was a tough fight!\n" +
                    "You may go now or take your time around here.");
            handleInput(player, location);
        }
    }

    private static void handleFight(Player player, Enemy enemy) {
        sleep();

        System.out.println("\nThe fight begins!");
        System.out.println(player);
        System.out.println(enemy);
        sleep();

        while (true) {
            handleAttack(player, enemy);
            if (!enemy.isAlive()) break;
            sleep();
            handleAttack(enemy, player);
            if (!player.isAlive()) break;
            sleep();
        }

        if (!enemy.isAlive()) {
            System.out.printf("\n%s has been defeated!\n", enemy.getName());
            handleLoot(enemy, player);
        }
        else if (!player.isAlive()) {
            System.out.println("\nYou have been defeated!");
            gameIsRunning = false;
        }
        sleep();
        return;
    }

    private static void handleAttack(Entity attacker, Entity target) {
        int attackDamage = attacker.getDamage();
        System.out.printf("\n%s attacks %s with %d damage!\n", attacker.getName(), target.getName(), attackDamage);
        target.looseHP(attackDamage);
        System.out.println(target);
    }

    private static void handleLoot(Enemy enemy, Player player) {
        Item item = enemy.getLoot();
        if (item != null) {
            System.out.printf("You looted %s from the defeated %s!\n", item.getName(), enemy.getName());

            if (item instanceof Weapon) {
                if (item.getName().equals(player.getInventory().getWeapon().getName())) {
                    System.out.printf("You already have %s as your weapon.\n", item.getName());
                }
                else if (((Weapon) item).getDamage() > player.getInventory().getWeapon().getDamage()) {
                    player.getInventory().addItem(item);
                    System.out.printf("%s is your new weapon!\n", item);
                }
                else {
                    System.out.printf("%s is worse than your current weapon.\n", item);
                }
            }
            else {
                player.getInventory().addItem(item);
            }
        }
        else {
            System.out.printf("Unfortunately, the defeated %s was stingy with loot.\n", enemy.getName());
        }
    }

    private static void handleInput(Player player, GameLocation currentLocation) {
        Scanner scanner = new Scanner(System.in);
        String userInput;

        do {
            System.out.print("\n> ");
            userInput = scanner.nextLine();

            if (userInput.matches("^use [1-9][0-9]*")) {
                int itemIndex = Integer.parseInt(userInput.substring(4)) - 1;
                if (player.getInventory().itemsCount() > itemIndex) {
                    Item item = player.getInventory().getItemAt(itemIndex);
                    if (item instanceof HealthRestorer) {
                        player.restoreHealth(((HealthRestorer) item).restoreAmount);
                        System.out.printf("You used %s! Your health is now %d/%d. \n", item.getName(), player.getHP(), player.getMaxHP());
                        player.getInventory().removeItemAt(itemIndex);
                    }
                }
                else {
                    System.out.printf("There is no item by index %d in your inventory\n", itemIndex);
                }
                continue;
            }

            switch (userInput) {
                case "me":
                    System.out.printf("You are at the %s.\n", currentLocation.location.toString().toLowerCase());
                    System.out.println(player);
                    break;
                case "inv":
                    System.out.println("Your weapon: " + player.getInventory().getWeapon());

                    if (player.getInventory().itemsCount() == 0) {
                        System.out.println("You have nothing in the inventory");
                    }
                    else {
                        System.out.println("Your inventory:");
                        for (int i = 0; i < player.getInventory().itemsCount(); i++) {
                            System.out.printf("\t%d - %s\n", i+1, player.getInventory().getItemAt(i));
                        }
                    }
                    break;
                case "help":
                    System.out.println("Commands:");
                    System.out.println("\t'me' - show your state and location");
                    System.out.println("\t'inv' - show your inventory");
                    System.out.println("\t'use <1..n>' - use an item by index 'n' from your inventory");
                    System.out.println("\t'go' - move to the next location");
                    System.out.println("\t'stay' - stay in the current location");
                    break;
                case "go":
                    currentLocation.location = currentLocation.location.next();
                    System.out.printf("You moved to the %s.\n", currentLocation.location.toString().toLowerCase());
                    currentLocation.firstEntrance = true;
                    break;
                case "stay":
                    System.out.printf("You stayed at the %s.\n", currentLocation.location.toString().toLowerCase());
                    currentLocation.firstEntrance = false;
                    break;
                default:
                    System.out.println("Unknown command! Type 'help' to view the list of commands.");
                    break;
            }
        }
        while (!userInput.equals("go") && !userInput.equals("stay"));
    }

    private static void sleep() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            System.out.println("The sleep was interrupted.");
            Thread.currentThread().interrupt();
        }
    }

    public static boolean randomChance(int chance) {
        Random random = new Random();
        return random.nextInt(100) < chance;
    }
}